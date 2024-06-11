package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import org.junit.Test;
import javax.swing.JDialog;
import fr.pingtimeout.jtail.util.JTailLogger;
import java.awt.Component;
import java.awt.event.ActionEvent;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import java.io.FileNotFoundException;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;

public class IndexFileActionTest {
    @Test
    public void testActionPerformedNullFile() {
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        
        ActionEvent actionEvent = new ActionEvent(new JDialog(), ActionEvent.ACTION_PERFORMED, "");
        action.actionPerformed(actionEvent);
        
        assertTrue(InformationHandler.isHandled());
        assertEquals(UIMessage.INFO_NO_FILE_SELECTED, InformationHandler.getMessage());
    }

    @Test
    public void testActionPerformedMemoryBased() {
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(IndexType.MEMORY_BASED);
        
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        
        ActionEvent actionEvent = new ActionEvent(new JDialog(), ActionEvent.ACTION_PERFORMED, "");
        action.actionPerformed(actionEvent);
        
        FileIndexer fileIndexer = jTailMainModel.getFileIndexer();
        
        assertNotNull(fileIndexer);
        assertEquals(openFileModel.getFile(), fileIndexer.getFile());
        assertTrue(fileIndexer instanceof RamFileIndex);
        assertTrue(fileIndexer.isObserver(this));
        
        LineReader lineReader = jTailMainModel.getLineReader();
        
        assertNotNull(lineReader);
        assertEquals(openFileModel.getFile(), lineReader.getFile());
        assertEquals(fileIndexer.getIndex(), lineReader.getIndex());
        
        JTailModel model = jTailMainModel.getModels().get(0);
        
        assertNotNull(model);
        assertEquals(openFileModel.getFile(), model.getFile());
        assertEquals(lineReader, model.getLineReader());
    }

    @Test
    public void testActionPerformedFileBased() {
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(IndexType.FILE_BASED);
        
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        
        ActionEvent actionEvent = new ActionEvent(new JDialog(), ActionEvent.ACTION_PERFORMED, "");
        action.actionPerformed(actionEvent);
        
        FileIndexer fileIndexer = jTailMainModel.getFileIndexer();
        
        assertNotNull(fileIndexer);
        assertEquals(openFileModel.getFile(), fileIndexer.getFile());
        assertTrue(fileIndexer instanceof RomFileIndex);
        assertTrue(fileIndexer.isObserver(this));
        
        LineReader lineReader = jTailMainModel.getLineReader();
        
        assertNotNull(lineReader);
        assertEquals(openFileModel.getFile(), lineReader.getFile());
        assertEquals(fileIndexer.getIndex(), lineReader.getIndex());
        
        JTailModel model = jTailMainModel.getModels().get(0);
        
        assertNotNull(model);
        assertEquals(openFileModel.getFile(), model.getFile());
        assertEquals(lineReader, model.getLineReader());
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testActionPerformedFileNotFoundException() {
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        openFileModel.setFile(new File("nonexistent.txt"));
        
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        
        ActionEvent actionEvent = new ActionEvent(new JDialog(), ActionEvent.ACTION_PERFORMED, "");
        action.actionPerformed(actionEvent);
    }
    
    @Test(expected = Exception.class)
    public void testActionPerformedException() {
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(IndexType.MEMORY_BASED);
        
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        
        ActionEvent actionEvent = new ActionEvent(new JDialog(), ActionEvent.ACTION_PERFORMED, "");
        action.actionPerformed(actionEvent);
        
        FileIndexer fileIndexer = jTailMainModel.getFileIndexer();
        
        assertNotNull(fileIndexer);
        assertEquals(openFileModel.getFile(), fileIndexer.getFile());
        assertTrue(fileIndexer instanceof RamFileIndex);
        assertTrue(fileIndexer.isObserver(this));
        
        LineReader lineReader = jTailMainModel.getLineReader();
        
        assertNotNull(lineReader);
        assertEquals(openFileModel.getFile(), lineReader.getFile());
        assertEquals(fileIndexer.getIndex(), lineReader.getIndex());
        
        JTailModel model = jTailMainModel.getModels().get(0);
        
        assertNotNull(model);
        assertEquals(openFileModel.getFile(), model.getFile());
        assertEquals(lineReader, model.getLineReader());
    }

    @Override
    public void update(final Observable o, final Object arg) {
        JTailLogger.debug("Notification received : {} emitted by {}", arg, o);
        if ((int)arg == 100) {
            final FileIndexer fileIndexer = (FileIndexer)o;
            final File file = fileIndexer.getFile();
            try {
                final LineReader lineReader = new LineReader(file, fileIndexer.getIndex());
                final JTailModel model = new JTailModel(file, lineReader);
                this.jTailMainModel.add(model);
            }
            catch (FileNotFoundException e) {
                ExceptionHandler.handle(e, UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
            }
            catch (Exception e2) {
                ExceptionHandler.handle(e2, UIMessage.ERROR_GENERIC_MESSAGE, new Object[0]);
            }
        }
    }
}