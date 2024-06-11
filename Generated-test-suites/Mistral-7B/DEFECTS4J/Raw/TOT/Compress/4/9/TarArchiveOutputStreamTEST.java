import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private File tempFile;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("test-", ".tar");
    }

    @After
    public void tearDown() throws IOException {
        if (tempFile.exists()) {
            IOUtils.deleteQuietly(tempFile);
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(tempFile))) {

            TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "Test content.");
            tarOut.putArchiveEntry(archiveEntry);

            byte[] testBytes = "Test content.".getBytes(StandardCharsets.UTF_8);
            tarOut.write(testBytes, 0, testBytes.length);
            tarOut.closeArchiveEntry();

            tarOut.finish();
            tarOut.close();

            // Verify that the file was written correctly
            File outputFile = new File(tempFile.getAbsolutePath() + "/test.txt");
            byte[] readBytes = IOUtils.toByteArray(new FileInputStream(outputFile));
            assertArrayEquals(testBytes, readBytes);
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongFileName() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(tempFile))) {

            String longFileName = "test/long/path/test.txt";
            TarArchiveEntry archiveEntry = new TarArchiveEntry(longFileName, "Test content.");

            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            tarOut.putArchiveEntry(archiveEntry);

            byte[] testBytes = "Test content.".getBytes(StandardCharsets.UTF_8);
            tarOut.write(testBytes, 0, testBytes.length);
            tarOut.closeArchiveEntry();

            tarOut.finish();
            tarOut.close();
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongFileNameTruncate() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(tempFile))) {

            String longFileName = "test/long/path/test.txt";
            TarArchiveEntry archiveEntry = new TarArchiveEntry(longFileName, "Test content.");

            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
            tarOut.putArchiveEntry(archiveEntry);

            byte[] testBytes = "Test content.".getBytes(StandardCharsets.UTF_8);
            tarOut.write(testBytes, 0, testBytes.length);
            tarOut.closeArchiveEntry();

            tarOut.finish();
            tarOut.close();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseArchiveEntryBeforePutArchiveEntry() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(tempFile))) {

            TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "Test content.");
            tarOut.closeArchiveEntry();
            tarOut.putArchiveEntry(archiveEntry);

            byte[] testBytes = "Test content.".getBytes(StandardCharsets.UTF_8);
            tarOut.write(testBytes, 0, testBytes.length);

            tarOut.finish();
            tarOut.close();
        }
    }
}