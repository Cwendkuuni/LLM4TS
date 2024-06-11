import static org.junit.Assert.*;
import org.junit.Test;

public class jgaapGUITest {

    private jgaapGUI gui;

    @Test
    public void testActionPerformedExit() {
        gui = new jgaapGUI();
        // Test Exit action
        gui.actionPerformed(new ActionEvent(this, 0, "Exit"));
        // Add assertion here
    }

    // Add more test cases for other public methods in jgaapGUI class

    @Test
    public void testLoadDemo() {
        gui = new jgaapGUI();
        // Test loadDemo method
        gui.loadDemo();
        // Add assertion here
    }

    // Add more test cases for private methods and edge cases

}