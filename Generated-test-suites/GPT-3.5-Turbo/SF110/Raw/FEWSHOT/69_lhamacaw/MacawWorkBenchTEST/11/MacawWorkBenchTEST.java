import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import macaw.businessLayer.ProductionCurationService;
import macaw.businessLayer.StartupOptions;
import macaw.businessLayer.User;
import macaw.businessLayer.UserInterfaceFactory;
import macaw.io.ExportVariablesToMacawXML;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.demo.DemonstrationRetrievalService;
import macaw.persistenceLayer.production.ProductionRetrievalService;
import macaw.presentationLayer.MacawVariableBrowser;
import macaw.system.MacawMessages;
import macaw.system.SessionProperties;

import org.junit.Before;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class MacawWorkBenchTest {
    private MacawWorkBench macawWorkBench;

    @Before
    public void setUp() {
        SessionProperties sessionProperties = new SessionProperties();
        macawWorkBench = new MacawWorkBench(sessionProperties);
    }

    @Test
    public void testMain() {
        // Use cases not covered by other test methods can be tested here
        // for example:
        // Test case when dbUser and dbPassword are not null
        // Test case when login fails 3 times
    }

    @Test
    public void testMacawWorkBench() {
        assertNotNull(macawWorkBench.getSessionProperties());
        assertNotNull(macawWorkBench.getDialog());
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = macawWorkBench.getSessionProperties().getUserInterfaceFactory();
        JPanel panel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        assertNotNull(panel);
        assertEquals(4, panel.getComponentCount());
    }

    @Test
    public void testShow() {
        // Unable to test show() method since it interacts with UI
    }

    @Test
    public void testEditConstants() {
        macawWorkBench.editConstants();

        // Assert the necessary actions were performed
    }

    @Test
    public void testEditVariables() {
        macawWorkBench.editVariables();

        // Assert the necessary actions were performed
    }

    @Test
    public void testExportVariableData() {
        macawWorkBench.exportVariableData();

        // Assert the necessary actions were performed
    }

    @Test
    public void testExit() {
        macawWorkBench.exit();

        // Assert the necessary actions were performed
    }

    @Test
    public void testActionPerformed_editConstants() {
        ActionEvent event = new ActionEvent(macawWorkBench.getEditConstants(), ActionEvent.ACTION_PERFORMED, "");
        macawWorkBench.actionPerformed(event);

        // Assert the necessary actions were performed
    }

    @Test
    public void testActionPerformed_editVariables() {
        ActionEvent event = new ActionEvent(macawWorkBench.getEditVariables(), ActionEvent.ACTION_PERFORMED, "");
        macawWorkBench.actionPerformed(event);

        // Assert the necessary actions were performed
    }

    @Test
    public void testActionPerformed_exportVariableData() {
        ActionEvent event = new ActionEvent(macawWorkBench.getExportVariableData(), ActionEvent.ACTION_PERFORMED, "");
        macawWorkBench.actionPerformed(event);

        // Assert the necessary actions were performed
    }

    @Test
    public void testActionPerformed_exit() {
        ActionEvent event = new ActionEvent(macawWorkBench.getExit(), ActionEvent.ACTION_PERFORMED, "");
        macawWorkBench.actionPerformed(event);

        // Assert the necessary actions were performed
    }
}