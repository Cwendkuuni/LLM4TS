
package fr.pingtimeout.jtail.gui.action;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.RamFileIndex;
import fr.pingtimeout.jtail.io.RomFileIndex;
import fr.pingtimeout.jtail.util.ExceptionHandler;
import fr.pingtimeout.jtail.util.InformationHandler;
import fr.pingtimeout.jtail.util.JTailLogger;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class IndexFileActionTest {
    private static final ResourceBundle bundle;
    private static final ImageIcon OPEN_ICON;

    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;

    static {
        bundle = ResourceBundle.getBundle("fr.pingtimeout.jtail.gui.language");
        OPEN_ICON = new ImageIcon(OpenFileAction.class.getResource("add16.png"));
    }

    @Before
    public void setUp() {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = mock(OpenFileModel.class);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformedWithNoFileSelected() {
        // Given
        ActionEvent mockEvent = mock(ActionEvent.class);
        Component mockComponent = mock(Component.class);
        when(mockEvent.getSource()).thenReturn(mockComponent);
        when(openFileModel.getFile()).thenReturn(null);

        // When
        indexFileAction.actionPerformed(mockEvent);

        // Then
        verify(mockComponent).setVisible(false);
        verifyStatic(InformationHandler.class);
        InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformedWithMemoryBasedIndex() throws FileNotFoundException {
        // Given
        ActionEvent mockEvent = mock(ActionEvent.class);
        Component mockComponent = mock(Component.class);
        when(mockEvent.getSource()).thenReturn(mockComponent);

        File mockFile = mock(File.class);
        when(openFileModel.getFile()).thenReturn(mockFile);

        FileIndexer mockFileIndexer = mock(FileIndexer.class);
        whenNew(FileIndexer.class).withArguments(eq(mockFile), eq(RamFileIndex.class))
                .thenReturn(mockFileIndexer);

        FileIndexerWorker mockFileIndexerWorker = mock(FileIndexerWorker.class);
        whenNew(FileIndexerWorker.class).withArguments(eq(mockFileIndexer))
                .thenReturn(mockFileIndexerWorker);

        // When
        indexFileAction.actionPerformed(mockEvent);

        // Then
        verify(mockComponent).setVisible(false);

        verify(mockFileIndexer).addObserver(indexFileAction);
        verify(mockFileIndexerWorker).execute();
    }

    @Test
    public void testActionPerformedWithFileBasedIndex() throws FileNotFoundException {
        // Given
        ActionEvent mockEvent = mock(ActionEvent.class);
        Component mockComponent = mock(Component.class);
        when(mockEvent.getSource()).thenReturn(mockComponent);

        File mockFile = mock(File.class);
        when(openFileModel.getFile()).thenReturn(mockFile);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);

        FileIndexer mockFileIndexer = mock(FileIndexer.class);
        whenNew(FileIndexer.class).withArguments(eq(mockFile), eq(RomFileIndex.class))
                .thenReturn(mockFileIndexer);

        FileIndexerWorker mockFileIndexerWorker = mock(FileIndexerWorker.class);
        whenNew(FileIndexerWorker.class).withArguments(eq(mockFileIndexer))
                .thenReturn(mockFileIndexerWorker);

        // When
        indexFileAction.actionPerformed(mockEvent);

        // Then
        verify(mockComponent).setVisible(false);

        verify(mockFileIndexer).addObserver(indexFileAction);
        verify(mockFileIndexerWorker).execute();
    }

    @Test
    public void testUpdate() throws FileNotFoundException {
        // Given
        Observable mockObservable = mock(Observable.class);
        Object arg = 100;
        File file = mock(File.class);
        FileIndexer mockFileIndexer = mock(FileIndexer.class);
        when((int) arg).thenReturn(100);
        when(mockObservable).thenReturn(mockFileIndexer);
        when(mockFileIndexer.getFile()).thenReturn(file);

        LineReader mockLineReader = mock(LineReader.class);
        whenNew(LineReader.class).withArguments(eq(file), eq(mockFileIndexer.getIndex()))
                .thenReturn(mockLineReader);

        JTailModel mockModel = mock(JTailModel.class);
        whenNew(JTailModel.class).withArguments(eq(file), eq(mockLineReader))
                .thenReturn(mockModel);

        // When
        indexFileAction.update(mockObservable, arg);

        // Then
        verify(mockFileIndexer).getFile();
        verifyNew(LineReader.class).withArguments(eq(file), eq(mockFileIndexer.getIndex()));
        verify(jTailMainModel).add(mockModel);
    }
}
