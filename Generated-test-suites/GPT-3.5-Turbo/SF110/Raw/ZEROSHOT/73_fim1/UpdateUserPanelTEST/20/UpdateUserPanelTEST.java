package osa.ora.server.admin.ui;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;

import javax.swing.*;

import static org.junit.Assert.*;

public class UpdateUserPanelTest {
    
    private UpdateUserPanel updateUserPanel;
    
    @Before
    public void setUp(){
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel(adminApp);
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
    }
    
    @Test
    public void testUpdateModel(){
        updateUserPanel.updateModel();
        JComboBox<Group> comboBox1 = updateUserPanel.jComboBox1;
        JComboBox<User> comboBox2 = updateUserPanel.jComboBox2;
        
        assertNotNull(comboBox1);
        assertNotNull(comboBox2);
        assertNotNull(comboBox1.getSelectedItem());
        assertNotNull(comboBox2.getSelectedItem());
    }
    
    @Test
    public void testReset(){
        updateUserPanel.reset();
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }   
    
    @Test
    public void testFillUser(){
        User user = new User();
        user.setName("User1");
        user.setEmail("user1@gmail.com");
        user.setDirectPhone("1234567890");
        user.setJobTitle("Software Developer");
        user.setShowMyStatus(true);
        user.setOnlyStartChat(false);
        user.setCanJoinChatRoom(true);
        
        updateUserPanel.fillUser(user);
        
        assertEquals("User1", updateUserPanel.jTextField1.getText());
        assertEquals("user1@gmail.com", updateUserPanel.jTextField2.getText());
        assertEquals("1234567890", updateUserPanel.jTextField3.getText());
        assertEquals("Software Developer", updateUserPanel.jTextField4.getText());
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());
    }
    
}