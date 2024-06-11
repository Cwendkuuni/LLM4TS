package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {
    private ChangeSetPerformer performer;
    private ChangeSet changes;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;

    @Before
    public void setUp() {
        changes = mock(ChangeSet.class);
        in = mock(ArchiveInputStream.class);
        out = mock(ArchiveOutputStream.class);
        performer = new ChangeSetPerformer(changes);
    }

    @Test
    public void testPerform() throws IOException {
        Set<Change> changeSet = new LinkedHashSet<>();
        Change addChange = mock(Change.class);
        Change deleteChange = mock(Change.class);
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(deleteChange);
        ArchiveEntry entry = mock(ArchiveEntry.class);

        when(changes.getChanges()).thenReturn(changeSet);
        when(in.getNextEntry()).thenReturn(entry, null);
        when(deleteChange.type()).thenReturn(Change.TYPE_DELETE);
        when(deleteChange.targetFile()).thenReturn("file1.txt");
        when(entry.getName()).thenReturn("file1.txt", "file2.txt", null);
        when(addChange.type()).thenReturn(Change.TYPE_ADD);
        when(addChange.isReplaceMode()).thenReturn(false);
        when(addChange.getInput()).thenReturn(mock(InputStream.class));
        when(addChange.getEntry()).thenReturn(entry);
        when(addChange.type()).thenReturn(Change.TYPE_ADD);
        when(addChange.isReplaceMode()).thenReturn(true);
        when(addChange.getInput()).thenReturn(mock(InputStream.class));
        when(addChange.getEntry()).thenReturn(entry);

        performer.perform(in, out);

        verify(changes).getChanges();
        verify(in, times(3)).getNextEntry();
        verify(deleteChange, times(2)).type();
        verify(deleteChange).targetFile();
        verify(entry, times(2)).getName();
        verify(addChange, times(3)).type();
        verify(addChange, times(2)).isReplaceMode();
        verify(addChange, times(2)).getInput();
        verify(addChange, times(2)).getEntry();
        verify(out).putArchiveEntry(entry);
        verify(IOUtils).copy(any(InputStream.class), any(ArchiveOutputStream.class));
        verify(out).closeArchiveEntry();
    }

    @Test
    public void testIsDeletedLater() {
        Set<Change> workingSet = new LinkedHashSet<>();
        ArchiveEntry entry = mock(ArchiveEntry.class);

        assertFalse(performer.isDeletedLater(workingSet, entry));

        Change deleteChange = mock(Change.class);
        when(deleteChange.type()).thenReturn(Change.TYPE_DELETE);
        when(deleteChange.targetFile()).thenReturn("file1.txt");

        workingSet.add(deleteChange);

        assertTrue(performer.isDeletedLater(workingSet, entry));
    }

    @Test
    public void testCopyStream() throws IOException {
        ArchiveEntry entry = mock(ArchiveEntry.class);
        InputStream inStream = mock(InputStream.class);

        performer.copyStream(inStream, out, entry);

        verify(out).putArchiveEntry(entry);
        verify(IOUtils).copy(any(InputStream.class), any(ArchiveOutputStream.class));
        verify(out).closeArchiveEntry();
    }
}