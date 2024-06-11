import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testjgaapGUIConstructor() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui);
    }

    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
        // Assuming loadDemo method prints a message, test if it executes without errors
    }

    @Test
    public void testActionPerformedExit() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(gui, 0, "Exit"));
        // Assuming Exit action should exit the program
    }

    @Test
    public void testActionPerformedKnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(gui, 0, "knownauthor"));
        // Assuming Known Author action should clear the current author field and enable it
    }

    @Test
    public void testActionPerformedUnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(gui, 0, "unknownauthor"));
        // Assuming Unknown Author action should clear the current author field and disable it
    }

    // Add more test cases for other public methods and edge cases

}