package osa.ora.server.admin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class UpdateUserPanelTest {
    
    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private UpdateUserPanel updateUserPanel;
    
    @Before
    public void setup() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
    }
    
    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertNotNull(updateUserPanel.jComboBox1.getModel());
        assertNotNull(updateUserPanel.jComboBox2.getModel());
    }
    
    @Test
    public void testReset() {
        updateUserPanel.reset();
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());
        assertEquals(false, updateUserPanel.jCheckBox1.isSelected());
        assertEquals(false, updateUserPanel.jCheckBox2.isSelected());
        assertEquals(false, updateUserPanel.jCheckBox3.isSelected());
    }
    
    @Test
    public void testFillUser() {
        User user = new User("John", "john@example.com", "1234567890", "Manager");
        updateUserPanel.fillUser(user);
        assertEquals("John", updateUserPanel.jTextField1.getText());
        assertEquals("john@example.com", updateUserPanel.jTextField2.getText());
        assertEquals("1234567890", updateUserPanel.jTextField3.getText());
        assertEquals("Manager", updateUserPanel.jTextField4.getText());
        assertEquals(false, updateUserPanel.jCheckBox1.isSelected());
        assertEquals(false, updateUserPanel.jCheckBox2.isSelected());
        assertEquals(false, updateUserPanel.jCheckBox3.isSelected());
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }
}