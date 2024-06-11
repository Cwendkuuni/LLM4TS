import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testActionPerformedExit() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent event = new ActionEvent(this, 0, "Exit");
        gui.actionPerformed(event);
        // verify that system exits
    }

    @Test
    public void testActionPerformedKnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent event = new ActionEvent(this, 0, "knownauthor");
        gui.actionPerformed(event);
        // verify that currentAuthor field is enabled
    }

    @Test
    public void testActionPerformedUnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent event = new ActionEvent(this, 0, "unknownauthor");
        gui.actionPerformed(event);
        // verify that currentAuthor field is disabled
    }

    @Test
    public void testActionPerformedImport() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent event = new ActionEvent(this, 0, "Import");
        gui.actionPerformed(event);
        // verify that file chooser dialog opens
        // verify that selected file is added to driver
        // verify that file name is appended to listKnown or listUnknown
    }

    @Test
    public void testActionPerformedCanonicize() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent event = new ActionEvent(this, 0, "Canonicize");
        gui.actionPerformed(event);
        // verify that preprocessEngineDriver is called with correct parameters
        // verify that preprocessing starts
    }

    @Test
    public void testActionPerformedCreateEventSet() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent event = new ActionEvent(this, 0, "CreateEventSet");
        gui.actionPerformed(event);
        // verify that createEventSetDriver is called with correct parameters
        // verify that event set creation starts
    }

    @Test
    public void testActionPerformedAnalyze() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent event = new ActionEvent(this, 0, "Analyze");
        gui.actionPerformed(event);
        // verify that runStatisticalAnalysisDriver is called with correct parameters
        // verify that statistical analysis starts
        // verify that results are appended to listResults
    }

    @Test
    public void testActionPerformedDemo() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent event = new ActionEvent(this, 0, "demo");
        gui.actionPerformed(event);
        // verify that loadDemo() method is called
    }

    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
        // verify that "Need to Demo" message is printed to the console
    }
}