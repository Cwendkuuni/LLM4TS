import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;

import static org.junit.Assert.*;
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
        Component component = mock(JDialog.class);

        when(actionEvent.getSource()).thenReturn(component);
        when(openFileModel.getFile()).thenReturn(null);

        indexFileAction.actionPerformed(actionEvent);

        verify(component).setVisible(false);
        verify(openFileModel).getFile();
        verify(jTailMainModel, never()).add(any(JTailModel.class));
    }

    @Test
    public void testActionPerformed_FileNotFoundException() throws FileNotFoundException {
        ActionEvent actionEvent = mock(ActionEvent.class);
        File file = mock(File.class);
        Class<FileIndex> indexerClass = FileIndex.class;
        Component component = mock(JDialog.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);

        when(actionEvent.getSource()).thenReturn(component);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexerWorker.isCancelled()).thenReturn(false);
        when(fileIndexerWorker.isDone()).thenReturn(true);
        doThrow(new FileNotFoundException()).when(fileIndexer).addObserver(any(IndexFileAction.class));

        indexFileAction.actionPerformed(actionEvent);

        verify(component).setVisible(false);
        verify(openFileModel).getFile();
        verify(openFileModel).getIndexType();
        verify(fileIndexer).addObserver(indexFileAction);
        verify(fileIndexerWorker).execute();
        verify(jTailMainModel, never()).add(any(JTailModel.class));
    }

    @Test
    public void testActionPerformed_NormalExecution() throws FileNotFoundException {
        ActionEvent actionEvent = mock(ActionEvent.class);
        File file = mock(File.class);
        Class<FileIndex> indexerClass = FileIndex.class;
        Component component = mock(JDialog.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel jTailModel = mock(JTailModel.class);

        when(actionEvent.getSource()).thenReturn(component);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexerWorker.isCancelled()).thenReturn(false);
        when(fileIndexerWorker.isDone()).thenReturn(true);
        when(fileIndexer.getIndex()).thenReturn(100);
        when(lineReader.getFile()).thenReturn(file);
        doNothing().when(jTailMainModel).add(any(JTailModel.class));

        indexFileAction.actionPerformed(actionEvent);
        indexFileAction.update(fileIndexer, 100);

        verify(component).setVisible(false);
        verify(openFileModel).getFile();
        verify(openFileModel).getIndexType();
        verify(fileIndexer).addObserver(indexFileAction);
        verify(fileIndexerWorker).execute();
        verify(jTailMainModel).add(jTailModel);
        verify(lineReader).getFile();
    }
}