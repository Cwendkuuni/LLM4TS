import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class IndexFileActionTest {
    
    @Mock
    private JTailMainModel jTailMainModel;
    
    @Mock
    private OpenFileModel openFileModel;
    
    private IndexFileAction indexFileAction;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }
    
    @Test
    public void testActionPerformed_NoFileSelected() {
        // Arrange
        InformationHandler expectedHandler = new InformationHandler();
        expectedHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
        FileIndexer expectedFileIndexer = null;
        File expectedResultFile = null;
        FileNotFoundException expectedFileNotFoundException = new FileNotFoundException();
        Exception expectedGenericException = new Exception();
        
        // Act
        indexFileAction.actionPerformed(null);
        
        // Assert
        assertEquals(expectedHandler, InformationHandler.getInstance());
        assertNull(jTailMainModel.getFile());
        assertNull(openFileModel.getFile());
        assertNull(jTailMainModel.getIndexType());
        assertNull(openFileModel.getIndexType());
        assertEquals(expectedFileIndexer, indexFileAction.getFileIndexer());
        assertEquals(expectedResultFile, indexFileAction.getResultFile());
        assertTrue(expectedFileNotFoundException, indexFileAction.isFileNotFoundException());
        assertTrue(expectedGenericException, indexFileAction.isGenericException());
        assertEquals("Dialog should be invisible", false, indexFileAction.isDialogVisible());
    }
    
    @Test
    public void testActionPerformed_MemoryBasedIndex() {
        // Arrange
        File expectedFile = new File("test.txt");
        Class<RamFileIndex> expectedIndexerClass = RamFileIndex.class;
        FileIndexer expectedFileIndexer = new FileIndexer(expectedFile, expectedIndexerClass);
        int expectedObservableValue = 100;
        LineReader expectedLineReader = new LineReader(expectedFile, expectedFileIndexer.getIndex());
        JTailModel expectedModel = new JTailModel(expectedFile, expectedLineReader);
        List<JTailModel> expectedJTailModels = new ArrayList<>();
        expectedJTailModels.add(expectedModel);
        
        // Act
        indexFileAction.actionPerformed(null);
        indexFileAction.update(expectedFileIndexer, expectedObservableValue);
        
        // Assert
        assertEquals(expectedFile, jTailMainModel.getFile());
        assertEquals(expectedFile, openFileModel.getFile());
        assertEquals(IndexTypeEnum.MEMORY_BASED, jTailMainModel.getIndexType());
        assertEquals(IndexTypeEnum.MEMORY_BASED, openFileModel.getIndexType());
        assertEquals(expectedFileIndexer, indexFileAction.getFileIndexer());
        assertEquals(expectedResultFile, indexFileAction.getResultFile());
        assertEquals(expectedLineReader, indexFileAction.getLineReader());
        assertEquals(expectedJTailModels, jTailMainModel.getModels());
        assertNotNull(expectedHandler);
        assertEquals(false, indexFileAction.isFileNotFoundException());
        assertEquals(false, indexFileAction.isGenericException());
        assertEquals(true, indexFileAction.isDialogVisible());
        assertEquals(InformationHandler.getInstance(), expectedHandler);
    }
    
    @Test
    public void testActionPerformed_FileBasedIndex() {
        // Arrange
        File expectedFile = new File("test.txt");
        Class<RomFileIndex> expectedIndexerClass = RomFileIndex.class;
        FileIndexer expectedFileIndexer = new FileIndexer(expectedFile, expectedIndexerClass);
        int expectedObservableValue = 100;
        LineReader expectedLineReader = new LineReader(expectedFile, expectedFileIndexer.getIndex());
        JTailModel expectedModel = new JTailModel(expectedFile, expectedLineReader);
        List<JTailModel> expectedJTailModels = new ArrayList<>();
        expectedJTailModels.add(expectedModel);
        
        // Act
        indexFileAction.actionPerformed(null);
        indexFileAction.update(expectedFileIndexer, expectedObservableValue);
        
        // Assert
        assertEquals(expectedFile, jTailMainModel.getFile());
        assertEquals(expectedFile, openFileModel.getFile());
        assertEquals(IndexTypeEnum.FILE_BASED, jTailMainModel.getIndexType());
        assertEquals(IndexTypeEnum.FILE_BASED, openFileModel.getIndexType());
        assertEquals(expectedFileIndexer, indexFileAction.getFileIndexer());
        assertEquals(expectedResultFile, indexFileAction.getResultFile());
        assertEquals(expectedLineReader, indexFileAction.getLineReader());
        assertEquals(expectedJTailModels, jTailMainModel.getModels());
        assertNotNull(expectedHandler);
        assertEquals(false, indexFileAction.isFileNotFoundException());
        assertEquals(false, indexFileAction.isGenericException());
        assertEquals(true, indexFileAction.isDialogVisible());
        assertEquals(InformationHandler.getInstance(), expectedHandler);
    }
}