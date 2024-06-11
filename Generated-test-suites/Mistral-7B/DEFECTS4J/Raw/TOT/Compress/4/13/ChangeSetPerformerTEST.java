import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ChangeSetPerformerTest {
    private ChangeSetPerformer performer;
    private ChangeSet changeSet;
    private TemporaryFolder tempFolder;

    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() throws IOException {
        changeSet = new ChangeSet();

        List<Change> changes = new ArrayList<>();
        changes.add(new AddChange("input1.txt", "output1.txt", false));
        changes.add(new DeleteChange("output1.txt", null));
        changes.add(new ReplaceChange("output1.txt", "output2.txt", true));

        changeSet.setChanges(changes);
        performer = new ChangeSetPerformer(changeSet);
        tempFolder = new TemporaryFolder();
    }

    @Test
    public void testPerform() throws IOException {
        // Prepare test data
        File inputFile = tempFolder.newFile("input.tar");
        ArchiveOutputStream inputStream = new ArchiveOutputStream(inputFile);
        File outputFile = tempFolder.newFile("output.tar");
        ArchiveInputStream archiveInput = new ArchiveInputStream(new FileInputStream(inputFile));

        // Test adding a file
        File input1 = tempFolder.newFile("input1.txt");
        FileOutputStream input1Stream = new FileOutputStream(input1);
        InputStream testStream = new FileInputStream(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input1.txt"));
        IOUtils.copy(testStream, input1Stream);
        inputStream.putNextEntry(new ArchiveEntry("input1.txt"));
        IOUtils.copy(TestUtils.inputStreamOf(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input1.txt")), inputStream);
        inputStream.closeArchiveEntry();
        inputStream.finish();
        inputStream.close();

        ChangeSetResults results = performer.perform(archiveInput, new ArchiveOutputStream(outputFile));

        // Test result
        assertEquals(1, results.size());
        assertEquals("input1.txt", results.get(0).getName());
        assertFalse(results.containsDeleted());
        assertFalse(results.containsAddedFromChangeSet());
        assertFalse(results.containsAddedFromStream());

        // Test files
        File output1 = new File(outputFile.getAbsolutePath() + "/output1.txt");
        File output2 = new File(outputFile.getAbsolutePath() + "/output2.txt");
        assertFalse(output1.exists());
        assertFalse(output2.exists());

        // Test stream
        ArchiveInputStream outputStream = new ArchiveInputStream(new FileInputStream(outputFile));
        ArchiveEntry entry = outputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("input1.txt", entry.getName());
        InputStream stream = outputStream.getInputStream();
        byte[] buffer = new byte[10];
        int length = stream.read(buffer, 0, buffer.length);
        assertFalse(length == -1);
        assertEquals(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input1.txt").length(), length);
        IOUtils.closeQuietly(stream);
        outputStream.close();

        // Test cleanup
        IOUtils.deleteQuietly(inputFile);
        IOUtils.deleteQuietly(outputFile);
    }

    @Test
    public void testPerform_DeleteFile() throws IOException {
        // Prepare test data
        File inputFile = tempFolder.newFile("input.tar");
        ArchiveOutputStream inputStream = new ArchiveOutputStream(inputFile);
        File outputFile = tempFolder.newFile("output.tar");
        ArchiveInputStream archiveInput = new ArchiveInputStream(new FileInputStream(inputFile));

        // Test deleting a file
        File input1 = tempFolder.newFile("input1.txt");
        FileOutputStream input1Stream = new FileOutputStream(input1);
        InputStream testStream = new FileInputStream(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input1.txt"));
        IOUtils.copy(testStream, input1Stream);
        inputStream.putNextEntry(new ArchiveEntry("input1.txt"));
        IOUtils.copy(TestUtils.inputStreamOf(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input1.txt")), inputStream);
        inputStream.closeArchiveEntry();
        inputStream.finish();
        inputStream.close();

        ChangeSetResults results = performer.perform(archiveInput, new ArchiveOutputStream(outputFile));

        // Test result
        assertEquals(0, results.size());
        assertFalse(results.containsDeleted());
        assertFalse(results.containsAddedFromChangeSet());
        assertFalse(results.containsAddedFromStream());

        // Test files
        File output1 = new File(outputFile.getAbsolutePath() + "/output1.txt");
        assertFalse(output1.exists());

        // Test stream
        ArchiveInputStream outputStream = new ArchiveInputStream(new FileInputStream(outputFile));
        assertNull(outputStream.getNextEntry());
        outputStream.close();

        // Test cleanup
        IOUtils.deleteQuietly(inputFile);
        IOUtils.deleteQuietly(outputFile);
    }

    @Test
    public void testPerform_ReplaceFile() throws IOException {
        // Prepare test data
        File inputFile = tempFolder.newFile("input.tar");
        ArchiveOutputStream inputStream = new ArchiveOutputStream(inputFile);
        File outputFile = tempFolder.newFile("output.tar");
        ArchiveInputStream archiveInput = new ArchiveInputStream(new FileInputStream(inputFile));

        // Test replacing a file
        File input1 = tempFolder.newFile("input1.txt");
        FileOutputStream input1Stream = new FileOutputStream(input1);
        InputStream testStream1 = new FileInputStream(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input1.txt"));
        IOUtils.copy(testStream1, input1Stream);
        inputStream.putNextEntry(new ArchiveEntry("input1.txt"));
        IOUtils.copy(TestUtils.inputStreamOf(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input1.txt")), inputStream);
        inputStream.closeArchiveEntry();

        File input2 = tempFolder.newFile("input2.txt");
        FileOutputStream input2Stream = new FileOutputStream(input2);
        InputStream testStream2 = new FileInputStream(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input2.txt"));
        IOUtils.copy(testStream2, input2Stream);
        inputStream.putNextEntry(new ArchiveEntry("input1.txt"));
        IOUtils.copy(TestUtils.inputStreamOf(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input2.txt")), inputStream);
        inputStream.closeArchiveEntry();
        inputStream.finish();
        inputStream.close();

        ChangeSetResults results = performer.perform(archiveInput, new ArchiveOutputStream(outputFile));

        // Test result
        assertEquals(0, results.size());
        assertFalse(results.containsDeleted());
        assertFalse(results.containsAddedFromChangeSet());
        assertTrue(results.containsAddedFromStream());

        // Test files
        File output1 = new File(outputFile.getAbsolutePath() + "/output1.txt");
        assertFalse(output1.exists());

        File output2 = new File(outputFile.getAbsolutePath() + "/output2.txt");
        assertFalse(output2.exists());

        // Test stream
        ArchiveInputStream outputStream = new ArchiveInputStream(new FileInputStream(outputFile));
        ArchiveEntry entry = outputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("input2.txt", entry.getName());
        InputStream stream = outputStream.getInputStream();
        byte[] buffer = new byte[10];
        int length = stream.read(buffer, 0, buffer.length);
        assertFalse(length == -1);
        assertEquals(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input2.txt").length(), length);
        IOUtils.closeQuietly(stream);
        outputStream.close();

        // Test cleanup
        IOUtils.deleteQuietly(inputFile);
        IOUtils.deleteQuietly(outputFile);
    }

    @Test
    public void testPerform_ReplaceFileWithAdd() throws IOException {
        // Prepare test data
        File inputFile = tempFolder.newFile("input.tar");
        ArchiveOutputStream inputStream = new ArchiveOutputStream(inputFile);
        File outputFile = tempFolder.newFile("output.tar");
        ArchiveInputStream archiveInput = new ArchiveInputStream(new FileInputStream(inputFile));

        // Test replacing a file with adding
        File input1 = tempFolder.newFile("input1.txt");
        FileOutputStream input1Stream = new FileOutputStream(input1);
        InputStream testStream1 = new FileInputStream(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input1.txt"));
        IOUtils.copy(testStream1, input1Stream);
        inputStream.putNextEntry(new ArchiveEntry("input1.txt"));
        IOUtils.copy(TestUtils.inputStreamOf(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input1.txt")), inputStream);
        inputStream.closeArchiveEntry();

        File input2 = tempFolder.newFile("input2.txt");
        FileOutputStream input2Stream = new FileOutputStream(input2);
        InputStream testStream2 = new FileInputStream(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input2.txt"));
        IOUtils.copy(testStream2, input2Stream);
        inputStream.putNextEntry(new ArchiveEntry("input2.txt"));
        IOUtils.copy(TestUtils.inputStreamOf(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input2.txt")), inputStream);
        inputStream.closeArchiveEntry();

        ChangeSetResults results = performer.perform(archiveInput, new ArchiveOutputStream(outputFile));

        // Test result
        assertEquals(2, results.size());
        List<String> expectedResult = Arrays.asList("input1.txt", "input2.txt");
        assertEquals(expectedResult, results.getNames());
        assertFalse(results.containsDeleted());
        assertFalse(results.containsAddedFromChangeSet());
        assertTrue(results.containsAddedFromStream());

        // Test files
        File output1 = new File(outputFile.getAbsolutePath() + "/input1.txt");
        File output2 = new File(outputFile.getAbsolutePath() + "/input2.txt");
        assertFalse(output1.exists());
        assertFalse(output2.exists());

        // Test stream
        ArchiveInputStream outputStream = new ArchiveInputStream(new FileInputStream(outputFile));
        ArchiveEntry entry1 = outputStream.getNextEntry();
        assertNotNull(entry1);
        assertEquals("input1.txt", entry1.getName());
        InputStream stream1 = outputStream.getInputStream();
        byte[] buffer = new byte[10];
        int length1 = stream1.read(buffer, 0, buffer.length);
        assertFalse(length1 == -1);
        assertEquals(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input1.txt").length(), length1);
        IOUtils.closeQuietly(stream1);
        ArchiveEntry entry2 = outputStream.getNextEntry();
        assertNotNull(entry2);
        assertEquals("input2.txt", entry2.getName());
        InputStream stream2 = outputStream.getInputStream();
        byte[] buffer2 = new byte[10];
        int length2 = stream2.read(buffer2, 0, buffer2.length);
        assertFalse(length2 == -1);
        assertEquals(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input2.txt").length(), length2);
        IOUtils.closeQuietly(stream2);
        outputStream.close();

        // Test cleanup
        IOUtils.deleteQuietly(inputFile);
        IOUtils.deleteQuietly(outputFile);
    }

    @Test
    public void testPerform_DeleteDirectory() throws IOException {
        // Prepare test data
        File inputFile = tempFolder.newFile("input.tar");
        ArchiveOutputStream inputStream = new ArchiveOutputStream(inputFile);
        File outputFile = tempFolder.newFile("output.tar");
        ArchiveInputStream archiveInput = new ArchiveInputStream(new FileInputStream(inputFile));

        // Test deleting a directory
        File directory = tempFolder.newDirectory("test");
        File file1 = new File(directory, "test1.txt");
        FileOutputStream file1Stream = new FileOutputStream(file1);
        InputStream testStream1 = new FileInputStream(TestUtils.getTestFile("ChangeSetPerformerTest/testData/test1.txt"));
        IOUtils.copy(testStream1, file1Stream);
        file1Stream.close();
        inputStream.putNextEntry(new ArchiveEntry("test/" + file1.getName()));
        IOUtils.copy(TestUtils.inputStreamOf(TestUtils.getTestFile("ChangeSetPerformerTest/testData/test1.txt")), inputStream);
        inputStream.closeArchiveEntry();
        inputStream.finish();
        inputStream.close();

        ChangeSetResults results = performer.perform(archiveInput, new ArchiveOutputStream(outputFile));

        // Test result
        assertEquals(0, results.size());
        assertFalse(results.containsDeleted());
        assertFalse(results.containsAddedFromChangeSet());
        assertFalse(results.containsAddedFromStream());

        // Test files
        File outputDirectory = new File(outputFile.getAbsolutePath() + "/test");
        assertFalse(outputDirectory.isDirectory());
        File outputFile1 = new File(outputDirectory.getAbsolutePath() + "/test1.txt");
        assertFalse(outputFile1.exists());

        // Test stream
        ArchiveInputStream outputStream = new ArchiveInputStream(new FileInputStream(outputFile));
        ArchiveEntry entry = outputStream.getNextEntry();
        assertNull(entry);
        outputStream.close();

        // Test cleanup
        IOUtils.deleteQuietly(inputFile);
        IOUtils.deleteQuietly(outputFile);
        IOUtils.deleteQuietly(directory);
    }
}