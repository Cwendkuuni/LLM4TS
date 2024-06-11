package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveInputStream mockedIn;
    private ArchiveOutputStream mockedOut;

    private ArchiveEntry createArchiveEntry(String name) {
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn(name);
        return entry;
    }

    private Change createChange(int type, String targetFile) {
        Change change = mock(Change.class);
        when(change.type()).thenReturn(type);
        when(change.targetFile()).thenReturn(targetFile);
        return change;
    }

    @Before
    public void setup() {
        changeSet = mock(ChangeSet.class);
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(createChange(Change.TYPE_ADD, "file1"));
        changes.add(createChange(Change.TYPE_DELETE, "file2"));
        changes.add(createChange(Change.TYPE_DELETE_DIR, "dir"));
        when(changeSet.getChanges()).thenReturn(changes);

        performer = new ChangeSetPerformer(changeSet);

        mockedIn = mock(ArchiveInputStream.class);
        mockedOut = mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_AddAndReplaceMode_EntryAddedAndRemovedFromSet() throws IOException {
        ArchiveEntry addReplaceEntry = createArchiveEntry("addReplaceEntry");
        when(addReplaceEntry.getName()).thenReturn("addReplaceEntry");
        Change addReplaceChange = mock(Change.class);
        when(addReplaceChange.type()).thenReturn(Change.TYPE_ADD);
        when(addReplaceChange.isReplaceMode()).thenReturn(true);
        when(addReplaceChange.getInput()).thenReturn(mock(InputStream.class));
        when(addReplaceChange.getEntry()).thenReturn(addReplaceEntry);

        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(addReplaceChange);

        when(changeSet.getChanges()).thenReturn(workingSet);

        ChangeSetResults results = performer.perform(mockedIn, mockedOut);
        assertTrue(results.hasBeenAdded("addReplaceEntry"));
        assertFalse(workingSet.contains(addReplaceChange));
        verify(addReplaceChange, times(1)).getInput();
        verify(mockedOut, times(1)).putArchiveEntry(addReplaceEntry);
        verify(mockedOut, times(1)).closeArchiveEntry();
    }

    @Test
    public void testPerform_AddAndNotReplaceMode_EntryAddedAndRemovedFromSet() throws IOException {
        ArchiveEntry addNotReplaceEntry = createArchiveEntry("addNotReplaceEntry");
        when(addNotReplaceEntry.getName()).thenReturn("addNotReplaceEntry");
        Change addNotReplaceChange = mock(Change.class);
        when(addNotReplaceChange.type()).thenReturn(Change.TYPE_ADD);
        when(addNotReplaceChange.isReplaceMode()).thenReturn(false);
        when(addNotReplaceChange.getInput()).thenReturn(mock(InputStream.class));
        when(addNotReplaceChange.getEntry()).thenReturn(addNotReplaceEntry);

        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(addNotReplaceChange);

        when(changeSet.getChanges()).thenReturn(workingSet);

        ChangeSetResults results = performer.perform(mockedIn, mockedOut);
        assertTrue(results.hasBeenAdded("addNotReplaceEntry"));
        assertFalse(workingSet.contains(addNotReplaceChange));
        verify(addNotReplaceChange, times(1)).getInput();
        verify(mockedOut, times(1)).putArchiveEntry(addNotReplaceEntry);
        verify(mockedOut, times(1)).closeArchiveEntry();
    }

    @Test
    public void testPerform_DeleteFile_EntryRemovedFromSet() throws IOException {
        ArchiveEntry deleteFileEntry = createArchiveEntry("deleteFile");
        when(deleteFileEntry.getName()).thenReturn("deleteFile");
        Change deleteFileChange = mock(Change.class);
        when(deleteFileChange.type()).thenReturn(Change.TYPE_DELETE);
        when(deleteFileChange.targetFile()).thenReturn("deleteFile");

        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(deleteFileChange);

        when(changeSet.getChanges()).thenReturn(workingSet);
        when(mockedIn.getNextEntry()).thenReturn(deleteFileEntry).thenReturn(null);

        ChangeSetResults results = performer.perform(mockedIn, mockedOut);
        assertTrue(results.deleted("deleteFile"));
        assertFalse(workingSet.contains(deleteFileChange));
        verify(mockedOut, times(0)).putArchiveEntry(deleteFileEntry);
        verify(mockedOut, times(0)).closeArchiveEntry();
    }

    @Test
    public void testPerform_DeleteDirectory_EntriesRemovedFromSet() throws IOException {
        ArchiveEntry deleteDirEntry1 = createArchiveEntry("deleteDir/file1");
        when(deleteDirEntry1.getName()).thenReturn("deleteDir/file1");
        ArchiveEntry deleteDirEntry2 = createArchiveEntry("deleteDir/file2");
        when(deleteDirEntry2.getName()).thenReturn("deleteDir/file2");
        Change deleteDirChange = mock(Change.class);
        when(deleteDirChange.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(deleteDirChange.targetFile()).thenReturn("deleteDir");

        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(deleteDirChange);

        when(changeSet.getChanges()).thenReturn(workingSet);
        when(mockedIn.getNextEntry()).thenReturn(deleteDirEntry1).thenReturn(deleteDirEntry2).thenReturn(null);

        ChangeSetResults results = performer.perform(mockedIn, mockedOut);
        assertTrue(results.deleted("deleteDir/file1"));
        assertTrue(results.deleted("deleteDir/file2"));
        assertFalse(workingSet.contains(deleteDirChange));
        verify(mockedOut, times(0)).putArchiveEntry(deleteDirEntry1);
        verify(mockedOut, times(0)).putArchiveEntry(deleteDirEntry2);
        verify(mockedOut, times(0)).closeArchiveEntry();
    }

    @Test
    public void testPerform_CopyEntry_EntryAddedFromStream() throws IOException {
        ArchiveEntry copyEntry = createArchiveEntry("copyEntry");
        when(copyEntry.getName()).thenReturn("copyEntry");

        Set<Change> workingSet = new LinkedHashSet<>();

        when(changeSet.getChanges()).thenReturn(workingSet);
        when(mockedIn.getNextEntry()).thenReturn(copyEntry).thenReturn(null).thenReturn(null);

        ChangeSetResults results = performer.perform(mockedIn, mockedOut);
        assertTrue(results.hasBeenAdded("copyEntry"));
        verify(mockedOut, times(1)).putArchiveEntry(copyEntry);
        verify(mockedOut, times(1)).closeArchiveEntry();
    }

    @Test
    public void testPerform_CopyEntryWithReplaceMode_EntryAddedFromChangeSet() throws IOException {
        ArchiveEntry copyReplaceEntry = createArchiveEntry("copyReplaceEntry");
        when(copyReplaceEntry.getName()).thenReturn("copyReplaceEntry");
        Change copyReplaceChange = mock(Change.class);
        when(copyReplaceChange.type()).thenReturn(Change.TYPE_ADD);
        when(copyReplaceChange.isReplaceMode()).thenReturn(true);
        when(copyReplaceChange.getInput()).thenReturn(mock(InputStream.class));
        when(copyReplaceChange.getEntry()).thenReturn(copyReplaceEntry);

        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(copyReplaceChange);

        when(changeSet.getChanges()).thenReturn(workingSet);
        when(mockedIn.getNextEntry()).thenReturn(copyReplaceEntry).thenReturn(null).thenReturn(null);

        ChangeSetResults results = performer.perform(mockedIn, mockedOut);
        assertTrue(results.hasBeenAdded("copyReplaceEntry"));
        assertFalse(workingSet.contains(copyReplaceChange));
        verify(copyReplaceChange, times(1)).getInput();
        verify(mockedOut, times(0)).putArchiveEntry(copyReplaceEntry);
        verify(mockedOut, times(0)).closeArchiveEntry();
    }

    @Test
    public void testPerform_CopyEntryDeletedLater_EntryAddedFromStream() throws IOException {
        ArchiveEntry copyDeletedLaterEntry = createArchiveEntry("copyDeletedLaterEntry");
        when(copyDeletedLaterEntry.getName()).thenReturn("copyDeletedLaterEntry");
        Change copyDeletedLaterChange = mock(Change.class);
        when(copyDeletedLaterChange.type()).thenReturn(Change.TYPE_DELETE);
        when(copyDeletedLaterChange.targetFile()).thenReturn("copyDeletedLaterEntry");

        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(copyDeletedLaterChange);

        when(changeSet.getChanges()).thenReturn(workingSet);
        when(mockedIn.getNextEntry()).thenReturn(copyDeletedLaterEntry).thenReturn(null).thenReturn(null);

        ChangeSetResults results = performer.perform(mockedIn, mockedOut);
        assertTrue(results.hasBeenAdded("copyDeletedLaterEntry"));
        assertFalse(workingSet.contains(copyDeletedLaterChange));
        verify(mockedOut, times(1)).putArchiveEntry(copyDeletedLaterEntry);
        verify(mockedOut, times(1)).closeArchiveEntry();
    }

    @Test
    public void testPerform_CopyEntryAlreadyAdded_EntryNotAdded() throws IOException {
        ArchiveEntry alreadyAddedEntry = createArchiveEntry("alreadyAddedEntry");
        when(alreadyAddedEntry.getName()).thenReturn("alreadyAddedEntry");

        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(createChange(Change.TYPE_ADD, "alreadyAddedEntry"));

        when(changeSet.getChanges()).thenReturn(workingSet);
        when(mockedIn.getNextEntry()).thenReturn(alreadyAddedEntry).thenReturn(null).thenReturn(null);

        ChangeSetResults results = performer.perform(mockedIn, mockedOut);
        assertFalse(results.hasBeenAdded("alreadyAddedEntry"));
        assertEquals(0, workingSet.size());
        verify(mockedOut, times(0)).putArchiveEntry(alreadyAddedEntry);
        verify(mockedOut, times(0)).closeArchiveEntry();
    }

    @Test
    public void testIsDeletedLater_DeletedLater_ReturnsTrue() {
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(createChange(Change.TYPE_DELETE, "deleteFile"));

        assertTrue(performer.isDeletedLater(workingSet, createArchiveEntry("deleteFile")));
    }

    @Test
    public void testIsDeletedLater_NotDeletedLater_ReturnsFalse() {
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(createChange(Change.TYPE_DELETE, "deleteFile"));

        assertFalse(performer.isDeletedLater(workingSet, createArchiveEntry("keepFile")));
    }

    @Test
    public void testCopyStream_CopyEntry_CopiesStream() throws IOException {
        InputStream input = mock(InputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);

        performer.copyStream(input, out, entry);

        verify(out, times(1)).putArchiveEntry(entry);
        verify(IOUtils, times(1)).copy(input, out);
        verify(out, times(1)).closeArchiveEntry();
    }
}