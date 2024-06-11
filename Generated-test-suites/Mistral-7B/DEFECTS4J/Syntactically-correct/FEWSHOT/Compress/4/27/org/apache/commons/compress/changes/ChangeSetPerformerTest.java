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
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveRecord;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private Set changes;
    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveEntry entry;
    private InputStream inputStream;
    private ArchiveOutputStream outputStream;
    private ChangeSetResults results;

    @Before
    public void setUp() {
        changes = new HashSet();
        Change addChange = new Change(Change.TYPE_ADD, null, "test.txt", null, null);
        Change addReplaceChange = new Change(Change.TYPE_ADD, true, null, "test.txt", null);
        Change deleteChange = new Change(Change.TYPE_DELETE, null, "test.txt", null, null);
        Change deleteDirChange = new Change(Change.TYPE_DELETE_DIR, null, "test/", null, null);

        changes.add(addChange);
        changes.add(addReplaceChange);
        changes.add(deleteChange);
        changes.add(deleteDirChange);

        changeSet = new ChangeSet("test", changes);
        performer = new ChangeSetPerformer(changeSet);

        entry = mock(ArchiveEntry.class);
        inputStream = new ByteArrayInputStream(new byte[1024]);
        outputStream = mock(ArchiveOutputStream.class);
        results = mock(ChangeSetResults.class);
    }

    @Test
    public void testPerformShouldReturnResults() {
        assertEquals(results, performer.perform(mock(ArchiveInputStream.class), outputStream));
    }

    @Test
    public void testPerformShouldAddFilesWithReplaceMode() {
        when(outputStream.putNextEntry(entry)).thenReturn(true);
        when(entry.getName()).thenReturn("test.txt");

        performer.perform(mock(ArchiveInputStream.class), outputStream);

        assertEquals(1, changes.size());
        assertFalse(changes.contains(addChange));
        assertTrue(changes.contains(addReplaceChange));
        assertTrue(results.hasBeenAdded("test.txt"));
    }

    @Test
    public void testPerformShouldDeleteFiles() {
        when(inputStream.readAllBytes()).thenReturn(new byte[1024]);
        when(entry.getName()).thenReturn("test.txt");

        performer.perform(new TarInputStream(inputStream), outputStream);

        assertEquals(2, changes.size());
        assertFalse(changes.contains(addChange));
        assertFalse(changes.contains(addReplaceChange));
        assertTrue(results.hasBeenDeleted("test.txt"));
    }

    @Test
    public void testPerformShouldDeleteDirectories() {
        String directoryToDelete = "test/";
        ArchiveEntry directoryEntry = mock(ArchiveEntry.class);
        when(directoryEntry.getName()).thenReturn(directoryToDelete);

        performer.perform(mock(ArchiveInputStream.class), outputStream);

        assertEquals(3, changes.size());
        assertFalse(changes.contains(addChange));
        assertFalse(changes.contains(addReplaceChange));
        assertTrue(results.hasBeenDeleted(directoryToDelete));
    }

    @Test
    public void testPerformShouldAddFilesWithoutReplaceMode() {
        Change addChangeWithoutReplace = new Change(Change.TYPE_ADD, null, "test.txt", null, null);
        changes.clear();
        changes.add(addChangeWithoutReplace);
        changeSet = new ChangeSet("test", changes);
        performer = new ChangeSetPerformer(changeSet);

        when(outputStream.putNextEntry(entry)).thenReturn(true);
        when(entry.getName()).thenReturn("test.txt");

        performer.perform(mock(ArchiveInputStream.class), outputStream);

        assertEquals(1, changes.size());
        assertTrue(changes.contains(addChangeWithoutReplace));
        assertTrue(results.hasBeenAdded("test.txt"));
    }

    @Test
    public void testIsDeletedLaterShouldReturnFalse() {
        when(inputStream.readAllBytes()).thenReturn(new byte[1024]);
        when(entry.getName()).thenReturn("test.txt");

        assertFalse(performer.isDeletedLater(changes, entry));
    }

    @Test
    public void testIsDeletedLaterShouldReturnTrue() {
        Change deleteChangeLater = new Change(Change.TYPE_DELETE, null, "test.txt", null, null);
        changes.clear();
        changes.add(deleteChangeLater);
        changes.add(addChange);

        assertFalse(performer.isDeletedLater(changes, entry));
        assertFalse(performer.isDeletedLater(changes, entry));

        changes.add(deleteChange);

        assertTrue(performer.isDeletedLater(changes, entry));
    }

    @Test
    public void testCopyStreamShouldCopyEntries() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        performer.copyStream(inputStream, outputStream, entry);

        ArchiveRecord archiveRecord = new TarEntry("test.txt");
        byte[] expected = new byte[1024];

        TarInputStream tarInputStream = new TarInputStream(new ByteArrayInputStream(expected));
        ArchiveEntry expectedEntry = tarInputStream.getNextEntry();

        assertEquals(expectedEntry.getSize(), outputStream.size());
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testCopyStreamShouldThrowException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);

        performer.copyStream(inputStream, null, entry);
    }

    @Test
    public void testCopyStreamShouldReturnResults() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ChangeSetResults results = performer.copyStream(inputStream, outputStream, entry);

        assertSame(results, performer.perform(mock(ArchiveInputStream.class), outputStream));
    }
}