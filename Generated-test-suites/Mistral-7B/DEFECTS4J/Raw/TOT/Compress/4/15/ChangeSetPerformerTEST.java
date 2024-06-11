package org.apache.commons.compress.changes.test;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ChangeSetPerformer} class.
 */
public class ChangeSetPerformerTest {
    private static final String ARCHIVE_NAME = "test.tar";
    private static final String FIRST_FILE = "file1.txt";
    private static final String SECOND_FILE = "file2.txt";
    private static final String DIRECTORY = "dir/";
    private static final String DIRECTORY_AND_FILE = DIRECTORY + FIRST_FILE;

    @Test
    public void testPerformer_EmptyChangeSet() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createTestArchive(Collections.emptySet());
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults results = performer.perform(in, out);

        Assert.assertEquals(Collections.emptySet(), results.getAddedFromChangeSet());
        Assert.assertEquals(Collections.emptySet(), results.getDeleted());
        Assert.assertEquals("", results.getAddedFromStream());
    }

    @Test
    public void testPerformer_SimpleAddition() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, FIRST_FILE, null));

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createTestArchive(Collections.singleton(FIRST_FILE));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults results = performer.perform(in, out);

        Set added = results.getAddedFromChangeSet();
        assertEquals(1, added.size());
        assertTrue(added.contains(new ArchiveEntry(FIRST_FILE)));

        assertEquals(Collections.emptySet(), results.getDeleted());
        assertEquals("", results.getAddedFromStream());

        ArchiveInputStream testIn = new ArchiveInputStream(new ByteArrayInputStream(out.toString().getBytes()));
        ArchiveEntry entry = testIn.getNextEntry();
        assertEquals(FIRST_FILE, entry.getName());
    }

    @Test
    public void testPerformer_SimpleDeletion() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_DELETE, FIRST_FILE, null));

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createTestArchive(Collections.singleton(FIRST_FILE));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults results = performer.perform(in, out);

        Set added = results.getAddedFromChangeSet();
        assertEquals(0, added.size());

        Set deleted = results.getDeleted();
        assertEquals(1, deleted.size());
        assertTrue(deleted.contains(new ArchiveEntry(FIRST_FILE)));

        assertEquals("", results.getAddedFromStream());

        ArchiveInputStream testIn = new ArchiveInputStream(new ByteArrayInputStream(out.toString().getBytes()));
        ArchiveEntry entry = testIn.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testPerformer_SimpleAdditionAndDeletion() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, FIRST_FILE, null));
        changeSet.addChange(new Change(Change.TYPE_DELETE, FIRST_File, null));

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createTestArchive(Collections.singleton(FIRST_FILE));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults results = performer.perform(in, out);

        Set added = results.getAddedFromChangeSet();
        assertEquals(0, added.size());

        Set deleted = results.getDeleted();
        assertEquals(1, deleted.size());
        assertTrue(deleted.contains(new ArchiveEntry(FIRST_FILE)));

        assertEquals("", results.getAddedFromStream());

        ArchiveInputStream testIn = new ArchiveInputStream(new ByteArrayInputStream(out.toString().getBytes()));
        ArchiveEntry entry = testIn.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testPerformer_SimpleAddition_ReplaceMode() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, FIRST_FILE, null, true));

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createTestArchive(Collections.singleton(FIRST_FILE));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());

        ArchiveEntry entry = new ArchiveEntry(FIRST_FILE);
        InputStream input = new ByteArrayInputStream(TestUtils.getFilesResourceAsBytes(FIRST_FILE));

        performer.perform(in, out, entry, input);

        Set added = performer.getAddedFromChangeSet();
        assertEquals(1, added.size());
        assertTrue(added.contains(new ArchiveEntry(FIRST_FILE)));

        Set deleted = performer.getDeleted();
        assertEquals(0, deleted.size());

        assertEquals(FIRST_FILE, performer.getAddedFromStream());

        ArchiveInputStream testIn = new ArchiveInputStream(new ByteArrayInputStream(out.toString().getBytes()));
        ArchiveEntry testEntry = testIn.getNextEntry();
        assertEquals(FIRST_FILE, testEntry.getName());
    }

    @Test
    public void testPerformer_SimpleAdditionAndReplaceMode() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, FIRST_FILE, null));
        changeSet.addChange(new Change(Change.TYPE_ADD, FIRST_FILE, null, true));

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createTestArchive(Collections.singleton(FIRST_FILE));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());

        ArchiveEntry entry = new ArchiveEntry(FIRST_FILE);
        InputStream input = new ByteArrayInputStream(TestUtils.getFilesResourceAsBytes(FIRST_FILE));

        performer.perform(in, out, entry, input);

        Set added = performer.getAddedFromChangeSet();
        assertEquals(1, added.size());
        assertTrue(added.contains(new ArchiveEntry(FIRST_FILE)));

        Set deleted = performer.getDeleted();
        assertEquals(0, deleted.size());

        assertEquals(FIRST_FILE, performer.getAddedFromStream());

        ArchiveInputStream testIn = new ArchiveInputStream(new ByteArrayInputStream(out.toString().getBytes()));
        ArchiveEntry testEntry = testIn.getNextEntry();
        assertEquals(FIRST_FILE, testEntry.getName());
    }

    @Test
    public void testPerformer_SimpleAdditionAndDeletionInSameDirectory() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, DIRECTORY_AND_FILE, null));
        changeSet.addChange(new Change(Change.TYPE_DELETE, FIRST_FILE, null));

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createTestArchive(Arrays.asList(FIRST_FILE, DIRECTORY_AND_FILE));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults results = performer.perform(in, out);

        Set added = results.getAddedFromChangeSet();
        assertEquals(1, added.size());
        assertTrue(added.contains(new ArchiveEntry(DIRECTORY_AND_FILE)));

        Set deleted = results.getDeleted();
        assertEquals(1, deleted.size());
        assertTrue(deleted.contains(new ArchiveEntry(FIRST_FILE)));

        assertEquals("", results.getAddedFromStream());

        ArchiveInputStream testIn = new ArchiveInputStream(new ByteArrayInputStream(out.toString().getBytes()));
        ArchiveEntry dirEntry = testIn.getNextEntry();
        ArchiveEntry fileEntry = testIn.getNextEntry();
        assertNotNull(dirEntry);
        assertNull(fileEntry);
    }

    @Test
    public void testPerformer_SimpleAdditionAndDeletionInSameDirectoryReplaceMode() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, DIRECTORY_AND_FILE, null, true));
        changeSet.addChange(new Change(Change.TYPE_DELETE, FIRST_FILE, null));

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createTestArchive(Arrays.asList(FIRST_FILE, DIRECTORY_AND_FILE));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());

        ArchiveEntry entry = new ArchiveEntry(FIRST_FILE);
        InputStream input = new ByteArrayInputStream(TestUtils.getFilesResourceAsBytes(FIRST_FILE));

        performer.perform(in, out, entry, input);

        Set added = performer.getAddedFromChangeSet();
        assertEquals(1, added.size());
        assertTrue(added.contains(new ArchiveEntry(DIRECTORY_AND_FILE)));

        Set deleted = performer.getDeleted();
        assertEquals(1, deleted.size());
        assertTrue(deleted.contains(new ArchiveEntry(FIRST_FILE)));

        assertEquals(FIRST_FILE, performer.getAddedFromStream());

        ArchiveInputStream testIn = new ArchiveInputStream(new ByteArrayInputStream(out.toString().getBytes()));
        ArchiveEntry testEntry = testIn.getNextEntry();
        assertEquals(FIRST_FILE, testEntry.getName());
    }

    @Test
    public void testPerformer_SimpleAdditionAndDeletionInDifferentDirectories() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, DIRECTORY + SECOND_FILE, null));
        changeSet.addChange(new Change(Change.TYPE_DELETE, DIRECTORY + FIRST_FILE, null));

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createTestArchive(Arrays.asList(FIRST_FILE, SECOND_FILE));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults results = performer.perform(in, out);

        Set added = results.getAddedFromChangeSet();
        assertEquals(1, added.size());
        assertTrue(added.contains(new ArchiveEntry(SECOND_FILE)));

        Set deleted = results.getDeleted();
        assertEquals(1, deleted.size());
        assertTrue(deleted.contains(new ArchiveEntry(FIRST_FILE)));

        assertEquals("", results.getAddedFromStream());

        ArchiveInputStream testIn = new ArchiveInputStream(new ByteArrayInputStream(out.toString().getBytes()));
        ArchiveEntry dirEntry1 = testIn.getNextEntry();
        ArchiveEntry dirEntry2 = testIn.getNextEntry();
        ArchiveEntry fileEntry1 = testIn.getNextEntry();
        ArchiveEntry fileEntry2 = testIn.getNextEntry();
        assertNotNull(dirEntry1);
        assertNull(fileEntry1);
        assertNotNull(dirEntry2);
        assertNull(fileEntry2);
    }

    @Test
    public void testPerformer_SimpleAdditionAndDeletionInDifferentDirectoriesReplaceMode() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, DIRECTORY + SECOND_FILE, null, true));
        changeSet.addChange(new Change(Change.TYPE_DELETE, DIRECTORY + FIRST_FILE, null));

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createTestArchive(Arrays.asList(FIRST_FILE, SECOND_FILE));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());

        ArchiveEntry entry = new ArchiveEntry(FIRST_FILE);
        InputStream input = new ByteArrayInputStream(TestUtils.getFilesResourceAsBytes(FIRST_FILE));

        performer.perform(in, out, entry, input);

        Set added = performer.getAddedFromChangeSet();
        assertEquals(1, added.size());
        assertTrue(added.contains(new ArchiveEntry(SECOND_FILE)));

        Set deleted = performer.getDeleted();
        assertEquals(1, deleted.size());
        assertTrue(deleted.contains(new ArchiveEntry(FIRST_FILE)));

        assertEquals(FIRST_FILE, performer.getAddedFromStream());

        ArchiveInputStream testIn = new ArchiveInputStream(new ByteArrayInputStream(out.toString().getBytes()));
        ArchiveEntry testEntry = testIn.getNextEntry();
        assertEquals(FIRST_FILE, testEntry.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testPerformer_AttemptToPerformMoreThanOnce() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createTestArchive(Collections.singleton(FIRST_FILE));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());

        performer.perform(in, out);
        performer.perform(in, out);
    }

    private ArchiveInputStream createTestArchive(Collection<String> files) throws IOException {
        ArchiveOutputStream out = new ArchiveOutputStream(new FileOutputStream(ARCHIVE_NAME));

        for (String fileName : files) {
            InputStream input = new FileInputStream(TestUtils.getTestDataFile(fileName));
            ArchiveEntry entry = new ArchiveEntry(fileName);
            out.putArchiveEntry(entry);
            IOUtils.copy(input, out);
            out.closeArchiveEntry();
        }

        out.finish();

        return new ArchiveInputStream(new FileInputStream(ARCHIVE_NAME));
    }
}