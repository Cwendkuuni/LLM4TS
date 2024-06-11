import org.junit.Test;

public class jgaapGUITest {

    @Test
    public void testConstructor() {
        jgaapGUI gui = new jgaapGUI();
        assertNotNull(gui); // Check if the object is created successfully
    }

    @Test
    public void testActionPerformedExit() {
        jgaapGUI gui = new jgaapGUI();
        // Test when "Exit" action is performed
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
        // Expected behavior: System should exit with status -1
    }

    @Test
    public void testActionPerformedKnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        // Test when "knownauthor" action is performed
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        // Expected behavior: Current author field should be enabled
    }

    @Test
    public void testActionPerformedUnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        // Test when "unknownauthor" action is performed
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        // Expected behavior: Current author field should be disabled
    }

    // Add more test cases for other methods

}