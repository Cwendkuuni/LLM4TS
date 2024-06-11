
import org.junit.Test;
import static org.junit.Assert.*;

public class UpdateUserPanelTest {

    @Test
    public void testUpdateModel() {
        // Create objects needed for testing
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);

        // Perform the test
        updateUserPanel.updateModel();

        // Verify the results
        assertNotNull(updateUserPanel.jComboBox1.getModel());
        assertNotNull(updateUserPanel.jComboBox2.getModel());
    }

    @Test
    public void testReset() {
        // Create objects needed for testing
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);

        // Perform the test
        updateUserPanel.reset();

        // Verify the results
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test
    public void testFillUser() {
        // Create objects needed for testing
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        
        // Create a user object
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setDirectPhone("1234567890");
        user.setJobTitle("Manager");
        user.setShowMyStatus(true);
        user.setOnlyStartChat(false);
        user.setCanJoinChatRoom(true);
        
        // Perform the test
        updateUserPanel.fillUser(user);
        
        // Verify the results
        assertEquals("John", updateUserPanel.jTextField1.getText());
        assertEquals("john@example.com", updateUserPanel.jTextField2.getText());
        assertEquals("1234567890", updateUserPanel.jTextField3.getText());
        assertEquals("Manager", updateUserPanel.jTextField4.getText());
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());
    }
}
