package org.apache.commons.compress.changes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import org.junit.Test;

/**
 * Test class for ChangeSetPerformer.
 */
public class ChangeSetPerformerTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        assertEquals(changeSet, performer.getChanges());
    }

    /**
     * Test perform method with empty ChangeSet.
     */
    @Test
    public void testPerformEmptyChangeSet() {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = new TarInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveOutputStream out = new TarOutputStream(new ByteArrayOutputStream());

        ChangeSetResults result = performer.perform(in, out);
        assertTrue(result.isEmpty());
    }

    /**
     * Test perform method with Add change.
     */
    @Test
    public void testPerformAddChange() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change("Add", new TarEntry("test.txt"), new ByteArrayInputStream(new byte[0]));
        changeSet.addChange(addChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = new TarInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveOutputStream out = new TarOutputStream(new ByteArrayOutputStream());

        ChangeSetResults result = performer.perform(in, out);
        assertEquals(1, result.size());
        assertTrue(result.containsKey("test.txt"));
        assertEquals(Change.TYPE_ADD, result.get(0).getType());
    }

    /**
     * Test perform method with Delete change.
     */
    @Test
    public void testPerformDeleteChange() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change deleteChange = new Change("Delete", new TarEntry("test.txt"), null);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = new TarInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveOutputStream out = new TarOutputStream(new ByteArrayOutputStream());

        ArchiveEntry entry = new TarEntry("test.txt");
        in.addEntry(entry);

        ChangeSetResults result = performer.perform(in, out);
        assertEquals(0, result.size());
        assertFalse(result.containsKey("test.txt"));
    }

    /**
     * Test perform method with Add and Delete changes.
     */
    @Test
    public void testPerformAddAndDeleteChanges() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change("Add", new TarEntry("test.txt"), new ByteArrayInputStream(new byte[0]));
        Change deleteChange = new Change("Delete", new TarEntry("test.txt"), null);
        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = new TarInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveOutputStream out = new TarOutputStream(new ByteArrayOutputStream());

        ArchiveEntry entry = new TarEntry("test.txt");
        in.addEntry(entry);

        ChangeSetResults result = performer.perform(in, out);
        assertEquals(0, result.size());
        assertFalse(result.containsKey("test.txt"));
    }

    /**
     * Test perform method with Add change and replacement.
     */
    @Test
    public void testPerformAddChangeWithReplacement() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change("Add", new TarEntry("test.txt"), new ByteArrayInputStream(new byte[0]));
        addChange.setReplaceMode(true);
        changeSet.addChange(addChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = new TarInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveOutputStream out = new TarOutputStream(new ByteArrayOutputStream());

        ArchiveEntry entry = new TarEntry("test.txt");
        in.addEntry(entry);

        InputStream input = new ByteArrayInputStream(new byte[0]);
        ChangeSetResults result = performer.perform(in, out);
        assertEquals(1, result.size());
        assertTrue(result.containsKey("test.txt"));
        assertEquals(Change.TYPE_ADD, result.get(0).getType());
    }

    /**
     * Test perform method with delete change and replacement.
     */
    @Test
    public void testPerformDeleteChangeWithReplacement() {
        ChangeSet changeSet = new ChangeSet();
        Change deleteChange = new Change("Delete", new TarEntry("test.txt"), null);
        deleteChange.setReplaceMode(true);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = new TarInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveOutputStream out = new TarOutputStream(new ByteArrayOutputStream());

        ArchiveEntry entry = new TarEntry("test.txt");
        in.addEntry(entry);

        assertThrows(IllegalStateException.class, () -> performer.perform(in, out));
    }

    /**
     * Test perform method with Delete change and same file name but different path.
     */
    @Test
    public void testPerformDeleteChangeWithDifferentPath() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change deleteChange = new Change("Delete", new TarEntry("test.txt"), null);
        Change addChange = new Change("Add", new TarEntry("test/test.txt"), new ByteArrayInputStream(new byte[0]));
        changeSet.addChange(deleteChange);
        changeSet.addChange(addChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = new TarInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveOutputStream out = new TarOutputStream(new ByteArrayOutputStream());

        ArchiveEntry entry = new TarEntry("test/test.txt");
        in.addEntry(entry);

        ChangeSetResults result = performer.perform(in, out);
        assertEquals(0, result.size());
        assertFalse(result.containsKey("test.txt"));
    }

    /**
     * Test perform method with Add change and different file name but same path.
     */
    @Test
    public void testPerformAddChangeWithDifferentFileName() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change("Add", new TarEntry("test.txt"), new ByteArrayInputStream(new byte[0]));
        Change addChange2 = new Change("Add", new TarEntry("test2.txt"), new ByteArrayInputStream(new byte[0]));
        changeSet.addChange(addChange);
        changeSet.addChange(addChange2);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = new TarInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveOutputStream out = new TarOutputStream(new ByteArrayOutputStream());

        ChangeSetResults result = performer.perform(in, out);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("test.txt"));
        assertTrue(result.containsKey("test2.txt"));
    }

    /**
     * Test isDeletedLater method with empty working set.
     */
    @Test
    public void testIsDeletedLaterEmptySet() {
        Set<Change> changes = new LinkedHashSet<>();
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveEntry entry = new TarEntry("test.txt");

        assertFalse(performer.isDeletedLater(changes, entry));
    }

    /**
     * Test isDeletedLater method with single delete change.
     */
    @Test
    public void testIsDeletedLaterSingleDeleteChange() {
        Set<Change> changes = new LinkedHashSet<>();
        Change deleteChange = new Change("Delete", new TarEntry("test.txt"), null);
        changes.add(deleteChange);
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveEntry entry = new TarEntry("test.txt");

        assertTrue(performer.isDeletedLater(changes, entry));
    }

    /**
     * Test isDeletedLater method with multiple add and delete changes.
     */
    @Test
    public void testIsDeletedLaterAddAndDeleteChanges() {
        Set<Change> changes = new LinkedHashSet<>();
        Change addChange = new Change("Add", new TarEntry("test.txt"), new ByteArrayInputStream(new byte[0]));
        Change deleteChange = new Change("Delete", new TarEntry("test.txt"), null);
        changes.add(addChange);
        changes.add(deleteChange);
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveEntry entry = new TarEntry("test.txt");

        assertTrue(performer.isDeletedLater(changes, entry));
    }

    /**
     * Test isDeletedLater method with multiple add changes.
     */
    @Test
    public void testIsDeletedLaterMultipleAddChanges() {
        Set<Change> changes = new LinkedHashSet<>();
        Change addChange = new Change("Add", new TarEntry("test.txt"), new ByteArrayInputStream(new byte[0]));
        Change addChange2 = new Change("Add", new TarEntry("test2.txt"), new ByteArrayInputStream(new byte[0]));
        changes.add(addChange);
        changes.add(addChange2);
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveEntry entry = new TarEntry("test.txt");

        assertFalse(performer.isDeletedLater(changes, entry));
    }

    /**
     * Test copyStream method.
     */
    @Test
    public void testCopyStream() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{'A'});

        performer.copyStream(input, new TarOutputStream(output), new TarEntry("test.txt"));

        byte[] expectedBytes = { 'A' };
        byte[] actualBytes = output.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test copyStream method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testCopyStreamNullInput() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        performer.copyStream(null, new TarOutputStream(output), new TarEntry("test.txt"));
    }

    /**
     * Test copyStream method with null output.
     */
    @Test(expected = NullPointerException.class)
    public void testCopyStreamNullOutput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        performer.copyStream(input, null, new TarEntry("test.txt"));
    }

    /**
     * Test copyStream method with null entry.
     */
    @Test(expected = NullPointerException.class)
    public void testCopyStreamNullEntry() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        performer.copyStream(input, new TarOutputStream(output), null);
    }

    /**
     * Test copyStream method with ArchiveException.
     */
    @Test(expected = ArchiveException.class)
    public void testCopyStreamArchiveException() throws IOException, ArchiveException {
        InputStream input = new TarInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveOutputStream output = new TarOutputStream(new ByteArrayOutputStream(), new TarFactory());
        output.setBlockSize(1024);

        performer.copyStream(input, output, new TarEntry("test.txt"));
        input.close();
        output.finish();

        input = new TarInputStream(new ByteArrayInputStream(new byte[0]));
        assertThrows(ArchiveException.class, () -> performer.copyStream(input, output, new TarEntry("test.txt")));
    }
}