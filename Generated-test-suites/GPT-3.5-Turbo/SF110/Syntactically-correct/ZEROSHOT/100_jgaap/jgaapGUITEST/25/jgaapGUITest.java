import static org.junit.Assert.*;
import org.junit.*;

public class jgaapGUITest {

    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();

        // Test Exit
        ActionEvent exitEvent = new ActionEvent(gui.menuFileExit, 0, "Exit");
        gui.actionPerformed(exitEvent);

        // Test known author
        ActionEvent knownAuthorEvent = new ActionEvent(gui.radioKnown, 0, "knownauthor");
        gui.actionPerformed(knownAuthorEvent);
        assertEquals("", gui.currentAuthor.getText());
        assertTrue(gui.currentAuthor.isEnabled());

        // Test unknown author
        ActionEvent unknownAuthorEvent = new ActionEvent(gui.radioUnknown, 0, "unknownauthor");
        gui.actionPerformed(unknownAuthorEvent);
        assertEquals("", gui.currentAuthor.getText());
        assertFalse(gui.currentAuthor.isEnabled());

        // Test Import
        ActionEvent importEvent = new ActionEvent(gui.buttonLocal, 0, "Import");
        gui.actionPerformed(importEvent);
        // TODO: Test the file import functionality

        // Test Canonicize
        ActionEvent canonicizeEvent = new ActionEvent(gui.buttonCanonicize, 0, "Canonicize");
        gui.actionPerformed(canonicizeEvent);
        // TODO: Test the canonicize functionality

        // Test CreateEventSet
        ActionEvent createEventSetEvent = new ActionEvent(gui.buttonEventSet, 0, "CreateEventSet");
        gui.actionPerformed(createEventSetEvent);
        // TODO: Test the create event set functionality

        // Test Analyze
        ActionEvent analyzeEvent = new ActionEvent(gui.buttonAnalyze, 0, "Analyze");
        gui.actionPerformed(analyzeEvent);
        // TODO: Test the analyze functionality

        // Test demo
        ActionEvent demoEvent = new ActionEvent(gui.menuFileDemo, 0, "demo");
        gui.actionPerformed(demoEvent);
        // TODO: Test the load demo functionality
    }

    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
        // TODO: Test the load demo functionality
    }
}