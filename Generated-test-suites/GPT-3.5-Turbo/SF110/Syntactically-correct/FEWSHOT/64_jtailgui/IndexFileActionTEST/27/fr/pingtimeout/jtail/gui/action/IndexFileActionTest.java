
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.io.FileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.RamFileIndex;
import fr.pingtimeout.jtail.io.RomFileIndex;
import fr.pingtimeout.jtail.util.ExceptionHandler;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class IndexFileActionTest {

    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private FileIndexer fileIndexer;

    @Before
    public void setup() {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = mock(OpenFileModel.class);
        fileIndexer = mock(FileIndexer.class);
    }

    @Test
    public void testActionPerformed() {
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(JDialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(openFileModel.getFile()).thenReturn(null);

        indexFileAction.actionPerformed(actionEvent);

        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verifyZeroInteractions(fileIndexer);
    }

    @Test
    public void testActionPerformed_fileNotFound() throws FileNotFoundException {
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(JDialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        File file = mock(File.class);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        when(file.getName()).thenReturn("test.log");
        when(fileIndexer.getFile()).thenThrow(new FileNotFoundException("File not found"));

        indexFileAction.actionPerformed(actionEvent);

        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verify(openFileModel).getFile();
        verify(openFileModel).getIndexType();
        verify(fileIndexer).getFile();
        verify(dialog).setVisible(false);
        verify(langBundle).getString("action.open.label");
        verify(langBundle).getString("action.open.tooltip");
        verify(langBundle).getString("action.open.mnemonic");
        verify(langBundle).getString("action.open.accelerator");
        verify(fileIndexerWorker).execute();
        verify(langBundle).getString("action.open.label");
        verify(openIcon).getImage();
        verify(langBundle).getString("action.open.label");
        verify(dialog).getParent();
        verify(fileIndexer, never()).addObserver(any(Observer.class));
        verify(exceptionHandler).handle(any(Exception.class), anyString(), any());
    }

    @Test
    public void testUpdate() throws FileNotFoundException {
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        File file = mock(File.class);
        FileIndex fileIndex = mock(FileIndex.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel model = mock(JTailModel.class);
        when(fileIndex.getFile()).thenReturn(file);
        when(file.getName()).thenReturn("test.log");
        when(fileIndex.getIndex()).thenReturn(fileIndex);
        when(lineReader).thenReturn(lineReader);
        when(jTailMainModel).thenReturn(jTailMainModel);
        when(jTailMainModel.add(model)).thenReturn(true);
        when(langBundle).thenReturn(langBundle);
        when(langBundle.getString("action.open.label")).thenReturn("Open");
        when(langBundle.getString("action.open.tooltip")).thenReturn("Open a file");
        when(langBundle.getString("action.open.mnemonic")).thenReturn("O");
        when(langBundle.getString("action.open.accelerator")).thenReturn("Ctrl+O");
        when(langBundle.getString("action.open.label")).thenReturn("Open");
        when(exceptionHandler).thenReturn(exceptionHandler);

        indexFileAction.update(fileIndex, 100);

        verify(jTailMainModel).add(model);
        verify(langBundle).getString("action.open.label");
        verify(fileIndex).addObserver(indexFileAction);
        verify(exceptionHandler).handle(any(Exception.class), anyString(), any());
    }
}
