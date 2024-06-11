import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer changeSetPerformer;
    private ArchiveInputStream mockInputStream;
    private ArchiveOutputStream mockOutputStream;
    private ChangeSetResults mockResults;
    private ChangeSet mockChangeSet;
    private Change mockChange;

    @Before
    public void setUp() {
        mockInputStream = Mockito.mock(ArchiveInputStream.class);
        mockOutputStream = Mockito.mock(ArchiveOutputStream.class);
        mockResults = Mockito.mock(ChangeSetResults.class);
        mockChangeSet = Mockito.mock(ChangeSet.class);
        mockChange = Mockito.mock(Change.class);

        Set<Change> changes = new LinkedHashSet<>();
        changes.add(mockChange);

        when(mockChangeSet.getChanges()).thenReturn(changes);
        changeSetPerformer = new ChangeSetPerformer(mockChangeSet);
    }

    @Test
    public void testPerform() throws IOException {
        when(mockChange.type()).thenReturn(Change.TYPE_ADD);
        when(mockChange.isReplaceMode()).thenReturn(true);
        when(mockChange.getInput()).thenReturn(mockInputStream);
        when(mockChange.getEntry()).thenReturn(mock(ArchiveEntry.class));

        changeSetPerformer.perform(mockInputStream, mockOutputStream);

        verify(mockChange, times(1)).getInput();
        verify(mockChange, times(1)).getEntry();
        verify(mockOutputStream, times(1)).putArchiveEntry(any(ArchiveEntry.class));
        verify(mockInputStream, times(1)).close();
        verify(mockOutputStream, times(1)).closeArchiveEntry();
        verify(mockResults, times(1)).addedFromChangeSet(anyString());
    }

    @Test
    public void testPerformDelete() throws IOException {
        when(mockChange.type()).thenReturn(Change.TYPE_DELETE);
        when(mockChange.getInput()).thenReturn(mockInputStream);
        when(mockChange.targetFile()).thenReturn("testFile");

        ArchiveEntry mockEntry = mock(ArchiveEntry.class);
        when(mockInputStream.getNextEntry()).thenReturn(mockEntry, (ArchiveEntry)null);

        changeSetPerformer.perform(mockInputStream, mockOutputStream);

        verify(mockChange, times(1)).targetFile();
        verify(mockResults, times(1)).deleted(anyString());
        verify(mockOutputStream, never()).putArchiveEntry(any(ArchiveEntry.class));
    }

    @Test
    public void testPerformDeleteDir() throws IOException {
        when(mockChange.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(mockChange.getInput()).thenReturn(mockInputStream);
        when(mockChange.targetFile()).thenReturn("testDir");

        ArchiveEntry mockEntry = mock(ArchiveEntry.class);
        when(mockEntry.getName()).thenReturn("testDir/testFile");
        when(mockInputStream.getNextEntry()).thenReturn(mockEntry, (ArchiveEntry)null);

        changeSetPerformer.perform(mockInputStream, mockOutputStream);

        verify(mockChange, times(1)).targetFile();
        verify(mockResults, times(1)).deleted(anyString());
        verify(mockOutputStream, never()).putArchiveEntry(any(ArchiveEntry.class));
    }

    @Test
    public void testPerformCopy() throws IOException {
        when(mockChange.type()).thenReturn(Change.TYPE_ADD);
        when(mockChange.isReplaceMode()).thenReturn(false);
        when(mockChange.getInput()).thenReturn(mockInputStream);
        when(mockChange.getEntry()).thenReturn(mock(ArchiveEntry.class));

        ArchiveEntry mockEntry = mock(ArchiveEntry.class);
        when(mockEntry.getName()).thenReturn("testFile");
        when(mockInputStream.getNextEntry()).thenReturn(mockEntry, (ArchiveEntry)null);

        changeSetPerformer.perform(mockInputStream, mockOutputStream);

        verify(mockChange, times(1)).getInput();
        verify(mockChange, times(1)).getEntry();
        verify(mockInputStream, times(1)).getNextEntry();
        verify(mockOutputStream, times(1)).putArchiveEntry(any(ArchiveEntry.class));
        verify(mockOutputStream, times(1)).closeArchiveEntry();
        verify(mockResults, times(1)).addedFromStream(anyString());
    }

    @Test
    public void testPerformAdd() throws IOException {
        when(mockChange.type()).thenReturn(Change.TYPE_ADD);
        when(mockChange.isReplaceMode()).thenReturn(false);
        when(mockChange.getInput()).thenReturn(mockInputStream);
        when(mockChange.getEntry()).thenReturn(mock(ArchiveEntry.class));

        ArchiveEntry mockEntry = mock(ArchiveEntry.class);
        when(mockEntry.getName()).thenReturn("testFile");
        when(mockInputStream.getNextEntry()).thenReturn(mockEntry, (ArchiveEntry)null);

        when(mockChange.isReplaceMode()).thenReturn(true);

        changeSetPerformer.perform(mockInputStream, mockOutputStream);

        verify(mockChange, times(1)).getInput();
        verify(mockChange, times(1)).getEntry();
        verify(mockInputStream, times(1)).getNextEntry();
        verify(mockOutputStream, times(1)).putArchiveEntry(any(ArchiveEntry.class));
        verify(mockOutputStream, times(1)).closeArchiveEntry();
        verify(mockResults, times(1)).addedFromChangeSet(anyString());
    }

    @Test
    public void testIsDeletedLater() {
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(mockChange);

        ArchiveEntry mockEntry = mock(ArchiveEntry.class);
        when(mockEntry.getName()).thenReturn("testFile");

        when(mockChange.type()).thenReturn(Change.TYPE_DELETE);
        when(mockChange.targetFile()).thenReturn("testFile");
        when(mockChange.isReplaceMode()).thenReturn(true);

        boolean isDeletedLater = changeSetPerformer.isDeletedLater(workingSet, mockEntry);

        assert isDeletedLater;
    }

    @Test
    public void testCopyStream() throws IOException {
        InputStream mockInputStream = Mockito.mock(InputStream.class);
        ArchiveEntry mockEntry = mock(ArchiveEntry.class);
        ArchiveOutputStream mockOutput = Mockito.mock(ArchiveOutputStream.class);

        changeSetPerformer.copyStream(mockInputStream, mockOutput, mockEntry);

        verify(mockOutput, times(1)).putArchiveEntry(any(ArchiveEntry.class));
        verify(mockInputStream, times(1)).close();
        verify(mockOutput, times(1)).closeArchiveEntry();
    }
}