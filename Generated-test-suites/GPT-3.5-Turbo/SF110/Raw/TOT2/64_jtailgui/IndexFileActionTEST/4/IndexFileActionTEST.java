import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IndexFileActionTest {
    
    @Test
    public void testActionPerformed_noFileSelected() {
        // Create mock objects
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        // Set up mock behavior
        when(openFileModel.getFile()).thenReturn(null);
        
        // Call the method to test
        indexFileAction.actionPerformed(null);
        
        // Verify that InformationHandler is handling the expected message for no file selected
        verifyStatic(InformationHandler.class);
        InformationHandler.handle(eq(true), eq(UIMessage.INFO_NO_FILE_SELECTED), any());
    }
    
    @Test
    public void testActionPerformed_fileNotFound() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        // Set up mock behavior
        File file = new File("nonexistentfile.txt");
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        
        // Call the method to test
        indexFileAction.actionPerformed(null);
        
        // Verify that ExceptionHandler is handling the expected message for file not found
        verifyStatic(ExceptionHandler.class);
        ExceptionHandler.handle(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq(file.getName()));
    }
    
    @Test
    public void testUpdate_success() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        final FileIndexer fileIndexer = mock(FileIndexer.class);
        final File file = new File("testfile.txt");
        when(fileIndexer.getFile()).thenReturn(file);
        
        // Call the update method
        indexFileAction.update(fileIndexer, 100);
        
        // Verify that the JTailMainModel is adding the expected model when update is called with arg 100
        verify(jTailMainModel).add(any(JTailModel.class));
    }
}