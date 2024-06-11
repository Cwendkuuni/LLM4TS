import static org.junit.Assert.*;
import org.junit.Test;

public class UpdateUserPanelTest {
    
    UpdateUserPanel panel;
    
    @Test
    public void testUpdateModel() {
        // Test case for when adminApp.getGroups() return null
        panel = new UpdateUserPanel(null, null);
        panel.updateModel();
        assertNull(panel.jComboBox1.getModel().getElementAt(0));
        
        // Test case for when adminApp.getGroups() return empty list
        panel = new UpdateUserPanel(new AdminApp(), null);
        panel.updateModel();
        assertEquals(0, panel.jComboBox1.getModel().getSize());
        
        // Test case for when adminApp.getGroups() return a non-empty list
        AdminApp adminApp = new AdminApp();
        adminApp.addGroup(new Group());
        panel = new UpdateUserPanel(adminApp, null);
        panel.updateModel();
        assertEquals(1, panel.jComboBox1.getModel().getSize());
    }
    
    @Test
    public void testReset() {
        panel = new UpdateUserPanel(null, null);
        panel.jTextField1.setText("Username");
        panel.jTextField2.setText("Email");
        panel.jTextField3.setText("Phone");
        panel.jTextField4.setText("Title");
        panel.jCheckBox1.setSelected(true);
        panel.jCheckBox2.setSelected(true);
        panel.jCheckBox3.setSelected(true);
        
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
        panel = new UpdateUserPanel(null, null);
        User user = new User();
        user.setName("Name");
        user.setEmail("Email");
        user.setDirectPhone("Phone");
        user.setJobTitle("Title");
        user.setShowMyStatus(true);
        user.setOnlyStartChat(true);
        user.setCanJoinChatRoom(true);
        
        panel.fillUser(user);
        
        assertEquals("Name", panel.jTextField1.getText());
        assertEquals("Email", panel.jTextField2.getText());
        assertEquals("Phone", panel.jTextField3.getText());
        assertEquals("Title", panel.jTextField4.getText());
        assertTrue(panel.jCheckBox1.isSelected());
        assertTrue(panel.jCheckBox2.isSelected());
        assertTrue(panel.jCheckBox3.isSelected());
    }
}