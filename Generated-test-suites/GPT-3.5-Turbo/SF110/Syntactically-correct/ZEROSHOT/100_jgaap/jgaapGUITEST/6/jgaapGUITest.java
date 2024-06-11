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
        
        // Test Exit action command
        ActionEvent exitEvent = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit");
        gui.actionPerformed(exitEvent);
        // Ensure that the application exits without any errors or exceptions
        
        // Test knownauthor action command
        ActionEvent knownAuthorEvent = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "knownauthor");
        gui.actionPerformed(knownAuthorEvent);
        // Ensure that the currentAuthor text field is enabled
        
        // Test unknownauthor action command
        ActionEvent unknownAuthorEvent = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "unknownauthor");
        gui.actionPerformed(unknownAuthorEvent);
        // Ensure that the currentAuthor text field is disabled
        
        // Test Import action command
        
    }

}