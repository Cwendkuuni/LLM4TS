import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {
    jgaapGUI gui = new jgaapGUI();
    
    @Test
    public void testActionPerfomedExit() {
        // Test actionPerformed method for Exit command
        // Edge case: Exit command
        // Expected: System exits with code -1
        // The test checks if the system exits with code -1
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
        assertEquals(-1, System.exit(-1));
    }
    
    @Test
    public void testActionPerfomedKnownAuthor() {
        // Test actionPerformed method for known author command
        // Edge case: Known author command
        // Expected: Current author text is cleared and current author field is enabled
        // The test checks if current author text field is cleared and enabled
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        assertEquals(0, gui.currentAuthor.getText().length());
        assertTrue(gui.currentAuthor.isEnabled());
    }
    
    @Test
    public void testActionPerfomedUnknownAuthor() {
        // Test actionPerformed method for unknown author command
        // Edge case: Unknown author command
        // Expected: Current author text is cleared and current author field is disabled
        // The test checks if current author text field is cleared and disabled
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        assertEquals(0, gui.currentAuthor.getText().length());
        assertFalse(gui.currentAuthor.isEnabled());
    }
    
    // Add more test cases for other public methods
    
}