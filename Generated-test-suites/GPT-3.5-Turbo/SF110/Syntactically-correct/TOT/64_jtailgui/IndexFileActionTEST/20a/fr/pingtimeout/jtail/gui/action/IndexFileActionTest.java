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
    public void testActionPerformed_OpenFileWithoutSelectedFile_ShouldShowInformationMessage() {
        indexFileAction.actionPerformed(null);
        assertTrue(InformationHandler.isInfoMessageShown());
    }

    @Test
    public void testActionPerformed_OpenFileWithMemoryIndexType_ShouldCreateRamFileIndexAndAddToJTailMainModel() {
        // Mocking dependencies
        File file = new File("test.txt");
        openFileModel.setFile(file);
        openFileModel.setIndexType(IndexType.MEMORY_BASED);

        indexFileAction.actionPerformed(null);

        FileIndexer createdFileIndexer = jTailMainModel.getLatestAddedModelIndexer();
        assertTrue(createdFileIndexer instanceof RamFileIndex);
    }

    @Test
    public void testActionPerformed_OpenFileWithFileIndexType_ShouldCreateRomFileIndexAndAddToJTailMainModel() {
        // Mocking dependencies
        File file = new File("test.txt");
        openFileModel.setFile(file);
        openFileModel.setIndexType(IndexType.FILE_BASED);

        indexFileAction.actionPerformed(null);

        FileIndexer createdFileIndexer = jTailMainModel.getLatestAddedModelIndexer();
        assertTrue(createdFileIndexer instanceof RomFileIndex);
    }

    @Test(expected = FileNotFoundException.class)
    public void testActionPerformed_OpenNonExistingFile_ShouldThrowFileNotFoundException() {
        // Mocking dependencies
        File file = new File("non_existing_file.txt");
        openFileModel.setFile(file);
        openFileModel.setIndexType(IndexType.MEMORY_BASED);

        indexFileAction.actionPerformed(null);
    }

    @Test
    public void testUpdate_NotificationWithValue100_ShouldCreateJTailModelAndAddToJTailMainModel() {
        // Mocking dependencies
        FileIndexer fileIndexer = new RamFileIndex();
        fileIndexer.setFile(new File("test.txt"));
        indexFileAction.jTailMainModel.addObserver(fileIndexer);

        indexFileAction.update(fileIndexer, 100);

        JTailModel jTailModel = jTailMainModel.getLatestAddedModel();
        assertNotNull(jTailModel);
    }

    @Test(expected = FileNotFoundException.class)
    public void testUpdate_NotificationWithValue100AndExceptionThrown_ShouldThrowFileNotFoundException() {
        // Mocking dependencies
        FileIndexer fileIndexer = new RamFileIndex();
        fileIndexer.setFile(new File("non_existing_file.txt"));
        indexFileAction.jTailMainModel.addObserver(fileIndexer);

        indexFileAction.update(fileIndexer, 100);
    }

}