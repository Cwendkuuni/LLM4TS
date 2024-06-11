import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class jgaapGUITest {

    private jgaapGUI testGUI;

    @Before
    public void setUp() {
        testGUI = new jgaapGUI();
    }

    @Test
    public void testActionPerformedExit() {
        // Test actionPerformed method for Exit command
        // Expected behavior: System exits
        testGUI.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
        // Assertion
        // No assert possible as system exits
    }

    @Test
    public void testActionPerformedKnownAuthor() {
        // Test actionPerformed method for Known Author command
        // Expected behavior: Current author field enabled
        testGUI.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        // Assertion
        assertTrue(testGUI.currentAuthor.isEnabled());
    }

    @Test
    public void testActionPerformedUnknownAuthor() {
        // Test actionPerformed method for Unknown Author command
        // Expected behavior: Current author field disabled
        testGUI.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        // Assertion
        assertFalse(testGUI.currentAuthor.isEnabled());
    }

    // More test cases for other public methods in jgaapGUI class

}