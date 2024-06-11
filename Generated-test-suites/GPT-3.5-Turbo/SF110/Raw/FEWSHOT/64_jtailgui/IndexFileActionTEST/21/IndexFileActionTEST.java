package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel.IndexType;
import fr.pingtimeout.jtail.gui.model.UIMessage;
import fr.pingtimeout.jtail.gui.view.JTailFrame;
import fr.pingtimeout.jtail.gui.view.JTailFrame.OpenDialog;
import fr.pingtimeout.jtail.logger.JTailLogger;
import fr.pingtimeout.jtail.util.ExceptionHandler;
import fr.pingtimeout.jtail.util.LineReader;
import fr.pingtimeout.jtail.util.RamFileIndex;
import fr.pingtimeout.jtail.util.RomFileIndex;

@RunWith(MockitoJUnitRunner.class)
public class IndexFileActionTest {
    
    IndexFileAction indexFileAction;
    
    @Mock
    JTailMainModel mockJTailMainModel;
    
    @Mock
    OpenFileModel mockOpenFileModel;
    
    @Mock
    FileIndexerWorker mockFileIndexerWorker;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        indexFileAction = new IndexFileAction(mockJTailMainModel, mockOpenFileModel);
    }
    
    @Test
    public void testActionPerformed_NoFileSelected() {
        when(mockOpenFileModel.getFile()).thenReturn(null);
        
        indexFileAction.actionPerformed(new ActionEvent(new Object(), 1, ""));
        
        verifyZeroInteractions(mockJTailMainModel);
        verifyStatic();
        InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }
    
    @Test
    public void testActionPerformed_IndexTypeIsMemoryBased() throws FileNotFoundException {
        File mockFile = mock(File.class);
        when(mockOpenFileModel.getFile()).thenReturn(mockFile);
        when(mockOpenFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
        
        FileIndexer mockFileIndexer = mock(FileIndexer.class);
        whenNew(FileIndexer.class).withArguments(mockFile, RamFileIndex.class).thenReturn(mockFileIndexer);
        when(mockFileIndexer.getFile()).thenReturn(mockFile);
        
        LineReader mockLineReader = mock(LineReader.class);
        whenNew(LineReader.class).withArguments(mockFile, mockFileIndexer.getIndex()).thenReturn(mockLineReader);
        
        JTailModel mockJTailModel = mock(JTailModel.class);
        whenNew(JTailModel.class).withArguments(mockFile, mockLineReader).thenReturn(mockJTailModel);
        
        indexFileAction.actionPerformed(new ActionEvent(new Object(), 1, ""));
        
        verify(mockJTailMainModel).add(mockJTailModel);
        verify(mockLineReader).close();
    }
    
    @Test
    public void testActionPerformed_IndexTypeIsFileBased() throws FileNotFoundException {
        File mockFile = mock(File.class);
        when(mockOpenFileModel.getFile()).thenReturn(mockFile);
        when(mockOpenFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        
        FileIndexer mockFileIndexer = mock(FileIndexer.class);
        whenNew(FileIndexer.class).withArguments(mockFile, RomFileIndex.class).thenReturn(mockFileIndexer);
        when(mockFileIndexer.getFile()).thenReturn(mockFile);
        
        LineReader mockLineReader = mock(LineReader.class);
        whenNew(LineReader.class).withArguments(mockFile, mockFileIndexer.getIndex()).thenReturn(mockLineReader);
        
        JTailModel mockJTailModel = mock(JTailModel.class);
        whenNew(JTailModel.class).withArguments(mockFile, mockLineReader).thenReturn(mockJTailModel);
        
        indexFileAction.actionPerformed(new ActionEvent(new Object(), 1, ""));
        
        verify(mockJTailMainModel).add(mockJTailModel);
        verify(mockLineReader).close();
    }
    
    @Test
    public void testUpdate_NotificationReceived() throws FileNotFoundException {
        Observable mockObservable = mock(FileIndexer.class);
        Object mockObject = 100;
        
        indexFileAction.update(mockObservable, mockObject);
        
        verifyStatic();
        JTailLogger.debug("Notification received : {} emitted by {}", mockObject, mockObservable);
    }
}