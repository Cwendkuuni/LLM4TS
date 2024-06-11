import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer changeSetPerformer;
    private ArchiveInputStream mockInputStream;
    private ArchiveOutputStream mockOutputStream;
    private ChangeSet mockChangeSet;
    private InputStream mockEntryInputStream;
    private ArchiveEntry mockEntry;

    @Before
    public void setUp() {
        mockInputStream = mock(ArchiveInputStream.class);
        mockOutputStream = mock(ArchiveOutputStream.class);
        mockChangeSet = mock(ChangeSet.class);
        mockEntryInputStream = mock(InputStream.class);
        mockEntry = mock(ArchiveEntry.class);

        when(mockChangeSet.getChanges()).thenReturn(new LinkedHashSet<>());

        changeSetPerformer = new ChangeSetPerformer(mockChangeSet);
    }

    @Test
    public void testPerform_AddReplaceTypeChange_AddsEntryToOutputStream() throws IOException {
        Change mockChange = mock(Change.class);
        when(mockChange.type()).thenReturn(Change.TYPE_ADD);
        when(mockChange.isReplaceMode()).thenReturn(true);
        when(mockChange.getInput()).thenReturn(mockEntryInputStream);
        when(mockChange.getEntry()).thenReturn(mockEntry);

        Set<Change> changes = new LinkedHashSet<>();
        changes.add(mockChange);

        when(mockChangeSet.getChanges()).thenReturn(changes);

        changeSetPerformer.perform(mockInputStream, mockOutputStream);

        verify(mockOutputStream).putArchiveEntry(mockEntry);
        verify(mockOutputStream).closeArchiveEntry();
    }

    @Test
    public void testPerform_DeleteTypeChange_DeletesEntryFromResults() throws IOException {
        Change mockChange = mock(Change.class);
        when(mockChange.type()).thenReturn(Change.TYPE_DELETE);
        when(mockChange.targetFile()).thenReturn("entry1");

        Set<Change> changes = new LinkedHashSet<>();
        changes.add(mockChange);

        when(mockChangeSet.getChanges()).thenReturn(changes);
        when(mockEntry.getName()).thenReturn("entry1");

        changeSetPerformer.perform(mockInputStream, mockOutputStream);

        verify(mockChange).targetFile();
        verify(mockOutputStream, never()).putArchiveEntry(mockEntry);
    }

    @Test
    public void testPerform_DeleteDirTypeChange_DeletesEntryFromResults() throws IOException {
        Change mockChange = mock(Change.class);
        when(mockChange.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(mockChange.targetFile()).thenReturn("dir1");

        Set<Change> changes = new LinkedHashSet<>();
        changes.add(mockChange);

        when(mockChangeSet.getChanges()).thenReturn(changes);
        when(mockEntry.getName()).thenReturn("dir1/entry1");

        changeSetPerformer.perform(mockInputStream, mockOutputStream);

        verify(mockChange).targetFile();
        verify(mockOutputStream, never()).putArchiveEntry(mockEntry);
    }

    @Test
    public void testPerform_AddTypeChange_CopyStreamToOutputStream() throws IOException {
        Change mockChange = mock(Change.class);
        when(mockChange.type()).thenReturn(Change.TYPE_ADD);
        when(mockChange.isReplaceMode()).thenReturn(false);
        when(mockChange.getInput()).thenReturn(mockEntryInputStream);
        when(mockChange.getEntry()).thenReturn(mockEntry);

        Set<Change> changes = new LinkedHashSet<>();
        changes.add(mockChange);

        when(mockChangeSet.getChanges()).thenReturn(changes);

        changeSetPerformer.perform(mockInputStream, mockOutputStream);

        verify(mockInputStream, never()).getNextEntry();
        verify(mockChange).getEntry();
        verify(mockOutputStream).putArchiveEntry(mockEntry);
        verify(mockOutputStream).closeArchiveEntry();
    }

    @Test
    public void testPerform_CopyTypeChange_CopyStreamToOutputStream() throws IOException {
        Change mockChange = mock(Change.class);
        when(mockChange.type()).thenReturn(Change.TYPE_COPY);

        Set<Change> changes = new LinkedHashSet<>();
        changes.add(mockChange);

        when(mockChangeSet.getChanges()).thenReturn(changes);
        when(mockInputStream.getNextEntry()).thenReturn(mockEntry);

        changeSetPerformer.perform(mockInputStream, mockOutputStream);

        verify(mockInputStream).getNextEntry();
        verify(mockOutputStream).putArchiveEntry(mockEntry);
        verify(mockOutputStream).closeArchiveEntry();
    }

    @Test
    public void testPerform_AddReplaceTypeChange_EntryAlreadyAdded_DoNotAddAgain() throws IOException {
        Change mockChange = mock(Change.class);
        when(mockChange.type()).thenReturn(Change.TYPE_ADD);
        when(mockChange.isReplaceMode()).thenReturn(true);
        when(mockChange.getInput()).thenReturn(mockEntryInputStream);
        when(mockChange.getEntry()).thenReturn(mockEntry);

        Set<Change> changes = new LinkedHashSet<>();
        changes.add(mockChange);

        when(mockChangeSet.getChanges()).thenReturn(changes);
        when(mockInputStream.getNextEntry()).thenReturn(mockEntry);

        changeSetPerformer.perform(mockInputStream, mockOutputStream);
        changeSetPerformer.perform(mockInputStream, mockOutputStream);

        verify(mockOutputStream, times(1)).putArchiveEntry(mockEntry);
        verify(mockOutputStream).closeArchiveEntry();
    }

    @Test
    public void testPerform_AddTypeChange_NoReplaceMode_EntryAlreadyAdded_DoNotAddAgain() throws IOException {
        Change mockChange = mock(Change.class);
        when(mockChange.type()).thenReturn(Change.TYPE_ADD);
        when(mockChange.isReplaceMode()).thenReturn(false);
        when(mockChange.getInput()).thenReturn(mockEntryInputStream);
        when(mockChange.getEntry()).thenReturn(mockEntry);

        Set<Change> changes = new LinkedHashSet<>();
        changes.add(mockChange);

        when(mockChangeSet.getChanges()).thenReturn(changes);
        when(mockInputStream.getNextEntry()).thenReturn(mockEntry);

        changeSetPerformer.perform(mockInputStream, mockOutputStream);
        changeSetPerformer.perform(mockInputStream, mockOutputStream);

        verify(mockOutputStream, times(1)).putArchiveEntry(mockEntry);
        verify(mockOutputStream).closeArchiveEntry();
    }

    @Test
    public void testPerform_NoChangesInChangeSet_NoActionsPerformed() throws IOException {
        changeSetPerformer.perform(mockInputStream, mockOutputStream);

        verify(mockInputStream, never()).getNextEntry();
        verify(mockOutputStream, never()).putArchiveEntry(any());
        verify(mockOutputStream, never()).closeArchiveEntry();
    }

    @Test
    public void testIsDeletedLater_EntryIsDeletedLater_ReturnsTrue() {
        Change mockDeleteChange = mock(Change.class);
        when(mockDeleteChange.type()).thenReturn(Change.TYPE_DELETE);
        when(mockDeleteChange.targetFile()).thenReturn("entry1");

        Set<Change> changes = new LinkedHashSet<>();
        changes.add(mockDeleteChange);

        when(mockChangeSet.getChanges()).thenReturn(changes);
        when(mockEntry.getName()).thenReturn("entry1");

        boolean result = changeSetPerformer.isDeletedLater(changes, mockEntry);

        org.junit.Assert.assertTrue(result);
    }

    @Test
    public void testIsDeletedLater_EntryIsNotDeletedLater_ReturnsFalse() {
        Change mockDeleteChange = mock(Change.class);
        when(mockDeleteChange.type()).thenReturn(Change.TYPE_DELETE);
        when(mockDeleteChange.targetFile()).thenReturn("entry2");

        Set<Change> changes = new LinkedHashSet<>();
        changes.add(mockDeleteChange);

        when(mockChangeSet.getChanges()).thenReturn(changes);
        when(mockEntry.getName()).thenReturn("entry1");

        boolean result = changeSetPerformer.isDeletedLater(changes, mockEntry);

        org.junit.Assert.assertFalse(result);
    }
}