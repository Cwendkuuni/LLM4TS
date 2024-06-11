import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import fr.pingtimeout.jtail.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.util.Observable;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.model.IndexType;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.FileIndex;

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
  public void testActionPerformed_fileSelected() {

    File file = new File("testFile.txt");
    openFileModel.setFile(file);
    openFileModel.setIndexType(IndexType.MEMORY_BASED);

    indexFileAction.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, ""));

    assertEquals(1, jTailMainModel.getModels().size());
    assertNotNull(jTailMainModel.getModel(file));
  }

  @Test
  public void testActionPerformed_noFileSelected() {

    File file = null;
    openFileModel.setFile(file);

    indexFileAction.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, ""));

    assertTrue(UIMessage.INFO_NO_FILE_SELECTED, InformationHandler.isHandled());
  }

  @Test
  public void testActionPerformed_fileNotFound() throws FileNotFoundException {

    File file = new File("nonExistentFile.txt");
    openFileModel.setFile(file);

    indexFileAction.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, ""));

    assertTrue(UIMessage.ERROR_FILE_NOT_FOUND, ExceptionHandler.isHandled());

    ExceptionHandler.clear();
  }

  @Test
  public void testActionPerformed_genericException() throws Exception {

    File file = new File("testFile.txt");
    openFileModel.setFile(file);
    openFileModel.setIndexType(IndexType.MEMORY_BASED);

    indexFileAction.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, ""));

    assertTrue(UIMessage.ERROR_GENERIC_MESSAGE, ExceptionHandler.isHandled());

    ExceptionHandler.clear();
  }

  @Test
  public void testUpdate_notificationReceived_arg100() throws FileNotFoundException {

    File file = new File("testFile.txt");
    FileIndexer fileIndexer = new FileIndexer(file, RamFileIndex.class);
    Observable observable = fileIndexer;
    Object arg = 100;

    indexFileAction.update(observable, arg);

    assertNotNull(jTailMainModel.getModel(file));

    jTailMainModel.remove(file);
  }

  @Test
  public void testUpdate_notificationReceived_argNot100() throws FileNotFoundException {

    File file = new File("testFile.txt");
    FileIndexer fileIndexer = new FileIndexer(file, RamFileIndex.class);
    Observable observable = fileIndexer;
    Object arg = 200;

    indexFileAction.update(observable, arg);

    assertNull(jTailMainModel.getModel(file));
  }

}