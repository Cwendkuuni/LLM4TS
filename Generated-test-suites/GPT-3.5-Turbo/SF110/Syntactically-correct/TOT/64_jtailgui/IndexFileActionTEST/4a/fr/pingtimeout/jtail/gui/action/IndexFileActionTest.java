import org.junit.*;
import static org.junit.Assert.*;
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
    
    // Test case for constructor
    @Test
    public void testIndexFileActionConstructor() {
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        assertNotNull(indexFileAction);
        assertEquals(indexFileAction.jTailMainModel, jTailMainModel);
        assertEquals(indexFileAction.openFileModel, openFileModel);
    }
    
    // Test case for actionPerformed method when file is null
    @Test
    public void testActionPerformedFileNull() {
        ActionEvent actionEvent = new ActionEvent(null, 0, null);
        Mockito.when(openFileModel.getFile()).thenReturn(null);
        
        indexFileAction.actionPerformed(actionEvent);
        
        Mockito.verify(openFileModel, Mockito.times(1)).getFile();
        assertEquals(UIMessage.INFO_NO_FILE_SELECTED, InformationHandler.lastHandledInfoMessage());
    }
    
    // Test case for actionPerformed method when index type is MEMORY_BASED
    @Test
    public void testActionPerformedMemoryBased() {
        ActionEvent actionEvent = new ActionEvent(null, 0, null);
        File file = new File("test.txt");
        Mockito.when(openFileModel.getFile()).thenReturn(file);
        Mockito.when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
        
        indexFileAction.actionPerformed(actionEvent);
        
        Mockito.verify(openFileModel, Mockito.times(1)).getFile();
        Mockito.verify(openFileModel, Mockito.times(1)).getIndexType();
        
        Mockito.verify(jTailMainModel, Mockito.times(1)).add(Mockito.any(JTailModel.class));
    }
    
    // Test case for actionPerformed method when index type is FILE_BASED
    @Test
    public void testActionPerformedFileBased() {
        ActionEvent actionEvent = new ActionEvent(null, 0, null);
        File file = new File("test.txt");
        Mockito.when(openFileModel.getFile()).thenReturn(file);
        Mockito.when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        
        indexFileAction.actionPerformed(actionEvent);
        
        Mockito.verify(openFileModel, Mockito.times(1)).getFile();
        Mockito.verify(openFileModel, Mockito.times(1)).getIndexType();
        
        Mockito.verify(jTailMainModel, Mockito.times(1)).add(Mockito.any(JTailModel.class));
    }
    
    // Test case for update method when arg is 100
    @Test
    public void testUpdate() {
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        Observable observable = Mockito.mock(Observable.class);
        Object arg = 100;
        indexFileAction.jTailMainModel = jTailMainModel;
        
        Mockito.when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
        Mockito.when(jTailMainModel.add(Mockito.any(JTailModel.class))).thenReturn(true);
        
        indexFileAction.update(observable, arg);
        
        Mockito.verify(fileIndexer, Mockito.times(1)).getFile();
        Mockito.verify(jTailMainModel, Mockito.times(1)).add(Mockito.any(JTailModel.class));
    }
    
}