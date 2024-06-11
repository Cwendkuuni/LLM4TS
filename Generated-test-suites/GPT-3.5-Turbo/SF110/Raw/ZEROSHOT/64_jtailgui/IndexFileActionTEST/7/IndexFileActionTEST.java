import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.awt.event.ActionEvent;
import fr.pingtimeout.jtail.util.JTailLogger;
import java.awt.Component;
import javax.swing.JDialog;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import java.io.File;
import java.io.FileNotFoundException;
import fr.pingtimeout.jtail.gui.message.ExceptionHandler;

public class IndexFileActionTest {
  
  @Mock
  private JTailMainModel mockJTailMainModel;
  
  @Mock
  private OpenFileModel mockOpenFileModel;
  
  private IndexFileAction indexFileAction;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    indexFileAction = new IndexFileAction(mockJTailMainModel, mockOpenFileModel);
  }
  
  @Test
  public void testActionPerformed_NoFileSelected() {
    // Given
    ActionEvent actionEvent = new ActionEvent(new JButton(), 1, "command");
    InformationHandler.setFail(true);

    // When
    indexFileAction.actionPerformed(actionEvent);

    // Then
    assertTrue(InformationHandler.getFail());
    assertEquals(UIMessage.INFO_NO_FILE_SELECTED, InformationHandler.getLastMessage());
  }
  
  @Test
  public void testActionPerformed_FileNotFound_ExceptionHandled() {
    // Given
    ActionEvent actionEvent = new ActionEvent(new JButton(), 1, "command");
    InformationHandler.setFail(false);
    File file = new File("non_existing_file.txt");
    when(mockOpenFileModel.getFile()).thenReturn(file);
    when(mockOpenFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);

    // When
    indexFileAction.actionPerformed(actionEvent);

    // Then
    assertTrue(InformationHandler.getFail());
    assertEquals(UIMessage.ERROR_FILE_NOT_FOUND, InformationHandler.getLastMessage());
    assertEquals(file.getName(), InformationHandler.getLastMessageParams()[0]);
  }
  
  @Test
  public void testActionPerformed_MemoryBasedIndexType() {
    // Given
    ActionEvent actionEvent = new ActionEvent(new JButton(), 1, "command");
    InformationHandler.setFail(false);
    File file = new File("existing_file.txt");
    when(mockOpenFileModel.getFile()).thenReturn(file);
    when(mockOpenFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);

    // When
    indexFileAction.actionPerformed(actionEvent);

    // Then
    verify(mockJTailMainModel).add(any(JTailModel.class));
  }
  
  @Test
  public void testUpdate_NotificationReceived_Index100() {
    // Given
    FileIndexer fileIndexer = mock(FileIndexer.class);
    File file = new File("test_file.txt");
    when(fileIndexer.getFile()).thenReturn(file);
    when(fileIndex.getIndex()).thenReturn(100);

    // When
    indexFileAction.update(fileIndexer, 100);

    // Then
    verify(mockJTailMainModel).add(any(JTailModel.class));
  }

  @Test
  public void testUpdate_NotificationReceived_IndexNot100() {
    // Given
    FileIndexer fileIndexer = mock(FileIndexer.class);
    File file = new File("test_file.txt");
    when(fileIndexer.getFile()).thenReturn(file);
    when(fileIndex.getIndex()).thenReturn(50);

    // When
    indexFileAction.update(fileIndexer, 50);

    // Then
    // No action to verify
  }
}