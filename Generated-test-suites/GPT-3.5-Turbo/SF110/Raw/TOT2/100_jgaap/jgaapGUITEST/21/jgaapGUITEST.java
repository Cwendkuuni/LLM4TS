import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {
    
    @Test
    public void testConstructor() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui);
    }
    
    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        // Test exit action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
        // Add more action tests
    }
    
    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
        // Add test for demo loading
    }
    
    // Add more test cases for other methods in jgaapGUI
    
}