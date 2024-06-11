import static org.junit.Assert.*;
import org.junit.Test;

public class jgaapGUITest {

    @Test
    public void testJgaapGUI() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui);
    }
    
    @Test
    public void testActionPerformedExit() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 1, "Exit");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedKnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 1, "knownauthor");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedUnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 1, "unknownauthor");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedImport() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 1, "Import");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedCanonicize() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 1, "Canonicize");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedCreateEventSet() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 1, "CreateEventSet");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedAnalyze() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 1, "Analyze");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedDemo() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 1, "demo");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
    }
}