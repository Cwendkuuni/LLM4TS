package osa.ora.server.admin.ui;

import static org.junit.Assert.*;

import javax.swing.DefaultComboBoxModel;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private UpdateUserPanel updateUserPanel;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
    }

    @Test
    public void testUpdateModel() {
        Group group = new Group();
        group.setName("Group1");

        User user1 = new User();
        user1.setName("User1");

        User user2 = new User();
        user2.setName("User2");

        group.getUsers().add(user1);
        group.getUsers().add(user2);

        adminApp.getGroups().add(group);

        updateUserPanel.updateModel();

        DefaultComboBoxModel<Group> groupComboBoxModel = (DefaultComboBoxModel<Group>) updateUserPanel.jComboBox1
                .getModel();
        DefaultComboBoxModel<User> userComboBoxModel = (DefaultComboBoxModel<User>) updateUserPanel.jComboBox2
                .getModel();

        assertEquals(group, groupComboBoxModel.getSelectedItem());
        assertEquals(user1, userComboBoxModel.getSelectedItem());
    }

    @Test
    public void testReset() {
        UpdateUserPanel spyUpdateUserPanel = spy(updateUserPanel);

        doNothing().when(spyUpdateUserPanel).reset();

        User user = new User();
        user.setName("User");

        updateUserPanel.fillUser(user);

        assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
        assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
        assertEquals(user.getDirectPhone(), updateUserPanel.jTextField3.getText());
        assertEquals(user.getJobTitle(), updateUserPanel.jTextField4.getText());
        assertEquals(user.isShowMyStatus(), updateUserPanel.jCheckBox1.isSelected());
        assertEquals(user.isOnlyStartChat(), updateUserPanel.jCheckBox2.isSelected());
        assertEquals(user.isCanJoinChatRoom(), updateUserPanel.jCheckBox3.isSelected());
    }

    // Additional test cases can be added for every method in the class

}