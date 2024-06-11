import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 * TarArchiveOutputStreamTest class for testing TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private File tempFile;
    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        tempFile = File.createTempFile("junit-tar-output", "");
        outputStream = new FileOutputStream(tempFile);
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
        outputStream.close();
        tempFile.delete();
    }

    /**
     * Test putArchiveEntry method with a regular file entry.
     * @throws IOException
     */
    @Test
    public void testPutArchiveEntryWithRegularFileEntry() throws IOException {
        final String testFileName = "testFile.txt";
        final String testFilePath = "/test/path/testFile.txt";
        final byte[] testData = "Hello World!".getBytes();
        File inputFile = File.createTempFile("test", "txt");
        File expectedOutputFile = new File(tempFile.getParentFile(),
                "test/" + testFileName + ".tar");

        // Write test data to temporary file
        Files.write(inputFile.toPath(), testData);

        // Put archive entry and write test data to output stream
        TarArchiveEntry entry = new TarArchiveEntry(testFilePath, inputFile);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        // Finish the archive and close the output stream
        tarOutputStream.finish();
        outputStream.close();

        // Verify that the expected output file has been generated
        TarArchiveOutputStream expectedTarOutputStream = new TarArchiveOutputStream(
                new FileOutputStream(expectedOutputFile));
        expectedTarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry expectedEntry = new TarArchiveEntry(testFilePath, inputFile.getName());
        expectedEntry.setSize(testData.length);
        expectedTarOutputStream.putNextEntry(expectedEntry);
        expectedTarOutputStream.write(testData, 0, testData.length);
        expectedTarOutputStream.finish();
        expectedTarOutputStream.close();

        // Compare the generated and expected output files
        byte[] generatedOutput = IOUtils.toByteArray(new FileInputStream(tempFile));
        byte[] expectedOutput = IOUtils.toByteArray(new FileInputStream(expectedOutputFile));
        assertArrayEquals(expectedOutput, generatedOutput);

        // Clean up
        expectedOutputFile.delete();
        inputFile.delete();
    }

    /**
     * Test putArchiveEntry method with a directory entry.
     * @throws IOException
     */
    @Test
    public void testPutArchiveEntryWithDirectoryEntry() throws IOException {
        final String testDirName = "testDir";
        final String testDirPath = "/test/path/testDir";
        File testDir = new File(System.getProperty("java.io.tmpdir"));
        testDir.mkdir();

        // Put archive entry for the directory
        TarArchiveEntry entry = new TarArchiveEntry(testDirPath, testDir);
        entry.setSize(0);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        // Finish the archive and close the output stream
        tarOutputStream.finish();
        outputStream.close();

        // Verify that the expected output file has been generated
        TarArchiveOutputStream expectedTarOutputStream = new TarArchiveOutputStream(
                new FileOutputStream(new File(tempFile.getParentFile(),
                        "test/" + testDirName + "/.tar")));
        expectedTarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry expectedDirectoryEntry = new TarArchiveEntry(testDirPath, testDirName);
        expectedDirectoryEntry.setSize(0);
        expectedDirectoryEntry.setDirectory(true);
        expectedTarOutputStream.putNextEntry(expectedDirectoryEntry);
        expectedTarOutputStream.finish();
        expectedTarOutputStream.close();

        // Clean up
        testDir.delete();
    }

    /**
     * Test putArchiveEntry method with a long file name.
     * @throws IOException
     */
    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        final String testFileName = "testFile___________________________________________________________________.txt";
        final String testFilePath = "/test/path/testFile___________________________________________________________________.txt";
        final byte[] testData = "Hello World!"
                .getBytes();
        File inputFile = File.createTempFile("test", "txt");
        File expectedOutputFile = new File(tempFile.getParentFile(),
                "test/" + testFileName + ".tar");

        // Write test data to temporary file
        Files.write(inputFile.toPath(), testData);

        // Set long file name mode
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // Put archive entry and write test data to output stream
        TarArchiveEntry entry = new TarArchiveEntry(testFilePath, inputFile);
        entry.setSize(testData.length);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        // Finish the archive and close the output stream
        tarOutputStream.finish();
        outputStream.close();

        // Verify that the expected output file has been generated
        TarArchiveOutputStream expectedTarOutputStream = new TarArchiveOutputStream(
                new FileOutputStream(expectedOutputFile));
        expectedTarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry expectedLongLinkEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK,
                TarConstants.LF_GNUTYPE_LONGNAME);
        expectedLongLinkEntry.setSize(testData.length.intValue() + 1 + testFileName.getBytes().length);
        ByteArrayInputStream expectedLongLinkEntryNameStream = new ByteArrayInputStream(testFileName.getBytes());
        expectedLongLinkEntry.setNameStream(expectedLongLinkEntryNameStream);
        expectedTarOutputStream.putNextEntry(expectedLongLinkEntry);
        expectedTarOutputStream.write(testData, 0, testData.length);
        expectedTarOutputStream.finish();
        expectedTarOutputStream.close();

        // Compare the generated and expected output files
        byte[] generatedOutput = IOUtils.toByteArray(new FileInputStream(tempFile));
        byte[] expectedOutput = IOUtils.toByteArray(new FileInputStream(expectedOutputFile));
        assertArrayEquals(expectedOutput, generatedOutput);

        // Clean up
        expectedOutputFile.delete();
        inputFile.delete();
    }

    /**
     * Test write method with a valid byte buffer.
     * @throws IOException
     */
    @Test
    public void testWriteWithValidByteBuffer() throws IOException {
        final byte[] testData = "Hello World!".getBytes();

        // Write test data to output stream
        tarOutputStream.write(testData, 0, testData.length);

        // Finish the archive and close the output stream
        tarOutputStream.finish();
        outputStream.close();

        // Read test data from the generated output file
        byte[] generatedOutput = IOUtils.toByteArray(new FileInputStream(tempFile));

        // Compare the generated and expected output data
        assertArrayEquals(testData, generatedOutput);
    }

    /**
     * Test write method with a larger byte buffer than the remaining space in the archive.
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testWriteWithLargerByteBufferThanRemainingSpace() throws IOException {
        final byte[] testData = new byte[TarConstants.DEFAULT_BLKSIZE * 4];

        // Write a byte buffer to the output stream larger than the remaining space in the archive
        tarOutputStream.write(testData, 0, testData.length);

        // Finish the archive and close the output stream
        tarOutputStream.finish();
        outputStream.close();
    }
}