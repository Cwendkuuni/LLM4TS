import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.ZipLong;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[]{}))) {
            Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testGetNextZipEntry_normalFile() throws IOException {
        String testFile = "TestFile.zip";
        try (InputStream inputStream = this.getClass().getResourceAsStream(testFile);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream)) {

            // Write a test file
            zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("TestFile.txt"));
            zipArchiveOutputStream.write("Test file content".getBytes());
            zipArchiveOutputStream.finish();

            try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()))) {
                ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
                Assert.assertNotNull(entry);
                Assert.assertEquals("TestFile.txt", entry.getName());
                zipArchiveInputStream.close();
            }
        }
    }

    @Test
    public void testGetNextZipEntry_centralDirectory() throws IOException {
        String testFile = "TestFile.zip";
        try (InputStream inputStream = this.getClass().getResourceAsStream(testFile);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream)) {

            // Write a test file with a central directory
            zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("TestFile.txt"));
            zipArchiveOutputStream.write("Test file content".getBytes());
            zipArchiveOutputStream.finish();

            byte[] centralDirectorySignature = new byte[]{(byte) 0x04, (byte) 0x03, (byte) 0x4b, (byte) 0x50};
            zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("CentralDirectory.zip"));
            zipArchiveOutputStream.write(centralDirectorySignature, 0, centralDirectorySignature.length);

            zipArchiveOutputStream.finish();

            try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()))) {
                ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
                Assert.assertNull(entry);

                entry = zipArchiveInputStream.getNextZipEntry();
                Assert.assertNotNull(entry);
                Assert.assertEquals("CentralDirectory.zip", entry.getName());

                zipArchiveInputStream.close();
            }
        }
    }

    @Test
    public void testGetNextZipEntry_noEntry() throws IOException {
        String testFile = "TestFile.zip";
        try (InputStream inputStream = this.getClass().getResourceAsStream(testFile);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Write an empty zip file
            ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
            zipArchiveOutputStream.finish();

            try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()))) {
                ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
                Assert.assertNull(entry);
                zipArchiveInputStream.close();
            }
        }
    }

    @Test
    public void testGetNextZipEntry_invalidLocalFileHeader() throws IOException {
        String testFile = "TestFile.zip";
        try (InputStream inputStream = this.getClass().getResourceAsStream(testFile);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Write an invalid local file header
            byte[] invalidSignature = new byte[]{(byte) 0x04, (byte) 0x03, (byte) 0x4b, (byte) 0x4c}; // Wrong last byte
            byte[] lfh = new byte[31];
            System.arraycopy(invalidSignature, 0, lfh, 0, invalidSignature.length);
            zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("TestFile.txt"));
            zipArchiveOutputStream.write(lfh);

            zipArchiveOutputStream.write("Test file content".getBytes());
            zipArchiveOutputStream.finish();

            try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()))) {
                ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
                Assert.assertNull(entry);
                zipArchiveInputStream.close();
            }
        }
    }

    @Test(expected = EOFException.class)
    public void testGetNextZipEntry_endOfStream() throws IOException {
        String testFile = "TestFile.zip";
        try (InputStream inputStream = this.getClass().getResourceAsStream(testFile);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Write a test file
            ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
            zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("TestFile.txt"));
            zipArchiveOutputStream.write("Test file content".getBytes());
            zipArchiveOutputStream.finish();

            try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()))) {
                ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
                Assert.assertNotNull(entry);
                zipArchiveInputStream.getNextZipEntry(); // Should read the next entry

                // Consume the rest of the input stream
                byte[] buffer = new byte[1024];
                while (zipArchiveInputStream.read(buffer, 0, buffer.length) > 0) {
                }

                zipArchiveInputStream.getNextZipEntry(); // Read past EOF
            }
        }
    }

    @Test
    public void testRead() throws IOException {
        String testFile = "TestFile.zip";
        try (InputStream inputStream = this.getClass().getResourceAsStream(testFile);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Write a test file
            ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
            zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("TestFile.txt"));
            zipArchiveOutputStream.write("Test file content".getBytes());
            zipArchiveOutputStream.finish();

            try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
                 ByteArrayOutputStream resultStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int readBytes;

                readBytes = zipArchiveInputStream.read(buffer, 0, buffer.length);
                Assert.assertFalse(readBytes == -1);
                Assert.assertEquals("Test file content".length(), readBytes);

                // Check that the read data matches the original data
                resultStream.write(buffer, 0, readBytes);
                byte[] result = resultStream.toByteArray();
                Assert.assertArrayEquals("Test file content".getBytes(), result);

                zipArchiveInputStream.close();
            }
        }
    }

    @Test(expected = IOException.class)
    public void testRead_closed() throws IOException {
        String testFile = "TestFile.zip";
        try (InputStream inputStream = this.getClass().getResourceAsStream(testFile);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Write a test file
            ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
            zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("TestFile.txt"));
            zipArchiveOutputStream.write("Test file content".getBytes());
            zipArchiveOutputStream.finish();

            try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()))) {
                zipArchiveInputStream.close();
                zipArchiveInputStream.read(new byte[1024], 0, 1024);
            }
        }
    }

    @Test
    public void testClose() throws IOException {
        String testFile = "TestFile.zip";
        try (InputStream inputStream = this.getClass().getResourceAsStream(testFile);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Write a test file
            ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
            zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("TestFile.txt"));
            zipArchiveOutputStream.write("Test file content".getBytes());
            zipArchiveOutputStream.finish();

            try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()))) {
                zipArchiveInputStream.close();
            }
        }
    }

    @Test
    public void testSkip() throws IOException {
        String testFile = "TestFile.zip";
        try (InputStream inputStream = this.getClass().getResourceAsStream(testFile);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Write a test file
            ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
            zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("TestFile.txt"));
            zipArchiveOutputStream.write("Test file content".getBytes());
            zipArchiveOutputStream.finish();

            try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()))) {
                long length = zipArchiveInputStream.size();
                zipArchiveInputStream.skip(length);
                Assert.assertEquals(-1, zipArchiveInputStream.getNextEntry().getSize());
            }
        }
    }

    @Test
    public void testMatches() {
        byte[] signature1 = new byte[]{(byte) 0x04, (byte) 0x03, (byte) 0x4b, (byte) 0x50, (byte) 0x02, (byte) 0x01};
        byte[] signature2 = new byte[]{(byte) 0x04, (byte) 0x03, (byte) 0x4b, (byte) 0x50};

        Assert.assertTrue(ZipArchiveInputStreamTest.matches(signature1, signature1.length));
        Assert.assertTrue(ZipArchiveInputStreamTest.matches(signature2, signature2.length));
        Assert.assertFalse(ZipArchiveInputStreamTest.matches(signature1, signature2.length));
    }

    @Test(expected = IOException.class)
    public void testReadFully_closed() throws IOException {
        byte[] buffer = new byte[1024];
        try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0][0]))) {
            zipArchiveInputStream.readFully(buffer);
        }
    }

    @Test
    public void testReadFully() throws IOException {
        String testFile = "TestFile.zip";
        try (InputStream inputStream = this.getClass().getResourceAsStream(testFile);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Write a test file
            ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
            zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("TestFile.txt"));
            zipArchiveOutputStream.write("Test file content".getBytes());
            zipArchiveOutputStream.finish();

            try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()))) {
                byte[] buffer = new byte[1024];
                zipArchiveInputStream.readFully(buffer);

                // Check that the read data matches the original data
                Assert.assertEquals("Test file content".length(), buffer.length);
                Assert.assertArrayEquals("Test file content".getBytes(), buffer);
            }
        }
    }
}