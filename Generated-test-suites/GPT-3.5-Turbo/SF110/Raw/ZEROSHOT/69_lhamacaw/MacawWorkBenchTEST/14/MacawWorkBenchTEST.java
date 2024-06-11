package macaw.presentationLayer;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import macaw.businessLayer.*;

public class MacawWorkBenchTest {

    private MacawWorkBench macawWorkBench;

    @Before
    public void setUp() {
        SessionProperties sessionProperties = new SessionProperties();
        macawWorkBench = new MacawWorkBench(sessionProperties);
    }

    @Test
    public void testShow() {
        macawWorkBench.show();
        // Assert that the dialog is visible
    }

    @Test
    public void testEditConstants() {
        macawWorkBench.editConstants();
        // Assert that the VariableConstantsEditor dialog is displayed
    }

    @Test
    public void testEditVariables() {
        macawWorkBench.editVariables();
        // Assert that the MacawVariableBrowser dialog is displayed
    }

    @Test
    public void testExportVariableData() {
        macawWorkBench.exportVariableData();
        // Assert that the ExportVariablesToMacawXML method is called
    }

    @Test
    public void testExit() {
        macawWorkBench.exit();
        // Assert that the System exit is called with code 0
    }

    @Test
    public void testActionPerformed_editConstants() {
        // Simulate the user clicking the editConstants button
        ActionEvent event = new ActionEvent(macawWorkBench.editConstants, ActionEvent.ACTION_PERFORMED, "");
        macawWorkBench.actionPerformed(event);
        // Assert that the editConstants method is called
    }

    @Test
    public void testActionPerformed_editVariables() {
        // Simulate the user clicking the editVariables button
        ActionEvent event = new ActionEvent(macawWorkBench.editVariables, ActionEvent.ACTION_PERFORMED, "");
        macawWorkBench.actionPerformed(event);
        // Assert that the editVariables method is called
    }

    @Test
    public void testActionPerformed_exportVariableData() {
        // Simulate the user clicking the exportVariableData button
        ActionEvent event = new ActionEvent(macawWorkBench.exportVariableData, ActionEvent.ACTION_PERFORMED, "");
        macawWorkBench.actionPerformed(event);
        // Assert that the exportVariableData method is called
    }

    @Test
    public void testActionPerformed_exit() {
        // Simulate the user clicking the exit button
        ActionEvent event = new ActionEvent(macawWorkBench.exit, ActionEvent.ACTION_PERFORMED, "");
        macawWorkBench.actionPerformed(event);
        // Assert that the exit method is called
    }
}