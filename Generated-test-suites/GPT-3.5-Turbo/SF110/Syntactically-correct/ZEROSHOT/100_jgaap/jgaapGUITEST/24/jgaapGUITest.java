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
    public void testActionPerformed() {
        // Test for Exit command
        ActionEvent exitEvent = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit");
        gui.actionPerformed(exitEvent);
        // Need to add assertion for System.exit(-1)

        // Test for knownauthor command
        ActionEvent knownAuthorEvent = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "knownauthor");
        gui.actionPerformed(knownAuthorEvent);
        assertTrue(gui.currentAuthor.isEnabled());

        // Test for unknownauthor command
        ActionEvent unknownAuthorEvent = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "unknownauthor");
        gui.actionPerformed(unknownAuthorEvent);
        assertFalse(gui.currentAuthor.isEnabled());

        // Need to add more test cases for other command events
    }

    @Test
    public void testLoadDemo() {
        // Need to implement the test for loadDemo() method
    }

    // Need to add test cases for other methods in the jgaapGUI class

}