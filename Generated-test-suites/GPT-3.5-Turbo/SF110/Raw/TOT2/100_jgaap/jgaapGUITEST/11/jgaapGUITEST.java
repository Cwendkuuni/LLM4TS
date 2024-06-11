import static org.junit.Assert.*;
import org.junit.Test;

public class jgaapGUITest {

    @Test
    public void testConstructor() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull("Object creation failed", gui);
    }

    @Test
    public void testActionPerformedExit() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit"));
        // Check if application exits
    }

    @Test
    public void testActionPerformedKnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        // Check if currentAuthor text field is enabled
    }

    @Test
    public void testActionPerformedUnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        // Check if currentAuthor text field is disabled
    }

    // More test cases can be added for other methods and scenarios

}