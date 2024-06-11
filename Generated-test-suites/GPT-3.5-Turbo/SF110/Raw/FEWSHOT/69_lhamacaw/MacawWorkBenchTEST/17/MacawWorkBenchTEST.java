
import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;

import macaw.businessLayer.SessionProperties;
import macaw.io.ExportVariablesToMacawXML;
import macaw.presentationLayer.MacawWorkBench;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.persistenceLayer.production.ProductionRetrievalService;
import macaw.persistenceLayer.demo.DemonstrationRetrievalService;
import macaw.system.User;
import macaw.system.StartupOptions;
import macaw.ui.UserInterfaceFactory;
import macaw.ui.VariableConstantsEditor;

import org.junit.Before;
import org.mockito.Mockito;

public class MacawWorkBenchTest {
    private MacawWorkBench macawWorkBench;
    private SessionProperties sessionProperties;
    
    @Before
    public void setUp() {
        sessionProperties = Mockito.mock(SessionProperties.class);
        macawWorkBench = new MacawWorkBench(sessionProperties);
    }
    
    @Test
    public void testMacawWorkBench() {
        // Verify that the MacawWorkBench dialog is created correctly
        assertNotNull(macawWorkBench.dialog);
        
        // Verify that the dialog title is set correctly
        assertEquals("macawWorkBench.title", macawWorkBench.dialog.getTitle());
        
        // Verify that the panel is created correctly
        JPanel panel = (JPanel)macawWorkBench.dialog.getContentPane().getComponent(0);
        assertNotNull(panel);
        
        // Verify that the instructions area is created correctly
        JTextArea instructionsArea = (JTextArea)panel.getComponent(0);
        assertNotNull(instructionsArea);
        assertEquals(3, instructionsArea.getRows());
        assertEquals(20, instructionsArea.getColumns());
        assertNotNull(instructionsArea.getBorder());
        
        // Verify that the button panel is created correctly
        JPanel buttonPanel = (JPanel)panel.getComponent(1);
        assertNotNull(buttonPanel);
        
        // Verify that the editConstants button is created correctly
        JButton editConstantsButton = (JButton)buttonPanel.getComponent(0);
        assertNotNull(editConstantsButton);
        assertEquals("macawWorkBench.editConstants", editConstantsButton.getActionCommand());
        assertNotNull(editConstantsButton.getToolTipText());
        editConstantsButton.doClick();
        Mockito.verify(macawWorkBench).editConstants();
        
        // Verify that the editVariables button is created correctly
        JButton editVariablesButton = (JButton)buttonPanel.getComponent(1);
        assertNotNull(editVariablesButton);
        assertEquals("macawWorkBench.editVariables", editVariablesButton.getActionCommand());
        assertNotNull(editVariablesButton.getToolTipText());
        editVariablesButton.doClick();
        Mockito.verify(macawWorkBench).editVariables();
        
        // Verify that the exportVariableData button is created correctly
        JButton exportVariableDataButton = (JButton)buttonPanel.getComponent(2);
        assertNotNull(exportVariableDataButton);
        assertEquals("macawWorkBench.exportVariableData", exportVariableDataButton.getActionCommand());
        assertNotNull(exportVariableDataButton.getToolTipText());
        exportVariableDataButton.doClick();
        Mockito.verify(macawWorkBench).exportVariableData();
        
        // Verify that the exit button is created correctly
        JButton exitButton = (JButton)buttonPanel.getComponent(3);
        assertNotNull(exitButton);
        assertEquals("general.buttons.exit", exitButton.getActionCommand());
        exitButton.doClick();
        Mockito.verify(macawWorkBench).exit();
        
        // Verify that the size of the dialog is set correctly
        assertEquals(300, macawWorkBench.dialog.getWidth());
        assertEquals(300, macawWorkBench.dialog.getHeight());
    }
    
