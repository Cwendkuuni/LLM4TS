import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testZipArchiveInputStreamConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    @Test
    public void testGetNextZipEntryWithValidLocalFileHeader() throws IOException {
        String testFile = "testFile.zip";
        try (InputStream inputStream = new FileInputStream(testFile)) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
            Assert.assertNotNull(entry);
            zipArchiveInputStream.close();
        }
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWithInvalidLocalFileHeader() throws IOException {
        byte[] invalidFileHeader = {(byte) 0x04, (byte) 0x03, (byte) 0x4B, (byte) 0x50,
                (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00};

        InputStream inputStream = new ByteArrayInputStream(invalidFileHeader);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWithCentralDirectorySignature() throws IOException {
        byte[] centralDirectorySignature = {(byte) 0x02, (byte) 0x01, (byte) 0x4B, (byte) 0x50,
                (byte) 0x03, (byte) 0x04, (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00};

        InputStream inputStream = new ByteArrayInputStream(centralDirectorySignature);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testReadWithClosedStream() throws IOException {
        String testFile = "testFile.zip";
        try (InputStream inputStream = new FileInputStream(testFile)) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            byte[] buffer = new byte[1];
            zipArchiveInputStream.close();
            zipArchiveInputStream.read(buffer, 0, 1);
        }
    }

    @Test
    public void testReadWithValidData() throws IOException, DataFormatException, ZipException {
        String testFile = "testFile.zip";
        try (InputStream inputStream = new FileInputStream(testFile)) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            byte[] buffer = new byte[1024];
            int actual = zipArchiveInputStream.read(buffer, 0, buffer.length);
            Assert.assertEquals(-1, actual);
            ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
            Assert.assertNotNull(entry);
            int dataRead = zipArchiveInputStream.read(buffer, 0, 1024);
            Assert.assertNotEquals(-1, dataRead);
            zipArchiveInputStream.close();
        }
    }

    @Test(expected = IOException.class)
    public void testReadWithInvalidData() throws IOException {
        byte[] invalidData = {(byte) 0x01, (byte) 0x02, (byte) 0x03};
        InputStream inputStream = new ByteArrayInputStream(invalidData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[1024];
        zipArchiveInputStream.read(buffer, 0, buffer.length);
    }

    @Test
    public void testCloseEntry() throws IOException {
        String testFile = "testFile.zip";
        try (InputStream inputStream = new FileInputStream(testFile)) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
            Assert.assertNotNull(entry);
            zipArchiveInputStream.closeEntry();
            entry = zipArchiveInputStream.getNextZipEntry();
            Assert.assertNull(entry);
            zipArchiveInputStream.close();
        }
    }

    @Test(expected = IOException.class)
    public void testCloseInputStreamAfterEndOfCentralDirectory() throws IOException {
        String testFile = "testFile.zip";
        try (InputStream inputStream = new FileInputStream(testFile)) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            while (zipArchiveInputStream.getNextZipEntry() != null) {
            }
            zipArchiveInputStream.close();
            zipArchiveInputStream.close();
        }
    }

    @Test
    public void testSkip() throws IOException {
        String testFile = "testFile.zip";
        try (InputStream inputStream = new FileInputStream(testFile)) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            long actual = zipArchiveInputStream.skip(Long.MAX_VALUE);
            Assert.assertEquals(Long.MAX_VALUE, actual);
            ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
            Assert.assertNotNull(entry);
            zipArchiveInputStream.close();
        }
    }
}