import org.junit.Test;

public class jgaapGUITest {

    @Test
    public void testActionPerformed() {
        // Test actionPerformed method

        jgaapGUI gui = new jgaapGUI();

        // Test Exit action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));

        // Test knownauthor action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor"));

        // Test unknownauthor action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "unknownauthor"));

        // Test Import action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Import"));

        // Test Canonicize action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Canonicize"));

        // Test CreateEventSet action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "CreateEventSet"));

        // Test Analyze action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Analyze"));

        // Test demo action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "demo"));

    }

    @Test
    public void testLoadDemo() {
        // Test loadDemo method

        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();

    }
}