package fr.pingtimeout.jtail.gui.action;

import org.junit.Before;
import org.junit.Test;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.FileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.Observer;

import static org.mockito.Mockito.*;

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

        verify(jDialog).setVisible(false);
        verifyStatic(InformationHandler.class);
        InformationHandler.handle(eq(true), eq(UIMessage.INFO_NO_FILE_SELECTED), any(Object[].class));
    }

    @Test
    public void testActionPerformed_FileNotFound() throws FileNotFoundException {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        JDialog jDialog = mock(JDialog.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        FileIndex fileIndex = mock(FileIndex.class);
        File file = mock(File.class);

        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(jDialog);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        when(fileIndex.getFile()).thenReturn(file);
        when(fileIndexerWorker.get()).thenReturn(fileIndex);
        doThrow(new FileNotFoundException()).when(fileIndex).addObserver(any(Observer.class));
        doNothing().when(fileIndexerWorker).execute();
        when(jTailMainModel.add(any(JTailModel.class))).thenReturn(true);

        indexFileAction.actionPerformed(actionEvent);

        verify(jDialog).setVisible(false);
        verifyStatic(ExceptionHandler.class);
        ExceptionHandler.handle(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq(file.getName()));
    }

    @Test
    public void testActionPerformed_IndexTypeMemoryBased() throws FileNotFoundException {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        JDialog jDialog = mock(JDialog.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        FileIndex fileIndex = mock(FileIndex.class);
        File file = mock(File.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel jTailModel = mock(JTailModel.class);

        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(jDialog);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
        when(fileIndex.getFile()).thenReturn(file);
        when(fileIndexerWorker.get()).thenReturn(fileIndex);
        doNothing().when(fileIndex).addObserver(any(Observer.class));
        doNothing().when(fileIndexerWorker).execute();
        when(jTailMainModel.add(any(JTailModel.class))).thenReturn(true);
        when(jTailModel.getLineReader()).thenReturn(lineReader);
        when(lineReader.getFile()).thenReturn(file);

        indexFileAction.actionPerformed(actionEvent);

        verify(jDialog).setVisible(false);

        verify(fileIndex).addObserver(indexFileAction);
        verifyStatic(ExceptionHandler.class, never());
        ExceptionHandler.handle(any(Exception.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), any(Object[].class));

        verify(jTailMainModel).add(any(JTailModel.class));
    }

    @Test
    public void testActionPerformed_IndexTypeFileBased() throws FileNotFoundException {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        JDialog jDialog = mock(JDialog.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        FileIndex fileIndex = mock(FileIndex.class);
        File file = mock(File.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel jTailModel = mock(JTailModel.class);

        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(jDialog);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        when(fileIndex.getFile()).thenReturn(file);
        when(fileIndexerWorker.get()).thenReturn(fileIndex);
        doNothing().when(fileIndex).addObserver(any(Observer.class));
        doNothing().when(fileIndexerWorker).execute();
        when(jTailMainModel.add(any(JTailModel.class))).thenReturn(true);
        when(jTailModel.getLineReader()).thenReturn(lineReader);
        when(lineReader.getFile()).thenReturn(file);

        indexFileAction.actionPerformed(actionEvent);

        verify(jDialog).setVisible(false);

        verify(fileIndex).addObserver(indexFileAction);
        verifyStatic(ExceptionHandler.class, never());
        ExceptionHandler.handle(any(Exception.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), any(Object[].class));

        verify(jTailMainModel).add(any(JTailModel.class));
    }

    @Test
    public void testUpdate_Index100() throws FileNotFoundException {
        Observer observer = (Observer) indexFileAction;
        FileIndex fileIndex = mock(FileIndex.class);
        File file = mock(File.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel jTailModel = mock(JTailModel.class);

        when(fileIndex.getFile()).thenReturn(file);
        when(jTailModel.getLineReader()).thenReturn(lineReader);
        when(lineReader.getFile()).thenReturn(file);

        observer.update(fileIndex, 100);

        verify(jTailMainModel).add(any(JTailModel.class));
        verifyStatic(ExceptionHandler.class, never());
        ExceptionHandler.handle(any(Exception.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), any(Object[].class));
    }

    @Test
    public void testUpdate_InvalidIndex() {
        Observer observer = (Observer) indexFileAction;
        FileIndex fileIndex = mock(FileIndex.class);

        observer.update(fileIndex, 200);

        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verifyStatic(ExceptionHandler.class, never());
        ExceptionHandler.handle(any(Exception.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), any(Object[].class));
    }
}