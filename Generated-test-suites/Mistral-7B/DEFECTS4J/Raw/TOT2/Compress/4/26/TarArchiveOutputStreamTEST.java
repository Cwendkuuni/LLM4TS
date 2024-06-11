import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * TarArchiveOutputStreamTest.
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Given
        final String entryName = "test.txt";
        final byte[] entryContent = "Hello World!".getBytes(StandardCharsets.UTF_8);
        final File tempFile = new File("temp.tar");
        final OutputStream outputStream = new FileOutputStream(tempFile);
        final TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        // When
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("test.txt"), entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(entryContent, 0, entryContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        // Then
        Assert.assertFalse(tempFile.delete());

        try (FileInputStream inputStream = new FileInputStream(tempFile);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            String expectedContent = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);

            int pos = expectedContent.indexOf(entryName + TarConstants.HEADER_SEPARATOR);
            String header = expectedContent.substring(pos, pos + TarConstants.NAMELEN + TarConstants.FILESIZE_LEN + 1);
            long fileSize = Long.parseLong(header.substring(header.length() - TarConstants.FILESIZE_LEN, header.length()), 16);
            Assert.assertEquals(entryContent.length, fileSize);
            Assert.assertEquals(entryName, new String(expectedContent.substring(0, pos), StandardCharsets.UTF_8));
        }
    }

    @Test
    public void testLongFileName() throws IOException {
        // Given
        final String entryName = "long_file_name_test.txt";
        final byte[] entryContent = "Hello World!".getBytes(StandardCharsets.UTF_8);
        final File tempFile = new File("temp.tar");
        final OutputStream outputStream = new FileOutputStream(tempFile);
        final TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // When
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("test.txt"), entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(entryContent, 0, entryContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        // Then
        Assert.assertFalse(tempFile.delete());

        try (FileInputStream inputStream = new FileInputStream(tempFile);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            String expectedContent = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);

            int pos = expectedContent.indexOf(entryName + TarConstants.HEADER_SEPARATOR);
            String header = expectedContent.substring(pos, pos + TarConstants.NAMELEN + TarConstants.FILESIZE_LEN + 1);
            long fileSize = Long.parseLong(header.substring(header.length() - TarConstants.FILESIZE_LEN, header.length()), 16);
            Assert.assertEquals(entryContent.length, fileSize);
            Assert.assertEquals(entryName, new String(expectedContent.substring(0, pos), StandardCharsets.UTF_8));
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndErrorMode() throws IOException {
        // Given
        final String entryName = "long_file_name_test.txt";
        final byte[] entryContent = "Hello World!".getBytes(StandardCharsets.UTF_8);
        final File tempFile = new File("temp.tar");
        final OutputStream outputStream = new FileOutputStream(tempFile);
        final TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        // When & Then
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("test.txt"), entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        try {
            tarArchiveOutputStream.finish();
            tarArchiveOutputStream.close();
        } finally {
            Assert.assertTrue(tempFile.delete());
            throw new IOException("Test expected to fail.");
        }
    }

    @Test
    public void testWrite() throws IOException {
        // Given
        final String entryName = "test.txt";
        final byte[] entryContent = "Hello World!".getBytes(StandardCharsets.UTF_8);
        final byte[] expectedContent = new byte[entryContent.length + 1024];
        final File tempFile = new File("temp.tar");
        final OutputStream outputStream = new FileOutputStream(tempFile);
        final TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        // When
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("test.txt"), entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(entryContent, 0, entryContent.length);
        tarArchiveOutputStream.write(new byte[1024], 0, 1024);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        // Then
        Assert.assertFalse(tempFile.delete());

        try (FileInputStream inputStream = new FileInputStream(tempFile);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) > 0) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            String expectedContentStr = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
            Assert.assertEquals(expectedContentStr.substring(0, entryContent.length + 1024), new String(expectedContent, 0, entryContent.length + 1024));
        }
    }
}