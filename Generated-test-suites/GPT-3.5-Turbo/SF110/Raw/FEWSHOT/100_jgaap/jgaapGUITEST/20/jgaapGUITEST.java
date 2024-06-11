import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class jgaapGUITest {

    private jgaapGUI gui;

    @Before
    public void setUp() {
        gui = new jgaapGUI();
    }

    // Test for actionPerformed method
    @Test
    public void testActionPerformed() {
        // Test "Exit" option
        assertEquals(System.exit(-1), gui.actionPerformed(new ActionEvent("Exit", 1)));

        // Test "knownauthor" option
        String expected = "";
        assertEquals(expected, gui.currentAuthor.getText());
        assertTrue(gui.currentAuthor.isEnabled());
        assertEquals(expected, gui.actionPerformed(new ActionEvent("knownauthor", 1)));

        // Test "unknownauthor" option
        assertFalse(gui.currentAuthor.isEnabled());
        assertEquals(expected, gui.actionPerformed(new ActionEvent("unknownauthor", 1)));

        // Test "Import" option
        expected = "";
        assertEquals(expected, gui.currentAuthor.getText());
        int returnVal = 0;
        File file = new File("");
        gui.driver.addDocument(file.toString(), gui.currentAuthor.getText());
        if (!gui.currentAuthor.getText().equals("")) {
            gui.listKnown.append(gui.currentAuthor.getText() + " - " + file.getName() + "\n");
        } else {
            gui.listUnknown.append(file.getName() + "\n");
        }
        assertEquals(expected, gui.listKnown.getText());
        assertEquals(expected, gui.currentAuthor.getText());
        assertEquals(expected, gui.listUnknown.getText());
        assertEquals(expected, gui.actionPerformed(new ActionEvent("Import", 1)));

        // Test "Canonicize" option
        expected = "";
        assertEquals(expected, gui.actionPerformed(new ActionEvent("Canonicize", 1)));

        // Test "CreateEventSet" option
        expected = "";
        assertEquals(expected, gui.actionPerformed(new ActionEvent("CreateEventSet", 1)));

        // Test "Analyze" option
        expected = "";
        assertEquals(expected, gui.actionPerformed(new ActionEvent("Analyze", 1)));

        // Test "demo" option
        expected = "";
        assertEquals(expected, gui.loadDemo());
        assertEquals(expected, gui.actionPerformed(new ActionEvent("demo", 1)));

        // Test for invalid action command
        expected = "Invalid command";
        assertEquals(expected, gui.actionPerformed(new ActionEvent("Invalid", 1)));
    }

    // Test for loadDemo method
    @Test
    public void testLoadDemo() {
        String expected = "";
        assertEquals(expected, gui.loadDemo());
    }
}