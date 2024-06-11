import static org.junit.Assert.*;
import org.junit.Test;

public class jgaapGUITest {
    
    //Test case for jgaapGUI constructor
    @Test
    public void testConstructor() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui);
    }
    
    //Test case for actionPerformed method with "Exit" command
    @Test
    public void testActionPerformedExit() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit");
        gui.actionPerformed(e);
        // Assert if program exits with code -1
        // Add relevant assertions
    }
    
    //Test case for actionPerformed method with "knownauthor" command
    @Test
    public void testActionPerformedKnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor");
        gui.actionPerformed(e);
        // Add relevant assertions
    }
    
    //Test case for actionPerformed method with "unknownauthor" command
    @Test
    public void testActionPerformedUnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "unknownauthor");
        gui.actionPerformed(e);
        // Add relevant assertions
    }
    
    // Add more test cases for each public method and edge cases
    
}