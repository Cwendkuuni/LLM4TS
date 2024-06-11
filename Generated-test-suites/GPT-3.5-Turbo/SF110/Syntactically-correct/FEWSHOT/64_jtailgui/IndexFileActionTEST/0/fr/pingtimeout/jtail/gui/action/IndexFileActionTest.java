package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;

public class IndexFileActionTest {
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private IndexFileAction indexFileAction;
    
    @Before
    public void setUp() {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = mock(OpenFileModel.class);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformed() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        when(openFileModel.getFile()).thenReturn(null);
        indexFileAction.actionPerformed(actionEvent);
        verify(jTailMainModel, never()).add(any());
        verifyZeroInteractions(actionEvent);
        
        when(openFileModel.getFile()).thenReturn(mock(File.class));
        when(openFileModel.getIndexType()).thenReturn(OpenFileModel.IndexType.MEMORY_BASED);
        indexFileAction.actionPerformed(actionEvent);
        verify(jTailMainModel, times(1)).add(any());
        verifyZeroInteractions(actionEvent);
        
        when(openFileModel.getIndexType()).thenReturn(OpenFileModel.IndexType.FILE_BASED);
        indexFileAction.actionPerformed(actionEvent);
        verify(jTailMainModel, times(2)).add(any());
        verifyZeroInteractions(actionEvent);
    }

    @Test
    public void testUpdate() {
        Observable o = mock(Observable.class);
        Object arg = 100;
        indexFileAction.update(o, arg);
        verify(jTailMainModel, times(1)).add(any());
    }
}