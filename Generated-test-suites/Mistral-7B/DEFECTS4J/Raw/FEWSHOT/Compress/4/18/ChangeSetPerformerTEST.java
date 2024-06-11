package org.apache.commons.compress.changes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
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
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final String TEST_DIR_NAME = "testDir/";
    private static final byte[] TEST_DATA = "Test Data".getBytes();

    @Test
    public void testChangeSetPerformerAddAndDelete() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change(Change.TYPE_ADD, TEST_FILE_NAME, false, null);
        Change deleteChange = new Change(Change.TYPE_DELETE, TEST_FILE_NAME, false, null);
        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new ByteArrayInputStream(TEST_DATA);
        ArchiveInputStream archiveInput = new TarArchiveInputStream(new ByteArrayInputStream(TestUtils.createTarFileData(TEST_FILE_NAME, TEST_DATA)));
        ArchiveOutputStream archiveOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput, input);

        //Check that the file was deleted
        ArchiveEntry deletedEntry = archiveInput.getNextEntry();
        assertNull(deletedEntry);

        //Check that the file was added
        ArchiveEntry addedEntry = archiveOutput.getNextArchiveEntry();
        assertNotNull(addedEntry);
        assertEquals(TEST_FILE_NAME, addedEntry.getName());
        byte[] resultData = IOUtils.toByteArray(addedEntry.getInputStream());
        assertArrayEquals(TEST_DATA, resultData);

        //Check that the results match the ChangeSet
        Set changes = performer.getChanges();
        Set expectedChanges = new LinkedHashSet(Arrays.asList(addChange, deleteChange));
        assertEquals(expectedChanges, changes);
    }

    @Test
    public void testChangeSetPerformerAddAndAdd() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        Change addChange1 = new Change(Change.TYPE_ADD, TEST_FILE_NAME, false, null);
        Change addChange2 = new Change(Change.TYPE_ADD, TEST_DIR_NAME + TEST_FILE_NAME, false, null);
        changeSet.addChange(addChange1);
        changeSet.addChange(addChange2);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new ByteArrayInputStream(TEST_DATA);
        ArchiveInputStream archiveInput = new TarArchiveInputStream(new ByteArrayInputStream(TestUtils.createTarFileData(TEST_FILE_NAME, TEST_DATA)));
        ArchiveOutputStream archiveOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput, input);

        //Check that both files were added
        Set changes = performer.getChanges();
        Set expectedChanges = new LinkedHashSet(Arrays.asList(addChange1, addChange2));
        assertEquals(expectedChanges, changes);

        //Check that the files were added in the correct order
        Iterator it = changes.iterator();
        Change change1 = (Change) it.next();
        Change change2 = (Change) it.next();
        assertTrue(TEST_FILE_NAME.equals(change1.getTargetFile()));
        assertTrue(TEST_DIR_NAME + TEST_FILE_NAME.equals(change2.getTargetFile()));
    }

    @Test
    public void testChangeSetPerformerDeleteAndAdd() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change(Change.TYPE_ADD, TEST_FILE_NAME, false, null);
        Change deleteChange = new Change(Change.TYPE_DELETE, TEST_FILE_NAME, false, null);
        changeSet.addChange(deleteChange);
        changeSet.addChange(addChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new ByteArrayInputStream(TEST_DATA);
        ArchiveInputStream archiveInput = new TarArchiveInputStream(new ByteArrayInputStream(TestUtils.createTarFileData(TEST_FILE_NAME, TEST_DATA)));
        ArchiveOutputStream archiveOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput, input);

        //Check that the file was deleted and then added
        ArchiveEntry deletedEntry = archiveInput.getNextEntry();
        assertNull(deletedEntry);

        ArchiveEntry addedEntry = archiveOutput.getNextArchiveEntry();
        assertNotNull(addedEntry);
        assertEquals(TEST_FILE_NAME, addedEntry.getName());
        byte[] resultData = IOUtils.toByteArray(addedEntry.getInputStream());
        assertArrayEquals(TEST_DATA, resultData);

        //Check that the results match the ChangeSet
        Set changes = performer.getChanges();
        Set expectedChanges = new LinkedHashSet(Arrays.asList(addChange, deleteChange));
        assertEquals(expectedChanges, changes);
    }

    @Test
    public void testChangeSetPerformerDeleteAndAddWithReplaceMode() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change(Change.TYPE_ADD, TEST_FILE_NAME, true, null);
        Change deleteChange = new Change(Change.TYPE_DELETE, TEST_FILE_NAME, false, null);
        changeSet.addChange(deleteChange);
        changeSet.addChange(addChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new ByteArrayInputStream(TEST_DATA);
        ArchiveInputStream archiveInput = new TarArchiveInputStream(new ByteArrayInputStream(TestUtils.createTarFileData(TEST_FILE_NAME, TEST_DATA)));
        ArchiveOutputStream archiveOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput, input);

        //Check that the file was deleted and then replaced
        ArchiveEntry deletedEntry = archiveInput.getNextEntry();
        assertNull(deletedEntry);

        ArchiveEntry addedEntry = archiveOutput.getNextArchiveEntry();
        assertNotNull(addedEntry);
        assertEquals(TEST_FILE_NAME, addedEntry.getName());
        byte[] resultData = IOUtils.toByteArray(addedEntry.getInputStream());
        assertArrayEquals(TEST_DATA, resultData);

        //Check that the results match the ChangeSet
        Set changes = performer.getChanges();
        Set expectedChanges = new LinkedHashSet(Arrays.asList(addChange, deleteChange));
        assertEquals(expectedChanges, changes);
    }

    @Test
    public void testChangeSetPerformerDeleteAndAddWithSameNameButDifferentPaths() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        Change addChange1 = new Change(Change.TYPE_ADD, TEST_DIR_NAME + TEST_FILE_NAME, false, null);
        Change addChange2 = new Change(Change.TYPE_ADD, TEST_FILE_NAME, false, null);
        Change deleteChange = new Change(Change.TYPE_DELETE, TEST_FILE_NAME, false, null);
        changeSet.addChange(deleteChange);
        changeSet.addChange(addChange1);
        changeSet.addChange(addChange2);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new ByteArrayInputStream(TEST_DATA);
        ArchiveInputStream archiveInput = new TarArchiveInputStream(new ByteArrayInputStream(TestUtils.createTarFileData(TEST_FILE_NAME, TEST_DATA)));
        ArchiveOutputStream archiveOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput, input);

        //Check that the first file was deleted and then added
        ArchiveEntry deletedEntry = archiveInput.getNextEntry();
        assertNull(deletedEntry);

        ArchiveEntry addedEntry1 = archiveOutput.getNextArchiveEntry();
        assertNotNull(addedEntry1);
        assertEquals(TEST_DIR_NAME + TEST_FILE_NAME, addedEntry1.getName());
        byte[] resultData1 = IOUtils.toByteArray(addedEntry1.getInputStream());
        assertArrayEquals(TEST_DATA, resultData1);

        //Check that the second file was added without deleting the first one
        ArchiveEntry addedEntry2 = archiveOutput.getNextArchiveEntry();
        assertNotNull(addedEntry2);
        assertEquals(TEST_FILE_NAME, addedEntry2.getName());
        byte[] resultData2 = IOUtils.toByteArray(addedEntry2.getInputStream());
        assertArrayEquals(TEST_DATA, resultData2);

        //Check that the results match the ChangeSet
        Set changes = performer.getChanges();
        Set expectedChanges = new LinkedHashSet(Arrays.asList(addChange1, addChange2, deleteChange));
        assertEquals(expectedChanges, changes);
    }

    @Test
    public void testChangeSetPerformerDeleteAndAddWithDifferentTypes() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        Change addChange1 = new Change(Change.TYPE_ADD, TEST_FILE_NAME, false, null);
        Change addChange2 = new Change(Change.TYPE_DELETE_DIR, TEST_DIR_NAME, false, null);
        Change deleteChange = new Change(Change.TYPE_DELETE, TEST_FILE_NAME, false, null);
        changeSet.addChange(deleteChange);
        changeSet.addChange(addChange1);
        changeSet.addChange(addChange2);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new ByteArrayInputStream(TEST_DATA);
        ArchiveInputStream archiveInput = new TarArchiveInputStream(new ByteArrayInputStream(TestUtils.createTarFileData(TEST_FILE_NAME, TEST_DATA)));
        ArchiveOutputStream archiveOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());

        try {
            performer.perform(archiveInput, archiveOutput, input);
            fail("Expected IOException due to mismatched change types");
        } catch (IOException e) {
            //Expected
        }

        //Check that the results match the ChangeSet
        Set changes = performer.getChanges();
        Set expectedChanges = new LinkedHashSet(Arrays.asList(addChange1, addChange2, deleteChange));
        assertEquals(expectedChanges, changes);
    }

    @Test
    public void testChangeSetPerformerWithEmptyChangeSet() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = new ByteArrayInputStream(TEST_DATA);
        ArchiveInputStream archiveInput = new TarArchiveInputStream(new ByteArrayInputStream(TestUtils.createTarFileData(TEST_FILE_NAME, TEST_DATA)));
        ArchiveOutputStream archiveOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput, input);

        //Check that no changes were made
        Set changes = performer.getChanges();
        assertTrue(Collections.isEmpty(changes));

        //Check that the input and output streams were not modified
        ArchiveEntry inputEntry = archiveInput.getNextEntry();
        assertNotNull(inputEntry);
        ArchiveEntry outputEntry = archiveOutput.getNextArchiveEntry();
        assertNull(outputEntry);
    }
}