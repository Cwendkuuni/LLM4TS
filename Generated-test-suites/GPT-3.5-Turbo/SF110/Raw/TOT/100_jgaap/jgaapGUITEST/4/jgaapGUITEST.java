import org.junit.*;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testActionPerformed_ExitCommand_ShouldExitSystem() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "Exit");

        assertDoesNotThrow(() -> gui.actionPerformed(e));
    }

    @Test
    public void testActionPerformed_KnownAuthorCommand_ShouldEnableCurrentAuthorTextField() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "knownauthor");

        assertDoesNotThrow(() -> gui.actionPerformed(e));
        assertTrue(gui.currentAuthor.isEnabled());
    }

    @Test
    public void testActionPerformed_UnknownAuthorCommand_ShouldDisableCurrentAuthorTextField() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "unknownauthor");

        assertDoesNotThrow(() -> gui.actionPerformed(e));
        assertFalse(gui.currentAuthor.isEnabled());
    }

    @Test
    public void testActionPerformed_ImportCommand_ShouldAddDocumentToListKnownIfCurrentAuthorIsNotEmpty() {
        jgaapGUI gui = new jgaapGUI();
        gui.currentAuthor.setText("Author");
        ActionEvent e = new ActionEvent(gui, 0, "Import");

        assertDoesNotThrow(() -> gui.actionPerformed(e));
        assertEquals("Author - filename.txt\n", gui.listKnown.getText());
    }

    @Test
    public void testActionPerformed_ImportCommand_ShouldAddDocumentToListUnknownIfCurrentAuthorIsEmpty() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "Import");

        assertDoesNotThrow(() -> gui.actionPerformed(e));
        assertEquals("filename.txt\n", gui.listUnknown.getText());
    }

    @Test
    public void testActionPerformed_CanonicizeCommand_ShouldStartPreprocessEngineDriver() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "Canonicize");

        assertDoesNotThrow(() -> gui.actionPerformed(e));
        // TODO: Test that preprocessEngineDriver is started
    }

    @Test
    public void testActionPerformed_CreateEventSetCommand_ShouldStartCreateEventSetDriver() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "CreateEventSet");

        assertDoesNotThrow(() -> gui.actionPerformed(e));
        // TODO: Test that createEventSetDriver is started
    }

    @Test
    public void testActionPerformed_AnalyzeCommand_ShouldStartRunStatisticalAnalysisDriver() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "Analyze");

        assertDoesNotThrow(() -> gui.actionPerformed(e));
        // TODO: Test that runStatisticalAnalysisDriver is started
        // TODO: Test that results are appended to listResults
    }

    @Test
    public void testActionPerformed_DemoCommand_ShouldCallLoadDemoMethod() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e = new ActionEvent(gui, 0, "demo");

        assertDoesNotThrow(() -> gui.actionPerformed(e));
        // TODO: Test that loadDemo method is called
    }

    @Test
    public void testLoadDemo_ShouldPrintNeedToDemo() {
        jgaapGUI gui = new jgaapGUI();

        assertDoesNotThrow(() -> gui.loadDemo());
        // TODO: Test that "Need to Demo" is printed
    }
}