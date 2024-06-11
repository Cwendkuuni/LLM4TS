import static org.junit.Assert.*;
import org.junit.Test;

public class jgaapGUITest {

    @Test
    public void testActionPerformed_exit() {
        jgaapGUI gui = new jgaapGUI();
        // Need to mock ActionEvent and check if System.exit(-1) is called
    }

    @Test
    public void testActionPerformed_knownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        // Set action command to "knownauthor" and check if currentAuthor is enabled
    }

    @Test
    public void testActionPerformed_unknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        // Set action command to "unknownauthor" and check if currentAuthor is disabled
    }

    // More test methods for other actionPerformed scenarios

    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        // Call loadDemo method and check if it prints "Need to Demo"
    }

    // Additional test methods for other public methods in jgaapGUI class

    // Test edge cases and exception scenarios

}