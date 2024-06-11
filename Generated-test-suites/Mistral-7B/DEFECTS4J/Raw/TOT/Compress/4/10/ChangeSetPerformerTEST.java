import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.ChangeSet;
import org.apache.commons.compress.ChangeSetResults;
import org.apache.commons.compress.utils.TestUtils;
import org.apache.commons.compress.utils.TestUtils.TestFile;
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

public class ChangeSetPerformerTest {

    @Test
    public void testPerform() throws IOException {
        // Given
        ChangeSet changeSet = new ChangeSet();

        Change addFileChange = new Change(Change.TYPE_ADD, null, "file1.txt", null, null, null, null, null, null, false);
        Change deleteFileChange = new Change(Change.TYPE_DELETE, null, "file1.txt", null, null, null, null, null, null, false);
        Change addDirChange = new Change(Change.TYPE_ADD_DIR, null, "dir1", null, null, null, null, null, null, false);
        Change deleteDirChange = new Change(Change.TYPE_DELETE_DIR, null, "dir1", null, null, null, null, null, null, false);
        Change addReplaceFileChange = new Change(Change.TYPE_ADD, "file1.txt", "file1.txt", null, null, null, null, null, null, true);
        Change replaceFileChange = new Change(Change.TYPE_REPLACE, "file1.txt", "file1.txt", "file2.txt", null, null, null, null, null, false);

        changeSet.addChange(addFileChange);
        changeSet.addChange(deleteFileChange);
        changeSet.addChange(addDirChange);
        changeSet.addChange(deleteDirChange);
        changeSet.addChange(addReplaceFileChange);
        changeSet.addChange(replaceFileChange);

        Set<Change> changes = changeSet.getChanges();

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        TestFile inputFile = TestUtils.createTestFile("testInput.tar.gz", "inputFile.txt");
        TestFile outputFile = TestUtils.createTestFile("testOutput.tar.gz", "expectedOutput.tar.gz");

        try (
                InputStream inputStream = TestUtils.getInputStream(inputFile);
                ArchiveInputStream archiveInput = new ArchiveInputStream(inputStream);
                ArchiveOutputStream archiveOutput = new ArchiveOutputStream(outputFile.getFileOutputStream());
                ChangeSetResults results = performer.perform(archiveInput, archiveOutput)
        ) {
            // When
            Set<ArchiveEntry> expectedEntries = new LinkedHashSet<>();
            expectedEntries.add(new TestUtils.TestArchiveEntry("file1.txt"));
            expectedEntries.add(new TestUtils.TestArchiveEntry("dir1/"));
            expectedEntries.add(new TestUtils.TestArchiveEntry("file1.txt"));
            expectedEntries.add(new TestUtils.TestArchiveEntry("file2.txt"));

            // Then
            Set<ArchiveEntry> actualEntries = new LinkedHashSet<>();
            while ((archiveOutput.getNextEntry() != null)) {
                actualEntries.add(archiveOutput.getNextEntry());
            }

            Assert.assertArrayEquals(expectedEntries.toArray(), actualEntries.toArray());
            Assert.assertEquals(ChangeSetResults.Status.COMPLETED, results.getStatus());
        }

        TestUtils.deleteQuietly(inputFile.getFile().getAbsolutePath());
        TestUtils.deleteQuietly(outputFile.getFile().getAbsolutePath());
    }

    @Test
    public void testPerformWithNoChanges() throws IOException {
        // Given
        ChangeSet changeSet = new ChangeSet();

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        TestFile inputFile = TestUtils.createTestFile("testInput.tar.gz", "");
        TestFile outputFile = TestUtils.createTestFile("testOutput.tar.gz", "");

        try (
                InputStream inputStream = TestUtils.getInputStream(inputFile);
                ArchiveInputStream archiveInput = new ArchiveInputStream(inputStream);
                ArchiveOutputStream archiveOutput = new ArchiveOutputStream(outputFile.getFileOutputStream());
                ChangeSetResults results = performer.perform(archiveInput, archiveOutput)
        ) {
            // Then
            Assert.assertEquals(Collections.emptySet(), archiveOutput.getEntries());
            Assert.assertEquals(ChangeSetResults.Status.COMPLETED, results.getStatus());
        }

        TestUtils.deleteQuietly(inputFile.getFile().getAbsolutePath());
        TestUtils.deleteQuietly(outputFile.getFile().getAbsolutePath());
    }

    @Test
    public void testPerformWithError() throws IOException {
        // Given
        ChangeSet changeSet = new ChangeSet();

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        TestFile inputFile = TestUtils.createTestFile("testInput.tar.gz", "");
        TestFile outputFile = TestUtils.createTestFile("testOutput.tar.gz", "");

        try (
                InputStream inputStream = new ByteArrayInputStream(new byte[0]);
                ArchiveInputStream archiveInput = new ArchiveInputStream(inputStream);
                ArchiveOutputStream archiveOutput = new ArchiveOutputStream(outputFile.getFileOutputStream());
                ChangeSetResults results = performer.perform(archiveInput, archiveOutput)
        ) {
            // Then
            Assert.assertThrows(IOException.class, () -> performer.perform(archiveInput, archiveOutput));
            Assert.assertEquals(ChangeSetResults.Status.FAILED, results.getStatus());
        }

        TestUtils.deleteQuietly(inputFile.getFile().getAbsolutePath());
        TestUtils.deleteQuietly(outputFile.getFile().getAbsolutePath());
    }

