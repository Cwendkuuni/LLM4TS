package org.apache.commons.compress.changes;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.test.TestUtils;
import org.apache.commons.compress.utils.test.archiver.TestArchiveEntry;
import org.apache.commons.compress.utils.test.archiver.TestArchiveInputStream;
import org.apache.commons.compress.utils.test.archiver.TestArchiveOutputStream;
import org.junit.Test;

import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    /**
     * Test method for {@link ChangeSetPerformer#ChangeSetPerformer(ChangeSet)}
     * @throws IOException
     */
    @Test
    public void testChangeSetPerformerConstruction() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        assertNotNull(performer);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * @throws IOException
     */
    @Test
    public void testPerformAddAndReplace() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        ArchiveEntry entry1 = new TestArchiveEntry("test1.txt");
        Change addChange1 = new Change(Change.TYPE_ADD, null, "test1.txt", null, null);
        Change addChange2 = new Change(Change.TYPE_ADD, null, "test2.txt", null, null);
        Change replaceChange1 = new Change(Change.TYPE_ADD, "test1.txt", "test1_orig.txt", null, null, true);

        changeSet.addChange(addChange1);
        changeSet.addChange(addChange2);
        changeSet.addChange(replaceChange1);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveOutputStream output = new TestArchiveOutputStream();

        ChangeSetResults result = performer.perform(new TestArchiveInputStream(input), output);

        assertEquals(3, result.size());
        assertEquals("test1.txt", result.getName(0));
        assertEquals("test1_orig.txt", result.getName(1));
        assertEquals("test2.txt", result.getName(2));

        TestArchiveEntry[] entries = output.getEntries();

        assertEquals(3, entries.length);
        TestArchiveEntry entry1Expected = new TestArchiveEntry("test1.txt");
        TestArchiveEntry entry2Expected = new TestArchiveEntry("test1_orig.txt");
        TestArchiveEntry entry3Expected = new TestArchiveEntry("test2.txt");

        assertArrayEquals(new TestArchiveEntry[] { entry1Expected, entry2Expected, entry3Expected }, entries);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * @throws IOException
     */
    @Test
    public void testPerformDelete() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        ArchiveEntry entry1 = new TestArchiveEntry("test1.txt");
        ArchiveEntry entry2 = new TestArchiveEntry("test2.txt");
        Change deleteChange1 = new Change(Change.TYPE_DELETE, null, "test1.txt", null, null);

        changeSet.addChange(deleteChange1);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new TestArchiveInputStream(new TestArchiveEntry[] { new TestArchiveEntry("test1.txt"), new TestArchiveEntry("test2.txt") });
        ArchiveOutputStream output = new TestArchiveOutputStream();

        ChangeSetResults result = performer.perform(input, output);

        assertEquals(1, result.size());
        assertEquals("test1.txt", result.getName(0));

        TestArchiveEntry[] entries = output.getEntries();

        assertEquals(1, entries.length);
        assertNull(entries[0]);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * @throws IOException
     */
    @Test
    public void testPerformDeleteDir() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        ArchiveEntry entry1 = new TestArchiveEntry("test1/test1.txt");
        ArchiveEntry entry2 = new TestArchiveEntry("test1/test2.txt");
        ArchiveEntry entry3 = new TestArchiveEntry("test2/test3.txt");
        Change deleteDirChange1 = new Change(Change.TYPE_DELETE_DIR, null, "test1", null, null);

        changeSet.addChange(deleteDirChange1);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new TestArchiveInputStream(new TestArchiveEntry[] { entry1, entry2, entry3 });
        ArchiveOutputStream output = new TestArchiveOutputStream();

        ChangeSetResults result = performer.perform(input, output);

        assertEquals(0, result.size());

        TestArchiveEntry[] entries = output.getEntries();

        assertEquals(0, entries.length);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * @throws IOException
     */
    @Test
    public void testPerformDoesNotModifyIfNoChanges() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveOutputStream output = new TestArchiveOutputStream();

        ChangeSetResults result = performer.perform(new TestArchiveInputStream(input), output);

        assertEquals(0, result.size());

        TestArchiveEntry[] entries = output.getEntries();

        assertEquals(0, entries.length);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testPerformWithNullInputs() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        performer.perform(null, new ArchiveOutputStream(new ByteArrayOutputStream()));
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testPerformWithNullOutput() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        performer.perform(new TestArchiveInputStream(new TestArchiveEntry[] {}), null);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * @throws IOException
     */
    @Test
    public void testPerformWithSameEntryAddedAndDeleted() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        ArchiveEntry entry1 = new TestArchiveEntry("test.txt");
        Change addChange1 = new Change(Change.TYPE_ADD, null, "test.txt", null, null);
        Change deleteChange1 = new Change(Change.TYPE_DELETE, null, "test.txt", null, null);

        changeSet.addChange(addChange1);
        changeSet.addChange(deleteChange1);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveOutputStream output = new TestArchiveOutputStream();

        performer.perform(new TestArchiveInputStream(input), output);

        TestArchiveEntry[] entries = output.getEntries();

        assertEquals(0, entries.length);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * @throws IOException
     */
    @Test
    public void testPerformWithSameEntryAddedAndAddedLater() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        ArchiveEntry entry1 = new TestArchiveEntry("test.txt");
        Change addChange1 = new Change(Change.TYPE_ADD, null, "test.txt", null, null);
        Change addChange2 = new Change(Change.TYPE_ADD, null, "test.txt", null, null);

        changeSet.addChange(addChange1);
        changeSet.addChange(addChange2);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveOutputStream output = new TestArchiveOutputStream();

        performer.perform(new TestArchiveInputStream(input), output);

        TestArchiveEntry[] entries = output.getEntries();

        assertEquals(1, entries.length);
        assertEquals(entry1, entries[0]);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * @throws IOException
     */
    @Test
    public void testPerformWithSameEntryAddedAndDeletedLater() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        ArchiveEntry entry1 = new TestArchiveEntry("test.txt");
        Change addChange1 = new Change(Change.TYPE_ADD, null, "test.txt", null, null);
        Change deleteChange1 = new Change(Change.TYPE_DELETE, null, "test.txt", null, null);

        changeSet.addChange(addChange1);
        changeSet.addChange(deleteChange1);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new TestArchiveInputStream(new TestArchiveEntry[] { entry1 });
        ArchiveOutputStream output = new TestArchiveOutputStream();

        performer.perform(input, output);

        TestArchiveEntry[] entries = output.getEntries();

        assertEquals(0, entries.length);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * @throws IOException
     */
    @Test
    public void testPerformWithSameEntryAddedAndAddedLaterWithReplace() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        ArchiveEntry entry1 = new TestArchiveEntry("test.txt");
        Change addChange1 = new Change(Change.TYPE_ADD, null, "test.txt", null, null);
        Change addChange2 = new Change(Change.TYPE_ADD, null, "test.txt", null, null, true);

        changeSet.addChange(addChange1);
        changeSet.addChange(addChange2);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveOutputStream output = new TestArchiveOutputStream();

        performer.perform(new TestArchiveInputStream(input), output);

        TestArchiveEntry[] entries = output.getEntries();

        assertEquals(1, entries.length);
        assertEquals(entry1, entries[0]);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * @throws IOException
     */
    @Test
    public void testPerformWithSameEntryDeletedAndAddedLater() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        ArchiveEntry entry1 = new TestArchiveEntry("test.txt");
        Change deleteChange1 = new Change(Change.TYPE_DELETE, null, "test.txt", null, null);
        Change addChange1 = new Change(Change.TYPE_ADD, null, "test.txt", null, null);

        changeSet.addChange(deleteChange1);
        changeSet.addChange(addChange1);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new TestArchiveInputStream(new TestArchiveEntry[] { entry1 });
        ArchiveOutputStream output = new TestArchiveOutputStream();

        performer.perform(input, output);

        TestArchiveEntry[] entries = output.getEntries();

        assertEquals(1, entries.length);
        assertEquals(entry1, entries[0]);
    }
}