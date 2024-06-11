import org.junit.*;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testConstructor() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui);
    }
    
    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        
        // Test Exit command
        ActionEvent exitEvent = new ActionEvent(gui, 1, "Exit");
        gui.actionPerformed(exitEvent);
        // Expected: exit program
        // No assertion required
        
        // Test knownauthor command
        ActionEvent knownAuthorEvent = new ActionEvent(gui, 1, "knownauthor");
        gui.actionPerformed(knownAuthorEvent);
        // Expected: enable currentAuthor text field
        assertTrue(gui.currentAuthor.isEnabled());
        
        // Test unknownauthor command
        ActionEvent unknownAuthorEvent = new ActionEvent(gui, 1, "unknownauthor");
        gui.actionPerformed(unknownAuthorEvent);
        // Expected: disable currentAuthor text field
        assertFalse(gui.currentAuthor.isEnabled());
        
        // Test Import command
        ActionEvent importEvent = new ActionEvent(gui, 1, "Import");
        gui.actionPerformed(importEvent);
        // Expected: add document to driver, update listKnown or listUnknown
        // No assertion required
        
        // Test Canonicize command
        ActionEvent canonicizeEvent = new ActionEvent(gui, 1, "Canonicize");
        gui.actionPerformed(canonicizeEvent);
        // Expected: start preprocessEngineDriver, update progressCanonicize
        // No assertion required
        
        // Test CreateEventSet command
        ActionEvent createEventSetEvent = new ActionEvent(gui, 1, "CreateEventSet");
        gui.actionPerformed(createEventSetEvent);
        // Expected: start createEventSetDriver, update progressEventSet
        // No assertion required
        
        // Test Analyze command
        ActionEvent analyzeEvent = new ActionEvent(gui, 1, "Analyze");
        gui.actionPerformed(analyzeEvent);
        // Expected: start runStatisticalAnalysisDriver, update progressAnalysis, append results to listResults
        // No assertion required
        
        // Test demo command
        ActionEvent demoEvent = new ActionEvent(gui, 1, "demo");
        gui.actionPerformed(demoEvent);
        // Expected: print "Need to Demo"
        // No assertion required
        
        // Test unknown command
        ActionEvent unknownEvent = new ActionEvent(gui, 1, "unknown");
        gui.actionPerformed(unknownEvent);
        // Expected: print "unknown"
        // No assertion required
    }
}