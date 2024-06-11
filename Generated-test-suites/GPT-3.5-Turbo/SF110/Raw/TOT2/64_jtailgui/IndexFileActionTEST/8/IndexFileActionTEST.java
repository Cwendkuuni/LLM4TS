import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class IndexFileActionTest {
    private IndexFileAction indexFileAction;

    @Before
    public void setup() {
        indexFileAction = new IndexFileAction(new JTailMainModel(), new OpenFileModel());
    }

    @Test
    public void testActionPerformed_noFileSelected() {
        // Test when no file is selected
        // Expected behavior is to handle with information message
        indexFileAction.openFileModel.setFile(null);
        indexFileAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "No file selected"));
        // Verify information message displayed
        assertTrue(InformationHandler.hasHandled());
    }

    @Test
    public void testActionPerformed_fileNotFound() {
        // Test when file is not found during indexing
        // Expected behavior is to handle with error message
        indexFileAction.openFileModel.setFile(new File("nonexistentfile.txt"));
        indexFileAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "File not found"));
        // Verify error message handling
        assertTrue(ExceptionHandler.hasHandled());
    }

    @Test
    public void testUpdate_notificationReceived() {
        // Test when notification update process is received
        // Expected behavior is to add new model to main model
        FileIndexer fileIndexer = new FileIndexer(new File("testfile.txt"), RamFileIndex.class);
        indexFileAction.update(fileIndexer, 100);
        // Verify new model added to main model
        assertEquals(1, indexFileAction.jTailMainModel.getModelList().size());
    }
}