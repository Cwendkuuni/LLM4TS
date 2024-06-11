
package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {
    private ChangeSetPerformer changeSetPerformer;

    @Before
    public void setUp() {
        ChangeSet changeSet = mock(ChangeSet.class);
        changeSetPerformer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform() throws IOException {
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ChangeSetResults expectedResults = mock(ChangeSetResults.class);
        Set<Change> changes = new LinkedHashSet<>();
        Change change1 = mock(Change.class);
        when(change1.type()).thenReturn(Change.TYPE_ADD);
        when(change1.isReplaceMode()).thenReturn(true);
        when(change1.getInput()).thenReturn(mock(InputStream.class));
        when(change1.getEntry()).thenReturn(mock(ArchiveEntry.class));
        changes.add(change1);
        when(expectedResults.hasBeenAdded(anyString())).thenReturn(false);
        Set<Change> workingSet = new LinkedHashSet<>(changes);
        when(changeSet.getChanges()).thenReturn(changes).thenReturn(workingSet);
        when(expectedResults.addedFromChangeSet(anyString())).thenReturn(true);
        when(in.getNextEntry()).thenReturn(mock(ArchiveEntry.class)).thenReturn(null);
        when(expectedResults.deleted(anyString())).thenReturn(true);
        when(expectedResults.addedFromStream(anyString())).thenReturn(true);
        when(expectedResults.hasBeenAdded(anyString())).thenReturn(false);

        ChangeSetResults results = changeSetPerformer.perform(in, out);

        assertEquals(expectedResults, results);
        verify(out, times(1)).closeArchiveEntry();
        verify(in, times(2)).getNextEntry();
    }

    @Test
    public void testIsDeletedLater() {
        Set<Change> workingSet = new LinkedHashSet<>();
        ArchiveEntry entry = mock(ArchiveEntry.class);
        Change change1 = mock(Change.class);
        when(change1.type()).thenReturn(Change.TYPE_DELETE);
        when(change1.targetFile()).thenReturn("file1");
        workingSet.add(change1);
        when(change1.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change1.targetFile()).thenReturn("dir1");
        assertTrue(changeSetPerformer.isDeletedLater(workingSet, entry));

        workingSet.clear();
        when(change1.type()).thenReturn(Change.TYPE_DELETE);
        when(change1.targetFile()).thenReturn("file2");
        workingSet.add(change1);
        when(change1.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change1.targetFile()).thenReturn("dir1");
        assertFalse(changeSetPerformer.isDeletedLater(workingSet, entry));
    }

    @Test
    public void testCopyStream() throws IOException {
        InputStream in = mock(InputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        changeSetPerformer.copyStream(in, out, entry);

        verify(out, times(1)).putArchiveEntry(entry);
        verify(in, times(1)).close();
        verify(out, times(1)).closeArchiveEntry();
    }
}
