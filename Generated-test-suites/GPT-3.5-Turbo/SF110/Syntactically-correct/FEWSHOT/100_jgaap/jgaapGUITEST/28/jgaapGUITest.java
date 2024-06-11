import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class jgaapGUITest {
    private jgaapGUI gui;
    
    @Before
    public void setUp() {
        gui = new jgaapGUI();
    }
    
    @Test
    public void testActionPerformed() {
        // test action command "Exit"
        assertEquals(gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit")), null);
        
        // test action command "knownauthor"
        assertTrue(gui.currentAuthor.isEnabled());
        
        // test action command "unknownauthor"
        assertFalse(gui.currentAuthor.isEnabled());
        
        // test action command "Import"
        assertEquals(gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Import")), null);
        
        // test action command "Canonicize"
        assertEquals(gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Canonicize")), null);
        
        // test action command "CreateEventSet"
        assertEquals(gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "CreateEventSet")), null);
        
        // test action command "Analyze"
        assertEquals(gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Analyze")), null);
        
        // test action command "demo"
        assertEquals(gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "demo")), null);
        
        // test unknown action command
        assertEquals(gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "UnknownCommand")), null);
    }
}