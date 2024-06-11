import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.ExceptionHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel.IndexType;
import fr.pingtimeout.jtail.gui.model.UIMessageHandler;
import fr.pingtimeout.jtail.gui.view.JTailFrame;
import fr.pingtimeout.jtail.io.FileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.RamFileIndex;
import fr.pingtimeout.jtail.io.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

    private static final String TEST_FILE_PATH = "testFilePath";
    
    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private FileIndexerWorker fileIndexerWorker;
    
    @Before
    public void setup() throws FileNotFoundException {
        jTailMainModel = new JTailMainModel();
        openFileModel = mock(OpenFileModel.class);
        Mockito.when(openFileModel.getFile()).thenReturn(new File(TEST_FILE_PATH));
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        fileIndexerWorker = mock(FileIndexerWorker.class);
    }
    
    @Test
    public void testActionPerformed() throws FileNotFoundException {
        JDialog dialog = new JDialog();
        ActionEvent actionEvent = new ActionEvent(dialog, ActionEvent.ACTION_PERFORMED, "");
        
        indexFileAction.actionPerformed(actionEvent);
        
        verify(openFileModel).getFile();
        verify(openFileModel).getIndexType();
        verify(fileIndexerWorker).execute();
    }
    
    @Test
    public void testUpdate() throws FileNotFoundException {
        FileIndexer fileIndexer = mock(FileIndexer.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel jTailModel = mock(JTailModel.class);
        
        Mockito.when(fileIndexer.getFile()).thenReturn(new File(TEST_FILE_PATH));
        Mockito.when(fileIndexer.getIndex()).thenReturn(100);
        Mockito.when(openFileModel.getFile()).thenReturn(new File(TEST_FILE_PATH));
        Mockito.when(jTailModel.getFile()).thenReturn(new File(TEST_FILE_PATH));
        Mockito.when(jTailMainModel.add(jTailModel)).thenReturn(true);
        Mockito.when(lineReader.getLineNumber()).thenReturn(100);
        Mockito.when(lineReader.getFileSize()).thenReturn(200);
        
        indexFileAction.update(fileIndexer, 100);
        
        verify(jTailMainModel).add(jTailModel);
        verify(lineReader).getLineNumber();
        verify(lineReader).getFileSize();
    }
}