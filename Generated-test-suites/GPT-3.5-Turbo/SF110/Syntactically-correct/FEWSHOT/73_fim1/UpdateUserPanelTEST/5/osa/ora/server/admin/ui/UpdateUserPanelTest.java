package osa.ora.server.admin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UpdateUserPanelTest {

    private UpdateUserPanel panel;
    
    @Before
    public void setUp() {
        panel = new UpdateUserPanel(null, null);
    }
    
    @Test
    public void testUpdateModel() {
        // Test case when adminApp groups are null
        panel.adminApp = new AdminApp();
        panel.updateModel();
        assertNull(panel.jComboBox1.getModel());
        assertNull(panel.jComboBox2.getModel());
        
        // Test case when adminApp groups are empty
        panel.adminApp.setGroups(new Group[]{});
        panel.updateModel();
        assertEquals(0, panel.jComboBox1.getModel().getSize());
        assertEquals(0, panel.jComboBox2.getModel().getSize());
        
        // Test case when adminApp groups and users are not null and not empty
        Group group1 = new Group();
        group1.setUsers(new User[]{new User(), new User()});
        Group group2 = new Group();
        group2.setUsers(new User[]{new User()});
        panel.adminApp.setGroups(new Group[]{group1, group2});
        panel.updateModel();
        assertEquals(2, panel.jComboBox1.getModel().getSize());
        assertEquals(2, panel.jComboBox2.getModel().getSize());
        assertEquals(group1.getUsers()[0], panel.jComboBox2.getSelectedItem());
        assertEquals(group1.getUsers()[0], panel.jComboBox2.getModel().getElementAt(0));
        assertEquals(group1.getUsers()[1], panel.jComboBox2.getModel().getElementAt(1));
    }
    
    @Test
    public void testReset() {
        panel.reset();
        assertEquals("", panel.jTextField1.getText());
        assertEquals("", panel.jTextField2.getText());
        assertEquals("", panel.jTextField3.getText());
        assertEquals("", panel.jTextField4.getText());
        assertFalse(panel.jCheckBox1.isSelected());
        assertFalse(panel.jCheckBox2.isSelected());
        assertFalse(panel.jCheckBox3.isSelected());
    }
    
    @Test
    public void testFillUser() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setDirectPhone("1234567890");
        user.setJobTitle("Engineer");
        user.setShowMyStatus(true);
        user.setOnlyStartChat(false);
        user.setCanJoinChatRoom(true);
        
        panel.fillUser(user);
        assertEquals("John", panel.jTextField1.getText());
        assertEquals("john@example.com", panel.jTextField2.getText());
        assertEquals("1234567890", panel.jTextField3.getText());
        assertEquals("Engineer", panel.jTextField4.getText());
        assertTrue(panel.jCheckBox1.isSelected());
        assertFalse(panel.jCheckBox2.isSelected());
        assertTrue(panel.jCheckBox3.isSelected());
    }
}