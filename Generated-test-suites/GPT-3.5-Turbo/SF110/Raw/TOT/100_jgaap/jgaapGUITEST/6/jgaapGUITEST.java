import org.junit.*;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testActionPerformed_ExitCommand_ShouldExitProgram() {
        jgaapGUI gui = new jgaapGUI();
        
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit");
        gui.actionPerformed(e);
        
        // Verify that the program has exited by checking if the GUI is null
        assertNull(gui);
    }
    
    @Test
    public void testActionPerformed_KnownAuthorCommand_ShouldEnableAuthorTextField() {
        jgaapGUI gui = new jgaapGUI();
        gui.currentAuthor.setEnabled(false);
        
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "knownauthor");
        gui.actionPerformed(e);
        
        // Verify that the author text field is enabled
        assertTrue(gui.currentAuthor.isEnabled());
    }
    
    @Test
    public void testActionPerformed_UnknownAuthorCommand_ShouldDisableAuthorTextField() {
        jgaapGUI gui = new jgaapGUI();
        gui.currentAuthor.setEnabled(true);
        
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "unknownauthor");
        gui.actionPerformed(e);
        
        // Verify that the author text field is disabled
        assertFalse(gui.currentAuthor.isEnabled());
    }
    
    @Test
    public void testActionPerformed_ImportCommand_ShouldAddDocumentAndAppendToList() {
        jgaapGUI gui = new jgaapGUI();
        gui.buttonGroup1.setSelected(gui.radioKnown.getModel(), true);
        gui.currentAuthor.setText("John Doe");
        String fileName = "test.txt";
        
        assertEquals("", gui.listKnown.getText());
        
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Import");
        gui.actionPerformed(e);
        
        // Verify that the document is added and the list is updated
        assertTrue(gui.listKnown.getText().contains("John Doe - " + fileName));
    }
    
    @Test
    public void testActionPerformed_CanonicizeCommand_ShouldStartPreprocessEngineDriver() {
        jgaapGUI gui = new jgaapGUI();
        gui.canonicizers[0].setSelected(true);
        gui.canonicizers[1].setSelected(true);
        gui.canonicizers[2].setSelected(true);
        
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Canonicize");
        gui.actionPerformed(e);
        
        // Verify that the preprocessEngineDriver is started with the correct arguments
        assertNotNull(gui.driver.preprocessEngineDriver);
        // TODO: Verify arguments passed to preprocessEngineDriver
    }
    
    @Test
    public void testActionPerformed_CreateEventSetCommand_ShouldStartCreateEventSetDriver() {
        jgaapGUI gui = new jgaapGUI();
        gui.eventifiers[0].setSelected(true);
        
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "CreateEventSet");
        gui.actionPerformed(e);
        
        // Verify that the createEventSetDriver is started with the correct arguments
        assertNotNull(gui.driver.createEventSetDriver);
        // TODO: Verify arguments passed to createEventSetDriver
    }
    
    @Test
    public void testActionPerformed_AnalyzeCommand_ShouldStartRunStatisticalAnalysisDriver() {
        jgaapGUI gui = new jgaapGUI();
        gui.radioCrossEnt.setSelected(true);
        
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Analyze");
        gui.actionPerformed(e);
        
        // Verify that the runStatisticalAnalysisDriver is started with the correct arguments
        assertNotNull(gui.driver.runStatisticalAnalysisDriver);
        // TODO: Verify arguments passed to runStatisticalAnalysisDriver
    }
    
    @Test
    public void testActionPerformed_DemoCommand_ShouldCallLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "demo");
        gui.actionPerformed(e);
        
        // Verify that the loadDemo method is called
        // TODO: Verify that System.out.println is called with "Need to Demo"
    }
}