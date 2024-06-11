package org.apache.commons.compress.changes.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.utils.IOUtils;

public class ChangeSetPerformerTest {

    /**
     * Test method for {@link ChangeSetPerformer#ChangeSetPerformer(ChangeSet)}
     * @throws IOException
     */
    @org.junit.Test
    public void testChangeSetPerformer() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        Change addChange = new Change();
        addChange.setType(Change.TYPE_ADD);
        addChange.setTargetFile("testFile1.txt");
        ByteArrayOutputStream addStream = new ByteArrayOutputStream();
        addStream.write("Test file 1 content".getBytes());
        addChange.setInput(addStream.toInputStream());
        addChange.setReplaceMode(false);
        changeSet.addChange(addChange);

        Change addAndReplaceChange = new Change();
        addAndReplaceChange.setType(Change.TYPE_ADD);
        addAndReplaceChange.setTargetFile("testFile2.txt");
        ByteArrayOutputStream addAndReplaceStream = new ByteArrayOutputStream();
        addAndReplaceStream.write("Test file 2 content".getBytes());
        addAndReplaceChange.setInput(addAndReplaceStream.toInputStream());
        addAndReplaceChange.setReplaceMode(true);
        changeSet.addChange(addAndReplaceChange);

        Change deleteChange = new Change();
        deleteChange.setType(Change.TYPE_DELETE);
        deleteChange.setTargetFile("testFile1.txt");
        changeSet.addChange(deleteChange);

