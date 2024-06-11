import static org.junit.Assert.*;
import org.junit.Test;

public class jgaapGUITest {
    
    @Test
    public void testActionPerformedExit() {
        // Test actionPerformed method for Exit command
        // Edge case: Exiting the application
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
        assertNull(gui); // Application should exit
    }
    
    @Test
    public void testActionPerformedKnownAuthor() {
        // Test actionPerformed method for Known Author command
        // Typical use case: Selecting Known Author radio button
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        assertTrue(gui.currentAuthor.isEnabled()); // Known Author text field should be enabled
    }
    
    @Test
    public void testActionPerformedUnknownAuthor() {
        // Test actionPerformed method for Unknown Author command
        // Typical use case: Selecting Unknown Author radio button
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        assertFalse(gui.currentAuthor.isEnabled()); // Known Author text field should be disabled
    }
    
    @Test
    public void testActionPerformedImport() {
        // Test actionPerformed method for Import command
        // Error scenario: No file selected
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Import"));
        // Should handle the error gracefully
    }
    
    // Add more test cases for other methods as needed
    
}