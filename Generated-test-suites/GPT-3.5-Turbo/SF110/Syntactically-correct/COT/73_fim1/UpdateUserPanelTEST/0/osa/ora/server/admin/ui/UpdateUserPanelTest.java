import static org.junit.Assert.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

    @Test
    public void testUpdateModel() {
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        updateUserPanel.updateModel();
        // Assert statements
    }

    @Test
    public void testInitComponents() {
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        updateUserPanel.initComponents();
        // Assert statements
    }

    @Test
    public void testjButton1ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        ActionEvent evt = new ActionEvent(updateUserPanel, ActionEvent.ACTION_PERFORMED, "");
        updateUserPanel.jButton1ActionPerformed(evt);
        // Assert statements
    }

    @Test
    public void testjButton2ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        ActionEvent evt = new ActionEvent(updateUserPanel, ActionEvent.ACTION_PERFORMED, "");
        updateUserPanel.jButton2ActionPerformed(evt);
        // Assert statements
    }

    @Test
    public void testjButton3ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        ActionEvent evt = new ActionEvent(updateUserPanel, ActionEvent.ACTION_PERFORMED, "");
        updateUserPanel.jButton3ActionPerformed(evt);
        // Assert statements
    }

    @Test
    public void testjButton4ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        ActionEvent evt = new ActionEvent(updateUserPanel, ActionEvent.ACTION_PERFORMED, "");
        updateUserPanel.jButton4ActionPerformed(evt);
        // Assert statements
    }

    @Test
    public void testjComboBox1ItemStateChanged() {
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        ItemEvent evt = new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.ITEM_STATE_CHANGED, "", ItemEvent.SELECTED);
        updateUserPanel.jComboBox1ItemStateChanged(evt);
        // Assert statements
    }

    @Test
    public void testreset() {
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        updateUserPanel.reset();
        // Assert statements
    }

    @Test
    public void testfillUser() {
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        User user = new User();
        updateUserPanel.fillUser(user);
        // Assert statements
    }

    @Test
    public void testjComboBox2ItemStateChanged() {
        AdminApp adminApp = new AdminApp();
        ControlPanel controlPanel = new ControlPanel();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        ItemEvent evt = new ItemEvent(updateUserPanel.jComboBox2, ItemEvent.ITEM_STATE_CHANGED, "", ItemEvent.SELECTED);
        updateUserPanel.jComboBox2ItemStateChanged(evt);
        // Assert statements
    }

}