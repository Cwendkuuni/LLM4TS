
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer changeSetPerformer;
    private ChangeSet changeSet;
    private ArchiveInputStream archiveInputStreamMock;
    private ArchiveOutputStream archiveOutputStreamMock;

    @Before
    public void setup() {
        changeSet = mock(ChangeSet.class);
        archiveInputStreamMock = mock(ArchiveInputStream.class);
        archiveOutputStreamMock = mock(ArchiveOutputStream.class);

        changeSetPerformer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void perform_AddWithReplaceModeEntry_CallsCopyStream() throws IOException {
        // Arrange
        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(true);
        when(change.getInput()).thenReturn(mock(InputStream.class));
        when(change.getEntry()).thenReturn(entry);
        when(changeSet.getChanges()).thenReturn(mock(Set.class));
        Set workingSet = new LinkedHashSet<Change>();
        workingSet.add(change);
        when(changeSet.getChanges()).thenReturn(workingSet);

        // Act
        changeSetPerformer.perform(archiveInputStreamMock, archiveOutputStreamMock);

        // Assert
        verify(changeSet, times(2)).getChanges();
        verify(change).type();
        verify(change).isReplaceMode();
        verify(change).getInput();
        verify(change).getEntry();
        verify(archiveOutputStreamMock).putArchiveEntry(entry);
        verify(archiveOutputStreamMock).closeArchiveEntry();
    }

    @Test
    public void perform_DeleteEntry_CallsCopyStream() throws IOException {
        // Arrange
        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("filename");
        when(changeSet.getChanges()).thenReturn(mock(Set.class));
        Set workingSet = new LinkedHashSet<Change>();
        workingSet.add(change);
        when(changeSet.getChanges()).thenReturn(workingSet);
        when(archiveInputStreamMock.getNextEntry()).thenReturn(entry).thenReturn(null);

        // Act
        changeSetPerformer.perform(archiveInputStreamMock, archiveOutputStreamMock);

        // Assert
        verify(changeSet, times(4)).getChanges();
        verify(change).type();
        verify(change).targetFile();
        verify(archiveInputStreamMock, times(2)).getNextEntry();
        verify(archiveOutputStreamMock).putArchiveEntry(entry);
        verify(archiveOutputStreamMock).closeArchiveEntry();
    }

    // Additional test cases for identified edge cases and exceptions: 

    @Test(expected = IOException.class)
    public void perform_CopyStreamThrowsIOException_ThrowsIOException() throws IOException {
        // Arrange
        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(true);
        when(change.getInput()).thenReturn(mock(InputStream.class));
        when(change.getEntry()).thenReturn(entry);
        when(changeSet.getChanges()).thenReturn(mock(Set.class));
        Set workingSet = new LinkedHashSet<Change>();
        workingSet.add(change);
        when(changeSet.getChanges()).thenReturn(workingSet);

        doThrow(IOException.class).when(archiveOutputStreamMock).putArchiveEntry(entry);

        // Act
        changeSetPerformer.perform(archiveInputStreamMock, archiveOutputStreamMock);
    }

    @Test
    public void isDeletedLater_EntryExistInChangeSet_ReturnsTrue() {
        // Arrange
        Set workingSet = new LinkedHashSet<Change>();
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("filename");
        workingSet.add(change);

        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("filename");

        // Act
        boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);

        // Assert
        assertEquals(true, result);
    }

    @Test
    public void isDeletedLater_DirectoryEntryExistInChangeSet_ReturnsTrue() {
        // Arrange
        Set workingSet = new LinkedHashSet<Change>();
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change.targetFile()).thenReturn("dir");
        workingSet.add(change);

        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("dir/filename");

        // Act
        boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);

        // Assert
        assertEquals(true, result);
    }

    @Test
    public void isDeletedLater_EntryDoesNotExistInChangeSet_ReturnsFalse() {
        // Arrange
        Set workingSet = new LinkedHashSet<Change>();
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("filename");
        workingSet.add(change);

        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("otherfilename");

        // Act
        boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);

        // Assert
        assertEquals(false, result);
    }

    @Test
    public void copyStream_PutsArchiveEntryAndClosesArchiveEntry() throws IOException {
        // Arrange
        ByteArrayInputStream input = new ByteArrayInputStream("test".getBytes());
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(archiveOutputStreamMock.putArchiveEntry(entry)).thenReturn(entry);

        // Act
        changeSetPerformer.copyStream(input, archiveOutputStreamMock, entry);

        // Assert
        verify(archiveOutputStreamMock).putArchiveEntry(entry);
        verify(archiveOutputStreamMock).closeArchiveEntry();
    }
}

