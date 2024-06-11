package fr.pingtimeout.jtail.gui.action;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.awt.event.ActionEvent;
import javax.swing.*;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.InformationHandler;
import fr.pingtimeout.jtail.util.UIMessage;
import java.io.File;

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

    @After
    public void tearDown() {
        reset(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformed_NoFileSelected() {
        // Arrange
        ActionEvent actionEvent = mock(ActionEvent.class);
        JDialog dialog = mock(JDialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(openFileModel.getFile()).thenReturn(null);

        // Act
        indexFileAction.actionPerformed(actionEvent);

        // Assert
        verify(dialog).setVisible(false);
        verify(openFileModel, never()).getIndexType();
        verify(jTailMainModel, never()).add(any());
        verifyStatic(InformationHandler.class);
        InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformed_RamFileIndex() {
        // Arrange
        ActionEvent actionEvent = mock(ActionEvent.class);
        JDialog dialog = mock(JDialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        File file = mock(File.class);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
        RamFileIndex ramFileIndex = mock(RamFileIndex.class);
        whenNew(RamFileIndex.class).withArguments(file).thenReturn(ramFileIndex);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        whenNew(FileIndexer.class).withArguments(file, RamFileIndex.class).thenReturn(fileIndexer);
        whenNew(FileIndexerWorker.class).withArguments(fileIndexer).thenReturn(mock(FileIndexerWorker.class));

        // Act
        indexFileAction.actionPerformed(actionEvent);

        // Assert
        verify(dialog).setVisible(false);
        verify(openFileModel).getFile();
        verify(openFileModel).getIndexType();
        verifyNew(RamFileIndex.class).withArguments(file);
        verifyNew(FileIndexer.class).withArguments(file, RamFileIndex.class);
        verify(fileIndexer).addObserver(indexFileAction);
        verifyNew(FileIndexerWorker.class).withArguments(fileIndexer);
    }

    @Test
    public void testActionPerformed_RomFileIndex() {
        // Arrange
        ActionEvent actionEvent = mock(ActionEvent.class);
        JDialog dialog = mock(JDialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        File file = mock(File.class);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        RomFileIndex romFileIndex = mock(RomFileIndex.class);
        whenNew(RomFileIndex.class).withArguments(file).thenReturn(romFileIndex);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        whenNew(FileIndexer.class).withArguments(file, RomFileIndex.class).thenReturn(fileIndexer);
        whenNew(FileIndexerWorker.class).withArguments(fileIndexer).thenReturn(mock(FileIndexerWorker.class));

        // Act
        indexFileAction.actionPerformed(actionEvent);

        // Assert
        verify(dialog).setVisible(false);
        verify(openFileModel).getFile();
        verify(openFileModel).getIndexType();
        verifyNew(RomFileIndex.class).withArguments(file);
        verifyNew(FileIndexer.class).withArguments(file, RomFileIndex.class);
        verify(fileIndexer).addObserver(indexFileAction);
        verifyNew(FileIndexerWorker.class).withArguments(fileIndexer);
    }

    @Test
    public void testUpdate_FileFound() throws FileNotFoundException {
        // Arrange
        FileIndexer fileIndexer = mock(FileIndexer.class);
        File file = mock(File.class);
        when(fileIndexer.getFile()).thenReturn(file);
        LineReader lineReader = mock(LineReader.class);
        whenNew(LineReader.class).withArguments(file, fileIndexer.getIndex()).thenReturn(lineReader);
        JTailModel jTailModel = mock(JTailModel.class);
        whenNew(JTailModel.class).withArguments(file, lineReader).thenReturn(jTailModel);

        // Act
        indexFileAction.update(fileIndexer, 100);

        // Assert
        verify(lineReader).read();
        verify(jTailMainModel).add(jTailModel);
        verifyStatic(ExceptionHandler.class, never());
        ExceptionHandler.handle(any(), any(), any());
    }

    @Test
    public void testUpdate_FileNotFound() throws FileNotFoundException {
        // Arrange
        FileIndexer fileIndexer = mock(FileIndexer.class);
        File file = mock(File.class);
        when(fileIndexer.getFile()).thenReturn(file);
        whenNew(LineReader.class).withArguments(file, fileIndexer.getIndex()).thenThrow(new FileNotFoundException());

        // Act
        indexFileAction.update(fileIndexer, 100);

        // Assert
        verifyStatic(ExceptionHandler.class);
        ExceptionHandler.handle(any(), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq(file.getName()));
        verify(jTailMainModel, never()).add(any());
    }

    @Test
    public void testUpdate_ExceptionThrown() throws FileNotFoundException {
        // Arrange
        FileIndexer fileIndexer = mock(FileIndexer.class);
        File file = mock(File.class);
        when(fileIndexer.getFile()).thenReturn(file);
        LineReader lineReader = mock(LineReader.class);
        whenNew(LineReader.class).withArguments(file, fileIndexer.getIndex()).thenReturn(lineReader);
        doThrow(new Exception()).when(lineReader).read();

        // Act
        indexFileAction.update(fileIndexer, 100);

        // Assert
        verifyStatic(ExceptionHandler.class);
        ExceptionHandler.handle(any(), eq(UIMessage.ERROR_GENERIC_MESSAGE), new Object[0]);
        verify(jTailMainModel, never()).add(any());
    }
}