import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IndexFileActionTest {

    @Test
    public void testActionPerformed_fileNotNull_indexerClassSet() {
        // Create mock models
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);

        // Initialize IndexFileAction
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        // Set file and index type
        when(openFileModel.getFile()).thenReturn(new File("test.txt"));
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);

        // Call actionPerformed method
        indexFileAction.actionPerformed(null);

        // Verify that the indexerClass is set correctly
        assertEquals(RomFileIndex.class, indexFileAction.getIndexerClass());
    }

    @Test
    public void testActionPerformed_fileNull_showInfoMessage() {
        // Create mock models
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);

        // Initialize IndexFileAction
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        // Set file as null
        when(openFileModel.getFile()).thenReturn(null);

        // Call actionPerformed method
        indexFileAction.actionPerformed(null);

        // Verify that InformationHandler is called with the correct message
        verify(InformationHandler).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testUpdate_notificationReceived_addedToModel() {
        // Create mock objects
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);

        // Initialize IndexFileAction
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        // Create mock FileIndexer and File object
        FileIndexer fileIndexer = mock(FileIndexer.class);
        File file = new File("test.txt");

        // Call update method
        indexFileAction.update(fileIndexer, 100);

        // Verify that a new model is added to the main model
        verify(jTailMainModel).add(any(JTailModel.class));
    }
}