import static org.junit.Assert.*;
import org.junit.Test;

public class jgaapGUITest {

    @Test
    public void testjgaapGUIConstructor() {
        jgaapGUI testGUI = new jgaapGUI();
        assertNotNull(testGUI);
    }
    
    @Test
    public void testActionPerformedExit() {
        jgaapGUI testGUI = new jgaapGUI();
        ActionEvent testEvent = new ActionEvent(testGUI, ActionEvent.ACTION_PERFORMED, "Exit");
        testGUI.actionPerformed(testEvent);
        // Verify that the program exits
    }
    
    @Test
    public void testActionPerformedUnknownAuthor() {
        jgaapGUI testGUI = new jgaapGUI();
        ActionEvent testEvent = new ActionEvent(testGUI, ActionEvent.ACTION_PERFORMED, "unknownauthor");
        testGUI.actionPerformed(testEvent);
        // Verify that the current author field is disabled
    }
    
    @Test
    public void testActionPerformedDemo() {
        jgaapGUI testGUI = new jgaapGUI();
        ActionEvent testEvent = new ActionEvent(testGUI, ActionEvent.ACTION_PERFORMED, "demo");
        testGUI.actionPerformed(testEvent);
        // Verify that the demo is loaded
    }
    
    // Add more test cases for other methods
    
}