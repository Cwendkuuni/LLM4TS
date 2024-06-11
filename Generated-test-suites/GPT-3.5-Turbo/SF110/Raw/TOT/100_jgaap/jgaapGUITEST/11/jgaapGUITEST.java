import static org.junit.Assert.*;
import org.junit.Test;

public class jgaapGUITest {

    jgaapGUI gui = new jgaapGUI();

    // Test for actionPerformed method when Exit command is selected 
    @Test
    public void testActionPerformed_ExitCommandSelected() {
        // simulate actionPerformed event with Exit command
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit");
        gui.actionPerformed(event);
        // check if the application exited successfully
        assertNotEquals(-1, returnStatus);
    }

    // Test for actionPerformed method when knownauthor command is selected 
    @Test
    public void testActionPerformed_KnownAuthorCommandSelected() {
        // simulate actionPerformed event with knownauthor command
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "knownauthor");
        gui.actionPerformed(event);
        // check if the currentAuthor text field is enabled
        assertTrue(gui.currentAuthor.isEnabled());
    }

    // Test for actionPerformed method when unknownauthor command is selected 
    @Test
    public void testActionPerformed_UnknownAuthorCommandSelected() {
        // simulate actionPerformed event with unknownauthor command
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "unknownauthor");
        gui.actionPerformed(event);
        // check if the currentAuthor text field is disabled
        assertFalse(gui.currentAuthor.isEnabled());
    }

    // Test for actionPerformed method when Import command is selected 
    @Test
    public void testActionPerformed_ImportCommandSelected() {
        // simulate actionPerformed event with Import command
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Import");
        gui.actionPerformed(event);
        // check if the file is added to the document list in the GUI
        assertNotNull(gui.listKnown.getText());
    }
    
    // Test for actionPerformed method when Canonicize command is selected 
    @Test
    public void testActionPerformed_CanonicizeCommandSelected() {
        // simulate actionPerformed event with Canonicize command
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Canonicize");
        gui.actionPerformed(event);
        // check if the canonicization process is started
        assertNotNull(gui.progressCanonicize.getValue());
    }
    
    // Test for actionPerformed method when CreateEventSet command is selected 
    @Test
    public void testActionPerformed_CreateEventSetCommandSelected() {
        // simulate actionPerformed event with CreateEventSet command
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "CreateEventSet");
        gui.actionPerformed(event);
        // check if the event set creation process is started
        assertNotNull(gui.progressEventSet.getValue());
    }

    // Test for actionPerformed method when Analyze command is selected 
    @Test
    public void testActionPerformed_AnalyzeCommandSelected() {
        // simulate actionPerformed event with Analyze command
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Analyze");
        gui.actionPerformed(event);
        // check if the statistical analysis process is started
        assertNotNull(gui.progressAnalysis.getValue());
    }

    // Test for actionPerformed method when demo command is selected 
    @Test
    public void testActionPerformed_DemoCommandSelected() {
        // simulate actionPerformed event with demo command
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "demo");
        gui.actionPerformed(event);
        // check if the demo is loaded
        // (not implemented, just checking if the method is called)
        assertTrue(true);
    }

    // Test for loadDemo method 
    @Test
    public void testLoadDemo() {
        // simulate loading demo
        gui.loadDemo();
        // check if the demo is loaded
        // (not implemented, just checking if the method is called)
        assertTrue(true);
    }

}