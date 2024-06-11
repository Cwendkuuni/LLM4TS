package fr.pingtimeout.jtail.gui.action;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito.*;
import java.awt.event.*;
import fr.pingtimeout.jtail.util.*;
import java.awt.*;
import javax.swing.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.io.index.*;
import java.util.*;
import fr.pingtimeout.jtail.io.*;
import fr.pingtimeout.jtail.gui.model.*;
import java.io.*;

public class IndexFileActionTest {

    @Test
    public void testActionPerformed_noFileSelected() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        ActionEvent actionEvent = mock(ActionEvent.class);
        JDialog dialog = mock(JDialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(null);
        
        indexFileAction.actionPerformed(actionEvent);
        
        verify(dialog).setVisible(false);
        // Verify information message is displayed
        verifyStatic(InformationHandler.class);
        InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformed_fileNotFound() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        ActionEvent actionEvent = mock(ActionEvent.class);
        JDialog dialog = mock(JDialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(null);
        
        File file = mock(File.class);
        when(openFileModel.getFile()).thenReturn(file);
        when(file.getName()).thenReturn("test.txt");
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        
        Class<? extends FileIndex> indexerClass = RamFileIndex.class;
        FileIndexer fileIndexer = mock(FileIndexer.class);
        whenNew(FileIndexer.class).withArguments(file, indexerClass).thenReturn(fileIndexer);
        doThrow(new FileNotFoundException()).when(fileIndexer).addObserver(any());
        
        indexFileAction.actionPerformed(actionEvent);
        
        verify(dialog).setVisible(false);
        // Verify exception message is displayed
        verifyStatic(ExceptionHandler.class);
        ExceptionHandler.handle(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq("test.txt"));
    }

    @Test
    public void testActionPerformed_success() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        ActionEvent actionEvent = mock(ActionEvent.class);
        JDialog dialog = mock(JDialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(null);
        
        File file = mock(File.class);
        when(openFileModel.getFile()).thenReturn(file);
        when(file.getName()).thenReturn("test.txt");
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        
        Class<? extends FileIndex> indexerClass = RamFileIndex.class;
        FileIndexer fileIndexer = mock(FileIndexer.class);
        whenNew(FileIndexer.class).withArguments(file, indexerClass).thenReturn(fileIndexer);
        
        indexFileAction.actionPerformed(actionEvent);
        
        verify(dialog).setVisible(false);
        verify(fileIndexer).addObserver(any());
    }

    @Test
    public void testUpdate_indexComplete() throws Exception {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        JTailLogger jTailLogger = mock(JTailLogger.class);
        Field field = JTailLogger.class.getDeclaredField("debugLogger");
        field.setAccessible(true);
        field.set(jTailLogger, jTailLogger);
        
        FileIndexer fileIndexer = mock(FileIndexer.class);
        File file = mock(File.class);
        when(fileIndexer.getFile()).thenReturn(file);
        
        LineReader lineReader = mock(LineReader.class);
        whenNew(LineReader.class).withArguments(file, null).thenReturn(lineReader);
        
        JTailModel jTailModel = mock(JTailModel.class);
        whenNew(JTailModel.class).withArguments(file, lineReader).thenReturn(jTailModel);
        
        Observable observable = mock(Observable.class);
        when((int) eq(observable)).thenReturn(100);
        
        indexFileAction.update(observable, 100);
        
        verify(jTailMainModel).add(jTailModel);
    }
}