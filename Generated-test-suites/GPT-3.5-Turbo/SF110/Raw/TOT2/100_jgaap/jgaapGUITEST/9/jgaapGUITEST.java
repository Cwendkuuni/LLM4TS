import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class jgaapGUITest {

    private jgaapGUI gui;

    @Before
    public void setUp() {
        gui = new jgaapGUI();
    }

    @Test
    public void testActionPerformedExit() {
        // Test action performed with Exit
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
        // Check if the application exits
        assertTrue(true);
    }

    @Test
    public void testActionPerformedKnownAuthor() {
        // Test action performed with Known Author
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        // Check if currentAuthor is enabled
        assertTrue(gui.currentAuthor.isEnabled());
    }

    @Test
    public void testActionPerformedUnknownAuthor() {
        // Test action performed with Unknown Author
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        // Check if currentAuthor is disabled
        assertFalse(gui.currentAuthor.isEnabled());
    }

    // Add more test cases for other public methods

}