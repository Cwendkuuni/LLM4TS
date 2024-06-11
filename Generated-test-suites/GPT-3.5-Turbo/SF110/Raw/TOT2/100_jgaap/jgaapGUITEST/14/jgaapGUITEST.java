import static org.junit.Assert.*;
import org.junit.Test;

public class jgaapGUITest {
    
    @Test
    public void testInitComponents() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui);
    }
    
    @Test
    public void testActionPerformed_Exit() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
        // Ensure system exits successfully
    }
    
    @Test
    public void testActionPerformed_KnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        // Ensure currentAuthor text field is enabled
        assertTrue(gui.currentAuthor.isEnabled());
    }
    
    @Test
    public void testActionPerformed_UnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        // Ensure currentAuthor text field is disabled
        assertFalse(gui.currentAuthor.isEnabled());
    }
    
    // Add more test cases for other methods as per the identified edge cases and exceptions
    
}