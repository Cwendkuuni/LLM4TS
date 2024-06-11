import org.junit.*;
import static org.junit.Assert.*;

public class jgaapGUITest {

    private jgaapGUI gui;

    @Before
    public void setUp() {
        gui = new jgaapGUI();
    }

    @After
    public void tearDown() {
        gui = null;
    }

    // Test for the actionPerformed method 
    @Test
    public void testActionPerformed() {
        // Test for Exit command
        ActionEvent exitEvent = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit");
        gui.actionPerformed(exitEvent);
        // Assert that the program exits
        assertTrue(gui.hasExited());

        // Test for knownauthor command
        ActionEvent knownAuthorEvent = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "knownauthor");
        gui.actionPerformed(knownAuthorEvent);
        // Assert that the author text field is enabled
        assertTrue(gui.isAuthorTextFieldEnabled());

        // Test for unknownauthor command
        ActionEvent unknownAuthorEvent = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "unknownauthor");
        gui.actionPerformed(unknownAuthorEvent);
        // Assert that the author text field is disabled
        assertFalse(gui.isAuthorTextFieldEnabled());

        // Add more test cases for other commands in the actionPerformed method
    }

    // Test for the loadDemo method
    @Test
    public void testLoadDemo() {
        // Call the loadDemo method
        gui.loadDemo();
        // Assert that the correct output is displayed
        assertEquals("Need to Demo", gui.getOutput());
    }

    // Add more test cases for other methods in the jgaapGUI class

}

