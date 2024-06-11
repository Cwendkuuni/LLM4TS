import static org.junit.Assert.*;
import org.junit.Test;

public class jgaapGUITest {

    @Test
    public void testConstructor() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui);
    }

    @Test
    public void testActionPerformed_Exit() {
        jgaapGUI gui = new jgaapGUI();
        try {
            gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
        } catch (Exception e) {
            fail("Exception thrown for Exit action");
        }
    }

    @Test
    public void testActionPerformed_KnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        try {
            gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        } catch (Exception e) {
            fail("Exception thrown for knownauthor action");
        }
    }

    @Test
    public void testActionPerformed_UnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        try {
            gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        } catch (Exception e) {
            fail("Exception thrown for unknownauthor action");
        }
    }

    // More test cases can be added for the remaining public methods

}