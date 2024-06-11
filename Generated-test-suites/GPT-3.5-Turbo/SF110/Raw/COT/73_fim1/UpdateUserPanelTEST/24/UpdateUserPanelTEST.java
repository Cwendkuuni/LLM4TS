import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UpdateUserPanelTest {

    UpdateUserPanel updateUserPanel;

    @Before
    public void setUp() {
        // Initialize the UpdateUserPanel object
        updateUserPanel = new UpdateUserPanel(null, null);
    }

    @Test
    public void testUpdateModel() {
        // Test if the updateModel method updates the JComboBox models correctly
        // Test with empty groups
        updateUserPanel.updateModel();
        assertEquals(0, updateUserPanel.jComboBox1.getItemCount());
        assertEquals(0, updateUserPanel.jComboBox2.getItemCount());

        // Test with non-empty groups
        Group groupA = new Group();
        groupA.setName("Group A");

        User user1 = new User();
        user1.setName("User 1");
        groupA.addUser(user1);

        User user2 = new User();
        user2.setName("User 2");
        groupA.addUser(user2);

        updateUserPanel.adminApp = new AdminApp();
        updateUserPanel.adminApp.addGroup(groupA);

        updateUserPanel.updateModel();
        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        assertEquals(2, updateUserPanel.jComboBox2.getItemCount());
        assertEquals("Group A", updateUserPanel.jComboBox1.getSelectedItem());
        assertEquals("User 1", updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testReset() {
        // Test if the reset method resets all the fields correctly
        updateUserPanel.jTextField1.setText("Test");
        updateUserPanel.jTextField2.setText("test@example.com");
        updateUserPanel.jCheckBox1.setSelected(true);

        updateUserPanel.reset();

        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
    }

    @Test
    public void testFillUser() {
        // Test if the fillUser method populates the fields with the correct user data
        User user = new User();
        user.setName("Test");
        user.setEmail("test@example.com");
        user.setDirectPhone("123456789");
        user.setJobTitle("Developer");
        user.setShowMyStatus(true);
        user.setOnlyStartChat(false);
        user.setCanJoinChatRoom(true);

        updateUserPanel.fillUser(user);

        assertEquals("Test", updateUserPanel.jTextField1.getText());
        assertEquals("test@example.com", updateUserPanel.jTextField2.getText());
        assertEquals("123456789", updateUserPanel.jTextField3.getText());
        assertEquals("Developer", updateUserPanel.jTextField4.getText());
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());
    }

    // Additional test cases for other methods can be added here

}