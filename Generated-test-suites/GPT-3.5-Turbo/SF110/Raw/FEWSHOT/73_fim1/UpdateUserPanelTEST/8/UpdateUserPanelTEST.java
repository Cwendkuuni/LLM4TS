package osa.ora.server.admin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UpdateUserPanelTest {

    private UpdateUserPanel updateUserPanel;

    @Before
    public void setUp() {
        updateUserPanel = new UpdateUserPanel(new AdminApp(), new ControlPanel());
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertEquals(org.apache.commons.cli.Util.EMPTY_STRING_ARRAY, updateUserPanel.jComboBox1.getModel().getSelectedItem().toString());
    }

    @Test
    public void testInitComponents() {
        updateUserPanel.initComponents();
        assertEquals("Username:", updateUserPanel.jLabel1.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertNull(updateUserPanel.jComboBox1.getModel().getSelectedItem());
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1ActionPerformed(null);
        assertFalse(updateUserPanel.jButton1.isEnabled());
        assert(updateUserPanel.jButton1.isVisible());
    }

    @Test
    public void testJButton2ActionPerformed() {
        updateUserPanel.jButton2ActionPerformed(null);
        assertFalse(updateUserPanel.jButton2.isEnabled());
        assert(updateUserPanel.jButton2.isVisible());
    }

    @Test
    public void testJButton3ActionPerformed() {
        updateUserPanel.jButton3ActionPerformed(null);
        assertFalse(updateUserPanel.jButton3.isEnabled());
        assert(updateUserPanel.jButton3.isVisible());
    }

    @Test
    public void testJButton4ActionPerformed() {
        updateUserPanel.jButton4ActionPerformed(null);
        assertFalse(updateUserPanel.jButton4.isEnabled());
        assert(updateUserPanel.jButton4.isVisible());
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        updateUserPanel.jComboBox1ItemStateChanged(null);
        assertNull(updateUserPanel.jComboBox1.getItemAt(0));
        assertFalse(updateUserPanel.jComboBox1.isEnabled());
    }

    @Test
    public void testReset() {
        updateUserPanel.reset();
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
    }

    @Test
    public void testFillUser() {
        updateUserPanel.fillUser(null);
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
    }

}