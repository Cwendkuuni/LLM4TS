package fr.pingtimeout.jtail.gui.action;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.load.FileLoader;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observer;
import java.util.Observable;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;

public class IndexFileActionTest {
    private static final ResourceBundle bundle;
    private static final ImageIcon OPEN_ICON;
    private IndexFileAction indexFileAction;
    @Mock
    private JTailMainModel jTailMainModel;
    @Mock
    private OpenFileModel openFileModel;

    public static final String FILE_NAME = "test.txt";
    public static final String INDEX_TYPE = "FILE_BASED";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformedWithNoFileSelected() {
        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");
        indexFileAction.actionPerformed(actionEvent);

        assertInformationHandlerCalled(UIMessage.INFO_NO_FILE_SELECTED);
    }

    @Test
    public void testActionPerformedWithFileNotExisting() throws FileNotFoundException {
        File file = new File(FILE_NAME);
        when(openFileModel.getFile()).thenReturn(file);

        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");
        indexFileAction.actionPerformed(actionEvent);

        assertExceptionHandlerCalled(UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
    }

    @Test
    public void testActionPerformedWithMemoryBasedIndexType() {
        File file = new File(FILE_NAME);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(INDEX_TYPE);

        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");
        indexFileAction.actionPerformed(actionEvent);

        assertFileIndexerCreated(file, RamFileIndex.class);
        assertFileIndexerWorkerExecuted(fileIndexer);
    }

    @Test
    public void testActionPerformedWithFileBasedIndexType() {
        File file = new File(FILE_NAME);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(INDEX_TYPE);

        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");
        indexFileAction.actionPerformed(actionEvent);

        assertFileIndexerCreated(file, RomFileIndex.class);
        assertFileIndexerWorkerExecuted(fileIndexer);
    }

    @Test
    public void testUpdate() throws FileNotFoundException {
        File file = new File(FILE_NAME);
        when(fileIndexer.getFile()).thenReturn(file);

        indexFileAction.update(fileIndexer, 100);

        assertJTailModelAdded(file, lineReader);
    }

    private void assertFileIndexerCreated(File file, Class<? extends FileIndex> indexerClass) throws FileNotFoundException {
        verifyNew(FileIndexer.class).withArguments(file, indexerClass);
        verify(fileIndexer).addObserver(indexFileAction);
    }

    private void assertFileIndexerWorkerExecuted(FileIndexer fileIndexer) {
        verify(fileIndexerWorker).execute();
    }

    private void assertJTailModelAdded(File file, LineReader lineReader) {
        verify(jTailMainModel).add(new JTailModel(file, lineReader));
    }

    private void assertExceptionHandlerCalled(String message, Object... args) {
        verify(ExceptionHandler).handle(any(Exception.class), eq(message), eq(args));
    }

    private void assertInformationHandlerCalled(String message, Object... args) {
        verify(InformationHandler).handle(eq(true), eq(message), eq(args));
    }

    static {
        bundle = ResourceBundle.getBundle("fr.pingtimeout.jtail.gui.language");
        OPEN_ICON = new ImageIcon(OpenFileAction.class.getResource("add16.png"));
    }
}