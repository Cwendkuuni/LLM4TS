import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.*;
import org.apache.commons.compress.archivers.cpio.*;
import org.apache.commons.compress.archivers.dump.*;
import org.apache.commons.compress.archivers.jar.*;
import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Test class for the ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    private static final String ARCHIVE_NAME = "test.tar";
    private static final String ARCHIVE_INPUT_STREAM_NAME = "test.tar.input";
    private static final String TEMP_FILE_NAME_1 = "temp1.txt";
    private static final String TEMP_FILE_NAME_2 = "temp2.txt";
    private static final String TEST_DATA_DIR = "testData";

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        // Given
        final String testFileContent1 = "Content of test file 1.";
        final String testFileContent2 = "Content of test file 2.";
        final File testDataDir = new File(TEST_DATA_DIR);

        // Create input files
        testDataDir.mkdirs();
        try (FileWriter writer1 = new FileWriter(new File(testDataDir, TEMP_FILE_NAME_1))) {
            writer1.write(testFileContent1, 0, testFileContent1.length());
        }
        try (FileWriter writer2 = new FileWriter(new File(testDataDir, TEMP_FILE_NAME_2))) {
            writer2.write(testFileContent2, 0, testFileContent2.length());
        }

        // When
        try (FileOutputStream outputStream = new FileOutputStream(ARCHIVE_INPUT_STREAM_NAME);
             ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                     .createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
             FileInputStream inputStream = new FileInputStream(ARCHIVE_INPUT_STREAM_NAME)) {

            // Then
            archiveOutputStream.putNextEntry(new TarArchiveEntry("temp1.txt"));
            try (InputStream testFileInputStream1 = new FileInputStream(new File(testDataDir, TEMP_FILE_NAME_1))) {
                IOUtils.copy(testFileInputStream1, archiveOutputStream);
            }
            archiveOutputStream.closeArchiveEntry();

            archiveOutputStream.putNextEntry(new TarArchiveEntry("temp2.txt"));
            try (InputStream testFileInputStream2 = new FileInputStream(new File(testDataDir, TEMP_FILE_NAME_2))) {
                IOUtils.copy(testFileInputStream2, archiveOutputStream);
            }
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            outputStream.close();

            // Test creating ArchiveInputStream from the created archive
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                    .createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
            Assert.assertNotNull(archiveInputStream);

            ArchiveEntry entry;
            int read;
            byte[] buffer = new byte[1024];

            // Test reading the first entry
            entry = archiveInputStream.getNextEntry();
            Assert.assertEquals("temp1.txt", entry.getName());
            while ((read = archiveInputStream.read(buffer, 0, buffer.length)) != -1) {
                String readData = new String(buffer, 0, read, StandardCharsets.UTF_8);
                Assert.assertEquals(testFileContent1, readData);
            }
            Assert.assertEquals("", archiveInputStream.readLine()); // EOF

            // Test reading the second entry
            entry = archiveInputStream.getNextEntry();
            Assert.assertEquals("temp2.txt", entry.getName());
            while ((read = archiveInputStream.read(buffer, 0, buffer.length)) != -1) {
                String readData = new String(buffer, 0, read, StandardCharsets.UTF_8);
                Assert.assertEquals(testFileContent2, readData);
            }
            Assert.assertEquals("", archiveInputStream.readLine()); // EOF

            // Test reading an empty entry
            entry = archiveInputStream.getNextEntry();
            Assert.assertNull(entry);

            // Clean up
            archiveInputStream.close();
            inputStream.close();
            Files.deleteIfExists(Paths.get(ARCHIVE_INPUT_STREAM_NAME));
            testDataDir.delete();
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws IOException {
        // Given
        final InputStream inputStream = new ByteArrayInputStream("Invalid Archive".getBytes());

        // When
        new ArchiveStreamFactory().createArchiveInputStream("INVALID", inputStream);

        // Then
        // Assertion not needed since test fails with expected ArchiveException
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        // Given
        final String testFileContent = "Content of test file.";
        final File testDataDir = new File(TEST_DATA_DIR);

        // Create output file
        testDataDir.mkdirs();
        try (FileOutputStream outputStream = new FileOutputStream(ARCHIVE_NAME);
             ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                     .createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream)) {

            // Then
            archiveOutputStream.putNextEntry(new TarArchiveEntry("testFile.txt"));
            try (OutputStream outputStreamEntry = archiveOutputStream.getOutputStream()) {
                outputStreamEntry.write(testFileContent.getBytes());
            }
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            outputStream.close();

            // Test creating ArchiveInputStream from the created archive
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                    .createArchiveInputStream(ArchiveStreamFactory.TAR, new FileInputStream(ARCHIVE_NAME));
            Assert.assertNotNull(archiveInputStream);

            ArchiveEntry entry;
            int read;
            byte[] buffer = new byte[1024];

            // Test reading the entry
            entry = archiveInputStream.getNextEntry();
            Assert.assertEquals("testFile.txt", entry.getName());
            while ((read = archiveInputStream.read(buffer, 0, buffer.length)) != -1) {
                String readData = new String(buffer, 0, read, StandardCharsets.UTF_8);
                Assert.assertEquals(testFileContent, readData);
            }
            Assert.assertEquals("", archiveInputStream.readLine()); // EOF

            // Clean up
            archiveInputStream.close();
            Files.deleteIfExists(Paths.get(ARCHIVE_NAME));
            testDataDir.delete();
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        // Given

        // When
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, null);

        // Then
        // Assertion not needed since test fails with expected NullPointerException
    }
}