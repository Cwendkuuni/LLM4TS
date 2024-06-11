import org.junit.Before;
import org.junit.Test;
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
    public void testActionPerformed_withNoFileSelected() {
        when(openFileModel.getFile()).thenReturn(null);
        
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(new JFrame());
        
        indexFileAction.actionPerformed(actionEvent);
        
        verify(dialog).setVisible(false);
        verify(openFileModel).getFile();
        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verifyZeroInteractions(openFileModel, jTailMainModel);
    }
    
    @Test
    public void testActionPerformed_withFileSelectedAndMemoryBasedIndexType() {
        File file = mock(File.class);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
        
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(new JFrame());
        
        FileIndexer fileIndexer = mock(FileIndexer.class);
        whenNew(FileIndexer.class).withArguments(eq(file), eq(RamFileIndex.class)).thenReturn(fileIndexer);
        
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        whenNew(FileIndexerWorker.class).withArguments(eq(fileIndexer)).thenReturn(fileIndexerWorker);
        
        indexFileAction.actionPerformed(actionEvent);
        
        verify(dialog).setVisible(false);
        verify(openFileModel).getFile();
        verify(openFileModel).getIndexType();
        verify(fileIndexer).addObserver(eq(indexFileAction));
        verify(fileIndexerWorker).execute();
        verifyZeroInteractions(jTailMainModel);
    }
    
    @Test
    public void testActionPerformed_withFileSelectedAndFileBasedIndexType() {
        File file = mock(File.class);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(new JFrame());
        
        FileIndexer fileIndexer = mock(FileIndexer.class);
        whenNew(FileIndexer.class).withArguments(eq(file), eq(RomFileIndex.class)).thenReturn(fileIndexer);
        
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        whenNew(FileIndexerWorker.class).withArguments(eq(fileIndexer)).thenReturn(fileIndexerWorker);
        
        indexFileAction.actionPerformed(actionEvent);
        
        verify(dialog).setVisible(false);
        verify(openFileModel).getFile();
        verify(openFileModel).getIndexType();
        verify(fileIndexer).addObserver(eq(indexFileAction));
        verify(fileIndexerWorker).execute();
        verifyZeroInteractions(jTailMainModel);
    }
    
    @Test
    public void testUpdate_withCompleteFileIndex() {
        Observable observable = mock(Observable.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(observable instanceof FileIndexer).thenReturn(true);
        when(observable).thenReturn(fileIndexer);
        when(fileIndexer.getFile()).thenReturn(mock(File.class));
        when(fileIndexer.getIndex()).thenReturn(new ArrayList<>());
        
        LineReader lineReader = mock(LineReader.class);
        whenNew(LineReader.class).withArguments(any(File.class), anyList()).thenReturn(lineReader);
        
        JTailModel model = mock(JTailModel.class);
        whenNew(JTailModel.class).withArguments(any(File.class), any(LineReader.class)).thenReturn(model);
        
        indexFileAction.update(observable, 100);
        
        verify(fileIndexer).getFile();
        verify(fileIndexer).getIndex();
        verifyNew(LineReader.class).withArguments(any(File.class), anyList());
        verifyNew(JTailModel.class).withArguments(any(File.class), any(LineReader.class));
        verify(jTailMainModel).add(model);
    }
    
    @Test
    public void testUpdate_withFileNotFoundException() {
        Observable observable = mock(Observable.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(observable instanceof FileIndexer).thenReturn(true);
        when(observable).thenReturn(fileIndexer);
        when(fileIndexer.getFile()).thenReturn(mock(File.class));
        when(fileIndexer.getIndex()).thenReturn(new ArrayList<>());
        
        FileNotFoundException exception = new FileNotFoundException();
        whenNew(LineReader.class).withArguments(any(File.class), anyList()).thenThrow(exception);
        
        indexFileAction.update(observable, 100);
        
        verify(fileIndexer).getFile();
        verify(fileIndexer).getIndex();
        verifyNew(LineReader.class).withArguments(any(File.class), anyList());
        verify(ExceptionHandler.class).handle(eq(exception), eq(UIMessage.ERROR_FILE_NOT_FOUND), anyString());
        verifyZeroInteractions(jTailMainModel);
    }
    
    @Test
    public void testUpdate_withGenericException() {
        Observable observable = mock(Observable.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(observable instanceof FileIndexer).thenReturn(true);
        when(observable).thenReturn(fileIndexer);
        when(fileIndexer.getFile()).thenReturn(mock(File.class));
        when(fileIndexer.getIndex()).thenReturn(new ArrayList<>());
        
        Exception exception = new Exception();
        whenNew(LineReader.class).withArguments(any(File.class), anyList()).thenThrow(exception);
        
        indexFileAction.update(observable, 100);
        
        verify(fileIndexer).getFile();
        verify(fileIndexer).getIndex();
        verifyNew(LineReader.class).withArguments(any(File.class), anyList());
        verify(ExceptionHandler.class).handle(eq(exception), eq(UIMessage.ERROR_GENERIC_MESSAGE), any(Object[].class));
        verifyZeroInteractions(jTailMainModel);
    }
}