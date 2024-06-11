import org.junit.*;
import static org.junit.Assert.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class IndexFileActionTest {

    private static JTailMainModel mockJTailMainModel;
    private static OpenFileModel mockOpenFileModel;
    private static IndexFileAction indexFileAction;

    @BeforeClass
    public static void setUp() {
        mockJTailMainModel = new JTailMainModel();
        mockOpenFileModel = new OpenFileModel();
        indexFileAction = new IndexFileAction(mockJTailMainModel, mockOpenFileModel);
    }

    @Test
    public void testActionPerformed_withNullFile() {
        ActionEvent actionEvent = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "");

        indexFileAction.actionPerformed(actionEvent);

        // Verify that InformationHandler handles the case when no file is selected
        assertTrue(InformationHandler.isHandled());
        assertEquals(UIMessage.INFO_NO_FILE_SELECTED, InformationHandler.getMessage());
    }

    @Test
    public void testActionPerformed_withMemoryBasedIndex() throws FileNotFoundException {
        File mockFile = new File("testFile.txt");
        mockOpenFileModel.setFile(mockFile);
        mockOpenFileModel.setIndexType(IndexType.MEMORY_BASED);

        ActionEvent actionEvent = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "");

        indexFileAction.actionPerformed(actionEvent);

        // Verify that FileIndexer and FileIndexerWorker are created and executed correctly
        assertNotNull(mockOpenFileModel.getFile());
        assertEquals(RamFileIndex.class, indexFileAction.indexerClass);
        assertNotNull(mockOpenFileModel.getObserver());
        assertTrue(mockOpenFileModel.getObserver() instanceof Observer);
        assertTrue(mockOpenFileModel.getFileIndexer() instanceof FileIndexer);
        assertTrue(mockOpenFileModel.getFileIndexer().getObserver() instanceof Observer);
        assertTrue(mockOpenFileModel.getFileIndexerWorker() instanceof FileIndexerWorker);
        assertTrue(mockOpenFileModel.getFileIndexerWorker().isExecuted());
    }

    @Test
    public void testActionPerformed_withFileBasedIndex() throws FileNotFoundException {
        File mockFile = new File("testFile.txt");
        mockOpenFileModel.setFile(mockFile);
        mockOpenFileModel.setIndexType(IndexType.FILE_BASED);

        ActionEvent actionEvent = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "");

        indexFileAction.actionPerformed(actionEvent);

        // Verify that FileIndexer and FileIndexerWorker are created and executed correctly
        assertNotNull(mockOpenFileModel.getFile());
        assertEquals(RomFileIndex.class, indexFileAction.indexerClass);
        assertNotNull(mockOpenFileModel.getObserver());
        assertTrue(mockOpenFileModel.getObserver() instanceof Observer);
        assertTrue(mockOpenFileModel.getFileIndexer() instanceof FileIndexer);
        assertTrue(mockOpenFileModel.getFileIndexer().getObserver() instanceof Observer);
        assertTrue(mockOpenFileModel.getFileIndexerWorker() instanceof FileIndexerWorker);
        assertTrue(mockOpenFileModel.getFileIndexerWorker().isExecuted());
    }

    @Test
    public void testUpdate_withCompletionArgEquals100() throws FileNotFoundException {
        File mockFile = new File("testFile.txt");
        FileIndexer mockFileIndexer = new FileIndexer(mockFile, RamFileIndex.class);
        LineReader mockLineReader = new LineReader(mockFile, mockFileIndexer.getIndex());
        JTailModel mockJTailModel = new JTailModel(mockFile, mockLineReader);
        mockJTailMainModel.add(mockJTailModel);

        Object completionArg = 100;
        indexFileAction.update(mockFileIndexer, completionArg);

        // Verify that JTailModel is added to JTailMainModel
        assertTrue(mockJTailMainModel.contains(mockJTailModel));
    }

    @Test
    public void testUpdate_withCompletionArgNotEquals100() throws FileNotFoundException {
        File mockFile = new File("testFile.txt");
        FileIndexer mockFileIndexer = new FileIndexer(mockFile, RamFileIndex.class);

        Object completionArg = 50;
        indexFileAction.update(mockFileIndexer, completionArg);

        // Verify that JTailModel is not added to JTailMainModel
        assertFalse(mockJTailMainModel.contains(mockFileIndexer));
    }
}