import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testActionListener() {
        jgaapGUI gui = new jgaapGUI();

        // Test actionPerformed method
        gui.actionPerformed(null);
    }

    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        
        // Test loadDemo method
        gui.loadDemo();
    }
    
    @Test
    public void testInitComponents() {
        jgaapGUI gui = new jgaapGUI();
        
        // Test initComponents method
        gui.initComponents();
    }
}