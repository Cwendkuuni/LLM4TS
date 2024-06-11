import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testConstructor() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui);
    }

    @Test
    public void testActionPerformed_Exit() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
    }

    @Test
    public void testActionPerformed_Import() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Import"));
    }

    @Test
    public void testActionPerformed_Canonicize() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Canonicize"));
    }

    @Test
    public void testActionPerformed_CreateEventSet() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "CreateEventSet"));
    }

    @Test
    public void testActionPerformed_Analyze() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Analyze"));
    }

    @Test
    public void testActionPerformed_Demo() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "demo"));
    }
}