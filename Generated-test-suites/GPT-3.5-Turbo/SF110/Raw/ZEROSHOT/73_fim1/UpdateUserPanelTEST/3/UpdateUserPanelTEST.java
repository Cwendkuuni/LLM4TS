import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        updateUserPanel.updateModel();
        
        // Assert that jComboBox1 has been set with the correct model
        JComboBox jComboBox1 = updateUserPanel.getjComboBox1();
        assertNotNull(jComboBox1);
        DefaultComboBoxModel<Group> expectedModel1 = new DefaultComboBoxModel<Group>(adminApp.getGroups());
        DefaultComboBoxModel<Group> actualModel1 = (DefaultComboBoxModel<Group>) jComboBox1.getModel();
        assertEquals(expectedModel1, actualModel1);
        
        // Assert that reset method has been called
        // reset method should reset all text fields and checkboxes
        assertEquals("", updateUserPanel.getjTextField1().getText());
        assertEquals("", updateUserPanel.getjTextField2().getText());
        assertEquals("", updateUserPanel.getjTextField3().getText());
        assertEquals("", updateUserPanel.getjTextField4().getText());
        assertFalse(updateUserPanel.getjCheckBox1().isSelected());
        assertFalse(updateUserPanel.getjCheckBox2().isSelected());
        assertFalse(updateUserPanel.getjCheckBox3().isSelected());

        // Assert that the selected group's users have been set to jComboBox2
        // Also, assert that the user's info has also been filled in the text fields and checkboxes
        JComboBox jComboBox2 = updateUserPanel.getjComboBox2();
        assertNotNull(jComboBox2);
        DefaultComboBoxModel<User> expectedModel2 = new DefaultComboBoxModel<User>(adminApp.getGroups().get(0).getUsers());
        DefaultComboBoxModel<User> actualModel2 = (DefaultComboBoxModel<User>) jComboBox2.getModel();
        assertEquals(expectedModel2, actualModel2);
        User selectedUser = adminApp.getGroups().get(0).getUsers().get(0);
        assertEquals(selectedUser.getName(), updateUserPanel.getjTextField1().getText());
        assertEquals(selectedUser.getEmail(), updateUserPanel.getjTextField2().getText());
        assertEquals(selectedUser.getDirectPhone(), updateUserPanel.getjTextField3().getText());
        assertEquals(selectedUser.getJobTitle(), updateUserPanel.getjTextField4().getText());
        assertEquals(selectedUser.isShowMyStatus(), updateUserPanel.getjCheckBox1().isSelected());
        assertEquals(selectedUser.isOnlyStartChat(), updateUserPanel.getjCheckBox2().isSelected());
        assertEquals(selectedUser.isCanJoinChatRoom(), updateUserPanel.getjCheckBox3().isSelected());
    }
    
    @Test
    public void testReset() {
        updateUserPanel.reset();
        
        // Assert that all text fields and checkboxes have been reset
        assertEquals("", updateUserPanel.getjTextField1().getText());
        assertEquals("", updateUserPanel.getjTextField2().getText());
        assertEquals("", updateUserPanel.getjTextField3().getText());
        assertEquals("", updateUserPanel.getjTextField4().getText());
        assertFalse(updateUserPanel.getjCheckBox1().isSelected());
        assertFalse(updateUserPanel.getjCheckBox2().isSelected());
        assertFalse(updateUserPanel.getjCheckBox3().isSelected());
    }
    
    @Test
    public void testFillUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setDirectPhone("1234567890");
        user.setJobTitle("Software Engineer");
        user.setShowMyStatus(true);
        user.setOnlyStartChat(false);
        user.setCanJoinChatRoom(true);
        
        updateUserPanel.fillUser(user);
        
        // Assert that the user's info has been correctly filled in the text fields and checkboxes
        assertEquals(user.getName(), updateUserPanel.getjTextField1().getText());
        assertEquals(user.getEmail(), updateUserPanel.getjTextField2().getText());
        assertEquals(user.getDirectPhone(), updateUserPanel.getjTextField3().getText());
        assertEquals(user.getJobTitle(), updateUserPanel.getjTextField4().getText());
        assertEquals(user.isShowMyStatus(), updateUserPanel.getjCheckBox1().isSelected());
        assertEquals(user.isOnlyStartChat(), updateUserPanel.getjCheckBox2().isSelected());
        assertEquals(user.isCanJoinChatRoom(), updateUserPanel.getjCheckBox3().isSelected());
    }
}