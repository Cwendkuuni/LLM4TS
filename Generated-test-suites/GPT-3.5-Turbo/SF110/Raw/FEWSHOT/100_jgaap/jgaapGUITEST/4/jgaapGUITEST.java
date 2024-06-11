import static org.junit.Assert.*;
import org.junit.Test;

public class jgaapGUITest {

    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();

        // Test case for Exit command
        assertEquals(System.exit(-1), gui.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "Exit")));

        // Test case for knownauthor command
        gui.currentAuthor.setEnabled(false);
        assertEquals("", gui.currentAuthor.getText());
        assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "knownauthor")));

        // Test case for unknownauthor command
        gui.currentAuthor.setEnabled(true);
        assertEquals("", gui.currentAuthor.getText());
        assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "unknownauthor")));

        // Test case for Import command (returnVal = 0)
        assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "Import")));

        // Test case for Import command (returnVal = !0)
        assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "Import")));

        // Test case for Canonicize command
        gui.paneOpts.setSelectedIndex(1);
        assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "Canonicize")));

        // Test case for CreateEventSet command
        gui.paneOpts.setSelectedIndex(2);
        assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "CreateEventSet")));

        // Test case for Analyze command
        gui.paneOpts.setSelectedIndex(3);
        assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "Analyze")));

        // Test case for demo command
        assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "demo")));

        // Test case for unknown command
        assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "unknown")));

    }
}