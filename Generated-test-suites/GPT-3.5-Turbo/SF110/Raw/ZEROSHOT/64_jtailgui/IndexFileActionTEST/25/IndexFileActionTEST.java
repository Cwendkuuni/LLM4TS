package fr.pingtimeout.jtail.gui.action;

import fr.pingtimeout.jtail.gui.controller.OpenFileModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.controller.ExceptionHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTree;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;

    @Before
    public void setUp() {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = mock(OpenFileModel.class);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformed() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        JDialog jDialog = mock(JDialog.class);
        JRootPane rootPane = mock(JRootPane.class);
        JPanel panel = mock(JPanel.class);

        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(jDialog);
        when(jDialog.getRootPane()).thenReturn(rootPane);
        when(rootPane.getParent()).thenReturn(panel);

        indexFileAction.actionPerformed(actionEvent);

        verify(dialog, times(1)).setVisible(false);
    }

    @Test
    public void testActionPerformed_NoFileSelected() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        JDialog jDialog = mock(JDialog.class);
        JRootPane rootPane = mock(JRootPane.class);
        JPanel panel = mock(JPanel.class);

        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(jDialog);
        when(jDialog.getRootPane()).thenReturn(rootPane);
        when(rootPane.getParent()).thenReturn(panel);
        when(openFileModel.getFile()).thenReturn(null);

        indexFileAction.actionPerformed(actionEvent);

        verify(dialog, times(1)).setVisible(false);
        verify(openFileModel, times(1)).getFile();
        verify(jTailMainModel, never()).add(any(JTailModel.class));
    }

    @Test
    public void testActionPerformed_FileNotFoundException() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);
        JDialog jDialog = mock(JDialog.class);
        JRootPane rootPane = mock(JRootPane.class);
        JPanel panel = mock(JPanel.class);
        File file = mock(File.class);

        when(actionEvent.getSource()).thenReturn(dialog);
        when(dialog.getParent()).thenReturn(jDialog);
        when(jDialog.getRootPane()).thenReturn(rootPane);
        when(rootPane.getParent()).thenReturn(panel);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexFileAction.IndexType.FILE_BASED);
        when(file.getName()).thenReturn("testFile.txt");
        doThrow(new FileNotFoundException()).when(openFileModel).getFile();

        indexFileAction.actionPerformed(actionEvent);

        verify(dialog, times(1)).setVisible(false);
        verify(openFileModel, times(1)).getFile();
        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verify(ExceptionHandler.class, times(1)).handle(any(FileNotFoundException.class), anyString(), eq("testFile.txt"));
    }

    @Test
    public void testUpdate() {
        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndex fileIndex = mock(FileIndex.class);
        File file = mock(File.class);
        LineReader lineReader = mock(LineReader.class);
        JTailModel model = mock(JTailModel.class);

        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(fileIndex);
        when(openFileModel.getFile()).thenReturn(file);
        when(file.getName()).thenReturn("testFile.txt");

        try {
            whenNew(LineReader.class).withArguments(file, fileIndex).thenReturn(lineReader);
        } catch (Exception e) {
            fail("Caught Exception during LineReader instantiation");
        }

        when(openFileModel.getFile()).thenReturn(file);

        try {
            whenNew(JTailModel.class).withArguments(file, lineReader).thenReturn(model);
        } catch (Exception e) {
            fail("Caught Exception during JTailModel instantiation");
        }

        int arg = 100;
        Object o = fileIndexer;

        indexFileAction.update(o, arg);

        verify(jTailMainModel, times(1)).add(model);
    }

    @Test
    public void testUpdate_FileNotFoundException() {
        FileIndexer fileIndexer = mock(FileIndexer.class);
        File file = mock(File.class);

        when(fileIndexer.getFile()).thenReturn(null);
        when(openFileModel.getFile()).thenReturn(file);
        when(file.getName()).thenReturn("testFile.txt");

        int arg = 100;
        Object o = fileIndexer;

        indexFileAction.update(o, arg);

        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verify(ExceptionHandler.class, times(1)).handle(any(FileNotFoundException.class), anyString(), eq("testFile.txt"));
    }

    @After
    public void tearDown() {
        indexFileAction = null;
        jTailMainModel = null;
        openFileModel = null;
    }
}