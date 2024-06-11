package org.apache.commons.compress.changes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ChangeSet changeSet;
    private ArchiveInputStream inputStream;
    private ArchiveOutputStream outputStream;
    private ByteArrayOutputStream outputStreamBuffer;
    private ByteArrayInputStream inputStreamBuffer;

    @Before
    public void setUp() throws Exception {
        ChangeSet changeSet = new ChangeSet();

        Change addChange = new Change("add", "file1.txt", null, null, false);
        Change addChange2 = new Change("add", "file2.txt", null, null, false);
        Change deleteChange = new Change("delete", "file1.txt", null, null, false);
        Change deleteChange2 = new Change("delete", "file2.txt", null, null, false);
        Change deleteDirectoryChange = new Change("delete_dir", "dir1/", null, null, false);

        changeSet.addChange(addChange);
        changeSet.addChange(addChange2);
        changeSet.addChange(deleteChange);
        changeSet.addChange(deleteChange2);
        changeSet.addChange(deleteDirectoryChange);

        performer = new ChangeSetPerformer(changeSet);

        inputStream = new ArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        outputStream = new ArchiveOutputStream("output.tar");
        outputStreamBuffer = new ByteArrayOutputStream();
        outputStream.setBufferedStream(outputStreamBuffer);
    }

    @Test
    public void testPerform_addAndDelete() throws IOException {
        InputStream file1Input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        InputStream file2Input = new ByteArrayInputStream(new byte[]{4, 5, 6});

        performer.perform(inputStream, outputStream);

        ArchiveEntry file1Entry = new ArchiveEntry("file1.txt");
        ArchiveEntry file2Entry = new ArchiveEntry("file2.txt");

        assertTrue(performer.perform(inputStream, outputStream).hasBeenAdded("file1.txt"));
        assertTrue(performer.perform(inputStream, outputStream).hasBeenAdded("file2.txt"));
        assertTrue(performer.perform(inputStream, outputStream).deleted("file1.txt"));
        assertFalse(performer.perform(inputStream, outputStream).deleted("file2.txt"));

        ArchiveEntry[] entries = outputStream.getArchive().getEntries();
        Set expectedEntries = new LinkedHashSet(Arrays.asList(file1Entry, file2Entry));
        Set actualEntries = new LinkedHashSet<>();
        for (ArchiveEntry entry : entries) {
            actualEntries.add(entry);
        }
        assertTrue(expectedEntries.equals(actualEntries));

        ArchiveInputStream testInput = new ArchiveInputStream(new ByteArrayInputStream(outputStreamBuffer.toByteArray()));
        ArchiveEntry testFile1Entry = testInput.getNextEntry();
        assertTrue(testFile1Entry != null && testFile1Entry.getName().equals("file1.txt"));
        InputStream testFile1 = testFile1Entry.getInputStream();
        byte[] file1 = IOUtils.toByteArray(testFile1);
        assertTrue(Arrays.equals(file1, file1Input.readAllBytes()));

        ArchiveEntry testFile2Entry = testInput.getNextEntry();
        assertTrue(testFile2Entry != null && testFile2Entry.getName().equals("file2.txt"));
        InputStream testFile2 = testFile2Entry.getInputStream();
        byte[] file2 = IOUtils.toByteArray(testFile2);
        assertTrue(Arrays.equals(file2, file2Input.readAllBytes()));

        testInput.close();
        outputStream.finish();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPerform_deleteLater() throws IOException {
        InputStream file1Input = new ByteArrayInputStream(new byte[]{1, 2, 3});

        performer.perform(inputStream, outputStream);

        ArchiveEntry file1Entry = new ArchiveEntry("file1.txt");

        performer.perform(inputStream, outputStream);

        assertTrue(performer.perform(inputStream, outputStream).deleted("file1.txt"));
    }

    @Test(expected = IOException.class)
    public void testPerform_addReplaceMode() throws IOException {
        InputStream file1Input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        InputStream file1ReplaceInput = new ByteArrayInputStream(new byte[]{7, 8, 9});
        ArchiveEntry file1Entry = new ArchiveEntry("file1.txt");
        file1Entry.setName(file1Entry.getName() + "_replace");

        performer.perform(inputStream, outputStream);
        performer.perform(inputStream, outputStream, file1Entry, file1Input);

        performer.perform(inputStream, outputStream, file1Entry, file1ReplaceInput);

        assertTrue(performer.perform(inputStream, outputStream).deleted("file1.txt_replace"));
        assertFalse(performer.perform(inputStream, outputStream).deleted("file1.txt"));
    }

    @Test
    public void testPerform_deleteDirectory() throws IOException {
        InputStream file1Input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        InputStream file2Input = new ByteArrayInputStream(new byte[]{4, 5, 6});

        performer.perform(inputStream, outputStream);

        ArchiveEntry dirEntry = new ArchiveEntry("dir1/");
        performer.perform(inputStream, outputStream, dirEntry);

        assertTrue(performer.perform(inputStream, outputStream).deleted("file1.txt"));
        assertFalse(performer.perform(inputStream, outputStream).deleted("file2.txt"));
        assertFalse(performer.perform(inputStream, outputStream).deleted("dir1/file1.txt"));

        ArchiveEntry[] entries = outputStream.getArchive().getEntries();
        Set expectedEntries = new LinkedHashSet();
        expectedEntries.add(new ArchiveEntry("file2.txt"));
        Set actualEntries = new LinkedHashSet<>();
        for (ArchiveEntry entry : entries) {
            actualEntries.add(entry);
        }
        assertTrue(expectedEntries.equals(actualEntries));

        ArchiveInputStream testInput = new ArchiveInputStream(new ByteArrayInputStream(outputStreamBuffer.toByteArray()));
        ArchiveEntry testFile2Entry = testInput.getNextEntry();
        assertTrue(testFile2Entry != null && testFile2Entry.getName().equals("file2.txt"));
        InputStream testFile2 = testFile2Entry.getInputStream();
        byte[] file2 = IOUtils.toByteArray(testFile2);
        assertTrue(Arrays.equals(file2, file2Input.readAllBytes()));

        testInput.close();
        outputStream.finish();
        outputStream.close();
    }

    @Test
    public void testPerform_addNonReplaceMode() throws IOException {
        InputStream file1Input = new ByteArrayInputStream(new byte[]{1, 2, 3});

        performer.perform(inputStream, outputStream);

        ArchiveEntry file1Entry = new ArchiveEntry("file1.txt");

        performer.perform(inputStream, outputStream, file1Entry, file1Input, true);

        assertTrue(performer.perform(inputStream, outputStream).added("file1.txt"));
        assertTrue(performer.perform(inputStream, outputStream).deleted("file1.txt"));

        ArchiveEntry[] entries = outputStream.getArchive().getEntries();
        Set expectedEntries = new LinkedHashSet(Arrays.asList(file1Entry));
        Set actualEntries = new LinkedHashSet<>();
        for (ArchiveEntry entry : entries) {
            actualEntries.add(entry);
        }
        assertTrue(expectedEntries.equals(actualEntries));

        ArchiveInputStream testInput = new ArchiveInputStream(new ByteArrayInputStream(outputStreamBuffer.toByteArray()));
        ArchiveEntry testFile1Entry = testInput.getNextEntry();
        assertTrue(testFile1Entry != null && testFile1Entry.getName().equals("file1.txt"));
        InputStream testFile1 = testFile1Entry.getInputStream();
        byte[] file1 = IOUtils.toByteArray(testFile1);
        assertTrue(Arrays.equals(file1, file1Input.readAllBytes()));

        testInput.close();
        outputStream.finish();
        outputStream.close();
    }
}