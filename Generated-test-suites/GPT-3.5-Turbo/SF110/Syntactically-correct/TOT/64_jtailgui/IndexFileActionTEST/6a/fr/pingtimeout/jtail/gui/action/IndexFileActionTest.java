
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
    public void testActionPerformed_NoFileSelected() {
        ActionEvent actionEvent = new ActionEvent(new JButton(), 1, "open");
        indexFileAction.actionPerformed(actionEvent);

        // Ensure that the information message is displayed
        assertTrue(InformationHandler.isMessageDisplayed());
        assertEquals(UIMessage.INFO_NO_FILE_SELECTED, InformationHandler.getLastDisplayedMessage());
    }

    @Test
    public void testActionPerformed_FileNotFoundException() {
        ActionEvent actionEvent = new ActionEvent(new JButton(), 1, "open");
        openFileModel.setFile(new File("nonexistentfile.txt")); // Set a non-existent file
        indexFileAction.actionPerformed(actionEvent);

        // Ensure that the error message is handled correctly
        assertTrue(ExceptionHandler.isExceptionHandled());
        assertEquals(UIMessage.ERROR_FILE_NOT_FOUND, ExceptionHandler.getLastHandledErrorMessage());
    }

    @Test
    public void testUpdate_IndexingCompletedSuccessfully() {
        Observable observable = new FileIndexer();
        int progress = 100;
        Object arg = progress;

        // Set up the necessary objects for indexing
        openFileModel.setFile(new File("testfile.txt"));
        openFileModel.setIndexType(IndexType.MEMORY_BASED);

        // Execute the update method
        indexFileAction.update(observable, arg);

        // Ensure that the JTailModel is added correctly to the JTailMainModel
        assertEquals(1, jTailMainModel.getModels().size());
        assertEquals(new File("testfile.txt"), jTailMainModel.getModels().get(0).getFile());
    }

    @Test
    public void testUpdate_FileNotFoundException() {
        Observable observable = new FileIndexer();
        int progress = 100;
        Object arg = progress;

        // Set up the necessary objects for indexing
        openFileModel.setFile(new File("nonexistentfile.txt")); // Set a non-existent file
        openFileModel.setIndexType(IndexType.MEMORY_BASED);

        // Execute the update method
        indexFileAction.update(observable, arg);

        // Ensure that the error message is handled correctly
        assertTrue(ExceptionHandler.isExceptionHandled());
        assertEquals(UIMessage.ERROR_FILE_NOT_FOUND, ExceptionHandler.getLastHandledErrorMessage());
    }

    @Test
    public void testUpdate_GenericException() {
        Observable observable = new FileIndexer();
        int progress = 100;
        Object arg = progress;

        // Set up the necessary objects for indexing
        openFileModel.setFile(new File("testfile.txt"));
        openFileModel.setIndexType(IndexType.MEMORY_BASED);

        // Throw a generic exception in the update method
        ExceptionHandler.setThrowException(true);

        // Execute the update method
        indexFileAction.update(observable, arg);

        // Ensure that the error message is handled correctly
        assertTrue(ExceptionHandler.isExceptionHandled());
        assertEquals(UIMessage.ERROR_GENERIC_MESSAGE, ExceptionHandler.getLastHandledErrorMessage());
    }
}

