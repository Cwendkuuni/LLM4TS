import org.junit.*;
import static org.junit.Assert.*;

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
        // Test when openFileModel.getFile() is null
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "");
        indexFileAction.actionPerformed(actionEvent);
        assertFalse(dialog.isVisible());
        verify(InformationHandler).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);

        // Test when openFileModel.getFile() is not null and openFileModel.getIndexType() is MEMORY_BASED
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(IndexType.MEMORY_BASED);
        indexFileAction.actionPerformed(actionEvent);
        verify(fileIndexer, times(1)).addObserver(indexFileAction);

        // Test when openFileModel.getFile() is not null and openFileModel.getIndexType() is FILE_BASED
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(IndexType.FILE_BASED);
        indexFileAction.actionPerformed(actionEvent);
        verify(fileIndexer, times(1)).addObserver(indexFileAction);
    }

    @Test
    public void testUpdate() {
        // Test when arg is 100
        Observable observable = new FileIndexer();
        indexFileAction.update(observable, 100);
        verify(jTailMainModel, times(1)).add(any(JTailModel.class));

        // Test when arg is not 100
        indexFileAction.update(observable, 50);
        verify(jTailMainModel, times(0)).add(any(JTailModel.class));
    }

}