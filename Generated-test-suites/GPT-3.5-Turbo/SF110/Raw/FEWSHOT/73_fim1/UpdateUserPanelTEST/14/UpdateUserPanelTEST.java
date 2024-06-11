package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UpdateUserPanelTest {
    
    private UpdateUserPanel updateUserPanel;
    private AdminApp adminApp;
    private ControlPanel controlPanel;
    
    @Before
    public void setUp() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel(adminApp);
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
    }
    
    @Test
    public void testUpdateModel() {
        // Test when adminApp.getGroups() is null
        updateUserPanel.updateModel();
        assertNull(updateUserPanel.jComboBox1.getSelectedItem());
        assertNull(updateUserPanel.jComboBox2.getSelectedItem());
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
        
        // Test when adminApp.getGroups() contains groups
        Group group1 = new Group();
        group1.setName("group1");
        Group group2 = new Group();
        group2.setName("group2");
        adminApp.getGroups().add(group1);
        adminApp.getGroups().add(group2);
        User user1 = new User();
        user1.setName("user1");
        User user2 = new User();
        user2.setName("user2");
        group1.getUsers().add(user1);
        group2.getUsers().add(user2);
        
        updateUserPanel.updateModel();
        assertEquals(group1, updateUserPanel.jComboBox1.getSelectedItem());
        assertEquals(user1, updateUserPanel.jComboBox2.getSelectedItem());
        assertEquals("user1", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }
    
    @Test
    public void testJButton1ActionPerformed() {
        // Test when jButton1 is clicked
        updateUserPanel.reset();
        updateUserPanel.jButton1ActionPerformed(null);
        assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible());
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }
    
    // Add more test methods for other methods in the class
}