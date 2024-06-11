package org.apache.commons.compress.changes.test;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Test the ChangeSetPerformer functionality.
 */
public class ChangeSetPerformerTest {
    private ChangeSet changeSet;
    private InputStream inputStream;
    private OutputStream outputStream;
    private File tempFile;

    @Before
    public void setUp() throws Exception {
        Change addChange = new Change("add.txt", Change.TYPE_ADD, null, null, null, false);
        Change deleteChange = new Change("delete.txt", Change.TYPE_DELETE, null, null, null, false);
        Change addReplaceChange = new Change("addReplace.txt", Change.TYPE_ADD, "addReplace.txt", null, null, true);
        Change deleteReplaceDirChange = new Change("deleteReplaceDir/", Change.TYPE_DELETE_DIR, null, null, null, false);

        changeSet = new ChangeSet();
        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);
        changeSet.addChange(addReplaceChange);
        changeSet.addChange(deleteReplaceDirChange);
    }

    @Test
    public void testPerformAdd() throws Exception {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        InputStream testInputStream = new ByteArrayInputStream("Test data".getBytes());
        File tempArchive = File.createTempFile("test", null);
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempArchive);
        ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream(tempFileOutputStream);

        ChangeSetResults results = performer.perform(new ArchiveInputStream(new FileInputStream(tempArchive)), archiveOutputStream);

        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getAddedFromStream().size());
        assertEquals("add.txt", results.getAddedFromChangeSet().get(0));
        assertEquals("add.txt", results.getAddedFromStream().get(0));

        assertNull(results.getDeleted());
        assertFalse(results.hasBeenAdded("addReplace.txt"));

        ArchiveEntry entry = archiveOutputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("add.txt", entry.getName());

        IOUtils.closeQuietly(testInputStream);
        IOUtils.closeQuietly(archiveOutputStream);
        IOUtils.closeQuietly(tempFileOutputStream);
        tempFile.delete();
    }

    @Test
    public void testPerformAddReplace() throws Exception {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        InputStream testInputStream = new ByteArrayInputStream("Test data".getBytes());
        File tempArchive = File.createTempFile("test", null);
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempArchive);
        ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream(tempFileOutputStream);

        ChangeSetResults results = performer.perform(new ArchiveInputStream(new FileInputStream(tempArchive)), archiveOutputStream);

        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getAddedFromStream().size());
        assertEquals("addReplace.txt", results.getAddedFromChangeSet().get(0));
        assertEquals("addReplace.txt", results.getAddedFromStream().get(0));

        assertNull(results.getDeleted());
        assertFalse(results.hasBeenAdded("add.txt"));

        ArchiveEntry entry = archiveOutputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("addReplace.txt", entry.getName());

        IOUtils.closeQuietly(testInputStream);
        IOUtils.closeQuietly(archiveOutputStream);
        IOUtils.closeQuietly(tempFileOutputStream);
        tempFile.delete();
    }

    @Test
    public void testPerformDelete() throws Exception {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        File tempArchive = File.createTempFile("test", null);
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempArchive);
        ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream(tempFileOutputStream);

        // Add file to archive
        archiveOutputStream.putNextEntry(new File("src/test/resources/testFile.txt").toPath().toFile());
        IOUtils.write("Test data".getBytes(), new FileOutputStream("src/test/resources/testFile.txt"));

        ChangeSetResults results = performer.perform(new ArchiveInputStream(new FileInputStream(tempArchive)), archiveOutputStream);

        assertEquals(0, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getAddedFromStream().size());
        assertEquals(1, results.getDeleted().size());
        assertEquals("testFile.txt", results.getDeleted().get(0));

        assertFalse(results.hasBeenAdded("delete.txt"));
        assertFalse(results.hasBeenAdded("add.txt"));

        // Check that file was deleted
        ArchiveEntry entry = archiveOutputStream.getNextEntry();
        assertNull(entry);

        IOUtils.closeQuietly(archiveOutputStream);
        IOUtils.closeQuietly(tempFileOutputStream);
        tempFile.delete();
    }

    @Test
    public void testPerformDeleteReplaceDir() throws Exception {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        File tempArchive = File.createTempFile("test", null);
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempArchive);
        ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream(tempFileOutputStream);

        File testDirectory = new File("src/test/resources/testDirectory");
        testDirectory.mkdirs();
        testDirectory.mkdirs();
        testDirectory.mkdir();

        IOUtils.write("Test data1".getBytes(), new FileOutputStream(new File(testDirectory, "file1.txt")));
        IOUtils.write("Test data2".getBytes(), new FileOutputStream(new File(testDirectory, "file2.txt")));

        ChangeSetResults results = performer.perform(new ArchiveInputStream(new FileInputStream(tempArchive)), archiveOutputStream);

        assertEquals(0, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getAddedFromStream().size());
        assertEquals(1, results.getDeleted().size());
        assertEquals("testDirectory/", results.getDeleted().get(0));

        assertFalse(results.hasBeenAdded("deleteReplaceDir/delete.txt"));
        assertFalse(results.hasBeenAdded("add.txt"));

        // Check that directory was deleted
        ArchiveEntry entry = archiveOutputStream.getNextEntry();
        assertNull(entry);

        IOUtils.closeQuietly(archiveOutputStream);
        IOUtils.closeQuietly(tempFileOutputStream);
        tempFile.delete();

        // Check that directory and all files were deleted
        assertFalse(new File(testDirectory, "file1.txt").exists());
        assertFalse(new File(testDirectory, "file2.txt").exists());
        assertFalse(testDirectory.exists());
    }

    @Test
    public void testPerformErrorStream() throws Exception {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        InputStream testInputStream = new ByteArrayInputStream("Test data".getBytes());
        OutputStream testOutputStream = new ByteArrayOutputStream();

        try {
            performer.perform(new ArchiveInputStream(new FileInputStream(new File("notExistingFile.txt"))), (ArchiveOutputStream) testOutputStream);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        assertEquals(0, changeSet.getChanges().size());
        assertEquals(0, testOutputStream.size());
    }

    @Test
    public void testPerformErrorChangeSet() throws Exception {
        ChangeSetPerformer performer = new ChangeSetPerformer(null);

        try {
            performer.perform(new ArchiveInputStream(new FileInputStream(new File("src/test/resources/testFile.txt"))), new ArchiveOutputStream(new FileOutputStream("test.tar.gz")));
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}