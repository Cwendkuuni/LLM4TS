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
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for ChangeSetPerformer class
 */
public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private InputStream inputStream;
    private InputStream inputStreamWithChanges;
    private ArchiveInputStream archiveInputStream;
    private ArchiveOutputStream archiveOutputStream;

    @Before
    public void setUp() throws Exception {
        // Create a change set with some changes
        byte[] inputData = TestUtils.createTestData("testData.txt");
        Change changeAdd = new Change(Change.TYPE_ADD, null, TestUtils.createTestData("addedFile.txt"), null, false);
        Change changeAddRepl = new Change(Change.TYPE_ADD, null, TestUtils.createTestData("addedFileRepl.txt"), null, true);
        Change changeDelete = new Change(Change.TYPE_DELETE, null, null, "testData.txt", false);
        Change changeDeleteDir = new Change(Change.TYPE_DELETE_DIR, null, null, "deletedDir/", false);

        changeSet = new ChangeSet();
        changeSet.addChange(changeAdd);
        changeSet.addChange(changeAddRepl);
        changeSet.addChange(changeDelete);
        changeSet.addChange(changeDeleteDir);

        inputStream = new ByteArrayInputStream(inputData);
        archiveInputStream = new ArchiveInputStream(inputStream);
        archiveOutputStream = new ArchiveOutputStream(new ByteArrayOutputStream());
    }

    @Test
    public void testCreateChangeSetPerformer() {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        assertNotNull(performer);
    }

    @Test
    public void testPerformAdd() throws IOException {
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Check that added file has been added
        TarEntry addedEntry = new TarEntry("addedFile.txt");
        TarEntry addedEntryFromChangeSet = changeSet.getChange(Change.TYPE_ADD, "addedFile.txt").getEntry();
        assertNotNull(addedEntryFromChangeSet);
        assertEquals(addedEntry.getName(), addedEntryFromChangeSet.getName());
        assertArrayEquals(TestUtils.createTestData("addedFile.txt"), archiveOutputStream.getBuffer());
        assertTrue(results.hasBeenAdded("addedFile.txt"));

        // Check that added file with replace mode has been added
        TarEntry addedEntryRepl = new TarEntry("addedFileRepl.txt");
        TarEntry addedEntryFromChangeSetRepl = changeSet.getChange(Change.TYPE_ADD, "addedFileRepl.txt").getEntry();
        assertNotNull(addedEntryFromChangeSetRepl);
        assertEquals(addedEntryRepl.getName(), addedEntryFromChangeSetRepl.getName());
        assertArrayEquals(TestUtils.createTestData("addedFileRepl.txt"), archiveOutputStream.getBuffer());
        assertTrue(results.hasBeenAdded("addedFileRepl.txt"));
    }

    @Test
    public void testPerformDelete() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // Perform changes
        performer.perform(archiveInputStream, archiveOutputStream);

        // Check that testData.txt has been deleted
        TarEntry testDataEntry = new TarEntry("testData.txt");
        assertNull(archiveOutputStream.getNextEntry());
        assertFalse(archiveOutputStream.hasNextEntry());
        assertFalse(archiveOutputStream.hasRemaining());
        assertFalse(archiveOutputStream.isClosed());
        assertNull(archiveOutputStream.getBuffer());
        assertFalse(results.hasBeenAdded("testData.txt"));
        assertFalse(results.hasBeenDeleted("testData.txt"));
        assertFalse(results.hasBeenAddedFromChangeSet("testData.txt"));
        assertNull(changeSet.getChange(Change.TYPE_DELETE, "testData.txt"));
    }

    @Test
    public void testPerformDeleteDir() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // Perform changes
        performer.perform(archiveInputStream, archiveOutputStream);

        // Check that deletedDir/ has been deleted
        TarEntry deletedDirEntry = new TarEntry("deletedDir/");
        assertNull(archiveOutputStream.getNextEntry());
        assertFalse(archiveOutputStream.hasNextEntry());
        assertFalse(archiveOutputStream.hasRemaining());
        assertFalse(archiveOutputStream.isClosed());
        assertNull(archiveOutputStream.getBuffer());
        assertFalse(results.hasBeenAdded("deletedDir/"));
        assertFalse(results.hasBeenDeleted("deletedDir/"));
        assertFalse(results.hasBeenAddedFromChangeSet("deletedDir/"));
        assertNull(changeSet.getChange(Change.TYPE_DELETE_DIR, "deletedDir/"));
    }

    @Test
    public void testPerformAddAfterDelete() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // Perform delete
        performer.perform(archiveInputStream, archiveOutputStream);

        // Perform add
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Check that added file has been added
        TarEntry addedEntry = new TarEntry("addedFile.txt");
        TarEntry addedEntryFromChangeSet = changeSet.getChange(Change.TYPE_ADD, "addedFile.txt").getEntry();
        assertNotNull(addedEntryFromChangeSet);
        assertEquals(addedEntry.getName(), addedEntryFromChangeSet.getName());
        assertArrayEquals(TestUtils.createTestData("addedFile.txt"), archiveOutputStream.getBuffer());
        assertTrue(results.hasBeenAdded("addedFile.txt"));
    }

    @Test(expected = IOException.class)
    public void testPerformAddAfterDeleteDir() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // Perform delete dir
        performer.perform(archiveInputStream, archiveOutputStream);

        // Perform add
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Check that add fails with IOException
        TarEntry addedEntry = new TarEntry("addedFile.txt");
        TarEntry addedEntryFromChangeSet = changeSet.getChange(Change.TYPE_ADD, "addedFile.txt").getEntry();
        assertNotNull(addedEntryFromChangeSet);
        assertEquals(addedEntry.getName(), addedEntryFromChangeSet.getName());
        assertArrayEquals(TestUtils.createTestData("addedFile.txt"), archiveOutputStream.getBuffer());
        assertTrue(results.hasBeenAdded("addedFile.txt"));

        // Check that archiveInputStream has no more entries
        TarEntry testEntry = archiveInputStream.getNextEntry();
        assertNull(testEntry);
        assertFalse(archiveInputStream.hasNextEntry());
        assertFalse(archiveInputStream.hasRemaining());
        assertFalse(archiveInputStream.isClosed());
        assertNull(archiveInputStream.getBuffer());

        // Check that added file has not been added
        assertFalse(results.hasBeenAddedFromStream("addedFile.txt"));
    }

    @Test
    public void testPerformAddAfterAdd() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // Perform add
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Check that added file has been added
        TarEntry addedEntry = new TarEntry("addedFile.txt");
        TarEntry addedEntryFromChangeSet = changeSet.getChange(Change.TYPE_ADD, "addedFile.txt").getEntry();
        assertNotNull(addedEntryFromChangeSet);
        assertEquals(addedEntry.getName(), addedEntryFromChangeSet.getName());
        assertArrayEquals(TestUtils.createTestData("addedFile.txt"), archiveOutputStream.getBuffer());
        assertTrue(results.hasBeenAdded("addedFile.txt"));

        // Perform add again
        ChangeSetResults results2 = performer.perform(archiveInputStream, archiveOutputStream);

        // Check that added file has not been added again
        TarEntry addedEntry2 = new TarEntry("addedFile.txt");
        assertArrayEquals(TestUtils.createTestData("addedFile.txt"), archiveOutputStream.getBuffer());
        assertFalse(results2.hasBeenAdded("addedFile.txt"));
        assertFalse(results2.hasBeenAddedFromStream("addedFile.txt"));
    }

    @Test
    public void testPerformAddAfterDeleteAndAdd() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // Perform delete
        performer.perform(archiveInputStream, archiveOutputStream);

        // Perform add
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Check that added file has been added
        TarEntry addedEntry = new TarEntry("addedFile.txt");
        TarEntry addedEntryFromChangeSet = changeSet.getChange(Change.TYPE_ADD, "addedFile.txt").getEntry();
        assertNotNull(addedEntryFromChangeSet);
        assertEquals(addedEntry.getName(), addedEntryFromChangeSet.getName());
        assertArrayEquals(TestUtils.createTestData("addedFile.txt"), archiveOutputStream.getBuffer());
        assertTrue(results.hasBeenAdded("addedFile.txt"));

        // Perform add again
        ChangeSetResults results2 = performer.perform(archiveInputStream, archiveOutputStream);

        // Check that added file has been added again
        TarEntry addedEntry2 = new TarEntry("addedFile.txt");
        assertArrayEquals(TestUtils.createTestData("addedFile.txt"), archiveOutputStream.getBuffer());
        assertTrue(results2.hasBeenAdded("addedFile.txt"));
        assertTrue(results2.hasBeenAddedFromStream("addedFile.txt"));
    }

    @Test
    public void testPerformAddAfterAddAndDelete() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // Perform add
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Check that added file has been added
        TarEntry addedEntry = new TarEntry("addedFile.txt");
        TarEntry addedEntryFromChangeSet = changeSet.getChange(Change.TYPE_ADD, "addedFile.txt").getEntry();
        assertNotNull(addedEntryFromChangeSet);
        assertEquals(addedEntry.getName(), addedEntryFromChangeSet.getName());
        assertArrayEquals(TestUtils.createTestData("addedFile.txt"), archiveOutputStream.getBuffer());
        assertTrue(results.hasBeenAdded("addedFile.txt"));

        // Perform delete
        performer.perform(archiveInputStream, archiveOutputStream);

        // Perform add again
        ChangeSetResults results2 = performer.perform(archiveInputStream, archiveOutputStream);

        // Check that added file has not been added again
        TarEntry addedEntry2 = new TarEntry("addedFile.txt");
        assertArrayEquals(TestUtils.createTestData("addedFile.txt"), archiveOutputStream.getBuffer());
        assertFalse(results2.hasBeenAdded("addedFile.txt"));
        assertFalse(results2.hasBeenAddedFromStream("addedFile.txt"));
    }
}