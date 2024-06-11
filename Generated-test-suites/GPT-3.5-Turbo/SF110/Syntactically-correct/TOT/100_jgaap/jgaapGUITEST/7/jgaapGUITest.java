import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {

    private jgaapGUI gui;

    @Before
    public void setUp() {
        gui = new jgaapGUI();
    }

    @Test
    public void testActionPerformed_ExitCommand() {
        // Test Exit command
        // Ensure that the system exits with code -1
        // This is an edge case
        System.exit = -1;
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
    }

    @Test
    public void testActionPerformed_KnownAuthorCommand() {
        // Test knownauthor command
        // Set currentAuthor to empty string and enable it
        // This is a typical use case
        gui.currentAuthor.setText("");
        gui.currentAuthor.setEnabled(false);
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        assertEquals("", gui.currentAuthor.getText());
        assertTrue(gui.currentAuthor.isEnabled());
    }

    @Test
    public void testActionPerformed_UnknownAuthorCommand() {
        // Test unknownauthor command
        // Set currentAuthor to empty string and disable it
        // This is a typical use case
        gui.currentAuthor.setText("");
        gui.currentAuthor.setEnabled(true);
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        assertEquals("", gui.currentAuthor.getText());
        assertFalse(gui.currentAuthor.isEnabled());
    }

    @Test
    public void testActionPerformed_ImportCommand() {
        // Test Import command
        // Select a file and add it to the documents list based on the currentAuthor
        // This is a typical use case
        gui.buttonGroup1.setSelected(gui.radioKnown.getModel(), true);
        gui.currentAuthor.setText("Test Author");
        gui.chooseFile.setSelectedFile(new File("testFile.txt"));
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Import"));
        assertEquals("Test Author - testFile.txt\n", gui.listKnown.getText());

        // Test Import command with empty currentAuthor
        // Add a file to the unknown documents list
        // This is a typical use case
        gui.buttonGroup1.setSelected(gui.radioKnown.getModel(), false);
        gui.currentAuthor.setText("");
        gui.chooseFile.setSelectedFile(new File("testFile2.txt"));
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Import"));
        assertEquals("testFile2.txt\n", gui.listUnknown.getText());
    }

    // Additional test cases for other methods...

}