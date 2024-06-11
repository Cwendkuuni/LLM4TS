
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.util.UIMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.ResourceBundle;

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
    ActionEvent mockActionEvent = new ActionEvent(new Object(), 0, "");
    InformationHandlerMock informationHandler = new InformationHandlerMock();
    FileIndexerWorkerMock fileIndexerWorker = new FileIndexerWorkerMock();
    
    indexFileAction.setInformationHandler(informationHandler);
    indexFileAction.setFileIndexerWorker(fileIndexerWorker);
    
    // No file selected
    indexFileAction.actionPerformed(mockActionEvent);
    
    assertEquals(UIMessage.INFO_NO_FILE_SELECTED, informationHandler.getMessage());
  }
  
  @Test
  public void testActionPerformed_FileIndexTypeMemory() {
    ActionEvent mockActionEvent = new ActionEvent(new Object(), 0, "");
    openFileModel.setFile(new File("test.log"));
    openFileModel.setIndexType(IndexType.MEMORY_BASED);
    FileIndexerMock fileIndexer = new FileIndexerMock();
    FileIndexerWorkerMock fileIndexerWorker = new FileIndexerWorkerMock();
    
    indexFileAction.setFileIndexer(fileIndexer);
    indexFileAction.setFileIndexerWorker(fileIndexerWorker);
    
    // File index type: MEMORY_BASED
    indexFileAction.actionPerformed(mockActionEvent);
    
    assertEquals(memoryIndexClass, fileIndexer.getIndexType());
  }
  
  @Test
  public void testActionPerformed_FileIndexTypeFile() {
    ActionEvent mockActionEvent = new ActionEvent(new Object(), 0, "");
    openFileModel.setFile(new File("test.log"));
    openFileModel.setIndexType(IndexType.FILE_BASED);
    FileIndexerMock fileIndexer = new FileIndexerMock();
    FileIndexerWorkerMock fileIndexerWorker = new FileIndexerWorkerMock();
    
    indexFileAction.setFileIndexer(fileIndexer);
    indexFileAction.setFileIndexerWorker(fileIndexerWorker);
    
    // File index type: FILE_BASED
    indexFileAction.actionPerformed(mockActionEvent);
    
    assertEquals(fileIndexClass, fileIndexer.getIndexType());
  }
  
  // TODO: Generate additional test cases for other methods in the class
  
  // Helper classes
  private class InformationHandlerMock extends InformationHandler {
    private String message;
    
    public void handle(boolean isError, String message, Object... args) {
      if (isError) {
        this.message = String.format(message, args);
      } else {
        this.message = message;
      }
    }
    
    public String getMessage() {
      return message;
    }
  }
  
  private class FileIndexerMock extends FileIndexer {
    private Class indexType;
    
    public FileIndexerMock(File file, Class indexType) {
      super(file, indexType);
      this.indexType = indexType;
    }
    
    public Class getIndexType() {
      return indexType;
    }
  }
  
  private class FileIndexerWorkerMock extends FileIndexerWorker {
    @Override
    protected Void doInBackground() throws Exception {
      setProgress(100);
      return null;
    }
  }
}
