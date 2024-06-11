package fr.pingtimeout.jtail.gui.action;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.JDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;

import fr.pingtimeout.jtail.util.ExceptionHandler;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

    private JTailMainModel jTailMainModel = Mockito.mock(JTailMainModel.class);
    private OpenFileModel openFileModel = Mockito.mock(OpenFileModel.class);

    @Test
    public void testActionPerformedWithNullFile() {
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        Mockito.when(openFileModel.getFile()).thenReturn(null);

        Component source = new JButton("Source");
        ActionEvent actionEvent = new ActionEvent(source, ActionEvent.ACTION_PERFORMED, "");

        JDialog dialog = new JDialog();
        dialog.add(source);

        indexFileAction.actionPerformed(actionEvent);

        Mockito.verify(openFileModel).getFile();
        Mockito.verify(jTailMainModel, Mockito.never()).add(Mockito.any(JTailModel.class));
        Mockito.verify(dialog).setVisible(Mockito.eq(false));
        Mockito.verify(dialog).getParent();
    }

    @Test
    public void testActionPerformedWithMemoryBasedIndex() throws FileNotFoundException {
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        File file = new File("testfile.txt");
        Mockito.when(openFileModel.getFile()).thenReturn(file);
        Mockito.when(openFileModel.getIndexType()).thenReturn(IndexFileAction.IndexType.MEMORY_BASED);

        Component source = new JButton("Source");
        ActionEvent actionEvent = new ActionEvent(source, ActionEvent.ACTION_PERFORMED, "");

        JDialog dialog = new JDialog();
        dialog.add(source);

        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        Mockito.when(fileIndexer.getFile()).thenReturn(file);

        FileIndexerWorker fileIndexerWorker = Mockito.mock(FileIndexerWorker.class);
        Mockito.when(fileIndexerWorker.getFileIndexer()).thenReturn(fileIndexer);

        Mockito.whenNew(FileIndexer.class)
                .withArguments(Mockito.eq(file), Mockito.eq(RamFileIndex.class))
                .thenReturn(fileIndexer);

        Mockito.whenNew(FileIndexerWorker.class)
                .withArguments(Mockito.eq(fileIndexer))
                .thenReturn(fileIndexerWorker);

        Mockito.doNothing().when(fileIndexer).addObserver(Mockito.any(IndexFileAction.class));
        Mockito.doNothing().when(fileIndexerWorker).execute();

        indexFileAction.actionPerformed(actionEvent);

        Mockito.verify(openFileModel).getFile();
        Mockito.verify(openFileModel).getIndexType();
        Mockito.verify(dialog).setVisible(Mockito.eq(false));
        Mockito.verify(dialog).getParent();
        Mockito.verify(fileIndexer).addObserver(Mockito.any(IndexFileAction.class));
        Mockito.verify(fileIndexerWorker).execute();
    }

    @Test
    public void testUpdateWithCompleteNotification() throws FileNotFoundException {
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        File file = new File("testfile.txt");
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        LineReader lineReader = Mockito.mock(LineReader.class);
        JTailModel jTailModel = Mockito.mock(JTailModel.class);

        Mockito.when(fileIndexer.getFile()).thenReturn(file);
        Mockito.when(fileIndexer.getIndex()).thenReturn(100);
        Mockito.when(fileIndexer.getIndexType()).thenReturn(IndexFileAction.IndexType.MEMORY_BASED);

        Mockito.whenNew(LineReader.class)
                .withArguments(Mockito.eq(file), Mockito.eq(fileIndexer.getIndex()))
                .thenReturn(lineReader);

        Mockito.whenNew(JTailModel.class)
                .withArguments(Mockito.eq(file), Mockito.eq(lineReader))
                .thenReturn(jTailModel);

        indexFileAction.update(fileIndexer, 100);

        Mockito.verify(fileIndexer).getFile();
        Mockito.verify(fileIndexer).getIndex();
        Mockito.verify(jTailMainModel).add(Mockito.eq(jTailModel));
    }
}