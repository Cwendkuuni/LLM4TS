import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.ui.UpdateUserPanel;

public class UpdateUserPanelTest {
    
    private UpdateUserPanel updateUserPanel;
    
    @Before
    public void setUp() {
        // Initialize the UpdateUserPanel object
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel(adminApp);
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
    }
    
    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        // Verify that the JComboBox jComboBox1 has the right model
        assertEquals(updateUserPanel.adminApp.getGroups(), updateUserPanel.jComboBox1.getModel());
        
        // Verify that the JComboBox jComboBox2 has the right model
        if (updateUserPanel.adminApp.getGroups().size() > 0) {
            assertEquals(updateUserPanel.adminApp.getGroups().get(0).getUsers(), updateUserPanel.jComboBox2.getModel());
        } else {
            assertEquals(updateUserPanel.adminApp.getGroups(), updateUserPanel.jComboBox2.getModel());
        }
    }
    
    @Test
    public void testReset() {
        updateUserPanel.reset();
        // Verify that the text fields are empty
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());
        
        // Verify that the check boxes are unchecked
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }
    
    @Test
    public void testFillUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setDirectPhone("1234567890");
        user.setJobTitle("Tester");
        user.setShowMyStatus(true);
        user.setOnlyStartChat(false);
        user.setCanJoinChatRoom(true);
        
        updateUserPanel.fillUser(user);
        
        // Verify that the text fields contain the values from the user object
        assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
        assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
        assertEquals(user.getDirectPhone(), updateUserPanel.jTextField3.getText());
        assertEquals(user.getJobTitle(), updateUserPanel.jTextField4.getText());
        
        // Verify that the check boxes have the correct selection
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());
    }
}