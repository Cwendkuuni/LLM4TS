package org.apache.commons.compress.changes;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
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
    private ChangeSetPerformer performer;
    private ArchiveInputStream inputStream;
    private ArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() throws Exception {
        changeSet = new ChangeSet();

        Change addChange = new Change(Change.TYPE_ADD, "test.txt", null, null);
        Change deleteChange = new Change(Change.TYPE_DELETE, "test.txt", null, null);
        Change addReplaceChange = new Change(Change.TYPE_ADD, "test.txt", "testReplaced.txt", null);
        Change deleteReplaceDirChange = new Change(Change.TYPE_DELETE_DIR, "/testDir/", null, null);

        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);
        changeSet.addChange(addReplaceChange);
        changeSet.addChange(deleteReplaceDirChange);

        performer = new ChangeSetPerformer(changeSet);

        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new ArchiveOutputStream("temp.tar.gz", new GzipCompressorOutputStream(byteArrayOutputStream));

        byteArrayInputStream = new ByteArrayInputStream(TestUtils.getResourceAsBytes("testData.tar.gz"));
        inputStream = new ArchiveInputStream(byteArrayInputStream);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);
        IOUtils.closeQuietly(byteArrayInputStream);
    }

    @Test
    public void testPerform_Add_ReplaceMode() throws IOException {
        InputStream input = changeSet.getChange(0).getInput();
        String expectedOutputName = changeSet.getChange(0).getEntry().getName();

        performer.perform(inputStream, outputStream);
        ArchiveEntry entry = outputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals(expectedOutputName, entry.getName());

        byte[] expectedContent = changeSet.getChange(0).getInput().readAllBytes();
        byte[] actualContent = IOUtils.toByteArray(entry.getInputStream());
        assertArrayEquals(expectedContent, actualContent);
    }

    @Test(expected = IOException.class)
    public void testPerform_Delete_MatchedEntry() throws IOException {
        performer.perform(inputStream, outputStream);

        ArchiveEntry entry = inputStream.getNextEntry();
        performer.perform(inputStream, outputStream);

        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testPerform_Delete_UnmatchedEntry() throws IOException {
        Change unmatchedChange = new Change(Change.TYPE_DELETE, "unmatched.txt", null, null);
        changeSet.addChange(unmatchedChange);
        performer = new ChangeSetPerformer(changeSet);

        performer.perform(inputStream, outputStream);

        ArchiveEntry entry = inputStream.getNextEntry();
        performer.perform(inputStream, outputStream);

        assertNotNull(entry);
        assertFalse(entry.getName().equals(unmatchedChange.getTargetFile()));
    }

    @Test
    public void testPerform_DeleteDir_MatchedEntry() throws IOException {
        InputStream input = changeSet.getChange(3).getInput();

        performer.perform(inputStream, outputStream);

        ArchiveEntry entry = inputStream.getNextEntry();
        while (entry != null) {
            String name = entry.getName();
            if (name.startsWith(changeSet.getChange(3).getTargetFile() + "/")) {
                assertFalse(outputStream.getNextEntry() == null);
                assertNull(inputStream.getNextEntry());
                break;
            }
            entry = inputStream.getNextEntry();
        }
    }

    @Test(expected = IOException.class)
    public void testPerform_DeleteDir_UnmatchedEntry() throws IOException {
        Change unmatchedChange = new Change(Change.TYPE_DELETE_DIR, "/unmatchedDir/", null, null);
        changeSet.addChange(unmatchedChange);
        performer = new ChangeSetPerformer(changeSet);

        performer.perform(inputStream, outputStream);

        ArchiveEntry entry = inputStream.getNextEntry();
        performer.perform(inputStream, outputStream);

        assertNotNull(entry);
        assertFalse(entry.getName().startsWith(unmatchedChange.getTargetFile()));
    }

    @Test
    public void testPerform_Add_NotReplaceMode() throws IOException {
        InputStream input = changeSet.getChange(2).getInput();
        String expectedOutputName = changeSet.getChange(2).getEntry().getName();

        performer.perform(inputStream, outputStream);

        ArchiveEntry entry = outputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals(expectedOutputName, entry.getName());

        byte[] expectedContent = changeSet.getChange(2).getInput().readAllBytes();
        byte[] actualContent = IOUtils.toByteArray(entry.getInputStream());
        assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    public void testPerform_Add_AlreadyAdded() throws IOException {
        InputStream input = changeSet.getChange(0).getInput();
        performer.perform(inputStream, outputStream);
        performer.perform(inputStream, outputStream);

        ArchiveEntry entry = outputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals(changeSet.getChange(0).getEntry().getName(), entry.getName());
    }

    @Test
    public void testPerform_PerformMultipleTimes() throws IOException {
        InputStream input = changeSet.getChange(0).getInput();

        performer.perform(inputStream, outputStream);
        performer.perform(inputStream, outputStream);

        ArchiveEntry entry = inputStream.getNextEntry();
        while (entry != null) {
            String name = entry.getName();
            if (name.equals(changeSet.getChange(0).getEntry().getName())) {
                fail("Expected to add the file only once.");
            }
            entry = inputStream.getNextEntry();
        }

        ArchiveEntry expectedEntry = outputStream.getNextEntry();
        assertNotNull(expectedEntry);
        assertEquals(changeSet.getChange(0).getEntry().getName(), expectedEntry.getName());
    }

    @Test
    public void testPerform_Add_EmptyChangeSet() throws IOException {
        ChangeSet emptyChangeSet = new ChangeSet();
        ChangeSetPerformer emptyPerformer = new ChangeSetPerformer(emptyChangeSet);

        performer.perform(inputStream, outputStream);

        ArchiveEntry entry = inputStream.getNextEntry();
        while (entry != null) {
            byte[] content = IOUtils.toByteArray(entry.getInputStream());
            assertTrue(content.length == 0);
            entry = inputStream.getNextEntry();
        }

        ArchiveEntry expectedEntry = outputStream.getNextEntry();
        assertNull(expectedEntry);
    }

    @Test(expected = IOException.class)
    public void testPerform_Add_NullChangeSet() throws IOException {
        ChangeSetPerformer nullPerformer = new ChangeSetPerformer(null);

        nullPerformer.perform(inputStream, outputStream);
    }

    @Test(expected = IOException.class)
    public void testPerform_Add_NullInputStream() throws IOException {
        performer.perform(null, outputStream);
    }

    @Test(expected = IOException.class)
    public void testPerform_Add_NullOutputStream() throws IOException {
        performer.perform(inputStream, null);
    }
}