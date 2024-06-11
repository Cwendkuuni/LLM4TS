import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class jgaapGUITest {
    
    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        
        // Test exit action
        assertEquals(System.exit(-1), gui.actionPerformed(new ActionEvent(null, 0, "Exit")));
        
        // Test known author action
        gui.actionPerformed(new ActionEvent(null, 0, "knownauthor"));
        assertEquals("", gui.currentAuthor.getText());
        assertEquals(true, gui.currentAuthor.isEnabled());
        
        // Test unknown author action
        gui.actionPerformed(new ActionEvent(null, 0, "unknownauthor"));
        assertEquals("", gui.currentAuthor.getText());
        assertEquals(false, gui.currentAuthor.isEnabled());
        
        // Test import action
        String fcomp = new String();
        int returnVal = 0;
        assertEquals(fcomp, gui.actionPerformed(new ActionEvent(null, 0, "Import")));
        assertEquals(0, returnVal);
        
        // Test canonicize action
        gui.actionPerformed(new ActionEvent(null, 0, "Canonicize"));
        // Need to write assertions for canonicize method
        
        // Test create event set action
        gui.actionPerformed(new ActionEvent(null, 0, "CreateEventSet"));
        // Need to write assertions for createEventSet method
        
        // Test analyze action
        gui.actionPerformed(new ActionEvent(null, 0, "Analyze"));
        // Need to write assertions for analyze method
        
        // Test demo action
        gui.actionPerformed(new ActionEvent(null, 0, "demo"));
        // Need to write assertions for loadDemo method
        
        // Test unknown command action
        gui.actionPerformed(new ActionEvent(null, 0, "unknowncommand"));
        // Need to write assertions for unknown command
        
    }
}