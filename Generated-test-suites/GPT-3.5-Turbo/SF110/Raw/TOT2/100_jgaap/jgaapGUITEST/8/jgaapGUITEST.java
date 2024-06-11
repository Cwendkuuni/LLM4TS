import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class jgaapGUITest {

    private jgaapGUI testGUI;

    @Before
    public void setUp() {
        testGUI = new jgaapGUI();
    }

    @After
    public void tearDown() {
        testGUI = null;
    }

    @Test
    public void testInitComponents() {
        assertNotNull(testGUI);
        // Add more assertions to test the initialization of components
        // Add edge cases and error scenarios
    }

    @Test
    public void testActionPerformedExit() {
        // Test for actionPerformed method with "Exit" command
        // Add edge cases and error scenarios
    }

    @Test
    public void testActionPerformedKnownAuthor() {
        // Test for actionPerformed method with "knownauthor" command
        // Add edge cases and error scenarios
    }

    @Test
    public void testActionPerformedUnknownAuthor() {
        // Test for actionPerformed method with "unknownauthor" command
        // Add edge cases and error scenarios
    }

    // Add more test methods for other public methods in the jgaapGUI class

}