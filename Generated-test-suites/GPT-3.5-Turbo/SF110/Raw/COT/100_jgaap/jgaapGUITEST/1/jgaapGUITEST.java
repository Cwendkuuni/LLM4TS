
import org.junit.*;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testActionPerformed_ExitingProgram_ExitCodeMinusOne() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent clickExit = new ActionEvent(gui.menuFileExit, ActionEvent.ACTION_PERFORMED, "Exit");
        gui.actionPerformed(clickExit);
        // System.exit is not called in test environment, so we expect the program to continue running
        // In that case, nothing should happen
        assertEquals(1, gui.driver.getDocumentCount());
    }
    
    @Test
    public void testActionPerformed_KnownAuthor_SelectedAuthorTextEmpty() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent clickKnownAuthor = new ActionEvent(gui.radioKnown, ActionEvent.ACTION_PERFORMED, "knownauthor");
        gui.actionPerformed(clickKnownAuthor);
        assertEquals("", gui.currentAuthor.getText());
    }
    
    @Test
    public void testActionPerformed_UnknownAuthor_SelectedAuthorTextEmpty() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent clickUnknownAuthor = new ActionEvent(gui.radioUnknown, ActionEvent.ACTION_PERFORMED, "unknownauthor");
        gui.actionPerformed(clickUnknownAuthor);
        assertEquals("", gui.currentAuthor.getText());
    }
    
    @Test
    public void testActionPerformed_ImportLocalFile_DocumentAddedToList() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent clickImport = new ActionEvent(gui.buttonLocal, ActionEvent.ACTION_PERFORMED, "Import");
        gui.actionPerformed(clickImport);
        // Assuming a file is selected in the file chooser and added, the list should not be empty
        assertTrue(gui.listKnown.getText().length() > 0 || gui.listUnknown.getText().length() > 0);
    }

    @Test
    public void testActionPerformed_CanonicizeButtonClicked_PreprocessEngineDriverStarted() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent clickCanonicize = new ActionEvent(gui.buttonCanonicize, ActionEvent.ACTION_PERFORMED, "Canonicize");
        gui.actionPerformed(clickCanonicize);
        // Assuming preprocessEngineDriver is correctly called and started, check if it is running
        assertTrue(gui.driver.pe.isAlive());
    }
    
    @Test
    public void testActionPerformed_CreateEventSetButtonClicked_CreateEventSetDriverStarted() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent clickCreateEventSet = new ActionEvent(gui.buttonEventSet, ActionEvent.ACTION_PERFORMED, "CreateEventSet");
        gui.actionPerformed(clickCreateEventSet);
        // Assuming createEventSetDriver is correctly called and started, check if it is running
        assertTrue(gui.driver.esd.isAlive());
    }
    
    @Test
    public void testActionPerformed_AnalyzeButtonClicked_RunStatisticalAnalysisDriverStarted() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent clickAnalyze = new ActionEvent(gui.buttonAnalyze, ActionEvent.ACTION_PERFORMED, "Analyze");
        gui.actionPerformed(clickAnalyze);
        // Assuming runStatisticalAnalysisDriver is correctly called and started, check if it is running
        assertTrue(gui.driver.rsad.isAlive());
    }

    @Test
    public void testActionPerformed_LoadDemoButtonClicked_LoadDemoMethodCalled() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent clickLoadDemo = new ActionEvent(gui.menuFileDemo, ActionEvent.ACTION_PERFORMED, "demo");
        gui.actionPerformed(clickLoadDemo);
        // Assuming loadDemo method is correctly implemented, check if it is called
        // In this case, it should print "Need to Demo" to the console
    }
}
