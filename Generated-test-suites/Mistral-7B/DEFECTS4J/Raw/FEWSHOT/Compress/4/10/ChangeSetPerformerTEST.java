package org.apache.commons.compress.changes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        ChangeSet changeSet = mock(ChangeSet.class);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        Set changes = performer.changes;
        assertEquals(changeSet, performer.changes);
        assertNotNull(changes);
        assertTrue(changes instanceof Set);
        assertTrue(changes.isEmpty());
    }

    /**
     * Test perform method with empty change set
     */
    @Test
    public void testPerformEmptyChangeSet() throws IOException {
        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(Collections.emptySet());
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);

        performer.perform(in, out);
        assertSame(in, performer.in);
        assertSame(out, performer.out);
        verify(in, never()).getNextEntry();
        verify(out, never()).putArchiveEntry(any(ArchiveEntry.class));
        verify(in, never()).close();
        verify(out, never()).finish();
    }

    /**
     * Test perform method with no changes to perform
     */
    @Test
    public void testPerformNoChanges() throws IOException {
        ChangeSet changeSet = mock(ChangeSet.class);
        Set changes = new HashSet();
        when(changeSet.getChanges()).thenReturn(changes);

        Change change1 = mock(Change.class);
        when(change1.type()).thenReturn(Change.TYPE_ADD);
        when(change1.isReplaceMode()).thenReturn(false);
        when(change1.getEntry()).thenReturn(mock(ArchiveEntry.class));
        changes.add(change1);

        Change change2 = mock(Change.class);
        when(change2.type()).thenReturn(Change.TYPE_ADD);
        when(change2.isReplaceMode()).thenReturn(false);
        when(change2.getEntry()).thenReturn(mock(ArchiveEntry.class));
        changes.add(change2);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);

        performer.perform(in, out);

        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        ArchiveEntry entry2 = mock(ArchiveEntry.class);
        when(entry1.getName()).thenReturn("entry1.txt");
        when(entry2.getName()).thenReturn("entry2.txt");

        when(in.getNextEntry()).thenReturn(entry1).thenReturn(entry2)
                .thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(out.getOutputStream(entry1)).thenReturn(outputStream)
                .thenReturn(outputStream);

        when(in.readAllBytes()).thenReturn(new byte[0]);

        ChangeSetResults result = performer.perform(in, out);

        assertEquals(2, result.addedFromChangeSet().size());
        verify(in, times(3)).getNextEntry();
        verify(out, times(2)).putArchiveEntry(entry1);
        verify(out, times(2)).putArchiveEntry(entry2);
        verify(out, times(2)).finish();
        verify(in, times(1)).close();
    }

    /**
     * Test perform method with no changes to add or delete
     */
    @Test
    public void testPerformNoChangesToAddOrDelete() throws IOException {
        ChangeSet changeSet = mock(ChangeSet.class);
        Set changes = new HashSet();
        when(changeSet.getChanges()).thenReturn(changes);

        Change change1 = mock(Change.class);
        when(change1.type()).thenReturn(Change.TYPE_DELETE);
        when(change1.targetFile()).thenReturn("delete1.txt");
        when(change1.getEntry()).thenReturn(mock(ArchiveEntry.class));
        changes.add(change1);

        Change change2 = mock(Change.class);
        when(change2.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change2.targetFile()).thenReturn("deleteDir/");
        when(change2.getEntry()).thenReturn(mock(ArchiveEntry.class));
        changes.add(change2);

        Change change3 = mock(Change.class);
        when(change3.type()).thenReturn(Change.TYPE_ADD);
        when(change3.isReplaceMode()).thenReturn(false);
        when(change3.getInput()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(change3.getEntry()).thenReturn(mock(ArchiveEntry.class));
        changes.add(change3);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);

        performer.perform(in, out);

        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        ArchiveEntry entry2 = mock(ArchiveEntry.class);
        ArchiveEntry entry3 = mock(ArchiveEntry.class);
        when(entry1.getName()).thenReturn("entry1.txt");
        when(entry2.getName()).thenReturn("entry2.txt");
        when(entry3.getName()).thenReturn("entry3.txt");

        when(in.getNextEntry()).thenReturn(entry1).thenReturn(entry2)
                .thenReturn(entry3).thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(out.getOutputStream(entry1)).thenReturn(outputStream)
                .thenReturn(outputStream)
                .thenReturn(outputStream);

        when(in.readAllBytes()).thenReturn(new byte[0]);

        ChangeSetResults result = performer.perform(in, out);

        assertEquals(0, result.addedFromChangeSet().size());
        assertEquals(0, result.deleted().size());
        verify(in, times(5)).getNextEntry();
        verify(out, never()).putArchiveEntry(any(ArchiveEntry.class));
        verify(out, times(3)).finish();
        verify(in, times(1)).close();
    }

    /**
     * Test perform method with add and delete changes
     */
    @Test
    public void testPerformAddAndDeleteChanges() throws IOException {
        ChangeSet changeSet = mock(ChangeSet.class);
        Set changes = new HashSet();

        Change addChange1 = mock(Change.class);
        when(addChange1.type()).thenReturn(Change.TYPE_ADD);
        when(addChange1.isReplaceMode()).thenReturn(false);
        when(addChange1.getInput()).thenReturn(new ByteArrayInputStream(
                new byte[0]));
        when(addChange1.getEntry()).thenReturn(mock(ArchiveEntry.class));
        changes.add(addChange1);

        Change addChange2 = mock(Change.class);
        when(addChange2.type()).thenReturn(Change.TYPE_ADD);
        when(addChange2.isReplaceMode()).thenReturn(false);
        when(addChange2.getInput()).thenReturn(new ByteArrayInputStream(
                new byte[0]));
        when(addChange2.getEntry()).thenReturn(mock(ArchiveEntry.class));
        changes.add(addChange2);

        Change deleteChange1 = mock(Change.class);
        when(deleteChange1.type()).thenReturn(Change.TYPE_DELETE);
        when(deleteChange1.targetFile()).thenReturn("delete1.txt");
        when(deleteChange1.getEntry()).thenReturn(mock(ArchiveEntry.class));
        changes.add(deleteChange1);

        Change deleteChange2 = mock(Change.class);
        when(deleteChange2.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(deleteChange2.targetFile()).thenReturn("deleteDir/");
        when(deleteChange2.getEntry()).thenReturn(mock(ArchiveEntry.class));
        changes.add(deleteChange2);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);

        performer.perform(in, out);

        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        ArchiveEntry entry2 = mock(ArchiveEntry.class);
        when(entry1.getName()).thenReturn("entry1.txt");
        when(entry2.getName()).thenReturn("entry2.txt");
        when(entry2.isDirectory()).thenReturn(false);

        when(in.getNextEntry()).thenReturn(entry1).thenReturn(entry2)
                .thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(out.getOutputStream(entry1)).thenReturn(outputStream)
                .thenReturn(outputStream)
                .thenReturn(outputStream);

        when(in.readAllBytes()).thenReturn(new byte[0]);

        ChangeSetResults result = performer.perform(in, out);

        assertEquals(2, result.addedFromChangeSet().size());
        assertEquals(2, result.deleted().size());
        verify(in, times(5)).getNextEntry();
        verify(out, times(3)).putArchiveEntry(entry1);
        verify(out, times(3)).putArchiveEntry(entry2);
        verify(out, times(3)).finish();
        verify(in, times(1)).close();
    }

    /**
     * Test isDeletedLater method
     */
    @Test
    public void testIsDeletedLater() {
        ChangeSetPerformer performer = new ChangeSetPerformer(null);

        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("entry.txt");

        Set changes = new HashSet();
        Change change1 = mock(Change.class);
        when(change1.type()).thenReturn(Change.TYPE_ADD);
        changes.add(change1);

        Change change2 = mock(Change.class);
        when(change2.type()).thenReturn(Change.TYPE_DELETE);
        when(change2.targetFile()).thenReturn("entry.txt");
        changes.add(change2);

        Set workingSet = new HashSet(changes);

        assertTrue(performer.isDeletedLater(workingSet, entry));
        assertFalse(performer.isDeletedLater(workingSet, null));
        assertFalse(performer.isDeletedLater(workingSet, mock(ArchiveEntry.class)));
    }

    /**
     * Test copyStream method
     */
    @Test
    public void testCopyStream() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(null);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveOutputStream outputStream = mock(ArchiveOutputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);

        performer.copyStream(inputStream, outputStream, entry);

        verify(outputStream, times(1)).putArchiveEntry(entry);
        verify(outputStream, times(1)).write(anyByteArray());
        verify(outputStream, times(1)).finish();
        verify(inputStream, times(1)).close();
    }
}