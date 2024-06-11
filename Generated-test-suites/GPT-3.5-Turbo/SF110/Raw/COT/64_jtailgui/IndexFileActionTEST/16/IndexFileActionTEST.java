
import static org.junit.Assert.*;
import org.junit.Test;

public class IndexFileActionTest {
    
    @Test
    public void testActionPerformed_NoFileSelected() {
        // Create a dummy JTailMainModel and dummy OpenFileModel
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        
        // Create an instance of IndexFileAction
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        // Create a dummy ActionEvent with null file
        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");
        
        // Execute the action
        indexFileAction.actionPerformed(actionEvent);
        
        // Check if the information handler displays the correct message
        assertTrue(InformationHandler.isMessageDisplayed(UIMessage.INFO_NO_FILE_SELECTED));
    }
    
    @Test
    public void testActionPerformed_FileNotFound() {
        // Create a dummy JTailMainModel and dummy OpenFileModel
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        
        // Set the file to a non-existing file
        openFileModel.setFile(new File("non_existing_file.txt"));
        
        // Create an instance of IndexFileAction
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        // Create a dummy ActionEvent
        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");
        
        // Execute the action
        indexFileAction.actionPerformed(actionEvent);
        
        // Check if the exception handler displays the correct message
        assertTrue(ExceptionHandler.isMessageHandled(UIMessage.ERROR_FILE_NOT_FOUND));
    }
    
    @Test
    public void testActionPerformed_IndexTypeMemoryBased() {
        // Create a dummy JTailMainModel and dummy OpenFileModel
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        
        // Set the file to an existing file
        openFileModel.setFile(new File("existing_file.txt"));
        
        // Set the index type to MEMORY_BASED
        openFileModel.setIndexType(IndexType.MEMORY_BASED);
        
        // Create an instance of IndexFileAction
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        // Create a dummy ActionEvent
        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");
        
        // Execute the action
        indexFileAction.actionPerformed(actionEvent);
        
        // Check if the fileIndexer is created with RamFileIndex
        assertNotNull(indexFileAction.getFileIndexer());
        assertTrue(indexFileAction.getFileIndexer() instanceof RamFileIndex);
    }
    
    @Test
    public void testActionPerformed_IndexTypeFileBased() {
        // Create a dummy JTailMainModel and dummy OpenFileModel
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        
        // Set the file to an existing file
        openFileModel.setFile(new File("existing_file.txt"));
        
        // Set the index type to FILE_BASED
        openFileModel.setIndexType(IndexType.FILE_BASED);
        
        // Create an instance of IndexFileAction
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        // Create a dummy ActionEvent
        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");
        
        // Execute the action
        indexFileAction.actionPerformed(actionEvent);
        
        // Check if the fileIndexer is created with RomFileIndex
        assertNotNull(indexFileAction.getFileIndexer());
        assertTrue(indexFileAction.getFileIndexer() instanceof RomFileIndex);
    }
    
    @Test
    public void testUpdate_IndexingCompleted() {
        // Create a dummy JTailMainModel and dummy OpenFileModel
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        
        // Set the file to an existing file
        openFileModel.setFile(new File("existing_file.txt"));
        
        // Set the index type to FILE_BASED
        openFileModel.setIndexType(IndexType.FILE_BASED);
        
        // Create an instance of IndexFileAction
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        // Create a dummy FileIndexer and set it as an observer
        FileIndexer fileIndexer = new FileIndexer(openFileModel.getFile(), RomFileIndex.class);
        fileIndexer.addObserver(indexFileAction);
        
        // Create a dummy Observable and invoke the update method
        Observable observable = new Observable();
        indexFileAction.update(observable, 100);
        
        // Check if the JTailMainModel has been updated with the correct JTailModel
        assertNotNull(jTailMainModel.getModel(fileIndexer.getFile()));
    }
}
