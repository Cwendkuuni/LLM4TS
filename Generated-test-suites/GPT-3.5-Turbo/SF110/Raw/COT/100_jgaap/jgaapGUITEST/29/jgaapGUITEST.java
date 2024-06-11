import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class jgaapGUITest {

    private jgaapGUI gui;

    @Before
    public void setup() {
        gui = new jgaapGUI();
    }

    @Test
    public void testActionListenerExit() {
        // Test the Exit action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
        // No assertion as this will exit the program
    }

    @Test
    public void testActionListenerKnownAuthor() {
        // Test the knownauthor action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        assertEquals("", gui.currentAuthor.getText());
        assertTrue(gui.currentAuthor.isEnabled());
    }

    @Test
    public void testActionListenerUnknownAuthor() {
        // Test the unknownauthor action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        assertEquals("", gui.currentAuthor.getText());
        assertFalse(gui.currentAuthor.isEnabled());
    }

    @Test
    public void testActionListenerImport() {
        // Test the Import action
        // Not possible to test as it requires interaction with the file chooser
    }

    // Add test cases for other public methods if needed...

}