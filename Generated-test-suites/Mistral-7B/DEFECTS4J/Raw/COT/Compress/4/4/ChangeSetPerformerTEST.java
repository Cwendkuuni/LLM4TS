package org.apache.commons.compress.changes;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZFileEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZInputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputStream;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Test class for ChangeSetPerformer
 * 
 * @author [Your Name]
 */
public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() throws IOException {
        byte[] input1 = TestUtils.getBytes("input1.txt");
        byte[] input2 = TestUtils.getBytes("input2.txt");
        byte[] input3 = TestUtils.getBytes("input3.txt");

        Change addChange1 = new Change(Change.TYPE_ADD, null, "output1.txt", null, null, false);
        Change addChange2 = new Change(Change.TYPE_ADD, null, "output2.txt", null, null, false);
        Change addChange3 = new Change(Change.TYPE_ADD, null, "output3.txt", null, null, false);

        Change deleteChange1 = new Change(Change.TYPE_DELETE, null, "output1.txt", null, null, false);
        Change deleteChange2 = new Change(Change.TYPE_DELETE_DIR, null, "output/", null, null, false);

        Change replaceChange1 = new Change(Change.TYPE_ADD, "input1.txt", "output1.txt", null, null, true);
        Change replaceChange2 = new Change(Change.TYPE_ADD, "input2.txt", "output2.txt", null, null, true);
        Change replaceChange3 = new Change(Change.TYPE_ADD, "input3.txt", "output3.txt", null, null, true);

        List<Change> changes = new ArrayList<>();
        changes.add(addChange1);
        changes.add(addChange2);
        changes.add(addChange3);
        changes.add(deleteChange1);
        changes.add(deleteChange2);
        changes.add(replaceChange1);
        changes.add(replaceChange2);
        changes.add(replaceChange3);

        changeSet = new ChangeSet(changes);
        performer = new ChangeSetPerformer(changeSet);
    }

    /**
     * Test method for {@link ChangeSetPerformer#ChangeSetPerformer(ChangeSet)}
     */
    @Test
    public void testChangeSetPerformerConstructor() {
        assertNotNull(performer);
        assertSame(changeSet, performer.getChanges());
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     */
    @Test
    public void testPerform() throws IOException {
        SevenZFile sourceArchive = new SevenZFile("source.7z");
        SevenZInputStream sourceStream = new SevenZInputStream(new FileInputStream("source.7z"));
        SevenZOutputStream targetArchive = new SevenZOutputStream(new FileOutputStream("target.7z"));

        performer.perform(sourceStream, targetArchive);

        SevenZFile targetArchiveFile = new SevenZFile("target.7z");
        SevenZInputStream targetStream = new SevenZInputStream(new FileInputStream("target.7z"));

        // Check added files
        ArchiveEntry entry1 = targetArchiveFile.getEntry("output1.txt");
        assertNotNull(entry1);
        InputStream input1Stream = targetStream.getInputStream(entry1);
        byte[] output1 = IOUtils.toByteArray(input1Stream);
        assertArrayEquals(input1, output1);

        ArchiveEntry entry2 = targetArchiveFile.getEntry("output2.txt");
        assertNotNull(entry2);
        InputStream input2Stream = targetStream.getInputStream(entry2);
        byte[] output2 = IOUtils.toByteArray(input2Stream);
        assertArrayEquals(input2, output2);

        ArchiveEntry entry3 = targetArchiveFile.getEntry("output3.txt");
        assertNotNull(entry3);
        InputStream input3Stream = targetStream.getInputStream(entry3);
        byte[] output3 = IOUtils.toByteArray(input3Stream);
        assertArrayEquals(input3, output3);

        // Check deleted files
        assertNull(targetArchiveFile.getEntry("input1.txt"));
        assertNull(targetArchiveFile.getEntry("input2.txt"));
        assertNull(targetArchiveFile.getEntry("input3.txt"));

        // Check replaced files
        ArchiveEntry entryReplace1 = targetArchiveFile.getEntry("output1.txt");
        assertNotNull(entryReplace1);
        InputStream inputReplace1Stream = targetStream.getInputStream(entryReplace1);
        byte[] outputReplace1 = IOUtils.toByteArray(inputReplace1Stream);
        assertArrayEquals(input1, outputReplace1);

        ArchiveEntry entryReplace2 = targetArchiveFile.getEntry("output2.txt");
        assertNotNull(entryReplace2);
        InputStream inputReplace2Stream = targetStream.getInputStream(entryReplace2);
        byte[] outputReplace2 = IOUtils.toByteArray(inputReplace2Stream);
        assertArrayEquals(input2, outputReplace2);

        ArchiveEntry entryReplace3 = targetArchiveFile.getEntry("output3.txt");
        assertNotNull(entryReplace3);
        InputStream inputReplace3Stream = targetStream.getInputStream(entryReplace3);
        byte[] outputReplace3 = IOUtils.toByteArray(inputReplace3Stream);
        assertArrayEquals(input3, outputReplace3);

        // Check non-existent files
        assertNull(targetArchiveFile.getEntry("non-existing.txt"));

        // Check directories
        assertFalse(targetArchiveFile.list().isEmpty());
        String[] directoryList = targetArchiveFile.list();
        assertTrue(Arrays.asList(directoryList).contains("output/"));

        // Check files inside directories
        ArchiveEntry directoryEntry = targetArchiveFile.getEntry("output/");
        assertNotNull(directoryEntry);
        SevenZInputStream directoryStream = targetStream.getInputStream(directoryEntry);
        SevenZFile directoryFile = new SevenZFile(directoryStream);
        List<ArchiveEntry> filesInDirectory = new ArrayList<>(directoryFile.getEntries());

        SevenZFileEntry fileInDirectory1 = (SevenZFileEntry) directoryFile.getEntry("output1.txt");
        assertNotNull(fileInDirectory1);
        InputStream fileInDirectoryStream1 = directoryStream.getInputStream(fileInDirectory1);
        byte[] outputFileInDirectory1 = IOUtils.toByteArray(fileInDirectoryStream1);
        assertArrayEquals(input1, outputFileInDirectory1);

        SevenZFileEntry fileInDirectory2 = (SevenZFileEntry) directoryFile.getEntry("output2.txt");
        assertNotNull(fileInDirectory2);
        InputStream fileInDirectoryStream2 = directoryStream.getInputStream(fileInDirectory2);
        byte[] outputFileInDirectory2 = IOUtils.toByteArray(fileInDirectoryStream2);
        assertArrayEquals(input2, outputFileInDirectory2);

        SevenZFileEntry fileInDirectory3 = (SevenZFileEntry) directoryFile.getEntry("output3.txt");
        assertNotNull(fileInDirectory3);
        InputStream fileInDirectoryStream3 = directoryStream.getInputStream(fileInDirectory3);
        byte[] outputFileInDirectory3 = IOUtils.toByteArray(fileInDirectoryStream3);
        assertArrayEquals(input3, outputFileInDirectory3);

        // Clean up
        IOUtils.closeQuietly(sourceStream);
        IOUtils.closeQuietly(targetStream);
        sourceArchive.close();
        targetArchiveFile.close();
        File targetFile = new File("target.7z");
        targetFile.delete();
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)} with replace mode
     */
    @Test
    public void testPerformReplaceMode() throws IOException {
        SevenZFile sourceArchive = new SevenZFile("source_replace.7z");
        SevenZInputStream sourceStream = new SevenZInputStream(new FileInputStream("source_replace.7z"));
        SevenZOutputStream targetArchive = new SevenZOutputStream(new FileOutputStream("target_replace.7z"));

        performer.perform(sourceStream, targetArchive);

        SevenZFile targetArchiveFile = new SevenZFile("target_replace.7z");
        SevenZInputStream targetStream = new SevenZInputStream(new FileInputStream("target_replace.7z"));

        // Check replaced files
        ArchiveEntry entryReplace1 = targetArchiveFile.getEntry("output1.txt");
        assertNotNull(entryReplace1);
        InputStream inputReplace1Stream = targetStream.getInputStream(entryReplace1);
        byte[] outputReplace1 = IOUtils.toByteArray(inputReplace1Stream);
        assertArrayEquals(input1, outputReplace1);

        ArchiveEntry entryReplace2 = targetArchiveFile.getEntry("output2.txt");
        assertNotNull(entryReplace2);
        InputStream inputReplace2Stream = targetStream.getInputStream(entryReplace2);
        byte[] outputReplace2 = IOUtils.toByteArray(inputReplace2Stream);
        assertArrayEquals(input2, outputReplace2);

        ArchiveEntry entryReplace3 = targetArchiveFile.getEntry("output3.txt");
        assertNotNull(entryReplace3);
        InputStream inputReplace3Stream = targetStream.getInputStream(entryReplace3);
        byte[] outputReplace3 = IOUtils.toByteArray(inputReplace3Stream);
        assertArrayEquals(input3, outputReplace3);

        // Check non-existent files
        assertNull(targetArchiveFile.getEntry("non-existing.txt"));

        // Check directories
        assertFalse(targetArchiveFile.list().isEmpty());
        String[] directoryList = targetArchiveFile.list();
        assertTrue(Arrays.asList(directoryList).contains("output/"));

        // Clean up
        IOUtils.closeQuietly(sourceStream);
        IOUtils.closeQuietly(targetStream);
        sourceArchive.close();
        targetArchiveFile.close();
        File targetFile = new File("target_replace.7z");
        targetFile.delete();
    }

    /**
     * Test method for {@link ChangeSetPerformer#isDeletedLater(Set, ArchiveEntry)}
     */
    @Test
    public void testIsDeletedLater() {
        Set<Change> changes = new LinkedHashSet<>();
        Change addChange1 = new Change(Change.TYPE_ADD, null, "output1.txt", null, null, false);
        Change deleteChange1 = new Change(Change.TYPE_DELETE, null, "output1.txt", null, null, false);
        changes.add(addChange1);
        changes.add(deleteChange1);

        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveEntry entry = new File("output1.txt").toPath().toFile().getAbsoluteFile().toPath()
                .getFileName().toString();
        assertTrue(performer.isDeletedLater(changes, new File("output1.txt").toPath().toFile()));
        assertFalse(performer.isDeletedLater(changes, new File("output2.txt").toPath().toFile()));
    }

    /**
     * Test method for {@link ChangeSetPerformer#copyStream(InputStream, ArchiveOutputStream, ArchiveEntry)}
     */
    @Test
    public void testCopyStream() throws IOException {
        byte[] input = TestUtils.getBytes("input.txt");
        GzipCompressorInputStream gzipInput = new GzipCompressorInputStream(new ByteArrayInputStream(input));
        byte[] expectedOutput = TestUtils.getBytes("expected_output.txt");
        GzipCompressorOutputStream gzipOutput = new GzipCompressorOutputStream(new ByteArrayOutputStream());

        performer.copyStream(gzipInput, gzipOutput, new File("output.txt").toPath().toFile().toArchiveEntry(null));

        byte[] output = ((ByteArrayOutputStream) gzipOutput).toByteArray();
        assertArrayEquals(expectedOutput, output);
    }
}