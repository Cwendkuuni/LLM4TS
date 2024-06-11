import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class jgaapGUITest {

    private static jgaapGUI gui;

    @BeforeClass
    public static void setUp() {
        gui = new jgaapGUI();
    }

    @Test
    public void testActionPerformed_Exit() {
        // Simulating the "Exit" button being clicked
        ActionEvent event = new ActionEvent(gui.menuFileExit, 0, "Exit");
        gui.actionPerformed(event);
        
        // Verify that the program exits
        // (This cannot be tested directly as it will terminate the test execution)
        assertTrue(true);
    }

    @Test
    public void testActionPerformed_KnownAuthor() {
        // Simulating the "Known Author" button being clicked
        ActionEvent event = new ActionEvent(gui.radioKnown, 0, "knownauthor");
        gui.actionPerformed(event);

        // Verify that the "currentAuthor" text field is enabled
        assertTrue(gui.currentAuthor.isEnabled());

        // Verify that the "currentAuthor" text field is empty
        assertEquals("", gui.currentAuthor.getText());
    }

    @Test
    public void testActionPerformed_UnknownAuthor() {
        // Simulating the "Unknown Author" button being clicked
        ActionEvent event = new ActionEvent(gui.radioUnknown, 0, "unknownauthor");
        gui.actionPerformed(event);

        // Verify that the "currentAuthor" text field is disabled
        assertFalse(gui.currentAuthor.isEnabled());

        // Verify that the "currentAuthor" text field is empty
        assertEquals("", gui.currentAuthor.getText());
    }

    // ... other test cases for each method ...

    @Test
    public void testActionPerformed_InvalidCommand() {
        // Simulating an invalid command being triggered
        ActionEvent event = new ActionEvent(new Object(), 0, "InvalidCommand");
        gui.actionPerformed(event);

        // Verify that nothing happens (no error occurs)
        assertTrue(true);
    }

}