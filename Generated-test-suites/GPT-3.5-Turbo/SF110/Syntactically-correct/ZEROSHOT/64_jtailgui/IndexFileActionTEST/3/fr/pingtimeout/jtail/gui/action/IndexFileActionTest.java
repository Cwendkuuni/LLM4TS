package fr.pingtimeout.jtail.gui.action;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.util.ExceptionHandler;
import fr.pingtimeout.jtail.util.JTailLogger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

public class IndexFileActionTest implements Observer {
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private IndexFileAction indexFileAction;
    private FileIndexerWorker fileIndexerWorker;
    private FileIndexer fileIndexer;
    private LineReader lineReader;
    private JTailModel jTailModel;
    private File file;

    @Before
    public void setUp() throws FileNotFoundException {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = mock(OpenFileModel.class);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexFileAction.IndexType.FILE_BASED);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        fileIndexerWorker = mock(FileIndexerWorker.class);
        fileIndexer = mock(FileIndexer.class);
        lineReader = mock(LineReader.class);
        jTailModel = mock(JTailModel.class);
        file = mock(File.class);
    }

    @Test
    public void actionPerformed_withNullFile_shouldHandleInformation() {
        when(openFileModel.getFile()).thenReturn(null);

        indexFileAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));

        verify(openFileModel).getFile();
        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verifyNoInteractions(fileIndexer, fileIndexerWorker, lineReader, jTailModel);
        verifyStatic();
        InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void actionPerformed_withFileNotFound_shouldHandleException() throws FileNotFoundException {
        whenNew(FileIndexer.class).withAnyArguments().thenThrow(new FileNotFoundException());

        indexFileAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));

        verify(openFileModel).getIndexType();
        verify(openFileModel).getFile();
        verify(fileIndexer).addObserver(indexFileAction);
        verifyStatic();
        ExceptionHandler.handle(any(), eq(UIMessage.ERROR_FILE_NOT_FOUND), anyString());
        verifyNoInteractions(fileIndexer, fileIndexerWorker, lineReader, jTailModel);
    }

    @Test
    public void actionPerformed_withFileBasedIndexType_shouldCreateFileIndexer() throws FileNotFoundException {
        indexFileAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));

        verify(openFileModel).getIndexType();
        verify(fileIndexer).addObserver(indexFileAction);
        verifyNoInteractions(fileIndexerWorker, lineReader, jTailModel);
    }

    @Test
    public void actionPerformed_withMemoryBasedIndexType_shouldCreateRamFileIndexer() throws FileNotFoundException {
        when(openFileModel.getIndexType()).thenReturn(IndexFileAction.IndexType.MEMORY_BASED);

        indexFileAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));

        verify(openFileModel).getIndexType();
        verify(fileIndexer).addObserver(indexFileAction);
        verifyNoInteractions(fileIndexerWorker, lineReader, jTailModel);
    }

    @Test
    public void actionPerformed_withValidFile_shouldCreateLineReaderAndJTailModel() throws Exception {
        when(fileIndexer.getFile()).thenReturn(file);
        whenNew(LineReader.class).withArguments(file, fileIndexer.getIndex()).thenReturn(lineReader);
        whenNew(JTailModel.class).withArguments(file, lineReader).thenReturn(jTailModel);

        indexFileAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));

        verify(openFileModel).getIndexType();
        verify(fileIndexer).addObserver(indexFileAction);
        verify(jTailMainModel).add(jTailModel);
        verifyNoInteractions(fileIndexerWorker);
        verifyStatic();
        JTailLogger.debug("Notification received : {} emitted by {}", 100, fileIndexer);
    }

    @Test
    public void update_withCompleteIndexProgress_shouldAddJTailModelToMainModel() throws FileNotFoundException {
        when(fileIndexer.getFile()).thenReturn(file);
        whenNew(LineReader.class).withArguments(file, fileIndexer.getIndex()).thenReturn(lineReader);
        whenNew(JTailModel.class).withArguments(file, lineReader).thenReturn(jTailModel);

        indexFileAction.update(fileIndexer, 100);

        verify(fileIndexer).getFile();
        verify(jTailMainModel).add(jTailModel);
        verifyNoMoreInteractions(jTailMainModel, lineReader, jTailModel);
        verifyStatic();
        JTailLogger.debug("Notification received : {} emitted by {}", 100, fileIndexer);
    }
}