        Change deleteDirChange = new Change();
        deleteDirChange.setType(Change.TYPE_DELETE_DIR);
        deleteDirChange.setTargetFile("testDir");
        changeSet.addChange(deleteDirChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        assertNotNull(performer);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * @throws IOException
     * @throws ArchiveException
     */
    @org.junit.Test
    public void testPerform() throws IOException, ArchiveException {
        ChangeSet changeSet = new ChangeSet();

        Change addChange = new Change();
        addChange.setType(Change.TYPE_ADD);
        addChange.setTargetFile("testFile1.txt");
        ByteArrayOutputStream addStream = new ByteArrayOutputStream();
        addStream.write("Test file 1 content".getBytes());
        addChange.setInput(addStream.toInputStream());
        addChange.setReplaceMode(false);
        changeSet.addChange(addChange);

        Change addAndReplaceChange = new Change();
        addAndReplaceChange.setType(Change.TYPE_ADD);
        addAndReplaceChange.setTargetFile("testFile2.txt");
        ByteArrayOutputStream addAndReplaceStream = new ByteArrayOutputStream();
        addAndReplaceStream.write("Test file 2 content".getBytes());
        addAndReplaceChange.setInput(addAndReplaceStream.toInputStream());
        addAndReplaceChange.setReplaceMode(true);
        changeSet.addChange(addAndReplaceChange);

        Change deleteChange = new Change();
        deleteChange.setType(Change.TYPE_DELETE);
        deleteChange.setTargetFile("testFile1.txt");
        changeSet.addChange(deleteChange);

        Change deleteDirChange = new Change();
        deleteDirChange.setType(Change.TYPE_DELETE_DIR);
        deleteDirChange.setTargetFile("testDir");
        changeSet.addChange(deleteDirChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                ("testFile1.txt\n" +
                        "testFile2.txt/\n" +
                        "testDir/\n").getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ChangeSetResults results = performer.perform(new TarArchiveInputStream(inputStream), new TarArchiveOutputStream(outputStream));

        assertNotNull(results);

        assertTrue(results.size() == 2);
        assertTrue(results.deleted().size() == 1);
        assertTrue(results.addedFromChangeSet().size() == 1);
        assertTrue(results.addedFromStream().size() == 1);

        Set expectedDeleted = new HashSet(Arrays.asList("testFile1.txt"));
        Set expectedAddedFromChangeSet = new HashSet(Arrays.asList("testFile1.txt"));
        Set expectedAddedFromStream = new HashSet(Collections.singletonList("testFile2.txt"));

        assertFalse(results.deleted().isEmpty());
        assertTrue(results.deleted().equals(expectedDeleted));

        assertFalse(results.addedFromChangeSet().isEmpty());
        assertTrue(results.addedFromChangeSet().equals(expectedAddedFromChangeSet));

        assertFalse(results.addedFromStream().isEmpty());
        assertTrue(results.addedFromStream().equals(expectedAddedFromStream));
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * with replace mode
     * @throws IOException
     * @throws ArchiveException
     */
    @org.junit.Test
    public void testPerformReplaceMode() throws IOException, ArchiveException {
        ChangeSet changeSet = new ChangeSet();

        Change addChange = new Change();
        addChange.setType(Change.TYPE_ADD);
        addChange.setTargetFile("testFile1.txt");
        ByteArrayOutputStream addStream = new ByteArrayOutputStream();
        addStream.write("Test file 1 content".getBytes());
        addChange.setInput(addStream.toInputStream());
        addChange.setReplaceMode(true);
        changeSet.addChange(addChange);

        Change addAndReplaceChange = new Change();
        addAndReplaceChange.setType(Change.TYPE_ADD);
        addAndReplaceChange.setTargetFile("testFile2.txt");
        ByteArrayOutputStream addAndReplaceStream = new ByteArrayOutputStream();
        addAndReplaceStream.write("Test file 2 content".getBytes());
        addAndReplaceChange.setInput(addAndReplaceStream.toInputStream());
        addAndReplaceChange.setReplaceMode(true);
        changeSet.addChange(addAndReplaceChange);

        Change deleteChange = new Change();
        deleteChange.setType(Change.TYPE_DELETE);
        deleteChange.setTargetFile("testFile1.txt");
        changeSet.addChange(deleteChange);

        Change deleteDirChange = new Change();
        deleteDirChange.setType(Change.TYPE_DELETE_DIR);
        deleteDirChange.setTargetFile("testDir");
        changeSet.addChange(deleteDirChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                ("testFile1.txt\n" +
                        "testFile2.txt/\n" +
                        "testDir/\n").getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ChangeSetResults results = performer.perform(new TarArchiveInputStream(inputStream), new TarArchiveOutputStream(outputStream));

        assertNotNull(results);

        assertTrue(results.size() == 1);
        assertTrue(results.deleted().isEmpty());
        assertTrue(results.addedFromChangeSet().isEmpty());
        assertTrue(results.addedFromStream().size() == 1);

        Set expectedAddedFromStream = new HashSet(Collections.singletonList("testFile2.txt"));

        assertFalse(results.deleted().isEmpty());
        assertTrue(results.deleted().isEmpty());

        assertFalse(results.addedFromChangeSet().isEmpty());
        assertTrue(results.addedFromChangeSet().isEmpty());

        assertFalse(results.addedFromStream().isEmpty());
        assertTrue(results.addedFromStream().equals(expectedAddedFromStream));
    }

    /**
     * Test method for {@link ChangeSetPerformer#isDeletedLater(Set, ArchiveEntry)}
     */
    @org.junit.Test
    public void testIsDeletedLater() {
        Set<Change> changes = new HashSet<>();
        Change addChange = new Change();
        addChange.setType(Change.TYPE_ADD);
        addChange.setTargetFile("testFile1.txt");
        changes.add(addChange);

        Change deleteChange = new Change();
        deleteChange.setType(Change.TYPE_DELETE);
        deleteChange.setTargetFile("testFile1.txt");
        changes.add(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveEntry testEntry = new TarArchiveEntry("testFile1.txt");

        assertTrue(performer.isDeletedLater(changes, testEntry));
    }

    /**
     * Test method for {@link ChangeSetPerformer#isDeletedLater(Set, ArchiveEntry)}
     * with a directory
     */
    @org.junit.Test
    public void testIsDeletedLaterDirectory() {
        Set<Change> changes = new HashSet<>();
        Change deleteDirChange = new Change();
        deleteDirChange.setType(Change.TYPE_DELETE_DIR);
        deleteDirChange.setTargetFile("testDir");
        changes.add(deleteDirChange);

        Change addChange = new Change();
        addChange.setType(Change.TYPE_ADD);
        addChange.setTargetFile("testDir/testFile1.txt");
        changes.add(addChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveEntry testEntry = new TarArchiveEntry("testDir/testFile1.txt");

        assertTrue(performer.isDeletedLater(changes, testEntry));
    }

    /**
     * Test method for {@link ChangeSetPerformer#isDeletedLater(Set, ArchiveEntry)}
     * with no deletion later
     */
    @org.junit.Test
    public void testIsDeletedLaterNoDeletionLater() {
        Set<Change> changes = new HashSet<>();
        Change addChange = new Change();
        addChange.setType(Change.TYPE_ADD);
        addChange.setTargetFile("testFile1.txt");
        changes.add(addChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveEntry testEntry = new TarArchiveEntry("testFile2.txt");

        assertFalse(performer.isDeletedLater(changes, testEntry));
    }

    /**
     * Test method for {@link ChangeSetPerformer#copyStream(InputStream, ArchiveOutputStream, ArchiveEntry)}
     */
    @org.junit.Test
    public void testCopyStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                "Test file content".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ChangeSetPerformer performer = new ChangeSetPerformer(null);
        performer.copyStream(inputStream, new TarArchiveOutputStream(outputStream), new TarArchiveEntry("testFile.txt"));

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        byte[] expectedContent = "Test file content".getBytes();

        byte[] actualContent = IOUtils.toByteArray(testInputStream);

        assertNotNull(actualContent);
        assertArrayEquals(expectedContent, actualContent);
    }
}