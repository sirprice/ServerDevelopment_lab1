package backend;

import account.Account;
import viewmodel.PostVM;
import viewmodel.ProfileItem;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.util.List;

/**
 * Created by o_0 on 2016-11-24.
 */
@ManagedBean(name = "profileService")
@SessionScoped
public class ProfileService implements ProfileItem {

    @ManagedProperty("#{account}")
    private Account userAccount;

    public Account getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(Account userAccount) {
        this.userAccount = userAccount;
    }


    private String name;
    private String info;
    private Integer age;
    private int relationshipStatus;
    private List<PostVM> wallPosts;
    private int selectedUserId;
    private int profileId;

    public int getProfileId() {
        return profileId;
    }

    @PostConstruct
    public void init() {
        if (userAccount == null) {
            System.out.println("inint error (userAccount == null) ");
            return;
        }
        this.selectedUserId = -1;
        this.profileId = -1;
        this.name = "";
        this.info = "";
        this.age = -1;
        this.relationshipStatus = -1;
        selectProfile(userAccount.getUserId());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(int relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public boolean saveUserProfile(ProfileItem profileItem) {

        System.out.println("ProfileView::saveUserProfile");
        boolean success = false;
        if (userAccount.isLoggedin()) {
            setProfileTo(profileItem);
            // TODO: parse id to correct one
            success = BackendFacade.saveProfile(userAccount.getUserId(), this);

        } else {
            System.out.println("not loggedin");
        }
        return success;
    }

    public boolean selectProfile(int userId) {
        // TODO: check friend status
        boolean ret = false;
        if (this.selectedUserId != userId) {
            this.selectedUserId = userId;
            ret = loadUserProfile();
        }

        return ret;
    }

    public boolean loadUserProfile() {
        System.out.println("ProfileService::loadUserProfile = " + this.selectedUserId);
        if (this.selectedUserId < 0) {
            return false;
        }
        ProfileItem profileItem = BackendFacade.loadProfile(this.selectedUserId);
        if (profileItem == null) {
            return false;
        }
        setProfileTo(profileItem);
        return true;
    }

    private void setProfileTo(ProfileItem profile) {
        this.name = profile.getName();
        this.info = profile.getInfo();
        this.age = profile.getAge();
        this.profileId = profile.getProfileId();
        this.relationshipStatus = profile.getRelationshipStatus();
    }

    public List<PostVM> getCurrentFeed() {
        System.out.println("getting current feed cuz i'm awesome");
        wallPosts = BackendFacade.getPostForProfile(profileId, userAccount.getUserId());
        return wallPosts;
    }
}
