import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import javax.swing.border.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import macaw.io.*;
import java.awt.event.*;

public class MacawWorkBenchTest {
    private MacawWorkBench workBench;
    private SessionProperties sessionProperties;

    @Before
    public void setUp() {
        sessionProperties = new SessionProperties();
        workBench = new MacawWorkBench(sessionProperties);
    }

    @Test
    public void testShow() {
        // Test that the dialog is visible after calling show()
        workBench.show();
        assertTrue(workBench.dialog.isVisible());
    }

    @Test
    public void testEditConstants() {
        // Test that the VariableConstantsEditor dialog is shown after calling editConstants()
        workBench.editConstants();
        assertTrue(workBench.variableConstantsEditor.isShown());
    }

    @Test
    public void testEditVariables() {
        // Test that the MacawVariableBrowser dialog is shown after calling editVariables()
        workBench.editVariables();
        assertTrue(workBench.macawVariableBrowser.isShown());
    }

    @Test
    public void testExportVariableData() {
        // Test that the ExportVariablesToMacawXML dialog is shown after calling exportVariableData()
        workBench.exportVariableData();
        assertTrue(workBench.exportToMacawXML.isShown());
    }

    @Test
    public void testExit() {
        // Test that the system exits with code 0 after calling exit()
        workBench.exit();
        assertEquals(0, System.exitCode);
    }

    @Test
    public void testActionPerformed() {
        // Test that the corresponding method is called based on the event source
        // Edit Constants Button
        ActionEvent editConstantsEvent = new ActionEvent(workBench.editConstants, ActionEvent.ACTION_PERFORMED, "");
        workBench.actionPerformed(editConstantsEvent);
        assertTrue(workBench.variableConstantsEditor.isShown());

        // Edit Variables Button
        ActionEvent editVariablesEvent = new ActionEvent(workBench.editVariables, ActionEvent.ACTION_PERFORMED, "");
        workBench.actionPerformed(editVariablesEvent);
        assertTrue(workBench.macawVariableBrowser.isShown());

        // Export Variable Data Button
        ActionEvent exportVariableDataEvent = new ActionEvent(workBench.exportVariableData, ActionEvent.ACTION_PERFORMED, "");
        workBench.actionPerformed(exportVariableDataEvent);
        assertTrue(workBench.exportToMacawXML.isShown());

        // Exit Button
        ActionEvent exitEvent = new ActionEvent(workBench.exit, ActionEvent.ACTION_PERFORMED, "");
        workBench.actionPerformed(exitEvent);
        assertEquals(0, System.exitCode);
    }
}