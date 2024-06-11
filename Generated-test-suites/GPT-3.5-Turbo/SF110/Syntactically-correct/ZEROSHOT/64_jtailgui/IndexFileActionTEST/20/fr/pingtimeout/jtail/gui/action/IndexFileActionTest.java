import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IndexFileActionTest {
    
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private IndexFileAction indexFileAction;
    
    @Before
    public void setUp() {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = mock(OpenFileModel.class);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }
    
    @Test
    public void testActionPerformed_withNullFile() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(null);
        when(openFileModel.getFile()).thenReturn(null);
        
        indexFileAction.actionPerformed(actionEvent);
        
        verify(dialog).setVisible(false);
        verifyNoMoreInteractions(jTailMainModel);
    }
    
    @Test
    public void testActionPerformed_withFileNotFoundException() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        Dialog fileNotFoundDialog = mock(Dialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(fileNotFoundDialog);
        when(openFileModel.getFile()).thenReturn(new File("test.txt"));
        when(openFileModel.getIndexType()).thenReturn(OpenFileModel.IndexType.MEMORY_BASED);
        when(fileNotFoundDialog.getName()).thenReturn("FileNotFoundDialog");
        
        indexFileAction.actionPerformed(actionEvent);
        
        verify(dialog).setVisible(false);
        verify(ExceptionHandler).handle(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq("test.txt"));
    }
    
    @Test
    public void testActionPerformed_withMemoryBasedIndexType() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        Dialog dialogParent = mock(Dialog.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel model = mock(JTailModel.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(dialogParent);
        when(openFileModel.getFile()).thenReturn(new File("test.txt"));
        when(openFileModel.getIndexType()).thenReturn(OpenFileModel.IndexType.MEMORY_BASED);
        when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
        when(fileIndexer.getIndex()).thenReturn(new HashMap<>());
        when(model.getFile()).thenReturn(new File("test.txt"));
        when(model.getLineReader()).thenReturn(lineReader);
        
        indexFileAction.actionPerformed(actionEvent);
        indexFileAction.update(fileIndexer, 100);
        
        verify(dialog).setVisible(false);
        verify(fileIndexer).addObserver(indexFileAction);
        verify(fileIndexerWorker).execute();
        verify(jTailMainModel).add(model);
    }
    
    @Test
    public void testActionPerformed_withFileBasedIndexType() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        Dialog dialogParent = mock(Dialog.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel model = mock(JTailModel.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(dialogParent);
        when(openFileModel.getFile()).thenReturn(new File("test.txt"));
        when(openFileModel.getIndexType()).thenReturn(OpenFileModel.IndexType.FILE_BASED);
        when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
        when(fileIndexer.getIndex()).thenReturn(new HashMap<>());
        when(model.getFile()).thenReturn(new File("test.txt"));
        when(model.getLineReader()).thenReturn(lineReader);
        
        indexFileAction.actionPerformed(actionEvent);
        indexFileAction.update(fileIndexer, 100);
        
        verify(dialog).setVisible(false);
        verify(fileIndexer).addObserver(indexFileAction);
        verify(fileIndexerWorker).execute();
        verify(jTailMainModel).add(model);
    }
    
    @Test
    public void testUpdate_withNotificationArg100() {
        FileIndexer fileIndexer = mock(FileIndexer.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel model = mock(JTailModel.class);
        when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
        when(fileIndexer.getIndex()).thenReturn(new HashMap<>());
        when(model.getFile()).thenReturn(new File("test.txt"));
        when(model.getLineReader()).thenReturn(lineReader);
        
        indexFileAction.update(fileIndexer, 100);
        
        verify(jTailMainModel).add(model);
    }
}