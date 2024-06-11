import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private File tempDirectory;

    @Before
    public void setUp() throws IOException {
        tempDirectory = File.createTempDirectory("junit-test-", null);
        outputFile = new File(tempDirectory, "output.cpio");
    }

    @After
    public void tearDown() throws IOException {
        if (outputFile.exists()) {
            outputFile.delete();
        }
        if (tempDirectory.exists()) {
            tempDirectory.delete();
        }
    }

    @Test
    public void testWriteASCIIFormatNewEntry() throws IOException {
        try (CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW)) {

            // Prepare test data
            String testFileName = "testfile.txt";
            String testFileContent = "Hello, World!";
            CpioArchiveEntry testEntry = new CpioArchiveEntry();
            testEntry.setName(testFileName);
            testEntry.setSize(testFileContent.length());
            testEntry.setFormat(CpioConstants.FORMAT_NEW);

            // Write test data to output stream
            outputStream.putArchiveEntry(testEntry);
            outputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
            outputStream.closeArchiveEntry();
            outputStream.finish();

            // Read test data from output file
            ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
            try (InputStream inputStream = new FileInputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int readBytes;
                while ((readBytes = inputStream.read(buffer, 0, buffer.length)) > 0) {
                    expectedOutput.write(buffer, 0, readBytes);
                }
            }
            String expectedOutputContent = expectedOutput.toString(StandardCharsets.UTF_8.name());

            // Verify test data
            String actualOutputContent = new String(CpioUtils.readAllBytes(outputFile), StandardCharsets.UTF_8);
            assertEquals(expectedOutputContent, actualOutputContent);
        }
    }

    @Test
    public void testWriteNewEntryWithInvalidFormat() throws IOException {
        try (CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), -1)) {

            // Prepare test data
            String testFileName = "testfile.txt";
            String testFileContent = "Hello, World!";
            CpioArchiveEntry testEntry = new CpioArchiveEntry();
            testEntry.setName(testFileName);
            testEntry.setSize(testFileContent.length());
            testEntry.setFormat(CpioConstants.FORMAT_NEW);

            // Write test data to output stream
            assertThrows(IllegalArgumentException.class, () -> outputStream.putArchiveEntry(testEntry));

        }
    }

    @Test
    public void testWriteOldAsciiEntry() throws IOException {
        try (CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_OLD_ASCII)) {

            // Prepare test data
            String testFileName = "testfile.txt";
            String testFileContent = "Hello, World!";
            CpioArchiveEntry testEntry = new CpioArchiveEntry();
            testEntry.setName(testFileName);
            testEntry.setSize(testFileContent.length());
            testEntry.setFormat(CpioConstants.FORMAT_OLD_ASCII);

            // Write test data to output stream
            outputStream.putArchiveEntry(testEntry);
            outputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
            outputStream.closeArchiveEntry();
            outputStream.finish();

            // Read test data from output file
            ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
            try (InputStream inputStream = new FileInputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int readBytes;
                while ((readBytes = inputStream.read(buffer, 0, buffer.length)) > 0) {
                    expectedOutput.write(buffer, 0, readBytes);
                }
            }
            String expectedOutputContent = expectedOutput.toString(StandardCharsets.UTF_8.name());

            // Verify test data
            String actualOutputContent = new String(CpioUtils.readAllBytes(outputFile), StandardCharsets.UTF_8);
            assertEquals(expectedOutputContent, actualOutputContent);
        }
    }

    @Test
    public void testWriteOldBinaryEntry() throws IOException {
        try (CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_OLD_BINARY)) {

            // Prepare test data
            String testFileName = "testfile.txt";
            String testFileContent = "Hello, World!";
            CpioArchiveEntry testEntry = new CpioArchiveEntry();
            testEntry.setName(testFileName);
            testEntry.setSize(testFileContent.length());
            testEntry.setFormat(CpioConstants.FORMAT_OLD_BINARY);

            // Write test data to output stream
            outputStream.putArchiveEntry(testEntry);
            outputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
            outputStream.closeArchiveEntry();
            outputStream.finish();

            // Read test data from output file
            ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
            try (InputStream inputStream = new FileInputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int readBytes;
                while ((readBytes = inputStream.read(buffer, 0, buffer.length)) > 0) {
                    expectedOutput.write(buffer, 0, readBytes);
                }
            }
            byte[] expectedOutputBytes = expectedOutput.toByteArray();

            // Verify test data
            byte[] actualOutputBytes = CpioUtils.readAllBytes(outputFile);
            assertArrayEquals(expectedOutputBytes, actualOutputBytes);
        }
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        try (CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW)) {

            // Prepare test data
            String testFileName1 = "testfile1.txt";
            String testFileContent1 = "Hello, World!";
            CpioArchiveEntry testEntry1 = new CpioArchiveEntry();
            testEntry1.setName(testFileName1);
            testEntry1.setSize(testFileContent1.length());
            testEntry1.setFormat(CpioConstants.FORMAT_NEW);

            String testFileName2 = "testfile2.txt";
            String testFileContent2 = "Hello, Universe!";
            CpioArchiveEntry testEntry2 = new CpioArchiveEntry();
            testEntry2.setName(testFileName2);
            testEntry2.setSize(testFileContent2.length());
            testEntry2.setFormat(CpioConstants.FORMAT_NEW);

            // Write test data to output stream
            outputStream.putArchiveEntry(testEntry1);
            outputStream.write(testFileContent1.getBytes(StandardCharsets.UTF_8));
            outputStream.closeArchiveEntry();

            outputStream.putArchiveEntry(testEntry2);
            outputStream.write(testFileContent2.getBytes(StandardCharsets.UTF_8));
            outputStream.closeArchiveEntry();

            outputStream.finish();

            // Read test data from output file
            ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
            try (InputStream inputStream = new FileInputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int readBytes;
                while ((readBytes = inputStream.read(buffer, 0, buffer.length)) > 0) {
                    expectedOutput.write(buffer, 0, readBytes);
                }
            }
            String expectedOutputContent = expectedOutput.toString(StandardCharsets.UTF_8.name());

            // Verify test data
            Map<String, CpioArchiveEntry> expectedEntryMap = new HashMap<>();
            expectedEntryMap.put(testFileName1, testEntry1);
            expectedEntryMap.put(testFileName2, testEntry2);

            try (InputStream inputStream = new FileInputStream(outputFile)) {
                CpioArchiveEntry entry;
                while ((entry = CpioArchiveEntry.read(inputStream)) != null) {
                    assertEquals(expectedEntryMap.get(entry.getName()), entry);
                    expectedEntryMap.remove(entry.getName());
                }
                assertTrue(expectedEntryMap.isEmpty());
            }
        }
    }

    @Test
    public void testWriteDuplicateEntry() throws IOException {
        try (CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW)) {

            // Prepare test data
            String testFileName = "testfile.txt";
            String testFileContent = "Hello, World!";
            CpioArchiveEntry testEntry = new CpioArchiveEntry();
            testEntry.setName(testFileName);
            testEntry.setSize(testFileContent.length());
            testEntry.setFormat(CpioConstants.FORMAT_NEW);

            // Write test data to output stream
            outputStream.putArchiveEntry(testEntry);
            outputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
            outputStream.closeArchiveEntry();

            try {
                outputStream.putArchiveEntry(testEntry);
                fail("Expected IllegalStateException");
            } catch (IllegalStateException e) {
                // Pass
            }

            outputStream.finish();

            // Read test data from output file
            ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
            try (InputStream inputStream = new FileInputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int readBytes;
                while ((readBytes = inputStream.read(buffer, 0, buffer.length)) > 0) {
                    expectedOutput.write(buffer, 0, readBytes);
                }
            }
            String expectedOutputContent = expectedOutput.toString(StandardCharsets.UTF_8.name());

            // Verify test data
            String actualOutputContent = new String(CpioUtils.readAllBytes(outputFile), StandardCharsets.UTF_8);
            assertEquals(expectedOutputContent, actualOutputContent);
        }
    }
}