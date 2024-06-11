import org.junit.*;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testActionPerformed_ExitCommand() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit");
        gui.actionPerformed(e);
        //assertions
    }
    
    @Test
    public void testActionPerformed_KnownAuthorCommand() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "knownauthor");
        gui.actionPerformed(e);
        //assertions
    }
    
    @Test
    public void testActionPerformed_UnknownAuthorCommand() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "unknownauthor");
        gui.actionPerformed(e);
        //assertions
    }
    
    @Test
    public void testActionPerformed_ImportCommand() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Import");
        gui.actionPerformed(e);
        //assertions
    }
    
    @Test
    public void testActionPerformed_CanonicizeCommand() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Canonicize");
        gui.actionPerformed(e);
        //assertions
    }
    
    @Test
    public void testActionPerformed_CreateEventSetCommand() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "CreateEventSet");
        gui.actionPerformed(e);
        //assertions
    }
    
    @Test
    public void testActionPerformed_AnalyzeCommand() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Analyze");
        gui.actionPerformed(e);
        //assertions
    }
    
    @Test
    public void testActionPerformed_DemoCommand() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "demo");
        gui.actionPerformed(e);
        //assertions
    }
    
    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
        //assertions
    }
}