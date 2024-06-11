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
        // Test actionPerformed method with Exit command
        testGUI.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
        // Add assertion here
    }

    @Test
    public void testActionPerformedKnownAuthor() {
        // Test actionPerformed method with Known Author command
        testGUI.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        // Add assertion here
    }

    @Test
    public void testLoadDemo() {
        // Test loadDemo method
        testGUI.loadDemo();
        // Add assertion here
    }

    // Add more test cases for other methods
}