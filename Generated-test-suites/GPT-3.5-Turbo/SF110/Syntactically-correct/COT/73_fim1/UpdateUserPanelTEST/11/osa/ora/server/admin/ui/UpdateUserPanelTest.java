
package osa.ora.server.admin.ui;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

public class UpdateUserPanelTest {

    UpdateUserPanel updateUserPanel;

    @Before
    public void setUp() {
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel(adminApp);
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        updateUserPanel.updateModel();
    }

    @After
    public void tearDown() {
        updateUserPanel = null;
    }

    @Test
    public void testUpdateModel() {
        assertTrue(updateUserPanel.jComboBox1.getModel().getSize() > 0);
        assertNotNull(updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testInitComponents() {
        assertNotNull(updateUserPanel.jLabel1);
        assertNotNull(updateUserPanel.jLabel2);
        assertNotNull(updateUserPanel.jLabel3);
        assertNotNull(updateUserPanel.jCheckBox1);
        assertNotNull(updateUserPanel.jCheckBox2);
        assertNotNull(updateUserPanel.jCheckBox3);
        assertNotNull(updateUserPanel.jLabel4);
        assertNotNull(updateUserPanel.jComboBox1);
        assertNotNull(updateUserPanel.jTextField2);
        assertNotNull(updateUserPanel.jTextField3);
        assertNotNull(updateUserPanel.jButton1);
        assertNotNull(updateUserPanel.jButton2);
        assertNotNull(updateUserPanel.jComboBox2);
        assertNotNull(updateUserPanel.jButton3);
        assertNotNull(updateUserPanel.jButton4);
        assertNotNull(updateUserPanel.jLabel5);
        assertNotNull(updateUserPanel.jTextField1);
        assertNotNull(updateUserPanel.jTextField4);
        assertNotNull(updateUserPanel.jLabel6);
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1.doClick();
        assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible());
    }

    @Test
    public void testJButton2ActionPerformed() {
        updateUserPanel.jButton2.doClick();
        JOptionPane pane = getOptionPane(updateUserPanel);
        assertEquals("Can't identify selected User!", pane.getMessage());
    }

    // Add test methods for other methods in the class

    // Utility method to retrieve JOptionPane from a component
    private JOptionPane getOptionPane(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JOptionPane) {
                return (JOptionPane) comp;
            }
            if (comp instanceof Container) {
                JOptionPane pane = getOptionPane((Container) comp);
                if (pane != null) {
                    return pane;
                }
            }
        }
        return null;
    }
}

