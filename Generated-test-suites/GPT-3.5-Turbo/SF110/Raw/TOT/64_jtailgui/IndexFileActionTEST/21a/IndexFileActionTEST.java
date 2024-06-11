import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.ImageIcon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.ExceptionHandler;
import fr.pingtimeout.jtail.gui.controller.InformationHandler;
import fr.pingtimeout.jtail.gui.controller.JTailAbstractAction;
import fr.pingtimeout.jtail.gui.controller.JTailLogger;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.FileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.RamFileIndex;
import fr.pingtimeout.jtail.io.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailUtil;

public class IndexFileActionTest {
	
	private IndexFileAction indexFileAction;
	private JTailMainModel jTailMainModel;
	private OpenFileModel openFileModel;
	private ResourceBundle bundle;
	private Component source;
	
	@Before
	public void setUp() {
		jTailMainModel = mock(JTailMainModel.class);
		openFileModel = mock(OpenFileModel.class);
		bundle = ResourceBundle.getBundle("fr.pingtimeout.jtail.gui.language");
		source = mock(Component.class);
		
		// Initialize dummy action event
		ActionEvent actionEvent = new ActionEvent(source, ActionEvent.ACTION_FIRST, "dummy_command");
		JTailLogger.setDebug(false);
		
		indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
		
		// Set the values needed for test cases
		when(jTailMainModel.getFileIndexer()).thenReturn(null);
		when(openFileModel.getFile()).thenReturn(null);
		when(openFileModel.getIndexType()).thenReturn(null);
	}
	
	@After
	public void tearDown() {
		jTailMainModel = null;
		openFileModel = null;
		bundle = null;
		source = null;
		indexFileAction = null;
	}
	
	@Test
	public void testActionPerformed_FileNull_DisplayError() {
		// Set up the test case
		when(openFileModel.getFile()).thenReturn(null);
		InformationHandler.setMockResult(true);
		
		// Perform the action
		indexFileAction.actionPerformed(mock(ActionEvent.class));
		
		// Verify that the error is displayed
		verifyStatic(InformationHandler.class);
		InformationHandler.handle(eq(true), eq(UIMessage.INFO_NO_FILE_SELECTED), any());
	}
	
	@Test
	public void testActionPerformed_FileNotFound_DisplayError() throws FileNotFoundException {
		// Set up the test case
		File file = new File("non_existing_file.txt");
		when(openFileModel.getFile()).thenReturn(file);
		ExceptionHandler.setMockResult(true);
		
		// Perform the action
		indexFileAction.actionPerformed(mock(ActionEvent.class));
		
		// Verify that the error is displayed
		verifyStatic(ExceptionHandler.class);
		ExceptionHandler.handle(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq(file.getName()));
	}
	
	@Test
	public void testActionPerformed_MemoryBasedIndex_Success() throws FileNotFoundException {
		// Set up the test case
		File file = new File("existing_file.txt");
		when(openFileModel.getFile()).thenReturn(file);
		when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
		
		// Perform the action
		indexFileAction.actionPerformed(mock(ActionEvent.class));
		
		// Verify that FileIndexer is created with the correct parameters
		verify(openFileModel).getFile();
		verify(openFileModel).getIndexType();
		verify(jTailMainModel).getFileIndexer();
		
		// Verify that FileIndexer is added as an observer
		verify(indexFileer).addObserver(indexFileAction);
		
		// Verify that FileIndexerWorker is created and executed
		verifyNew(FileIndexerWorker.class).withArguments(indexFileer);
		verify(fileIndexerWorker).execute();
	}
	
	@Test
	public void testUpdate_IndexerFinished_Success() throws FileNotFoundException {
		// Set up the test case
		FileIndexer fileIndexerMock = mock(FileIndexer.class);
		FileIndex fileIndexMock = mock(FileIndex.class);
		File fileMock = mock(File.class);
		LineReader lineReaderMock = mock(LineReader.class);
		JTailModel jTailModelMock = mock(JTailModel.class);

		when(openFileModel.getFile()).thenReturn(fileMock);
		when(fileIndexMock.getLines()).thenReturn(100);
		when(fileIndexMock.getParent()).thenReturn(fileMock);
		whenNew(LineReader.class).withArguments(fileMock, fileIndexMock).thenReturn(lineReaderMock);
		whenNew(JTailModel.class).withArguments(fileMock, lineReaderMock).thenReturn(jTailModelMock);
		
		// Perform the update
		indexFileAction.update(fileIndexerMock, 100);
		
		// Verify that the jTailMainModel receives and adds the JTailModel
		verify(jTailMainModel).add(jTailModelMock);
		// Verify that the Logger is called with the correct arguments
		verifyStatic(JTailLogger.class);
		JTailLogger.debug(eq("Notification received : {} emitted by {}"), eq(100), same(fileIndexerMock));
	}
	
	@Test
	public void testUpdate_IndexerFinished_FileNotFound_DisplayError() throws FileNotFoundException {
		// Set up the test case
		FileIndexer fileIndexerMock = mock(FileIndexer.class);
		File fileMock = mock(File.class);

		when(fileIndexerMock.getFile()).thenReturn(fileMock);
		ExceptionHandler.setMockResult(true);
		
		// Perform the update
		indexFileAction.update(fileIndexerMock, 100);
		
		// Verify that the ExceptionHandler is called with the correct arguments
		verifyStatic(ExceptionHandler.class);
		ExceptionHandler.handle(any(), eq(UIMessage.ERROR_FILE_NOT_FOUND), same(fileMock.getName()));
	}

}