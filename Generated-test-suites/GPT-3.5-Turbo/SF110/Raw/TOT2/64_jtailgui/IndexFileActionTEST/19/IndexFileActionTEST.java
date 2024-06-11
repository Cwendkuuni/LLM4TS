import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.*;
import fr.pingtimeout.jtail.io.index.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.io.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;
import fr.pingtimeout.jtail.gui.action.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class IndexFileActionTest {
    
    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private File testFile;
    
    @Before
    public void setUp() {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = mock(OpenFileModel.class);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        testFile = new File("test.txt");
    }
    
    @Test
    public void testActionPerformed_noFileSelected() {
        JButton source = new JButton();
        source.addActionListener(indexFileAction);
        ActionEvent actionEventMock = mock(ActionEvent.class);
        when(openFileModel.getFile()).thenReturn(null);
        
        indexFileAction.actionPerformed(actionEventMock);
        
        verify(InformationHandler.class, times(1)).handle(eq(true), eq(UIMessage.INFO_NO_FILE_SELECTED), anyObject());
    }
    
    @Test
    public void testActionPerformed_fileNotFound() {
        JButton source = new JButton();
        source.addActionListener(indexFileAction);
        ActionEvent actionEventMock = mock(ActionEvent.class);
        when(openFileModel.getFile()).thenReturn(testFile);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        
        indexFileAction.actionPerformed(actionEventMock);
        
        verify(ExceptionHandler.class, times(1)).handle(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq(testFile.getName()));
    }
    
    @Test
    public void testUpdate_notificationReceived() {
        FileIndexer fileIndexerMock = mock(FileIndexer.class);
        LineReader lineReaderMock = mock(LineReader.class);
        when(fileIndexerMock.getFile()).thenReturn(testFile);
        when(fileIndexerMock.getIndex()).thenReturn(new int[]{0, 0});
        JTailModel jTailModelMock = mock(JTailModel.class);
        when(jTailMainModel.add(jTailModelMock)).thenReturn(true);
        
        indexFileAction.update(fileIndexerMock, 100);
        
        verify(jTailMainModel, times(1)).add(jTailModelMock);
    }
}