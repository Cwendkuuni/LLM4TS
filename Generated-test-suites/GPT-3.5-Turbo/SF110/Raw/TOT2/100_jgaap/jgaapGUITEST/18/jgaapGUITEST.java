import static org.junit.Assert.*;

import org.junit.Test;

public class jgaapGUITest {

    @Test
    public void testInitComponents() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui);
    }

    @Test
    public void testActionPerformedExit() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
    }

    @Test
    public void testActionPerformedImport() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Import"));
    }

    @Test
    public void testActionPerformedCanonicize() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Canonicize"));
    }

    @Test
    public void testActionPerformedCreateEventSet() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "CreateEventSet"));
    }

    @Test
    public void testActionPerformedAnalyze() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Analyze"));
    }

    @Test
    public void testActionPerformedDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "demo"));
    }

}