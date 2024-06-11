import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class IndexFileActionTest {
    
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private IndexFileAction indexFileAction;
    
    @Before
    public void setUp() {
        jTailMainModel = new JTailMainModel();
        openFileModel = new OpenFileModel();
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }
    
    @Test
    public void testActionPerformed() {
        // Typical use case - file selected
        openFileModel.setFile(new File("test.txt"));
        indexFileAction.actionPerformed(null);
        // Assert that the JTailModel is added to jTailMainModel
        assertTrue(jTailMainModel.getModels().size() == 1);
        
        // Error scenario - file not selected
        openFileModel.setFile(null);
        indexFileAction.actionPerformed(null);
        // Assert that an InformationHandler UIMessage is displayed
        // with the INFO_NO_FILE_SELECTED message
        assertTrue(InformationHandler.getCalledWith() == UIMessage.INFO_NO_FILE_SELECTED);
        
        // Edge case - indexer class is null
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(null);
        indexFileAction.actionPerformed(null);
        // Assert that no FileIndexer is created
        assertTrue(jTailMainModel.getModels().size() == 1);
    }
    
    @Test
    public void testUpdate() {
        FileIndexer fileIndexer = new FileIndexer(new File("test.txt"), RamFileIndex.class);
        indexFileAction.update(fileIndexer, 100);
        // Assert that a JTailModel is added to jTailMainModel
        assertTrue(jTailMainModel.getModels().size() == 1);
        
        // Error scenario - file not found
        fileIndexer = new FileIndexer(new File("nonexistent.txt"), RamFileIndex.class);
        indexFileAction.update(fileIndexer, 100);
        // Assert that an ExceptionHandler UIMessage is displayed
        // with the ERROR_FILE_NOT_FOUND message
        assertTrue(ExceptionHandler.getCalledWith() == UIMessage.ERROR_FILE_NOT_FOUND);
        
        // Error scenario - generic exception
        fileIndexer = new FileIndexer(new File("test.txt"), RomFileIndex.class);
        indexFileAction.update(fileIndexer, 100);
        // Assert that an ExceptionHandler UIMessage is displayed
        // with the ERROR_GENERIC_MESSAGE message
        assertTrue(ExceptionHandler.getCalledWith() == UIMessage.ERROR_GENERIC_MESSAGE);
    }
}