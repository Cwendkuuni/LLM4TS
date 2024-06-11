import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.*;

public class jgaapGUITest {

    @Test
    public void testInitComponents() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui);
        assertNotNull(gui.menuBar);
        assertNotNull(gui.menuFile);
        assertNotNull(gui.menuHelp);
        assertNotNull(gui.paneOpts);
    }

    @Test
    public void testActionPerformedExit() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui.menuFileExit, ActionEvent.ACTION_PERFORMED, "Exit");
        gui.actionPerformed(e);
        // Verify that the application exits
    }

    @Test
    public void testActionPerformedKnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui.radioKnown, ActionEvent.ACTION_PERFORMED, "knownauthor");
        gui.actionPerformed(e);
        // Verify that author field is enabled
    }

    // Add more test cases for other actionPerformed scenarios

    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
        // Verify that the demo loads successfully
    }
}