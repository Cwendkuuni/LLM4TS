import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mockito;

public class MacawWorkBenchTest {

    private MacawWorkBench macawWorkBench;
    private SessionProperties sessionPropertiesMock;
    
    @Before
    public void setup() {
        sessionPropertiesMock = Mockito.mock(SessionProperties.class);
        macawWorkBench = new MacawWorkBench(sessionPropertiesMock);
    }
    
    @Test
    public void testMacawWorkBench_show() {
        macawWorkBench.show();
        // Check that the dialog is visible
        assertTrue(macawWorkBench.dialog.isVisible());
    }
    
    @Test
    public void testMacawWorkBench_actionPerformed_editConstants() {
        macawWorkBench.actionPerformed(Mockito.mock(ActionEvent.class));
        // Check that the editConstants method is called
        Mockito.verify(macawWorkBench).editConstants();
    }
    
    @Test
    public void testMacawWorkBench_actionPerformed_editVariables() {
        macawWorkBench.actionPerformed(Mockito.mock(ActionEvent.class));
        // Check that the editVariables method is called
        Mockito.verify(macawWorkBench).editVariables();
    }
    
    @Test
    public void testMacawWorkBench_actionPerformed_exportVariableData() {
        macawWorkBench.actionPerformed(Mockito.mock(ActionEvent.class));
        // Check that the exportVariableData method is called
        Mockito.verify(macawWorkBench).exportVariableData();
    }
    
    @Test
    public void testMacawWorkBench_actionPerformed_exit() {
        macawWorkBench.actionPerformed(Mockito.mock(ActionEvent.class));
        // Check that the exit method is called
        Mockito.verify(macawWorkBench).exit();
    }
    
    @Test
    public void testMacawWorkBench_editConstants() {
        VariableConstantsEditor variableConstantsEditorMock = Mockito.mock(VariableConstantsEditor.class);
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory()).thenReturn(Mockito.mock(UserInterfaceFactory.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createDialog()).thenReturn(Mockito.mock(JDialog.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createButton(Mockito.anyString())).thenReturn(Mockito.mock(JButton.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createPanel()).thenReturn(Mockito.mock(JPanel.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createImmutableTextArea(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Mockito.mock(JTextArea.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createGridBagConstraints()).thenReturn(Mockito.mock(GridBagConstraints.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createDialog().setTitle(Mockito.anyString())).thenReturn(Mockito.mock(JDialog.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createPanel().add(Mockito.any(Component.class), Mockito.any(GridBagConstraints.class))).thenReturn(Mockito.mock(JPanel.class));
        
        macawWorkBench.editConstants();
        // Check that the VariableConstantsEditor is created and shown
        Mockito.verifyNew(VariableConstantsEditor.class).withArguments(sessionPropertiesMock, true);
        Mockito.verify(variableConstantsEditorMock).show();
    }
    
    @Test
    public void testMacawWorkBench_editVariables() {
        MacawVariableBrowser macawVariableBrowserMock = Mockito.mock(MacawVariableBrowser.class);
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory()).thenReturn(Mockito.mock(UserInterfaceFactory.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createDialog()).thenReturn(Mockito.mock(JDialog.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createButton(Mockito.anyString())).thenReturn(Mockito.mock(JButton.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createPanel()).thenReturn(Mockito.mock(JPanel.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createGridBagConstraints()).thenReturn(Mockito.mock(GridBagConstraints.class));
        
        macawWorkBench.editVariables();
        // Check that the MacawVariableBrowser is created and shown
        Mockito.verifyNew(MacawVariableBrowser.class).withArguments(sessionPropertiesMock);
        Mockito.verify(macawVariableBrowserMock).show();
    }
    
    @Test
    public void testMacawWorkBench_exportVariableData() {
        ExportVariablesToMacawXML exportVariablesToMacawXMLMock = Mockito.mock(ExportVariablesToMacawXML.class);
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory()).thenReturn(Mockito.mock(UserInterfaceFactory.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createDialog()).thenReturn(Mockito.mock(JDialog.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createButton(Mockito.anyString())).thenReturn(Mockito.mock(JButton.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createPanel()).thenReturn(Mockito.mock(JPanel.class));
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory().createGridBagConstraints()).thenReturn(Mockito.mock(GridBagConstraints.class));
        
        macawWorkBench.exportVariableData();
        // Check that the ExportVariablesToMacawXML is created and export method is called
        Mockito.verifyNew(ExportVariablesToMacawXML.class).withArguments(sessionPropertiesMock);
        Mockito.verify(exportVariablesToMacawXMLMock).export();
    }
    
    @Test
    public void testMacawWorkBench_exit() {
        macawWorkBench.exit();
        // Check that the System.exit method is called with exit code 0
        Mockito.verifyStatic(System.class);
        System.exit(0);
    }
}