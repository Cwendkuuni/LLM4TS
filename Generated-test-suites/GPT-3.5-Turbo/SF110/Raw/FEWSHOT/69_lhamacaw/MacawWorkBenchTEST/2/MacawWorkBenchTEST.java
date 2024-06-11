package macaw.presentationLayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import macaw.businessLayer.ProductionCurationService;
import macaw.businessLayer.DemonstrationCurationService;
import macaw.persistenceLayer.demo.DemonstrationRetrievalService;
import macaw.persistenceLayer.production.ProductionRetrievalService;
import macaw.system.SessionProperties;
import macaw.system.StartupOptions;
import macaw.system.User;
import macaw.io.LoginDialog;
import macaw.io.ExportVariablesToMacawXML;
import macaw.persistenceLayer.demo.VariableConstantsEditor;
import macaw.persistenceLayer.demo.MacawVariableBrowser;

public class MacawWorkBenchTest {
    private MacawWorkBench macawWorkBench;
    private SessionProperties sessionProperties;
    private StartupOptions startupOptions;

    @Before
    public void setUp() {
        sessionProperties = new SessionProperties();
        startupOptions = (StartupOptions) sessionProperties.getProperty("startupOptions");
    }

    @Test
    public void testMain() {
        MacawWorkBench.main(new String[] {});
    }

    @Test
    public void testMacawWorkBench() {
        macawWorkBench = new MacawWorkBench(sessionProperties);
        assertEquals(sessionProperties, macawWorkBench.getSessionProperties());
    }

    @Test
    public void testShow() {
        macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.show();
    }

    @Test
    public void testEditConstants() {
        macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.editConstants();
    }

    @Test
    public void testEditVariables() {
        macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.editVariables();
    }

    @Test
    public void testExportVariableData() {
        macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.exportVariableData();
    }

    @Test
    public void testExit() {
        macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.exit();
    }

    @Test
    public void testActionPerformed() {
        macawWorkBench = new MacawWorkBench(sessionProperties);

        macawWorkBench.actionPerformed(null);

        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.getEditConstantsButton(), 0, ""));
        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.getEditVariablesButton(), 0, ""));
        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.getExportVariableDataButton(), 0, ""));
        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.getExitButton(), 0, ""));
    }
}