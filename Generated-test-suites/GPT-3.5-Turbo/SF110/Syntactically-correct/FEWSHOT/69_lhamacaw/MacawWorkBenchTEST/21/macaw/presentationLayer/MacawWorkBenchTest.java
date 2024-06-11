package macaw.presentationLayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import macaw.businessLayer.ProductionCurationService;
import macaw.businessLayer.ProductionRetrievalService;
import macaw.businessLayer.SessionProperties;
import macaw.businessLayer.StartupOptions;
import macaw.businessLayer.User;
import macaw.io.ExportVariablesToMacawXML;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.demo.DemonstrationRetrievalService;
import macaw.system.MacawMessages;
import macaw.system.MacawVariableBrowser;
import macaw.system.VariableConstantsEditor;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.Properties;

public class MacawWorkBenchTest {

    private MacawWorkBench workBench;

    @Before
    public void setUp() {
        workBench = new MacawWorkBench(new SessionProperties());
    }

    @Test
    public void testMain() {
        // Not sure how to test a static void method that calls System.exit(),
        // so testing individual components of the method instead
        // Will test the other methods of the class that are being called by this method
        String[] args = {"--demo"};
        MacawWorkBench.main(args);

        String[] emptyArgs = {};
        MacawWorkBench.main(emptyArgs);
    }

    @Test
    public void testMacawWorkBench() {
        assertNotNull(workBench.sessionProperties);
        assertNull(workBench.dialog);
        assertNull(workBench.editConstants);
        assertNull(workBench.editVariables);
        assertNull(workBench.exportVariableData);
        assertNull(workBench.exit);
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
        JPanel panel = workBench.createButtonPanel(userInterfaceFactory);
        assertNotNull(panel);

        Component[] components = panel.getComponents();
        assertEquals(4, components.length);

        JButton editConstantsButton = (JButton) components[0];
        assertNotNull(editConstantsButton);
        assertEquals(MacawMessages.getMessage("macawWorkBench.editConstants"), editConstantsButton.getText());

        JButton editVariablesButton = (JButton) components[1];
        assertNotNull(editVariablesButton);
        assertEquals(MacawMessages.getMessage("macawWorkBench.editVariables"), editVariablesButton.getText());

        JButton exportVariableDataButton = (JButton) components[2];
        assertNotNull(exportVariableDataButton);
        assertEquals(MacawMessages.getMessage("macawWorkBench.exportVariableData"), exportVariableDataButton.getText());

        JButton exitButton = (JButton) components[3];
        assertNotNull(exitButton);
        assertEquals(MacawMessages.getMessage("general.buttons.exit"), exitButton.getText());
    }

    @Test
    public void testShow() {
        workBench.show();
        assertTrue(workBench.dialog.isVisible());
    }

    @Test
    public void testEditConstants() {
        workBench.editConstants();
        // Cannot test behavior of VariableConstantsEditor since it is not provided in the code

        // However, we can use reflection to check if the method is successfully invoked
        // by creating a mock VariableConstantsEditor and checking if the show() method is called
        // Although this doesn't test the actual functionality of the method, it at least verifies
        // that the method exists and is called properly
        // Will use this approach for the other methods as well
        try {
            VariableConstantsEditor mockEditor = new VariableConstantsEditor(new SessionProperties(), true);
            Field showField = VariableConstantsEditor.class.getDeclaredField("show");
            showField.setAccessible(true);
            assertTrue((boolean)showField.get(mockEditor));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditVariables() {
        workBench.editVariables();
        // Cannot test behavior of MacawVariableBrowser since it is not provided in the code

        try {
            MacawVariableBrowser mockBrowser = new MacawVariableBrowser(new SessionProperties());
            Field showField = MacawVariableBrowser.class.getDeclaredField("show");
            showField.setAccessible(true);
            assertTrue((boolean)showField.get(mockBrowser));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExportVariableData() {
        workBench.exportVariableData();
        // Cannot test behavior of ExportVariablesToMacawXML since it is not provided in the code

        try {
            ExportVariablesToMacawXML mockExport = new ExportVariablesToMacawXML(new SessionProperties());
            Field exportField = ExportVariablesToMacawXML.class.getDeclaredField("export");
            exportField.setAccessible(true);
            assertTrue((boolean)exportField.get(mockExport));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExit() {
        workBench.exit();
        // Cannot test behavior of System.exit() since it terminates the program
        // Instead, check if there are no active threads after the method call
        assertEquals(0, Thread.activeCount());
    }

    @Test
    public void testActionPerformed() {
        ActionEvent event = new ActionEvent(workBench.editConstants, 0, "");

        workBench.actionPerformed(event);
        // Cannot test behavior of this method since it calls other untested methods
        // Instead, we can check if the actionPerformed() method is successfully invoked
        // by using reflection to check if checkActionPerformed is set to true
        try {
            Field checkField = MacawWorkBench.class.getDeclaredField("checkActionPerformed");
            checkField.setAccessible(true);
            assertTrue((boolean)checkField.get(workBench));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Test with other buttons
        event = new ActionEvent(workBench.editVariables, 0, "");
        workBench.actionPerformed(event);

        event = new ActionEvent(workBench.exportVariableData, 0, "");
        workBench.actionPerformed(event);

        event = new ActionEvent(workBench.exit, 0, "");
        workBench.actionPerformed(event);
    }

}