
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.LineReader;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

public class IndexFileActionTest {
    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private FileIndexer fileIndexer;

    @Before
    public void setup() throws FileNotFoundException {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = mock(OpenFileModel.class);
        fileIndexer = mock(FileIndexer.class);

        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        indexFileAction.setFileIndexer(fileIndexer);

        // Mock setup for openFileModel
        File file = new File("test.txt");
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);

        // Mock setup for FileIndexer
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(new RamFileIndex());

        LineReader lineReader = mock(LineReader.class);

        // Mock setup for JTailMainModel
        JTailModel model = mock(JTailModel.class);
        when(model.getLineReader()).thenReturn(lineReader);
        when(model.getFile()).thenReturn(file);
        when(jTailMainModel.add(any(JTailModel.class))).thenReturn(model);
    }

    @Test
    public void testActionPerformedWithFile() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        File file = new File("test.txt");

        // Mock setup for actionEvent
        when(actionEvent.getSource()).thenReturn(file);

        // Call the method under test
        indexFileAction.actionPerformed(actionEvent);

        // Verify that the fileIndexer has been created correctly
        verify(fileIndexer).setFile(file);
        verify(fileIndexer).setIndex(any(RamFileIndex.class));
        verify(fileIndexer).addObserver(indexFileAction);

        // Verify that the model has been added to jTailMainModel
        verify(jTailMainModel).add(any(JTailModel.class));
    }

    @Test
    public void testActionPerformedWithNullFile() {
        ActionEvent actionEvent = mock(ActionEvent.class);

        // Mock setup for actionEvent
        when(actionEvent.getSource()).thenReturn(null);

        // Call the method under test
        indexFileAction.actionPerformed(actionEvent);

        // Verify that the InformationHandler has been called with the correct message
        verify(InformationHandler).handle(true, UIMessage.INFO_NO_FILE_SELECTED);
    }

    @Test
    public void testUpdate() {
        Observable observable = mock(Observable.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        File file = new File("test.txt");

        // Mock setup for observable
        when(observable instanceof FileIndexer).thenReturn(true);

        // Mock setup for fileIndexer
        when(fileIndexer.getFile()).thenReturn(file);

        // Mock setup for JTailModel
        LineReader lineReader = mock(LineReader.class);
        JTailModel model = mock(JTailModel.class);
        when(model.getLineReader()).thenReturn(lineReader);
        when(model.getFile()).thenReturn(file);

        // Call the method under test
        indexFileAction.update(observable, 100);

        // Verify that the model has been added to jTailMainModel
        verify(jTailMainModel).add(model);
    }
}
