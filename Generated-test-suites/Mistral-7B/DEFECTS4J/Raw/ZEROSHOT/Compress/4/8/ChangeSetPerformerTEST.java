package org.apache.commons.compress.changes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * Unit tests for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    @Test
    public void testPerform() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        ArchiveInputStream archiveInput = new TarInputStream(inputStream);

        Change addChange = new Change(Change.TYPE_ADD, new TarArchiveEntry("file1.txt"), null);
        Change replaceChange = new Change(Change.TYPE_ADD, new TarArchiveEntry("file1.txt"), "file1_original.txt");
        Change addReplaceChange = new Change(Change.TYPE_ADD, new TarArchiveEntry("file2.txt"), null);
        Change deleteChange = new Change(Change.TYPE_DELETE, null, "file1.txt");

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInput, new ByteArrayOutputStream(1024));

        ChangeSetResults results = performer.getResults();

        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals("file1.txt", results.getAddedFromChangeSet().get(0));
        assertFalse(results.hasBeenAdded("file1.txt"));

        // Perform again
        performer.perform(archiveInput, new ByteArrayOutputStream(1024));

        assertEquals(2, results.getAddedFromChangeSet().size());
        assertEquals("file1.txt", results.getAddedFromChangeSet().get(0));
        assertEquals("file2.txt", results.getAddedFromChangeSet().get(1));

        // Test replace change
        ByteArrayOutputStream replaceOutputStream = new ByteArrayOutputStream(1024);
        performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInput, replaceOutputStream);

        TarArchiveEntry replaceEntry = new TarArchiveEntry("file1.txt");
        InputStream replaceInputStream = new ByteArrayInputStream("Replaced content".getBytes());
        changeSet.addChange(new Change(Change.TYPE_ADD, replaceEntry, replaceInputStream));

        performer = new ChangeSetPerformer(changeSet);
        performer.perform(new ByteArrayInputStream("Original content".getBytes()), replaceOutputStream);

        ByteArrayOutputStream resultStream = performer.getResults().getStream();
        String resultContent = IOUtils.toString(resultStream, "UTF-8");
        assertEquals("Replaced content", resultContent);

        // Test delete change
        ByteArrayOutputStream deleteOutputStream = new ByteArrayOutputStream(1024);
        performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInput, deleteOutputStream);

        performer = new ChangeSetPerformer(changeSet);
        performer.perform(new ByteArrayInputStream(new byte[]{}), deleteOutputStream);

        assertNull(deleteOutputStream.toByteArray());

        // Test delete directory change
        ByteArrayOutputStream deleteDirOutputStream = new ByteArrayOutputStream(1024);
        performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInput, deleteDirOutputStream);

        performer = new ChangeSetPerformer(changeSet);
        performer.perform(new ByteArrayInputStream(new byte[]{'A', 'B', 'C'}), deleteDirOutputStream);

        assertNull(deleteDirOutputStream.toByteArray());

        // Test multiple changes of the same file
        ChangeSet changeSet2 = new ChangeSet();

        addChange = new Change(Change.TYPE_ADD, new TarArchiveEntry("file1.txt"), null);
        replaceChange = new Change(Change.TYPE_ADD, new TarArchiveEntry("file1.txt"), "file1_original.txt");

        changeSet2.addChange(addChange);
        changeSet2.addChange(replaceChange);

        performer = new ChangeSetPerformer(changeSet2);
        InputStream testInputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        ArchiveOutputStream testOutputStream = new ArchiveOutputStream(new ByteArrayOutputStream(1024),
                ArchiveStreamFactory.getTarOutputStream());

        performer.perform(testInputStream, testOutputStream);

        resultStream = performer.getResults().getStream();
        resultContent = IOUtils.toString(resultStream, "UTF-8");
        assertArrayEquals(new byte[]{'A', 'B', 'C'}, resultContent.getBytes());
        assertFalse(performer.getResults().hasBeenAdded("file1.txt"));
    }

    @Test
    public void testIsDeletedLater() {
        Set<Change> changes = new LinkedHashSet<>();
        ArchiveEntry entry = new TarArchiveEntry("file1.txt");

        Change change1 = new Change(Change.TYPE_ADD, null, null);
        Change change2 = new Change(Change.TYPE_DELETE, entry, null);
        Change change3 = new Change(Change.TYPE_DELETE_DIR, new TarArchiveEntry("dir1"), null);

        changes.add(change1);
        changes.add(change2);
        changes.add(change3);

        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));

        assertTrue(performer.isDeletedLater(Collections.singleton(entry), changes));
        assertFalse(performer.isDeletedLater(Collections.singleton(new TarArchiveEntry("file2.txt")), changes));
        assertFalse(performer.isDeletedLater(Collections.singleton(new TarArchiveEntry("file1.txt/file1.txt")), changes));
    }

    @Test
    public void testCopyStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Input content".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);

        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        performer.copyStream(input, output, null);

        ByteArrayOutputStream resultOutputStream = performer.getResults().getStream();
        String resultContent = IOUtils.toString(resultOutputStream, "UTF-8");
        assertEquals("Input content", resultContent);
    }

    @Test(expected = IOException.class)
    public void testCopyStreamWithNullInput() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);

        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        performer.copyStream(null, output, null);
    }

    @Test(expected = IOException.class)
    public void testCopyStreamWithNullOutput() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Input content".getBytes());

        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        performer.copyStream(input, null, null);
    }
}