    @Test
    public void testPerformWithMissingEntry() throws IOException {
        // Given
        ChangeSet changeSet = new ChangeSet();

        Change addFileChange = new Change(Change.TYPE_ADD, null, "file1.txt", null, null, null, null, null, null, false);
        Change deleteFileChange = new Change(Change.TYPE_DELETE, null, "file1.txt", null, null, null, null, null, null, false);

        changeSet.addChange(addFileChange);
        changeSet.addChange(deleteFileChange);

        Set<Change> changes = changeSet.getChanges();

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        TestFile inputFile = TestUtils.createTestFile("testInput.tar.gz", "inputFile.txt");
        TestFile outputFile = TestUtils.createTestFile("testOutput.tar.gz", "");

        try (
                InputStream inputStream = TestUtils.getInputStream(inputFile);
                ArchiveInputStream archiveInput = new ArchiveInputStream(inputStream);
                ArchiveOutputStream archiveOutput = new ArchiveOutputStream(outputFile.getFileOutputStream());
                ChangeSetResults results = performer.perform(archiveInput, archiveOutput)
        ) {
            // Then
            Assert.assertEquals(1, archiveOutput.getNextEntry() == null ? 0 : 1);
            Assert.assertEquals(ChangeSetResults.Status.FAILED, results.getStatus());
        }

        TestUtils.deleteQuietly(inputFile.getFile().getAbsolutePath());
        TestUtils.deleteQuietly(outputFile.getFile().getAbsolutePath());
    }

    @Test
    public void testPerformWithDuplicateEntry() throws IOException {
        // Given
        ChangeSet changeSet = new ChangeSet();

        Change addFileChange1 = new Change(Change.TYPE_ADD, null, "file1.txt", null, null, null, null, null, null, false);
        Change addFileChange2 = new Change(Change.TYPE_ADD, null, "file1.txt", null, null, null, null, null, null, false);

        changeSet.addChange(addFileChange1);
        changeSet.addChange(addFileChange2);

        Set<Change> changes = changeSet.getChanges();

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        TestFile inputFile = TestUtils.createTestFile("testInput.tar.gz", "inputFile.txt");
        TestFile outputFile = TestUtils.createTestFile("testOutput.tar.gz", "");

        try (
                InputStream inputStream = TestUtils.getInputStream(inputFile);
                ArchiveInputStream archiveInput = new ArchiveInputStream(inputStream);
                ArchiveOutputStream archiveOutput = new ArchiveOutputStream(outputFile.getFileOutputStream());
                ChangeSetResults results = performer.perform(archiveInput, archiveOutput)
        ) {
            // Then
            Assert.assertEquals(1, archiveOutput.getNextEntry() == null ? 0 : 1);
            Assert.assertEquals(ChangeSetResults.Status.FAILED, results.getStatus());
        }

        TestUtils.deleteQuietly(inputFile.getFile().getAbsolutePath());
        TestUtils.deleteQuietly(outputFile.getFile().getAbsolutePath());
    }

    @Test
    public void testPerformWithNestedDirectory() throws IOException {
        // Given
        ChangeSet changeSet = new ChangeSet();

        Change addDirChange = new Change(Change.TYPE_ADD_DIR, null, "dir1", null, null, null, null, null, null, false);
        Change addFileChange = new Change(Change.TYPE_ADD, null, "dir1/file1.txt", null, null, null, null, null, null, false);

        changeSet.addChange(addDirChange);
        changeSet.addChange(addFileChange);

        Set<Change> changes = changeSet.getChanges();

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        TestFile inputFile = TestUtils.createTestFile("testInput.tar.gz", "inputFile.txt");
        TestFile outputFile = TestUtils.createTestFile("testOutput.tar.gz", "expectedOutput.tar.gz");

        try (
                InputStream inputStream = TestUtils.getInputStream(inputFile);
                ArchiveInputStream archiveInput = new ArchiveInputStream(inputStream);
                ArchiveOutputStream archiveOutput = new ArchiveOutputStream(outputFile.getFileOutputStream());
                ChangeSetResults results = performer.perform(archiveInput, archiveOutput)
        ) {
            // When
            Set<ArchiveEntry> expectedEntries = new LinkedHashSet<>();
            expectedEntries.add(new TestUtils.TestArchiveEntry("dir1/"));
            expectedEntries.add(new TestUtils.TestArchiveEntry("dir1/file1.txt"));

            // Then
            Set<ArchiveEntry> actualEntries = new LinkedHashSet<>();
            while ((archiveOutput.getNextEntry() != null)) {
                actualEntries.add(archiveOutput.getNextEntry());
            }

            Assert.assertArrayEquals(expectedEntries.toArray(), actualEntries.toArray());
            Assert.assertEquals(ChangeSetResults.Status.COMPLETED, results.getStatus());
        }

        TestUtils.deleteQuietly(inputFile.getFile().getAbsolutePath());
        TestUtils.deleteQuietly(outputFile.getFile().getAbsolutePath());
    }
}