
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class jgaapGUITest {
    
    private jgaapGUI gui;
    
    @Before
    public void setUp() {
        gui = new jgaapGUI();
    }
    
    @Test
    public void testActionPerformed_ExitCommand() {
        try {
            gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit"));
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }
    
    @Test
    public void testActionPerformed_KnownAuthorCommand() {
        try {
            gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "knownauthor"));
            
            assertTrue(gui.currentAuthor.isEnabled());
            assertEquals("", gui.currentAuthor.getText());
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }
    
    @Test
    public void testActionPerformed_UnknownAuthorCommand() {
        try {
            gui.actionPerformed(new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
            
            assertFalse(gui.currentAuthor.isEnabled());
            assertEquals("", gui.currentAuthor.getText());
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }
    
    // Write additional test cases for all other action commands
    
}

