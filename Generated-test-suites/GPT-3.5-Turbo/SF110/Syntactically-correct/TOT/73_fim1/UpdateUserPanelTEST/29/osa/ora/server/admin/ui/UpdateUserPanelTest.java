package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.User;
import javax.swing.DefaultComboBoxModel;

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
        updateUserPanel.updateModel();
        assertNotNull(updateUserPanel.jComboBox1.getModel());
        assertNotNull(updateUserPanel.jComboBox2.getModel());
        assertNotNull(updateUserPanel.jTextField1.getText());
        assertNotNull(updateUserPanel.jTextField2.getText());
        assertNotNull(updateUserPanel.jTextField3.getText());
        // Test any other relevant assertions for this method
    }

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
        // Test any other relevant assertions for this method
    }

    @Test
    public void testFillUser() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setDirectPhone("123456789");
        user.setJobTitle("Manager");
        user.setShowMyStatus(true);
        user.setOnlyStartChat(false);
        user.setCanJoinChatRoom(true);
        updateUserPanel.fillUser(user);
        assertEquals("John", updateUserPanel.jTextField1.getText());
        assertEquals("john@example.com", updateUserPanel.jTextField2.getText());
        assertEquals("123456789", updateUserPanel.jTextField3.getText());
        assertEquals("Manager", updateUserPanel.jTextField4.getText());
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());
        // Test any other relevant assertions for this method
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(1);
        assertEquals(1, updateUserPanel.jComboBox1.getSelectedIndex());
        // Test any other relevant assertions for this method
    }

    @Test
    public void testJComboBox2ItemStateChanged() {
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox2.setSelectedIndex(1);
        assertEquals(1, updateUserPanel.jComboBox2.getSelectedIndex());
        // Test any other relevant assertions for this method
    }

    // Test other methods in UpdateUserPanel class in a similar way

}