import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class IndexFileActionTest {

    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;

    @Before
    public void setUp() {
        jTailMainModel = Mockito.mock(JTailMainModel.class);
        openFileModel = Mockito.mock(OpenFileModel.class);
    }

    @Test
    public void testActionPerformed_withNonNullFileAndMemoryIndexType_shouldCreateFileIndexerAndExecuteWorker() {
        // Arrange
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        Mockito.when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
        Mockito.when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
        
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        Mockito.whenNew(FileIndexer.class).withArguments(Mockito.any(File.class), Mockito.eq(RamFileIndex.class)).thenReturn(fileIndexer);
        
        FileIndexerWorker fileIndexerWorker = Mockito.mock(FileIndexerWorker.class);
        Mockito.whenNew(FileIndexerWorker.class).withArguments(fileIndexer).thenReturn(fileIndexerWorker);
        
        // Act
        indexFileAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
        
        // Assert
        Mockito.verify(fileIndexer).addObserver(indexFileAction);
        Mockito.verify(fileIndexerWorker).execute();
    }

    @Test
    public void testActionPerformed_withNonNullFileAndFileIndexType_shouldCreateFileIndexerAndExecuteWorker() {
        // Arrange
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        Mockito.when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
        Mockito.when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        Mockito.whenNew(FileIndexer.class).withArguments(Mockito.any(File.class), Mockito.eq(RomFileIndex.class)).thenReturn(fileIndexer);
        
        FileIndexerWorker fileIndexerWorker = Mockito.mock(FileIndexerWorker.class);
        Mockito.whenNew(FileIndexerWorker.class).withArguments(fileIndexer).thenReturn(fileIndexerWorker);
        
        // Act
        indexFileAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
        
        // Assert
        Mockito.verify(fileIndexer).addObserver(indexFileAction);
        Mockito.verify(fileIndexerWorker).execute();
    }

    @Test
    public void testActionPerformed_withNullFile_shouldHandleNoFileSelectedInformation() {
        // Arrange
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        Mockito.when(openFileModel.getFile()).thenReturn(null);
        
        // Act
        indexFileAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
        
        // Assert
        Mockito.verifyStatic(InformationHandler.class);
        InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testUpdate_withProgressEqualTo100_shouldAddJTailModelToJTailMainModel() throws FileNotFoundException {
        // Arrange
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        Mockito.when(fileIndexer.getFile()).thenReturn(new File("testFile.txt"));
        
        LineReader lineReader = Mockito.mock(LineReader.class);
        Mockito.whenNew(LineReader.class).withArguments(Mockito.any(File.class), Mockito.any(Index.class)).thenReturn(lineReader);
        
        JTailModel jTailModel = Mockito.mock(JTailModel.class);
        Mockito.whenNew(JTailModel.class).withArguments(Mockito.any(File.class), Mockito.eq(lineReader)).thenReturn(jTailModel);
        
        // Act
        indexFileAction.update(fileIndexer, 100);
        
        // Assert
        Mockito.verify(jTailMainModel).add(jTailModel);
    }

    @Test
    public void testUpdate_withFileNotFoundException_shouldHandleFileNotFoundError() throws FileNotFoundException {
        // Arrange
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        Mockito.when(fileIndexer.getFile()).thenReturn(new File("testFile.txt"));
        
        Mockito.whenNew(LineReader.class).withArguments(Mockito.any(File.class), Mockito.any(Index.class)).thenThrow(new FileNotFoundException());
        
        // Act
        indexFileAction.update(fileIndexer, 100);
        
        // Assert
        Mockito.verifyStatic(ExceptionHandler.class);
        ExceptionHandler.handle(Mockito.any(FileNotFoundException.class), Mockito.eq(UIMessage.ERROR_FILE_NOT_FOUND), Mockito.anyString());
    }

    @Test
    public void testUpdate_withGenericException_shouldHandleGenericErrorMessage() throws FileNotFoundException {
        // Arrange
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        Mockito.when(fileIndexer.getFile()).thenReturn(new File("testFile.txt"));
        
        Mockito.whenNew(LineReader.class).withArguments(Mockito.any(File.class), Mockito.any(Index.class)).thenThrow(new Exception());
        
        // Act
        indexFileAction.update(fileIndexer, 100);
        
        // Assert
        Mockito.verifyStatic(ExceptionHandler.class);
        ExceptionHandler.handle(Mockito.any(Exception.class), Mockito.eq(UIMessage.ERROR_GENERIC_MESSAGE), Mockito.any(Object[].class));
    }

}