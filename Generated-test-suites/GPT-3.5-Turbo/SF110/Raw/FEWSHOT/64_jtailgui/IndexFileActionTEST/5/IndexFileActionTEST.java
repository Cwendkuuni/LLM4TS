package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.FileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.ExceptionHandler;
import fr.pingtimeout.jtail.util.InformationHandler;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("fr.pingtimeout.jtail.gui.language");
    private static final ImageIcon OPEN_ICON = new ImageIcon(OpenFileAction.class.getResource("add16.png"));

    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private IndexFileAction indexFileAction;

    @Before
    public void setup() {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = mock(OpenFileModel.class);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformed_noFileSelected() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(JDialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(openFileModel.getFile()).thenReturn(null);

        indexFileAction.actionPerformed(actionEvent);

        verify(dialog).setVisible(false);
        verify(openFileModel).getFile();
        verifyNoInteractions(jTailMainModel);
        verifyStatic(InformationHandler.class, times(1));
        InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
        verifyNoMoreInteractions(InformationHandler.class);
        verifyNoInteractions(FileIndex.class, FileIndexer.class);
    }

    @Test
    public void testActionPerformed_memoryBasedIndex() throws FileNotFoundException {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(JDialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        File file = mock(File.class);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        whenNew(FileIndexer.class).withArguments(file, RamFileIndex.class).thenReturn(fileIndexer);
        doNothing().when(fileIndexer).addObserver(any(Observer.class));
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        whenNew(FileIndexerWorker.class).withArguments(fileIndexer).thenReturn(fileIndexerWorker);

        indexFileAction.actionPerformed(actionEvent);

        verify(dialog).setVisible(false);
        verify(openFileModel).getFile();
        verify(openFileModel).getIndexType();
        verifyStatic(Class.class, times(1));
        Class.forName(RamFileIndex.class.getName());
        verifyNew(FileIndexer.class, times(1)).withArguments(file, RamFileIndex.class);
        verify(fileIndexer).addObserver(indexFileAction);
        verifyNew(FileIndexerWorker.class, times(1)).withArguments(fileIndexer);
        verify(fileIndexerWorker).execute();
        verifyNoInteractions(jTailMainModel);
        verifyNoStaticInteractions();
    }

    @Test
    public void testUpdate_completionNotification() throws FileNotFoundException {
        FileIndex fileIndexer = mock(FileIndex.class);
        File file = mock(File.class);
        when(fileIndexer.getFile()).thenReturn(file);
        LineReader lineReader = mock(LineReader.class);
        whenNew(LineReader.class).withArguments(file, fileIndexer.getIndex()).thenReturn(lineReader);
        JTailModel model = mock(JTailModel.class);
        whenNew(JTailModel.class).withArguments(file, lineReader).thenReturn(model);
        UpdateArg updateArg = new UpdateArg(100);
        indexFileAction.update(fileIndexer, updateArg);

        verify(fileIndexer).getFile();
        verifyNew(LineReader.class).withArguments(file, fileIndexer.getIndex());
        verifyNew(JTailModel.class).withArguments(file, lineReader);
        verifyNoInteractions(ExceptionHandler.class);
    }

    @Test
    public void testUpdate_fileNotFoundException() throws FileNotFoundException {
        File file = mock(File.class);
        when(file.getName()).thenReturn("test.txt");
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(file);
        whenNew(LineReader.class).withArguments(file, fileIndexer.getIndex()).thenThrow(new FileNotFoundException());

        UpdateArg updateArg = new UpdateArg(100);
        indexFileAction.update(fileIndexer, updateArg);

        verify(fileIndexer).getFile();
        verifyNew(LineReader.class).withArguments(file, fileIndexer.getIndex());
        verifyStatic(ExceptionHandler.class, times(1));
        ExceptionHandler.handle(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq("test.txt"));
        verifyNoMoreInteractions(ExceptionHandler.class);
    }

    @Test
    public void testUpdate_otherException() throws FileNotFoundException {
        File file = mock(File.class);
        when(file.getName()).thenReturn("test.txt");
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(file);
        whenNew(LineReader.class).withArguments(file, fileIndexer.getIndex()).thenThrow(new RuntimeException());

        UpdateArg updateArg = new UpdateArg(100);
        indexFileAction.update(fileIndexer, updateArg);

        verify(fileIndexer).getFile();
        verifyNew(LineReader.class).withArguments(file, fileIndexer.getIndex());
        verifyStatic(ExceptionHandler.class, times(1));
        ExceptionHandler.handle(any(RuntimeException.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), eq(new Object[0]));
        verifyNoMoreInteractions(ExceptionHandler.class);
    }

    private class UpdateArg {
        private int status;

        public UpdateArg(int status) {
            this.status = status;
        }

        public int intValue() {
            return status;
        }
    }
}