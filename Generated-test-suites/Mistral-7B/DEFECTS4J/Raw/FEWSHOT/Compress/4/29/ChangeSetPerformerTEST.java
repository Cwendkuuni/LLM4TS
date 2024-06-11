package org.apache.commons.compress.changes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private Set changes;
    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveEntry entry1;
    private ArchiveEntry entry2;
    private ArchiveEntry entry3;

    @Before
    public void setup() throws IOException {
        changeSet = mock(ChangeSet.class);
        changes = new LinkedHashSet<>();
        performer = new ChangeSetPerformer(changeSet);

        Change addChange1 = new Change(Change.TYPE_ADD, "file1.txt", null);
        Change addChange2 = new Change(Change.TYPE_ADD, "file2.txt", null);
        Change addChange3 = new Change(Change.TYPE_ADD, "file3.txt", null);
        Change deleteChange1 = new Change(Change.TYPE_DELETE, "file1.txt", null);
        Change deleteChange2 = new Change(Change.TYPE_DELETE, "file2.txt", null);
        Change deleteChange3 = new Change(Change.TYPE_DELETE, "file3.txt", null);
        Change deleteDirChange = new Change(Change.TYPE_DELETE_DIR, "dir1/", null);
        Change replaceChange1 = new Change(Change.TYPE_ADD, "file1.txt", "/original/file1.txt");

        changes.add(addChange1);
        changes.add(addChange2);
        changes.add(addChange3);
        changes.add(deleteChange1);
        changes.add(deleteChange2);
        changes.add(deleteChange3);
        changes.add(deleteDirChange);
        changes.add(replaceChange1);

        when(changeSet.getChanges()).thenReturn(changes);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        entry1 = new TarEntry("file1.txt");
        entry2 = new TarEntry("file2.txt");
        entry3 = new TarEntry("file3.txt");

        when(changeSet.getChange(0)).thenReturn(addChange1);
        when(changeSet.getChange(1)).thenReturn(addChange2);
        when(changeSet.getChange(2)).thenReturn(addChange3);
        when(changeSet.getChange(3)).thenReturn(deleteChange1);
        when(changeSet.getChange(4)).thenReturn(deleteChange2);
        when(changeSet.getChange(5)).thenReturn(deleteChange3);
        when(changeSet.getChange(6)).thenReturn(deleteDirChange);
        when(changeSet.getChange(7)).thenReturn(replaceChange1);
    }

    @Test
    public void testPerformWithEmptyChangeSet() throws IOException {
        performer = new ChangeSetPerformer(new EmptyChangeSet());
        ArchiveInputStream input = mock(ArchiveInputStream.class);
        ArchiveOutputStream output = mock(ArchiveOutputStream.class);

        performer.perform(input, output);
    }

    @Test
    public void testPerformWithNoChanges() throws IOException {
        performer = new ChangeSetPerformer(new NoChangeSet());
        ArchiveInputStream input = mock(ArchiveInputStream.class);
        ArchiveOutputStream output = mock(ArchiveOutputStream.class);

        performer.perform(input, output);

        verify(output, never()).putArchiveEntry(entry1);
        verify(output, never()).putArchiveEntry(entry2);
        verify(output, never()).putArchiveEntry(entry3);
    }

    @Test
    public void testPerformWithAddChanges() throws IOException {
        performer.perform(mock(ArchiveInputStream.class), mock(ArchiveOutputStream.class));

        verify(output, times(3)).putArchiveEntry(entry1);
        verify(output, times(3)).putArchiveEntry(entry2);
        verify(output, times(3)).putArchiveEntry(entry3);
    }

    @Test
    public void testPerformWithDeleteChanges() throws IOException {
        performer.perform(mock(ArchiveInputStream.class), mock(ArchiveOutputStream.class));

        verify(output, never()).putArchiveEntry(entry1);
        verify(output, never()).putArchiveEntry(entry2);
        verify(output, never()).putArchiveEntry(entry3);
    }

    @Test
    public void testPerformWithReplaceChange() throws IOException {
        performer.perform(mock(ArchiveInputStream.class), mock(ArchiveOutputStream.class));

        verify(output, times(1)).putArchiveEntry(entry1);
    }

    @Test
    public void testPerformWithDeletedLater() throws IOException {
        performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream input = mock(ArchiveInputStream.class);
        ArchiveOutputStream output = mock(ArchiveOutputStream.class);

        performer.perform(input, output);

        verify(output, times(1)).putArchiveEntry(entry1);
        verify(output, times(1)).putArchiveEntry(entry2);
        verify(output, times(1)).putArchiveEntry(entry3);
    }

    @Test
    public void testPerformWithAddedLater() throws IOException {
        performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream input = mock(ArchiveInputStream.class);
        ArchiveOutputStream output = mock(ArchiveOutputStream.class);

        performer.perform(input, output);

        verify(output, times(3)).putArchiveEntry(entry1);
        verify(output, times(3)).putArchiveEntry(entry2);
        verify(output, times(3)).putArchiveEntry(entry3);
    }

    @Test
    public void testPerformWithDeletedDir() throws IOException {
        performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream input = mock(ArchiveInputStream.class);
        ArchiveOutputStream output = mock(ArchiveOutputStream.class);

        performer.perform(input, output);

        verify(output, never()).putArchiveEntry(entry1);
        verify(output, never()).putArchiveEntry(entry2);
        verify(output, never()).putArchiveEntry(entry3);
    }

    @Test
    public void testIsDeletedLaterWithNoDeletions() {
        performer = new ChangeSetPerformer(changeSet);

        assertFalse(performer.isDeletedLater(changes, null));
    }

    @Test
    public void testIsDeletedLaterWithDeletionLater() {
        performer = new ChangeSetPerformer(changeSet);

        assertTrue(performer.isDeletedLater(changes, entry1));
    }

    @Test
    public void testIsDeletedLaterWithNoDeletionLater() {
        performer = new ChangeSetPerformer(changeSet);

        assertFalse(performer.isDeletedLater(changes, entry1));
    }

    private static class EmptyChangeSet implements ChangeSet {
        @Override
        public Set getChanges() {
            return Collections.emptySet();
        }
    }

    private static class NoChangeSet implements ChangeSet {
        @Override
        public Set getChanges() {
            return Arrays.asList(new Change(Change.TYPE_ADD, "file.txt", null)).iterator();
        }
    }
}