package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.util.ExceptionHandler;
import fr.pingtimeout.jtail.gui.util.JTailLogger;
import fr.pingtimeout.jtail.io.FileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.RamFileIndex;
import fr.pingtimeout.jtail.io.RomFileIndex;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class IndexFileActionTest {
    
    @Mock
    JTailMainModel jTailMainModel;
    
    @Mock
    OpenFileModel openFileModel;
    
    @Mock
    FileIndexerWorker fileIndexerWorker;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testActionPerformed() throws FileNotFoundException {
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        ActionEvent actionEvent = mock(ActionEvent.class);
        File file = new File("test.txt");
        
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        when(jTailMainModel.add(any(JTailModel.class))).thenReturn(true);
        
        action.actionPerformed(actionEvent);
        
        verify(jTailMainModel, times(1)).add(any(JTailModel.class));
        
    }
    
    @Test
    public void testUpdate() throws FileNotFoundException {
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        Observable o = mock(Observable.class);
        Object arg = 100;
        File file = new File("test.txt");
        
        when(fileIndexer.getFile()).thenReturn(file);
        when(jTailMainModel.add(any(JTailModel.class))).thenReturn(true);
        
        action.update(o, arg);
        
        verify(jTailMainModel, times(1)).add(any(JTailModel.class));
        
    }

}