import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.*;

public class IndexFileActionTest {

    @Mock
    private JTailMainModel jTailMainModel;
    
    @Mock
    private OpenFileModel openFileModel;

    @InjectMocks
    private IndexFileAction indexFileAction;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testActionPerformed_fileIsNull() {
        Mockito.when(openFileModel.getFile()).thenReturn(null);
        
        indexFileAction.actionPerformed(null);
        
        Mockito.verifyZeroInteractions(jTailMainModel);
        Mockito.verify(openFileModel, Mockito.times(1)).getFile();
        // verify InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformed_fileNotFound() throws FileNotFoundException {
        File file = new File("test.txt");
        Mockito.when(openFileModel.getFile()).thenReturn(file);
        Mockito.when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        
        indexFileAction.actionPerformed(null);
        
        Mockito.verify(openFileModel, Mockito.times(1)).getFile();
        Mockito.verify(openFileModel, Mockito.times(1)).getIndexType();
        // verify ExceptionHandler.handle(e, UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
    }

    @Test
    public void testActionPerformed_success() throws FileNotFoundException {
        File file = new File("test.txt");
        Mockito.when(openFileModel.getFile()).thenReturn(file);
        Mockito.when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
        
        indexFileAction.actionPerformed(null);

        Mockito.verify(openFileModel, Mockito.times(1)).getFile();
        Mockito.verify(openFileModel, Mockito.times(1)).getIndexType();
        // verify new FileIndexer(file, RamFileIndex.class)
        // verify fileIndexer.addObserver(this)
        // verify fileIndexerWorker.execute();
    }

    @Test
    public void testUpdate() throws Exception {
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        File file = new File("test.txt");
        Mockito.when(fileIndexer.getFile()).thenReturn(file);
        Mockito.when(fileIndexer.getIndex()).thenReturn(100);
        Mockito.when(openFileModel.getFile()).thenReturn(file);
        
        indexFileAction.update(fileIndexer, 100);

        Mockito.verify(fileIndexer, Mockito.times(1)).getFile();
        Mockito.verify(fileIndexer, Mockito.times(1)).getIndex();
        Mockito.verify(openFileModel, Mockito.atLeastOnce()).getFile();
        // verify new LineReader(file, fileIndexer.getIndex())
        // verify jTailMainModel.add(model);
    }
}