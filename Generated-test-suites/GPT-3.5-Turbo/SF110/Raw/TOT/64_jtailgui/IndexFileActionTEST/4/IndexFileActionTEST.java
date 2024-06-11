import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;
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
    public void testActionPerformed_NoFileSelected() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        JDialog jDialog = mock(JDialog.class);
        Component comp = jDialog;
        
        when(openFileModel.getFile()).thenReturn(null);
        when(actionEvent.getSource()).thenReturn(comp);

        indexFileAction.actionPerformed(actionEvent);

        verify(jDialog).setVisible(false);
        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verify(InformationHandler).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformed_FileNotFound() {
        File file = mock(File.class);
        Class<? extends FileIndex> indexType = mock(Class.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        JDialog jDialog = mock(JDialog.class);
        Component comp = jDialog;
        
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(indexType);
        when(actionEvent.getSource()).thenReturn(comp);
        when(indexType.getName()).thenReturn("MEMORY_BASED");
        doThrow(FileNotFoundException.class).when(fileIndexer).new FileIndexer(file, indexType);

        indexFileAction.actionPerformed(actionEvent);

        verify(jDialog).setVisible(false);
        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verify(ExceptionHandler).handle(e, UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
    }

    @Test
    public void testActionPerformed_Success() {
        File file = mock(File.class);
        Class<? extends FileIndex> indexType = mock(Class.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        JDialog jDialog = mock(JDialog.class);
        Component comp = jDialog;
        FileIndex fileIndex = mock(FileIndex.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel model = mock(JTailModel.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(indexType);
        when(actionEvent.getSource()).thenReturn(comp);
        when(indexType.getName()).thenReturn("MEMORY_BASED");
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(fileIndex);
        when(file.getName()).thenReturn("testfile.txt");
        when(lineReader.getLinesCount()).thenReturn(100);
        when(fileIndex.getIndex()).thenReturn(100);
        when(fileIndexerWorker.isDone()).thenReturn(true);
        when(fileIndexerWorker.get()).thenReturn(fileIndexer);

        indexFileAction.actionPerformed(actionEvent);

        verify(jDialog).setVisible(false);
        verify(jTailMainModel).add(model);
        verify(ExceptionHandler, never()).handle(any(Exception.class), anyString(), any(Object[].class));
    }

    @Test
    public void testUpdate_Success() {
        Observable observable = mock(Observable.class);
        FileIndex fileIndex = mock(FileIndex.class);
        File file = mock(File.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel model = mock(JTailModel.class);

        when(observable).thenReturn(fileIndex);
        when(fileIndex.getFile()).thenReturn(file);
        when(file.getName()).thenReturn("testfile.txt");
        when(lineReader.getLinesCount()).thenReturn(100);
        when(fileIndex.getIndex()).thenReturn(fileIndex);
        when(fileIndexerWorker.isDone()).thenReturn(true);
        when(fileIndexerWorker.get()).thenReturn(fileIndexer);

        indexFileAction.update(observable, 100);

        verify(jTailMainModel).add(model);
        verify(ExceptionHandler, never()).handle(any(Exception.class), anyString(), any(Object[].class));
    }

}