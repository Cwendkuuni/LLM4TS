package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import org.junit.Test;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.*;
import fr.pingtimeout.jtail.io.index.*;
import fr.pingtimeout.jtail.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Component;
import java.util.Observer;
import java.util.Observable;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class IndexFileActionTest {
    
    @Test
    public void testActionPerformed() {
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        // Set up file and index type
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(IndexType.FILE_BASED);
        
        // Mock the JDialog and ActionEvent
        Component dialog = new JDialog();
        ActionEvent actionEvent = new ActionEvent(new Component(), ActionEvent.ACTION_PERFORMED, "");
        
        // Test with null file
        openFileModel.setFile(null);
        assertNull(openFileModel.getFile());
        assertNull(indexFileAction.jTailMainModel);
        indexFileAction.actionPerformed(actionEvent);
        assertTrue(dialog.isVisible());
        assertNotNull(openFileModel.getFile());
        assertEquals(UIMessage.INFO_NO_FILE_SELECTED, InformationHandler.getMessage());
        InformationHandler.clearMessage();
        
        // Test with valid file and index type
        openFileModel.setFile(new File("test.txt"));
        assertNotNull(openFileModel.getFile());
        assertNull(indexFileAction.jTailMainModel);
        indexFileAction.actionPerformed(actionEvent);
        assertFalse(dialog.isVisible());
        assertNotNull(indexFileAction.jTailMainModel);
        JTailModel model = indexFileAction.jTailMainModel.getModel(0);
        assertEquals(openFileModel.getFile(), model.getFile());
        
        // Test with file not found exception
        openFileModel.setFile(new File("nonexistent.txt"));
        assertNotNull(openFileModel.getFile());
        assertNull(indexFileAction.jTailMainModel);
        indexFileAction.actionPerformed(actionEvent);
        assertTrue(dialog.isVisible());
        assertNotNull(openFileModel.getFile());
        assertEquals(UIMessage.ERROR_FILE_NOT_FOUND, ExceptionHandler.getMessage());
        ExceptionHandler.clearMessage();
    }
    
    @Test
    public void testUpdate() {
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        // Set up file and index type
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(IndexType.FILE_BASED);
        
        // Create a mock FileIndexer and Observable
        class MockFileIndexer extends FileIndexer {
            public MockFileIndexer(File file, Class<? extends FileIndex> indexerClass) throws FileNotFoundException {
                super(file, indexerClass);
            }
            public File getFile() {
                return new File("test.txt");
            }
            public Index getIndex() {
                return new Index();
            }
        }
        
        FileIndexer fileIndexer = new MockFileIndexer(new File("test.txt"), RomFileIndex.class);
        
        // Test with 100 notification
        fileIndexer.setChanged();
        fileIndexer.notifyObservers(100);
        assertEquals(1, indexFileAction.jTailMainModel.getModelCount());
        assertEquals(openFileModel.getFile(), indexFileAction.jTailMainModel.getModel(0).getFile());
        
        // Test with other notifications
        fileIndexer.setChanged();
        fileIndexer.notifyObservers(50);
        fileIndexer.setChanged();
        fileIndexer.notifyObservers(200);
        fileIndexer.setChanged();
        fileIndexer.notifyObservers("test");
        
        // Test with exception
        fileIndexer.setChanged();
        fileIndexer.notifyObservers(new Exception());
        assertEquals(UIMessage.ERROR_GENERIC_MESSAGE, ExceptionHandler.getMessage());
        ExceptionHandler.clearMessage();
    }
}