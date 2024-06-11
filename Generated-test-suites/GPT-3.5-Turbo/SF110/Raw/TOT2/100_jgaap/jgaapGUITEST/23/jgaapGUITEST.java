import org.junit.*;
import static org.junit.Assert.*;

public class jgaapGUITest {
    
    private jgaapGUI gui;
    
    @Before
    public void setUp() {
        gui = new jgaapGUI();
    }
    
    @Test
    public void testActionPerformedExit() {
        final ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit");
        int returnValue = 0;
        try {
            gui.actionPerformed(e);
        } catch(Exception ex) {
            fail("Exception thrown: " + ex.getMessage());
        }
        assertEquals(-1, returnValue);
    }
    
    // Add more test cases for other actionPerformed scenarios
    
    // Edge cases and exception handling scenarios
    
    // Test case for loading a demo
    @Test
    public void testLoadDemo() {
        // The loadDemo method in jgaapGUI should print "Need to Demo" to the console
        gui.loadDemo();
    }
    
    @After
    public void tearDown() {
        gui = null;
    }
}