import org.junit.Test;
import static org.junit.Assert.*;

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
        // Test if the program exits successfully
    }

    @Test
    public void testActionPerformedKnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        // Test if currentAuthor is enabled and cleared when selecting known author
    }

    @Test
    public void testActionPerformedUnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        // Test if currentAuthor is disabled and cleared when selecting unknown author
    }

    @Test
    public void testActionPerformedImport() {
        jgaapGUI gui = new jgaapGUI();
        File file = new File("testFile.txt");
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Import"));
        // Test if document is successfully added and displayed in the list
    }
    
    @Test
    public void testActionPerformedCanonicize() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Canonicize"));
        // Test if Canonicize operation is performed correctly
    }
    
    @Test
    public void testActionPerformedCreateEventSet() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "CreateEventSet"));
        // Test if Event Set is created successfully
    }
    
    @Test
    public void testActionPerformedAnalyze() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Analyze"));
        // Test if Statistical Analysis is performed correctly and results are displayed
    }

    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
        // Test if loadDemo method is functioning properly
    }
}