import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JgaapGUITest {
    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        // test "Exit"
        gui.actionPerformed(new ActionEvent(new Object(), 0, "Exit"));
        // add more test cases for remaining actions
    }
    
    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
        // add assertions to verify the effect of loading demo
    }
}