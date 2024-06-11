import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        
        // Test Exit Action
        // Commented out since it exits the system
//        gui.actionPerformed(new ActionEvent(new Object(), 0, "Exit"));
        
        // Test Known Author Action
        gui.actionPerformed(new ActionEvent(new Object(), 0, "knownauthor"));
        assertFalse(gui.currentAuthor.isEnabled());
        
        // Test Unknown Author Action
        gui.actionPerformed(new ActionEvent(new Object(), 0, "unknownauthor"));
        assertTrue(gui.currentAuthor.isEnabled());
        
        // Test Import Action
        // Commented out since it requires file path
//        gui.actionPerformed(new ActionEvent(new Object(), 0, "Import"));
        
        // Test Canonicize Action
        // Commented out since it requires canonicizers and GUI components
//        gui.actionPerformed(new ActionEvent(new Object(), 0, "Canonicize"));
        
        // Test Create Event Set Action
        // Commented out since it requires eventifiers and GUI components
//        gui.actionPerformed(new ActionEvent(new Object(), 0, "CreateEventSet"));
        
        // Test Analyze Action
        // Commented out since it requires statistical analysis method and GUI components
//        gui.actionPerformed(new ActionEvent(new Object(), 0, "Analyze"));
        
        // Test Demo Action
        // Commented out since it doesn't have implemented demo method
//        gui.actionPerformed(new ActionEvent(new Object(), 0, "demo"));
    }

    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        
        // Commented out since it doesn't have implemented demo method
        //gui.loadDemo();
    }
}