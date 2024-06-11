import org.junit.*;
import static org.junit.Assert.*;
import java.awt.event.*;

public class jgaapGUITest {
    private jgaapGUI gui;

    @Before
    public void setUp() {
        gui = new jgaapGUI();
    }

    @Test
    public void testActionPerformed_ExitCommand_ShouldExitProgram() {
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "Exit");
        gui.actionPerformed(event);

        assertFalse(gui.isDisplayable());
    }

    @Test
    public void testActionPerformed_KnownAuthorCommand_ShouldEnableCurrentAuthorTextField() {
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "knownauthor");
        gui.actionPerformed(event);

        assertTrue(gui.currentAuthor.isEnabled());
    }

    @Test
    public void testActionPerformed_UnknownAuthorCommand_ShouldDisableCurrentAuthorTextField() {
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "unknownauthor");
        gui.actionPerformed(event);

        assertFalse(gui.currentAuthor.isEnabled());
    }

    @Test
    public void testActionPerformed_ImportCommand_ShouldAddDocument() {
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "Import");
        gui.currentAuthor.setEnabled(true);
        gui.currentAuthor.setText("John");
        gui.driver = new guiDriver() {
            @Override
            public void addDocument(String file, String author) {}
        };

        gui.actionPerformed(event);

        // Assert the expected changes in the JTextAreas or other components
    }

    // Write more test methods for other actionPerformed commands

}