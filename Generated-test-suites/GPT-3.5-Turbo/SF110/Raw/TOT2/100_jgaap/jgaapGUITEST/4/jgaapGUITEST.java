import static org.junit.Assert.*;
import org.junit.Test;

public class jgaapGUITest {

    @Test
    public void testConstructor() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui);
    }

    @Test
    public void testActionPerformedExit() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, 0, "Exit"));
        // Need some way to verify that the system exits
    }

    @Test
    public void testActionPerformedKnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, 0, "knownauthor"));
        // Need some way to verify that the currentAuthor field is enabled
    }

    @Test
    public void testActionPerformedUnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, 0, "unknownauthor"));
        // Need some way to verify that the currentAuthor field is disabled
    }

    // Additional test cases for other actionPerformed scenarios

    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
        // Need some way to verify that the loadDemo method executes
    }

    // Additional test cases for other methods in jgaapGUI

}