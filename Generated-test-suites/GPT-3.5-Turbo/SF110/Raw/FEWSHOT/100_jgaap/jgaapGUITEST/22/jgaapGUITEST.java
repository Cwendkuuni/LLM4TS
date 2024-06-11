
import org.junit.Test;

import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();

        gui.actionPerformed(null);
        assertNotNull(gui);

    }

    @Test
    public void testInitComponents() {
        jgaapGUI gui = new jgaapGUI();
        gui.initComponents();

        assertNotNull(gui);

    }

    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();

        assertNotNull(gui);
    }
}
