import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer changeSetPerformer;
    private final String TEST_FILE_NAME = "testFile.txt";
    private final String TEST_FOLDER_NAME = "testFolder";

    @Before
    public void setup() {
        changeSet = new ChangeSet();
        changeSetPerformer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_AllChangesAddedFromChangeSet_OutputStreamContainsAllAddedChanges() throws IOException {
        // Arrange
        Change change1 = new Change.Builder()
                .type(Change.TYPE_ADD)
                .replaceMode(true)
                .input(getTestInputStream(TEST_FILE_NAME))
                .entry(getTestEntry(TEST_FILE_NAME))
                .build();
        Change change2 = new Change.Builder()
                .type(Change.TYPE_ADD)
                .input(getTestInputStream(TEST_FILE_NAME))
                .entry(getTestEntry(TEST_FILE_NAME))
                .build();
        changeSet.add(change1);
        changeSet.add(change2);

        ArchiveInputStream mockArchiveInputStream = getMockArchiveInputStream();
        ArchiveOutputStream mockArchiveOutputStream = getMockArchiveOutputStream();

        // Act
        ChangeSetResults results = changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        // Assert
        assertTrue(results.hasBeenAdded(TEST_FILE_NAME));
        assertTrue(results.hasBeenAdded(TEST_FOLDER_NAME + "/" + TEST_FILE_NAME));
        verify(mockArchiveOutputStream, times(2)).putArchiveEntry(any(ArchiveEntry.class));
        verify(mockArchiveOutputStream, times(2)).closeArchiveEntry();
    }

    @Test
    public void testPerform_DeleteChangeExists_OutputStreamDoesNotContainDeletedEntries() throws IOException {
        // Arrange
        Change change = new Change.Builder()
                .type(Change.TYPE_DELETE)
                .targetFile(TEST_FILE_NAME)
                .build();
        changeSet.add(change);

        ArchiveInputStream mockArchiveInputStream = getMockArchiveInputStream();
        ArchiveOutputStream mockArchiveOutputStream = getMockArchiveOutputStream();

        // Act
        ChangeSetResults results = changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        // Assert
        assertFalse(results.hasBeenAdded(TEST_FILE_NAME));
        verify(mockArchiveInputStream).getNextEntry();
        verify(mockArchiveInputStream, never()).read(any(byte[].class), anyInt(), anyInt());
        verify(mockArchiveInputStream, never()).closeEntry();
        verify(mockArchiveOutputStream, never()).putArchiveEntry(any(ArchiveEntry.class));
        verify(mockArchiveOutputStream, never()).closeArchiveEntry();
    }

    @Test
    public void testPerform_DeleteDirChangeExists_OutputStreamDoesNotContainDeletedDirEntries() throws IOException {
        // Arrange
        Change change = new Change.Builder()
                .type(Change.TYPE_DELETE_DIR)
                .targetFile(TEST_FOLDER_NAME)
                .build();
        changeSet.add(change);

        ArchiveInputStream mockArchiveInputStream = getMockArchiveInputStream();
        ArchiveOutputStream mockArchiveOutputStream = getMockArchiveOutputStream();

        // Act
        ChangeSetResults results = changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        // Assert
        assertFalse(results.hasBeenAdded(TEST_FOLDER_NAME + "/" + TEST_FILE_NAME));
        verify(mockArchiveOutputStream, never()).putArchiveEntry(any(ArchiveEntry.class));
        verify(mockArchiveOutputStream, never()).closeArchiveEntry();
    }

    @Test
    public void testPerform_ChangeSetEmpty_OutputStreamRemainsUntouched() throws IOException {
        // Arrange
        ArchiveInputStream mockArchiveInputStream = getMockArchiveInputStream();
        ArchiveOutputStream mockArchiveOutputStream = getMockArchiveOutputStream();

        // Act
        ChangeSetResults results = changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        // Assert
        assertFalse(results.hasBeenAdded(TEST_FILE_NAME));
        verify(mockArchiveInputStream).getNextEntry();
        verify(mockArchiveInputStream, never()).read(any(byte[].class), anyInt(), anyInt());
        verify(mockArchiveInputStream, never()).closeEntry();
        verify(mockArchiveOutputStream, never()).putArchiveEntry(any(ArchiveEntry.class));
        verify(mockArchiveOutputStream, never()).closeArchiveEntry();
    }
    
    @Test
    public void testPerform_ErrorWhileReadingInputInputStream_ThrowsIOException() throws IOException {
        // Arrange
        Change change = new Change.Builder()
                .type(Change.TYPE_ADD)
                .input(getTestInputStream(TEST_FILE_NAME))
                .entry(getTestEntry(TEST_FILE_NAME))
                .build();
        changeSet.add(change);

        ArchiveInputStream mockArchiveInputStream = getMockArchiveInputStreamWithIoException();
        ArchiveOutputStream mockArchiveOutputStream = getMockArchiveOutputStream();

        // Act & Assert
        assertThrows(IOException.class, () -> {
            changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        });
    }
    
    @Test
    public void testPerform_ErrorWhileWritingToOutputOutputStream_ThrowsIOException() throws IOException {
        // Arrange
        Change change = new Change.Builder()
                .type(Change.TYPE_ADD)
                .input(getTestInputStream(TEST_FILE_NAME))
                .entry(getTestEntry(TEST_FILE_NAME))
                .build();
        changeSet.add(change);

        ArchiveInputStream mockArchiveInputStream = getMockArchiveInputStream();
        ArchiveOutputStream mockArchiveOutputStream = getMockArchiveOutputStreamWithIoException();

        // Act & Assert
        assertThrows(IOException.class, () -> {
            changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        });
    }

    private InputStream getTestInputStream(String fileName) {
        // return test input stream implementation
    }

    private ArchiveEntry getTestEntry(String fileName) {
        // return test archive entry implementation
    }

    private ArchiveInputStream getMockArchiveInputStream() throws IOException {
        // return mock ArchiveInputStream
    }

    private ArchiveInputStream getMockArchiveInputStreamWithIoException() throws IOException {
        // return mock ArchiveInputStream that throws IOException
    }

    private ArchiveOutputStream getMockArchiveOutputStream() throws IOException {
        // return mock ArchiveOutputStream
    }

    private ArchiveOutputStream getMockArchiveOutputStreamWithIoException() throws IOException {
        // return mock ArchiveOutputStream that throws IOException
    }
}