package org.apache.commons.compress.changes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private InputStream inputStream;
    private ArchiveInputStream archiveInputStream;
    private ArchiveOutputStream archiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        changeSet = new ChangeSet();

        Change addChange1 = new Change(Change.TYPE_ADD, "file1.txt", null, null);
        Change addChange2 = new Change(Change.TYPE_ADD, "file2.txt", null, null);
        Change deleteChange1 = new Change(Change.TYPE_DELETE, "file1.txt", null, null);
        Change deleteChange2 = new Change(Change.TYPE_DELETE, "file2.txt", null, null);
        Change replaceChange1 = new Change(Change.TYPE_ADD, "file1.txt", "file1_orig.txt", null);

        changeSet.addChange(addChange1);
        changeSet.addChange(addChange2);
        changeSet.addChange(deleteChange1);
        changeSet.addChange(deleteChange2);
        changeSet.addChange(replaceChange1);

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = new ArchiveInputStream(inputStream);
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(archiveInputStream);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(archiveOutputStream);
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testPerform_EmptyChangeSet() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInputStream, archiveOutputStream);

        assertTrue(IOUtils.isEmpty(outputStream.toByteArray()));
    }

    @Test
    public void testPerform_NoChanges() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        performer.perform(archiveInputStream, archiveOutputStream);

        assertTrue(IOUtils.isEmpty(outputStream.toByteArray()));
    }

    @Test
    public void testPerform_AddFile() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInputStream, archiveOutputStream);

        ArchiveEntry entry = archiveOutputStream.getNextEntry();
        assertTrue(entry != null);
        assertEquals("file1.txt", entry.getName());

        InputStream inputStream = archiveOutputStream.getInputStream(entry);
        byte[] expectedData = new byte[5];
        inputStream.read(expectedData, 0, expectedData.length);
        assertArrayEquals(expectedData, new byte[5]);
    }

    @Test
    public void testPerform_AddFile_ReplaceMode() throws IOException {
        Change addChangeWithReplace = new Change(Change.TYPE_ADD, "file1.txt", "file1_orig.txt", null);
        changeSet.addChange(addChangeWithReplace);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInputStream, archiveOutputStream);

        ArchiveEntry entry = archiveOutputStream.getNextEntry();
        assertTrue(entry != null);
        assertEquals("file1.txt", entry.getName());

        InputStream inputStream = archiveOutputStream.getInputStream(entry);
        byte[] expectedData = new byte[10];
        inputStream.read(expectedData, 0, expectedData.length);
        assertArrayEquals(expectedData, new byte[10]);
    }

    @Test(expected = IOException.class)
    public void testPerform_SameFileAddedAndDeleted() throws IOException {
        Change addChange = new Change(Change.TYPE_ADD, "file1.txt", null, null);
        Change deleteChange = new Change(Change.TYPE_DELETE, "file1.txt", null, null);
        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInputStream, archiveOutputStream);

        // This test case should fail with an IOException as the file is both added and deleted
    }

    @Test
    public void testPerform_AddFile_DifferentPathAddedLater() throws IOException {
        Change addChange1 = new Change(Change.TYPE_ADD, "file1.txt", null, null);
        Change addChange2 = new Change(Change.TYPE_ADD, "dir/file2.txt", null, null);
        changeSet.addChange(addChange1);
        changeSet.addChange(addChange2);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInputStream, archiveOutputStream);

        ArchiveEntry entry1 = archiveOutputStream.getNextEntry();
        ArchiveEntry entry2 = archiveOutputStream.getNextEntry();

        assertTrue(entry1 != null);
        assertEquals("file1.txt", entry1.getName());

        assertTrue(entry2 != null);
        assertTrue(entry2.getName().startsWith("dir/file2.txt"));

        InputStream inputStream1 = archiveOutputStream.getInputStream(entry1);
        byte[] expectedData1 = new byte[5];
        inputStream1.read(expectedData1, 0, expectedData1.length);
        assertArrayEquals(expectedData1, new byte[5]);

        InputStream inputStream2 = archiveOutputStream.getInputStream(entry2);
        byte[] expectedData2 = new byte[10];
        inputStream2.read(expectedData2, 0, expectedData2.length);
        assertArrayEquals(expectedData2, new byte[10]);
    }

    @Test
    public void testPerform_DeleteFile() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInputStream, archiveOutputStream);

        ArchiveEntry entry = archiveOutputStream.getNextEntry();
        assertFalse(entry != null);
    }

    @Test
    public void testPerform_DeleteDir() throws IOException {
        Change addChange = new Change(Change.TYPE_ADD, "file1.txt", null, null);
        Change deleteDirChange = new Change(Change.TYPE_DELETE_DIR, "dir", null, null);
        changeSet.addChange(addChange);
        changeSet.addChange(deleteDirChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInputStream, archiveOutputStream);

        ArchiveEntry entry = archiveOutputStream.getNextEntry();
        assertFalse(entry != null);
    }

    @Test
    public void testPerform_NoDuplicateAdditions() throws IOException {
        Change addChange1 = new Change(Change.TYPE_ADD, "file1.txt", null, null);
        Change addChange2 = new Change(Change.TYPE_ADD, "file1.txt", null, null);
        changeSet.addChange(addChange1);
        changeSet.addChange(addChange2);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInputStream, archiveOutputStream);

        ArchiveEntry entry = archiveOutputStream.getNextEntry();
        assertTrue(entry != null);
        assertEquals("file1.txt", entry.getName());

        InputStream inputStream = archiveOutputStream.getInputStream(entry);
        byte[] expectedData = new byte[5];
        inputStream.read(expectedData, 0, expectedData.length);
        assertArrayEquals(expectedData, new byte[5]);

        // Only one file should be added
        assertTrue(archiveOutputStream.getNextEntry() == null);
    }

    @Test
    public void testPerform_NoDuplicateDeletions() throws IOException {
        Change deleteChange1 = new Change(Change.TYPE_DELETE, "file1.txt", null, null);
        Change deleteChange2 = new Change(Change.TYPE_DELETE, "file1.txt", null, null);
        changeSet.addChange(deleteChange1);
        changeSet.addChange(deleteChange2);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInputStream, archiveOutputStream);

        // No files should be deleted as they are both marked for deletion
        assertTrue(archiveInputStream.getNextEntry() != null);
    }
}