    @Test
    public void testEditConstants() {
        // Verify that the VariableConstantsEditor is created correctly
        VariableConstantsEditor variableConstantsEditor = Mockito.mock(VariableConstantsEditor.class);
        Mockito.when(sessionProperties.getUserInterfaceFactory()).thenReturn(Mockito.mock(UserInterfaceFactory.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createPanel())
            .thenReturn(Mockito.mock(JPanel.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createGridBagConstraints())
            .thenReturn(Mockito.mock(GridBagConstraints.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createButton(Mockito.anyString()))
            .thenReturn(Mockito.mock(JButton.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createImmutableTextArea(Mockito.anyInt(), Mockito.anyInt()))
            .thenReturn(Mockito.mock(JTextArea.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createDialog())
            .thenReturn(Mockito.mock(JDialog.class));
        Mockito.when(sessionProperties.getProperty(Mockito.anyString()))
            .thenReturn(Mockito.mock(StartupOptions.class));
        Mockito.when(sessionProperties.getProperty("startupOptions")).thenReturn(Mockito.mock(StartupOptions.class));
        Mockito.when(sessionProperties.getProperty("currentUser")).thenReturn(Mockito.mock(User.class));
        Mockito.when(sessionProperties.getProperty("retrievalService")).thenReturn(Mockito.mock(DemonstrationRetrievalService.class));
        Mockito.when(sessionProperties.getProperty("database")).thenReturn(Mockito.mock(DemonstrationCurationService.class));
        macawWorkBench.editConstants();
        Mockito.verify(variableConstantsEditor).show();
    }
    
    @Test
    public void testEditVariables() {
        // Verify that the MacawVariableBrowser is created correctly
        MacawVariableBrowser macawVariableBrowser = Mockito.mock(MacawVariableBrowser.class);
        Mockito.when(sessionProperties.getUserInterfaceFactory()).thenReturn(Mockito.mock(UserInterfaceFactory.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createPanel())
            .thenReturn(Mockito.mock(JPanel.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createGridBagConstraints())
            .thenReturn(Mockito.mock(GridBagConstraints.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createButton(Mockito.anyString()))
            .thenReturn(Mockito.mock(JButton.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createImmutableTextArea(Mockito.anyInt(), Mockito.anyInt()))
            .thenReturn(Mockito.mock(JTextArea.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createDialog())
            .thenReturn(Mockito.mock(JDialog.class));
        Mockito.when(sessionProperties.getProperty(Mockito.anyString()))
            .thenReturn(Mockito.mock(StartupOptions.class));
        Mockito.when(sessionProperties.getProperty("startupOptions")).thenReturn(Mockito.mock(StartupOptions.class));
        Mockito.when(sessionProperties.getProperty("currentUser")).thenReturn(Mockito.mock(User.class));
        Mockito.when(sessionProperties.getProperty("retrievalService")).thenReturn(Mockito.mock(DemonstrationRetrievalService.class));
        Mockito.when(sessionProperties.getProperty("database")).thenReturn(Mockito.mock(DemonstrationCurationService.class));
        macawWorkBench.editVariables();
        Mockito.verify(macawVariableBrowser).show();
    }
    
    @Test
    public void testExportVariableData() {
        // Verify that the ExportVariablesToMacawXML is created correctly
        ExportVariablesToMacawXML exportVariablesToMacawXML = Mockito.mock(ExportVariablesToMacawXML.class);
        Mockito.when(sessionProperties.getUserInterfaceFactory()).thenReturn(Mockito.mock(UserInterfaceFactory.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createPanel())
            .thenReturn(Mockito.mock(JPanel.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createGridBagConstraints())
            .thenReturn(Mockito.mock(GridBagConstraints.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createButton(Mockito.anyString()))
            .thenReturn(Mockito.mock(JButton.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createImmutableTextArea(Mockito.anyInt(), Mockito.anyInt()))
            .thenReturn(Mockito.mock(JTextArea.class));
        Mockito.when(sessionProperties.getUserInterfaceFactory().createDialog())
            .thenReturn(Mockito.mock(JDialog.class));
        Mockito.when(sessionProperties.getProperty(Mockito.anyString()))
            .thenReturn(Mockito.mock(StartupOptions.class));
        Mockito.when(sessionProperties.getProperty("startupOptions")).thenReturn(Mockito.mock(StartupOptions.class));
        Mockito.when(sessionProperties.getProperty("currentUser")).thenReturn(Mockito.mock(User.class));
        Mockito.when(sessionProperties.getProperty("retrievalService")).thenReturn(Mockito.mock(DemonstrationRetrievalService.class));
        Mockito.when(sessionProperties.getProperty("database")).thenReturn(Mockito.mock(DemonstrationCurationService.class));
        macawWorkBench.exportVariableData();
        Mockito.verify(exportVariablesToMacawXML).export();
    }
    
    @Test
    public void testExit() {
        macawWorkBench.exit();
        Mockito.verify(macawWorkBench.dialog).setVisible(false);
        System.exit(0);
    }
    
    @Test
    public void testActionPerformed() {
        // Test editConstants button click
        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.editConstants, 1, ""));
        Mockito.verify(macawWorkBench).editConstants();
        
        // Test editVariables button click
        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.editVariables, 1, ""));
        Mockito.verify(macawWorkBench).editVariables();
        
        // Test exportVariableData button click
        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.exportVariableData, 1, ""));
        Mockito.verify(macawWorkBench).exportVariableData();
        
        // Test exit button click
        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.exit, 1, ""));
        Mockito.verify(macawWorkBench).exit();
    }
}
