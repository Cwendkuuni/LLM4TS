import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class IndexFileActionTest {

    @Mock
    private JTailMainModel jTailMainModel;
    @Mock
    private OpenFileModel openFileModel;
    private IndexFileAction indexFileAction;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformed_noFileSelected() {
        // Arrange
        JDialog dialog = Mockito.mock(JDialog.class);
        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");

        // Act
        indexFileAction.actionPerformed(actionEvent);

        // Assert
        Mockito.verify(dialog).setVisible(false);
        InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformed_fileNotFound() {
        // Arrange
        JDialog dialog = Mockito.mock(JDialog.class);
        File file = Mockito.mock(File.class);
        Mockito.when(openFileModel.getFile()).thenReturn(file);
        Mockito.when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");

        // Act
        indexFileAction.actionPerformed(actionEvent);

        // Assert
        Mockito.verify(dialog).setVisible(false);
        ExceptionHandler.handle(Mockito.any(FileNotFoundException.class), UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
    }

    @Test
    public void testActionPerformed_success() throws Exception {
        // Arrange
        JDialog dialog = Mockito.mock(JDialog.class);
        File file = Mockito.mock(File.class);
        Mockito.when(openFileModel.getFile()).thenReturn(file);
        Mockito.when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");

        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        Mockito.whenNew(FileIndexer.class).withArguments(file, RomFileIndex.class).thenReturn(fileIndexer);

        FileIndexerWorker fileIndexerWorker = Mockito.mock(FileIndexerWorker.class);
        Mockito.whenNew(FileIndexerWorker.class).withArguments(fileIndexer).thenReturn(fileIndexerWorker);

        JTailModel jTailModel = Mockito.mock(JTailModel.class);
        LineReader lineReader = Mockito.mock(LineReader.class);
        Mockito.whenNew(LineReader.class).withArguments(file, fileIndexer.getIndex()).thenReturn(lineReader);
        Mockito.whenNew(JTailModel.class).withArguments(file, lineReader).thenReturn(jTailModel);

        // Act
        indexFileAction.actionPerformed(actionEvent);

        // Assert
        Mockito.verify(dialog).setVisible(false);
        Mockito.verify(fileIndexer).addObserver(indexFileAction);
        Mockito.verify(fileIndexerWorker).execute();
        Mockito.verify(jTailMainModel).add(jTailModel);
        Mockito.verify(lineReader).close();
    }

    @Test
    public void testUpdate_notificationReceived() {
        // Arrange
        File file = Mockito.mock(File.class);
        LineReader lineReader = Mockito.mock(LineReader.class);
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        Mockito.when(fileIndexer.getFile()).thenReturn(file);
        Mockito.when(fileIndexer.getIndex()).thenReturn(Mockito.mock(FileIndex.class));
        Mockito.whenNew(LineReader.class).withArguments(file, fileIndexer.getIndex()).thenReturn(lineReader);
        JTailModel jTailModel = Mockito.mock(JTailModel.class);
        Mockito.whenNew(JTailModel.class).withArguments(file, lineReader).thenReturn(jTailModel);

        // Act
        indexFileAction.update(fileIndexer, 100);

        // Assert
        Mockito.verify(jTailMainModel).add(jTailModel);
        Mockito.verify(lineReader).close();
    }

}