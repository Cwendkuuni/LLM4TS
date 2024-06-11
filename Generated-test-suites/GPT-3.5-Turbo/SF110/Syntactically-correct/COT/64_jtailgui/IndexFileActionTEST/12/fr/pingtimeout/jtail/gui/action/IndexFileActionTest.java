import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.JDialog;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import java.io.File;
import java.io.FileNotFoundException;
import fr.pingtimeout.jtail.gui.message.ExceptionHandler;

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
  public void testActionPerformedNullFile() {
    ActionEvent actionEvent = new ActionEvent(new Object(), 1, "command");
    
    indexFileAction.actionPerformed(actionEvent);
    
    assertTrue(InformationHandler.isHandled());
    assertEquals(UIMessage.INFO_NO_FILE_SELECTED, InformationHandler.getHandledMessage());
  }
  
  @Test
  public void testActionPerformedMemoryBased() throws FileNotFoundException {
    ActionEvent actionEvent = new ActionEvent(new Object(), 1, "command");
    openFileModel.setFile(new File("test.txt"));
    openFileModel.setIndexType(IndexType.MEMORY_BASED);
    
    indexFileAction.actionPerformed(actionEvent);
    
    // Assert the file indexer is created with correct parameters
    FileIndexer fileIndexer = openFileModel.getFileIndexer();
    assertNotNull(fileIndexer);
    assertEquals("test.txt", fileIndexer.getFile().getName());
    assertEquals(RamFileIndex.class, fileIndexer.getIndexerClass());
    
    // Assert the file indexer is added as an observer
    assertTrue(fileIndexer.getObservers().contains(indexFileAction));
    
    // Assert the file indexer worker is created and executed
    assertNotNull(fileIndexer.getWorker());
    assertTrue(fileIndexer.getWorker().isDone());
  }
  
  @Test
  public void testActionPerformedFileBased() throws FileNotFoundException {
    ActionEvent actionEvent = new ActionEvent(new Object(), 1, "command");
    openFileModel.setFile(new File("test.txt"));
    openFileModel.setIndexType(IndexType.FILE_BASED);
    
    indexFileAction.actionPerformed(actionEvent);
    
    // Assert the file indexer is created with correct parameters
    FileIndexer fileIndexer = openFileModel.getFileIndexer();
    assertNotNull(fileIndexer);
    assertEquals("test.txt", fileIndexer.getFile().getName());
    assertEquals(RomFileIndex.class, fileIndexer.getIndexerClass());
    
    // Assert the file indexer is added as an observer
    assertTrue(fileIndexer.getObservers().contains(indexFileAction));
    
    // Assert the file indexer worker is created and executed
    assertNotNull(fileIndexer.getWorker());
    assertTrue(fileIndexer.getWorker().isDone());
  }
  
  @Test
  public void testUpdateNotificationReceived() throws FileNotFoundException {
    FileIndexer fileIndexer = new FileIndexer(new File("test.txt"), RamFileIndex.class);
    indexFileAction.update(fileIndexer, 100);
    
    // Assert the file indexer is correctly handled
    assertEquals("Notification received : 100 emitted by " + fileIndexer, JTailLogger.getDebugLogs().get(0));
    
    // Assert a JTailModel is added to the jTailMainModel
    assertEquals(1, jTailMainModel.getSize());
    assertNotNull(jTailMainModel.get(0));
  }
  
  @Test
  public void testUpdateFileNotFoundException() throws FileNotFoundException {
    FileIndexer fileIndexer = new FileIndexer(new File("test.txt"), RamFileIndex.class);
    indexFileAction.update(fileIndexer, new FileNotFoundException("File not found."));
    
    // Assert the exception is correctly handled
    assertTrue(ExceptionHandler.isHandled());
    assertEquals(UIMessage.ERROR_FILE_NOT_FOUND, ExceptionHandler.getHandledMessage());
    assertEquals("test.txt", ExceptionHandler.getHandledArgs()[0]);
  }
  
  @Test
  public void testUpdateGenericException() throws FileNotFoundException {
    FileIndexer fileIndexer = new FileIndexer(new File("test.txt"), RamFileIndex.class);
    indexFileAction.update(fileIndexer, new Exception("Generic exception."));
    
    // Assert the exception is correctly handled
    assertTrue(ExceptionHandler.isHandled());
    assertEquals(UIMessage.ERROR_GENERIC_MESSAGE, ExceptionHandler.getHandledMessage());
  }
}