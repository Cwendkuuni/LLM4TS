import org.junit.*;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        
        gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit"));
        // Test Exit action command
        
        // Test knownauthor action command
        gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        assertEquals("", gui.currentAuthor.getText());
        assertTrue(gui.currentAuthor.isEnabled());
        
        // Test unknownauthor action command
        gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        assertEquals("", gui.currentAuthor.getText());
        assertFalse(gui.currentAuthor.isEnabled());
        
        // Test Import action command
        gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Import"));
        // Test Canonicize action command
        gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Canonicize"));
        // Test CreateEventSet action command
        gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "CreateEventSet"));
        // Test Analyze action command
        gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Analyze"));
        
        gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "demo"));
        // Test demo action command
        
        // Test invalid action command
        gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "InvalidCommand"));
    }
    
    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        assertNull(gui.loadDemo());
    }
}