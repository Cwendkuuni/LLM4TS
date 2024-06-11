import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.util.ExceptionHandler;
import fr.pingtimeout.jtail.gui.controller.FileIndexerWorker;

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
    public void testActionPerformed_NoFileSelected() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        JDialog jDialog = mock(JDialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(jDialog);
        when(openFileModel.getFile()).thenReturn(null);

        indexFileAction.actionPerformed(actionEvent);

        verify(dialog).setVisible(false);
        verify(openFileModel).getFile();
        verify(InformationHandler).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
        verify(jTailMainModel, never()).add(any(JTailModel.class));
    }

    @Test
    public void testActionPerformed_MemoryBasedIndexType() throws Exception {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        JDialog jDialog = mock(JDialog.class);
        File file = mock(File.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel jTailModel = mock(JTailModel.class);

        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(jDialog);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(any(FileIndex.class));
        when(lineReader.init()).thenReturn(true);
        when(lineReader.readLine()).thenReturn("test line");
        when(jTailModel.getFile()).thenReturn(file);
        
        doReturn(ModelStatus.IDLE).when(JTailModel.class);
        doNothing().when(JTailModel.class);

        indexFileAction.actionPerformed(actionEvent);

        verify(dialog).setVisible(false);
        verify(openFileModel).getFile();
        verify(openFileModel).getIndexType();
        verify(jTailMainModel).add(any(JTailModel.class));
        verify(JTailModel, times(1)).getFile();
        verify(jTailMainModel, never()).update(mock(Observable.class), any());
    }

    @Test
    public void testActionPerformed_FileBasedIndexType() throws Exception {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        JDialog jDialog = mock(JDialog.class);
        File file = mock(File.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel jTailModel = mock(JTailModel.class);

        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(jDialog);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(any(FileIndex.class));
        when(lineReader.init()).thenReturn(true);
        when(lineReader.readLine()).thenReturn("test line");
        when(jTailModel.getFile()).thenReturn(file);
        
        doReturn(ModelStatus.IDLE).when(JTailModel.class);
        doNothing().when(JTailModel.class);

        indexFileAction.actionPerformed(actionEvent);

        verify(dialog).setVisible(false);
        verify(openFileModel).getFile();
        verify(openFileModel).getIndexType();
        verify(jTailMainModel).add(any(JTailModel.class));
        verify(JTailModel, times(1)).getFile();
        verify(jTailMainModel, never()).update(mock(Observable.class), any());
    }
    
    @Test
    public void testUpdate() throws Exception {
      Observable observable = mock(Observable.class);
      Object object = mock(Object.class);
      FileIndexer fileIndexer = mock(FileIndexer.class);
      File file = mock(File.class);
      LineReader lineReader = mock(LineReader.class);
      JTailModel model = mock(JTailModel.class);

      when(object).thenReturn(100);
      when(observable).thenReturn(fileIndexer);
      when(fileIndexer.getFile()).thenReturn(file);
      when(file.getName()).thenReturn("test.txt");
      when(lineReader.init()).thenReturn(true);
      when(lineReader.readLine()).thenReturn("test line");
      when(jTailMainModel.add(model)).thenReturn(true);
      
      doReturn(ModelStatus.IDLE).when(JTailModel.class);
      doNothing().when(JTailModel.class);

      indexFileAction.update(observable, object);

      verify(fileIndexer).getFile();
      verify(jTailMainModel).add(model);
      verify(InformationHandler, never()).handle(true, UIMessage.ERROR_FILE_NOT_FOUND, "test.txt");
      verify(jTailMainModel, never()).update(mock(Observable.class), any());
    }
   
}