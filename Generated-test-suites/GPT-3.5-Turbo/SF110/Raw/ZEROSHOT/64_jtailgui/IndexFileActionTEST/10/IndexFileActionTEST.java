import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import fr.pingtimeout.jtail.gui.controller.OpenFileModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.io.FileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.ExceptionHandler;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.util.UIMessage;

@RunWith(MockitoJUnitRunner.class)
public class IndexFileActionTest {

    @Mock
    private JTailMainModel jTailMainModel;
    
    @Mock
    private OpenFileModel openFileModel;
    
    @InjectMocks
    private IndexFileAction indexFileAction;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testActionPerformed() throws FileNotFoundException {
        ActionEvent actionEvent = mock(ActionEvent.class);
        JDialog dialog = mock(JDialog.class);
        
        when(actionEvent.getSource()).thenReturn(dialog);
        when(openFileModel.getFile()).thenReturn(null);
        
        indexFileAction.actionPerformed(actionEvent);
        
        verify(dialog).setVisible(false);
        verify(openFileModel).getFile();
        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verifyStatic(InformationHandler.class);
        InformationHandler.handle(eq(true), eq(UIMessage.INFO_NO_FILE_SELECTED), any(Object[].class));
    }
    
    @Test
    public void testActionPerformedWithFileNotFoundError() throws FileNotFoundException {
        ActionEvent actionEvent = mock(ActionEvent.class);
        File file = mock(File.class);
        JDialog dialog = mock(JDialog.class);
        
        when(actionEvent.getSource()).thenReturn(dialog);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(OpenFileModel.IndexType.FILE_BASED);
        when(file.getName()).thenReturn("test.txt");
        
        indexFileAction.actionPerformed(actionEvent);
        
        verify(dialog).setVisible(false);
        verify(openFileModel).getFile();
        
        verifyStatic(ExceptionHandler.class);
        ExceptionHandler.handle(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq("test.txt"));
        
        verify(jTailMainModel, never()).add(any(JTailModel.class));
    }
    
    @Test
    public void testActionPerformedWithMemoryBasedIndex() throws FileNotFoundException {
        ActionEvent actionEvent = mock(ActionEvent.class);
        File file = mock(File.class);
        JDialog dialog = mock(JDialog.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        
        when(actionEvent.getSource()).thenReturn(dialog);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(OpenFileModel.IndexType.MEMORY_BASED);
        when(fileIndexer.getFile()).thenReturn(file);
        
        doReturn(fileIndexer).when(indexFileAction).createFileIndexer(eq(file), eq(RamFileIndex.class));
        doReturn(fileIndexerWorker).when(indexFileAction).createFileIndexerWorker(eq(fileIndexer));
        
        indexFileAction.actionPerformed(actionEvent);
        
        verify(dialog).setVisible(false);
        verify(openFileModel).getFile();
        verify(indexFileAction).createFileIndexer(eq(file), eq(RamFileIndex.class));
        verify(fileIndexer).addObserver(eq(indexFileAction));
        verify(indexFileAction).createFileIndexerWorker(eq(fileIndexer));
        verify(fileIndexerWorker).execute();
        
        verify(jTailMainModel, never()).add(any(JTailModel.class));
    }
    
    @Test
    public void testUpdate() throws Exception {
        FileIndexer fileIndexer = mock(FileIndexer.class);
        File file = mock(File.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel jTailModel = mock(JTailModel.class);
        
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(new HashMap<>());
        when(file.getName()).thenReturn("test.txt");
        whenNew(LineReader.class).withArguments(eq(file), eq(fileIndexer.getIndex())).thenReturn(lineReader);
        whenNew(JTailModel.class).withArguments(eq(file), eq(lineReader)).thenReturn(jTailModel);
        
        indexFileAction.update(fileIndexer, 100);
        
        verify(fileIndexer).getFile();
        verify(fileIndexer).getIndex();
        verify(jTailMainModel).add(eq(jTailModel));
    }
    
    private FileIndexer createFileIndexer(File file, Class<? extends FileIndex> indexerClass) {
        try {
            FileIndexer fileIndexer = mock(FileIndexer.class);
            whenNew(FileIndexer.class).withArguments(eq(file), eq(indexerClass)).thenReturn(fileIndexer);
            return fileIndexer;
        } catch (Exception e) {
            fail("Failed to create FileIndexer mock");
            return null;
        }
    }
    
    private FileIndexerWorker createFileIndexerWorker(FileIndexer fileIndexer) {
        try {
            FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
            whenNew(FileIndexerWorker.class).withArguments(eq(fileIndexer)).thenReturn(fileIndexerWorker);
            return fileIndexerWorker;
        } catch (Exception e) {
            fail("Failed to create FileIndexerWorker mock");
            return null;
        }
    }
    
}