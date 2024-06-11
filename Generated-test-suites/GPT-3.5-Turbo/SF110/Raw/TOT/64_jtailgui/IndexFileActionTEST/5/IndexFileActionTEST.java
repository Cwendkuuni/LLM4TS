import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.FileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.RamFileIndex;
import fr.pingtimeout.jtail.io.RomFileIndex;
import fr.pingtimeout.jtail.util.ExceptionHandler;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.ResourceBundle;

public class IndexFileActionTest {
    
    private JTailMainModel jTailMainModelMock;
    private OpenFileModel openFileModelMock;
    private IndexFileAction indexFileAction;
    
    @Before
    public void setup() {
        jTailMainModelMock = mock(JTailMainModel.class);
        openFileModelMock = mock(OpenFileModel.class);
        indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
    }
    
    @Test
    public void testActionPerformed_NoFileSelected() {
        ActionEvent actionEventMock = mock(ActionEvent.class);
        Component dialogMock = mock(Component.class);
        when(actionEventMock.getSource()).thenReturn(dialogMock);
        when(dialogMock.getParent()).thenReturn(new JDialog());
        when(openFileModelMock.getFile()).thenReturn(null);
        
        indexFileAction.actionPerformed(actionEventMock);
        
        verify(openFileModelMock).getFile();
        verifyZeroInteractions(jTailMainModelMock);
        verify(dialogMock).setVisible(false);
        verifyStatic(InformationHandler.class);
        InformationHandler.handle(eq(true), eq(UIMessage.INFO_NO_FILE_SELECTED), any());
    }
    
    @Test
    public void testActionPerformed_FileNotFoundException() throws FileNotFoundException {
        ActionEvent actionEventMock = mock(ActionEvent.class);
        Component dialogMock = mock(Component.class);
        File fileMock = mock(File.class);
        when(actionEventMock.getSource()).thenReturn(dialogMock);
        when(dialogMock.getParent()).thenReturn(new JDialog());
        when(openFileModelMock.getFile()).thenReturn(fileMock);
        when(openFileModelMock.getIndexType()).thenReturn(OpenFileModel.IndexType.FILE_BASED);
        when(fileMock.getName()).thenReturn("testFile.txt");
        whenNew(RomFileIndex.class).withNoArguments().thenReturn(mock(RomFileIndex.class));
        doThrow(new FileNotFoundException()).when(fileMock).exists();
        
        indexFileAction.actionPerformed(actionEventMock);
        
        verify(openFileModelMock, times(2)).getFile();
        verifyZeroInteractions(jTailMainModelMock);
        verify(dialogMock).setVisible(false);
        verifyStatic(ExceptionHandler.class);
        ExceptionHandler.handle(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq("testFile.txt"));
    }
    
    @Test
    public void testActionPerformed_Successful() throws FileNotFoundException {
        ActionEvent actionEventMock = mock(ActionEvent.class);
        Component dialogMock = mock(Component.class);
        File fileMock = mock(File.class);
        FileIndex fileIndexMock = mock(FileIndex.class);
        LineReader lineReaderMock = mock(LineReader.class);
        JTailModel jTailModelMock = mock(JTailModel.class);
        when(actionEventMock.getSource()).thenReturn(dialogMock);
        when(dialogMock.getParent()).thenReturn(new JDialog());
        when(openFileModelMock.getFile()).thenReturn(fileMock);
        when(openFileModelMock.getIndexType()).thenReturn(OpenFileModel.IndexType.MEMORY_BASED);
        when(fileMock.getName()).thenReturn("testFile.txt");
        when(lineReaderMock.getFile()).thenReturn(fileMock);
        when(lineReaderMock.getLinesCount()).thenReturn(100);
        whenNew(RamFileIndex.class).withNoArguments().thenReturn(mock(RamFileIndex.class));
        whenNew(FileIndexer.class).withArguments(eq(fileMock), eq(RamFileIndex.class)).thenReturn(mock(FileIndexer.class));
        whenNew(LineReader.class).withArguments(eq(fileMock), eq(fileIndexMock.getIndex())).thenReturn(lineReaderMock);
        whenNew(JTailModel.class).withArguments(eq(fileMock), eq(lineReaderMock)).thenReturn(jTailModelMock);
        
        indexFileAction.actionPerformed(actionEventMock);
        
        verify(openFileModelMock, times(2)).getFile();
        verify(jTailMainModelMock).add(eq(jTailModelMock));
        verify(dialogMock).setVisible(false);
        verifyStatic(ExceptionHandler.class, never());
        ExceptionHandler.handle(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq("testFile.txt"));
    }
    
    @Test
    public void testUpdate_NotificationReceivedIncorrectArg() {
        Observable oMock = mock(Observable.class);
        Object incorrectArg = new Object();
        
        indexFileAction.update(oMock, incorrectArg);
        
        verifyZeroInteractions(jTailMainModelMock);
        verifyStatic(JTailLogger.class);
        JTailLogger.debug(eq("Notification received : {} emitted by {}"), eq(incorrectArg), eq(oMock));
    }
    
    @Test
    public void testUpdate_NotificationReceivedCorrectArg() throws FileNotFoundException {
        Observable oMock = mock(Observable.class);
        FileIndexer fileIndexerMock = mock(FileIndexer.class);
        File fileMock = mock(File.class);
        LineReader lineReaderMock = mock(LineReader.class);
        JTailModel jTailModelMock = mock(JTailModel.class);
        when(fileIndexerMock.getFile()).thenReturn(fileMock);
        whenNew(LineReader.class).withArguments(eq(fileMock), eq(fileIndexerMock.getIndex())).thenReturn(lineReaderMock);
        whenNew(JTailModel.class).withArguments(eq(fileMock), eq(lineReaderMock)).thenReturn(jTailModelMock);
        
        indexFileAction.update(fileIndexerMock, 100);
        
        verifyZeroInteractions(jTailMainModelMock);
        verify(jTailMainModelMock).add(eq(jTailModelMock));
        verifyStatic(JTailLogger.class);
        JTailLogger.debug(eq("Notification received : {} emitted by {}"), eq(100), same(fileIndexerMock));
        verifyStatic(ExceptionHandler.class, never());
        ExceptionHandler.handle(any(FileNotFoundException.class), any(), any());
    }

}