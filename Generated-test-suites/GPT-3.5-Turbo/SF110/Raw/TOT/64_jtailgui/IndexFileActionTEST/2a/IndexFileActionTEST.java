import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IndexFileActionTest {

    @Test
    public void testActionPerformed_NoFileSelected() {
        // Given
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        ActionEvent actionEvent = mock(ActionEvent.class);

        // When
        when(openFileModel.getFile()).thenReturn(null);
        action.actionPerformed(actionEvent);

        // Then
        verify(openFileModel, times(1)).getFile();
        // Assert expected information message is displayed
        // verify(InformationHandler, times(1)).handle(anyBoolean(), eq(UIMessage.INFO_NO_FILE_SELECTED), any(Object[].class));
    }

    @Test
    public void testActionPerformed_FileNotFoundException() {
        // Given
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        ActionEvent actionEvent = mock(ActionEvent.class);
        File file = mock(File.class);
        
        // When
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        try {
            when(FileIndexer.class.newInstance()).thenThrow(new FileNotFoundException());
        } catch (Exception e) { }

        action.actionPerformed(actionEvent);

        // Then
        verify(openFileModel, times(1)).getFile();
        verify(openFileModel, times(1)).getIndexType();
        verify(FileIndexer.class, times(1)).newInstance();
        // Assert expected error handling is called
        // verify(ExceptionHandler, times(1)).handle(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), any(String.class));
    }

    @Test
    public void testUpdate_IndexCompleted() {
        // Given
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        Observable observable = mock(Observable.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        File file = mock(File.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel jTailModel = mock(JTailModel.class);

        // When
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(100);
        when(new LineReader(file, fileIndexer.getIndex())).thenReturn(lineReader);
        when(new JTailModel(file, lineReader)).thenReturn(jTailModel);
        action.update(observable, 100);

        // Then
        verify(jTailMainModel, times(1)).add(jTailModel);
    }
}