import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.*;
import fr.pingtimeout.jtail.util.*;
import java.awt.*;
import javax.swing.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.io.index.*;
import java.util.*;
import fr.pingtimeout.jtail.io.*;
import fr.pingtimeout.jtail.gui.model.*;
import java.io.*;
import java.nio.file.Files;

public class IndexFileActionTest {
  
  private JTailMainModel jTailMainModel;
  private OpenFileModel openFileModel;
  private IndexFileAction indexFileAction;
  
  @Before
  public void setup() {
    jTailMainModel = new JTailMainModel();
    openFileModel = new OpenFileModel();
    indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
  }
  
  @Test
  public void testActionPerformed() {
    File tempFile;
    try {
        tempFile = File.createTempFile("temp", ".txt");
        openFileModel.setFile(tempFile);
        ActionEvent actionEvent = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "command");
        indexFileAction.actionPerformed(actionEvent);
        assertTrue(jTailMainModel.getModelCount() > 0);
    } catch (IOException e) {
        fail("IOException occurred while creating temp file.");
    }
  }
  
  @Test
  public void testUpdate() {
    File tempFile;
    try {
        tempFile = File.createTempFile("temp", ".txt");
        FileIndexer fileIndexer = new FileIndexer(tempFile, RamFileIndex.class);
        fileIndexer.addObserver(indexFileAction);
        fileIndexer.notifyObservers(100);
        assertTrue(jTailMainModel.getModelCount() > 0);
    } catch (IOException e) {
        fail("IOException occurred while creating temp file.");
    }
  }
  
}

