import static org.junit.Assert.*;
import org.junit.Test;

public class jgaapGUITest {
    
    @Test
    public void testConstructor() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui);
    }
    
    @Test
    public void testActionPerformedExit() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "Exit");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedKnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "knownauthor");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedUnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "unknownauthor");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedImport() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "Import");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedCanonicize() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "Canonicize");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedCreateEventSet() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "CreateEventSet");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedAnalyze() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "Analyze");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testActionPerformedDemo() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "demo");
        gui.actionPerformed(e);
    }
    
    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
    }
}