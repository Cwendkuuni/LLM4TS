import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.JDialog;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.util.ExceptionHandler;

public class IndexFileActionTest {

    @Test
    public void testActionPerformed_noFileSelected() {
        // Test when no file has been selected
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        ActionEvent actionEvent = new ActionEvent(new Component() {
        }, ActionEvent.ACTION_FIRST, "command");
        
        indexFileAction.actionPerformed(actionEvent);
        
        assertTrue(UIMessage.INFO_NO_FILE_SELECTED, InformationHandler.handled);
    }

    @Test
    public void testActionPerformed_fileNotFound() {
        // Test when selected file is not found
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        openFileModel.setFile(new File("nonExistentFile.txt"));
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        ActionEvent actionEvent = new ActionEvent(new Component() {
        }, ActionEvent.ACTION_FIRST, "command");
        
        indexFileAction.actionPerformed(actionEvent);
        
        assertTrue(UIMessage.ERROR_FILE_NOT_FOUND, ExceptionHandler.handled);
    }

    @Test
    public void testUpdate_successfulNotification() {
        // Test successful notification handling
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        FileIndexer fileIndexer = new FileIndexer(new File("testFile.txt"), RamFileIndex.class);
        fileIndexer.addObserver(indexFileAction);
        
        indexFileAction.update(fileIndexer, 100);
        
        assertEquals(1, jTailMainModel.getSize());
    }

    @Test
    public void testUpdate_fileNotFoundExceptionHandling() {
        // Test file not found exception handling
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        FileIndexer fileIndexer = new FileIndexer(new File("nonExistentFile.txt"), RamFileIndex.class);
        fileIndexer.addObserver(indexFileAction);
        
        indexFileAction.update(fileIndexer, 100);
        
        assertTrue(UIMessage.ERROR_FILE_NOT_FOUND, ExceptionHandler.handled);
    }

}