import org.junit.*;
import static org.junit.Assert.*;

public class jgaapGUITest {

    private jgaapGUI gui;

    @Before
    public void setUp() {
        gui = new jgaapGUI();
    }

    @After
    public void tearDown() {
        gui = null;
    }

    @Test
    public void testActionPerformed_ExitCommand_ShouldExitSystem() {
        // Assume Exit command is selected
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit");
        gui.actionPerformed(e);

        // Add assertions here to check if the system has properly exited
    }

    @Test
    public void testActionPerformed_KnownAuthorCommand_ShouldEnableCurrentAuthorField() {
        // Assume Known Author command is selected
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "knownauthor");
        gui.actionPerformed(e);

        // Add assertions here to check if the CurrentAuthor field is enabled
    }

    @Test
    public void testActionPerformed_UnknownAuthorCommand_ShouldDisableCurrentAuthorField() {
        // Assume Unknown Author command is selected
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "unknownauthor");
        gui.actionPerformed(e);

        // Add assertions here to check if the CurrentAuthor field is disabled
    }

    // Add more test cases for other public methods in the jgaapGUI class

}