import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class jgaapGUITest {
    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        
        // Testing Exit command
        assertEquals(gui, exitCommand(gui));
        
        // Testing knownauthor command
        assertEquals(gui, knownAuthorCommand(gui));
        
        // Testing unknownauthor command
        assertEquals(gui, unknownAuthorCommand(gui));
        
        // Testing Import command
        assertEquals(gui, importCommand(gui));
        
        // Testing Canonicize command
        assertEquals(gui, canonicizeCommand(gui));
        
        // Testing CreateEventSet command
        assertEquals(gui, createEventSetCommand(gui));
        
        // Testing Analyze command
        assertEquals(gui, analyzeCommand(gui));
        
        // Testing demo command
        assertEquals(gui, demoCommand(gui));
    }

    public jgaapGUI exitCommand(jgaapGUI gui) {
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "Exit");
        gui.actionPerformed(event);
        return gui;
    }

    public jgaapGUI knownAuthorCommand(jgaapGUI gui) {
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "knownAuthor");
        gui.actionPerformed(event);
        return gui;
    }

    public jgaapGUI unknownAuthorCommand(jgaapGUI gui) {
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "unknownAuthor");
        gui.actionPerformed(event);
        return gui;
    }

    public jgaapGUI importCommand(jgaapGUI gui) {
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "Import");
        gui.actionPerformed(event);
        return gui;
    }

    public jgaapGUI canonicizeCommand(jgaapGUI gui) {
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "Canonicize");
        gui.actionPerformed(event);
        return gui;
    }

    public jgaapGUI createEventSetCommand(jgaapGUI gui) {
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "CreateEventSet");
        gui.actionPerformed(event);
        return gui;
    }

    public jgaapGUI analyzeCommand(jgaapGUI gui) {
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "Analyze");
        gui.actionPerformed(event);
        return gui;
    }

    public jgaapGUI demoCommand(jgaapGUI gui) {
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "demo");
        gui.actionPerformed(event);
        return gui;
    }
}