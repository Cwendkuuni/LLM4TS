import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class UpdateUserPanelTest {

    private UpdateUserPanel updateUserPanel;
    private AdminApp adminApp;
    private ControlPanel controlPanel;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
    }

    @Test
    public void testUpdateModel() {
        // Test case 1: adminApp has no groups
        updateUserPanel.updateModel();
        assertEquals(new DefaultComboBoxModel<Group>(), updateUserPanel.jComboBox1.getModel());
        assertEquals(new DefaultComboBoxModel<User>(), updateUserPanel.jComboBox2.getModel());
        
        // Test case 2: adminApp has groups and users
        Group group1 = new Group();
        Group group2 = new Group();
        User user1 = new User();
        User user2 = new User();
        group1.getUsers().add(user1);
        group2.getUsers().add(user2);
        adminApp.getGroups().add(group1);
        adminApp.getGroups().add(group2);
        updateUserPanel.updateModel();
        assertEquals(new DefaultComboBoxModel<Group>(adminApp.getGroups()), updateUserPanel.jComboBox1.getModel());
        assertEquals(new DefaultComboBoxModel<User>(group1.getUsers()), updateUserPanel.jComboBox2.getModel());
        assertEquals(user1.getName(), updateUserPanel.jTextField1.getText());
        assertEquals(user1.getEmail(), updateUserPanel.jTextField2.getText());
        assertEquals(user1.getDirectPhone(), updateUserPanel.jTextField3.getText());
        assertEquals(user1.getJobTitle(), updateUserPanel.jTextField4.getText());
        assertEquals(user1.isShowMyStatus(), updateUserPanel.jCheckBox1.isSelected());
        assertEquals(user1.isOnlyStartChat(), updateUserPanel.jCheckBox2.isSelected());
        assertEquals(user1.isCanJoinChatRoom(), updateUserPanel.jCheckBox3.isSelected());
    }

    // Add test methods for other public methods
    
    @Test
    public void testReset() {
        updateUserPanel.reset();
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }

}