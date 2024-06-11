import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {
    
    @Test
    public void testConstructor() {
        try {
            jgaapGUI gui = new jgaapGUI();
            assertNotNull(gui);
        } catch (Exception e) {
            fail("Constructor threw an exception");
        }
    }
    
    @Test
    public void testActionPerformed_exitCommand() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 1, "Exit"));
        // Verify that the application exits by checking if the window is closed
        assertTrue(gui.isWindowClosing());
    }
    
    @Test
    public void testActionPerformed_knownAuthorCommand() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 1, "knownauthor"));
        
        assertTrue(gui.getCurrentAuthor().isEnabled());
        assertEquals("", gui.getCurrentAuthor().getText());
    }
    
    @Test
    public void testActionPerformed_unknownAuthorCommand() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 1, "unknownauthor"));
        
        assertFalse(gui.getCurrentAuthor().isEnabled());
        assertEquals("", gui.getCurrentAuthor().getText());
    }
    
    // TODO: Add more test cases for the other methods and actions
    
}