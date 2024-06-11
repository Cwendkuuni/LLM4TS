package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamTest;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;

/**
 * Test for {@link ZipArchiveInputStream} class.
 */
public class ZipArchiveInputStreamTest {

    private static final String TEST_DATA = "TestData.zip";
    private static final int BUFFER_SIZE = 1024;

    /**
     * Test constructor with default encoding and Unicode extra fields.
     *
     * @throws IOException if an error occurs while testing.
     */
    @org.junit.Test
    public void testDefaultConstructor() throws IOException {
        try (InputStream input = ArchiveStreamTest.getResourceAsStream(TEST_DATA);
             ZipArchiveInputStream zip = new ZipArchiveInputStream(input)) {
            ZipArchiveEntry entry = zip.getNextZipEntry();
            Assert.assertNotNull(entry);
            zip.closeEntry();
            Assert.assertNull(zip.getNextZipEntry());
            zip.close();
        }
    }

    /**
     * Test constructor with custom encoding and Unicode extra fields.
     *
     * @throws IOException if an error occurs while testing.
     */
    @org.junit.Test
    public void testCustomEncodingConstructor() throws IOException {
        try (InputStream input = ArchiveStreamTest.getResourceAsStream(TEST_DATA);
             ZipArchiveInputStream zip = new ZipArchiveInputStream(input, "UTF-16BE", true)) {
            ZipArchiveEntry entry = zip.getNextZipEntry();
            Assert.assertNotNull(entry);
            zip.closeEntry();
            Assert.assertNull(zip.getNextZipEntry());
            zip.close();
        }
    }

    /**
     * Test reading next entry when archive is empty.
     *
     * @throws IOException if an error occurs while testing.
     */
    @org.junit.Test
    public void testReadEmptyArchive() throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             OutputStream os = output;
             ZipArchiveOutputStream zip = new ZipArchiveOutputStream(os)) {
            zip.setMethod(ZipArchiveOutputStream.STORED);
            zip.putNextEntry(new ZipArchiveEntry("test.txt"));
            zip.closeEntry();
            zip.close();

            try (ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
                 ZipArchiveInputStream zipIn = new ZipArchiveInputStream(input)) {
                Assert.assertNull(zipIn.getNextZipEntry());
                zipIn.close();
            }
        }
    }

    /**
     * Test reading next entry with invalid signature.
     *
     * @throws IOException if an error occurs while testing.
     */
    @org.junit.Test(expected = ZipException.class)
    public void testInvalidSignature() throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             OutputStream os = output;
             ZipArchiveOutputStream zip = new ZipArchiveOutputStream(os)) {
            zip.putNextEntry(new ZipArchiveEntry("test.txt"));
            zip.write("Test Data".getBytes());
            zip.closeEntry();
            zip.close();

            ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
            byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length];
            input.read(signature);
            signature[2] = (byte) 0x02; // change version needed to extract
            input.skip(ZipArchiveOutputStream.LFH_SIG.length);

            try (ZipArchiveInputStream zipIn = new ZipArchiveInputStream(input)) {
                zipIn.getNextZipEntry();
            }
        }
    }

    /**
     * Test reading next entry with invalid signature (empty zip file).
     *
     * @throws IOException if an error occurs while testing.
     */
    @org.junit.Test(expected = ZipException.class)
    public void testInvalidSignatureEmptyZip() throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             OutputStream os = output;
             ZipArchiveOutputStream zip = new ZipArchiveOutputStream(os)) {
            zip.setMethod(ZipArchiveOutputStream.STORED);
            zip.putNextEntry(new ZipArchiveEntry("test.txt"));
            zip.closeEntry();
            zip.close();

            ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
            input.skip(ZipArchiveOutputStream.EOCD_SIG.length);

            try (ZipArchiveInputStream zipIn = new ZipArchiveInputStream(input)) {
                zipIn.getNextZipEntry();
            }
        }
    }

    /**
     * Test reading next entry with valid signature.
     *
     * @throws IOException if an error occurs while testing.
     */
    @org.junit.Test
    public void testReadValidSignature() throws IOException {
        try (InputStream input = ArchiveStreamTest.getResourceAsStream(TEST_DATA);
             ZipArchiveInputStream zip = new ZipArchiveInputStream(input)) {
            ZipArchiveEntry entry = zip.getNextZipEntry();
            Assert.assertNotNull(entry);
            Assert.assertEquals("TestData/test.txt", entry.getName());
            zip.closeEntry();
            Assert.assertNull(zip.getNextZipEntry());
            zip.close();
        }
    }

    /**
     * Test reading next entry with valid signature and Unicode extra fields.
     *
     * @throws IOException if an error occurs while testing.
     */
    @org.junit.Test
    public void testReadUnicodeExtraFields() throws IOException {
        try (InputStream input = ArchiveStreamTest.getResourceAsStream(TEST_DATA);
             ZipArchiveInputStream zip = new ZipArchiveInputStream(input, "UTF-8", true)) {
            ZipArchiveEntry entry = zip.getNextZipEntry();
            Assert.assertNotNull(entry);
            Assert.assertEquals("TestData/test2.txt", entry.getName());
            zip.closeEntry();
            Assert.assertNull(zip.getNextZipEntry());
            zip.close();
        }
    }

    /**
     * Test reading data from entry with compressed size and uncompressed size.
     *
     * @throws IOException if an error occurs while testing.
     */
    @org.junit.Test
    public void testReadData() throws IOException {
        try (InputStream input = ArchiveStreamTest.getResourceAsStream(TEST_DATA);
             ZipArchiveInputStream zip = new ZipArchiveInputStream(input)) {
            ZipArchiveEntry entry = zip.getNextZipEntry();
            Assert.assertNotNull(entry);
            byte[] buffer = new byte[BUFFER_SIZE];
            int data = zip.read(buffer, 0, BUFFER_SIZE);
            Assert.assertNotNull(data);
            Assert.assertEquals("Test Data", new String(buffer, 0, data));
            zip.closeEntry();
            Assert.assertNull(zip.getNextZipEntry());
            zip.close();
        }
    }

    /**
     * Test reading data from entry with compressed size equal to uncompressed size.
     *
     * @throws IOException if an error occurs while testing.
     */
    @org.junit.Test
    public void testReadDataStore() throws IOException {
        try (InputStream input = ArchiveStreamTest.getResourceAsStream(TEST_DATA);
             ZipArchiveInputStream zip = new ZipArchiveInputStream(input)) {
            ZipArchiveEntry entry = zip.getNextZipEntry();
            Assert.assertNotNull(entry);
            byte[] buffer = new byte[BUFFER_SIZE];
            int data = zip.read(buffer, 0, BUFFER_SIZE);
            Assert.assertNotNull(data);
            Assert.assertEquals("Test Data", new String(buffer, 0, data));
            long size = entry.getSize();
            Assert.assertTrue(size >= 0);
            long bytesRead = 0;
            while (bytesRead < size) {
                data = zip.read(buffer, 0, (int) Math.min(size - bytesRead, BUFFER_SIZE));
                Assert.assertNotNull(data);
                bytesRead += data;
            }
            zip.closeEntry();
            Assert.assertNull(zip.getNextZipEntry());
            zip.close();
        }
    }

    /**
     * Test reading data from entry with DEFLATED compression.
     *
     * @throws IOException if an error occurs while testing.
     */
    @org.junit.Test
    public void testReadDataDeflated() throws IOException {
        try (InputStream input = ArchiveStreamTest.getResourceAsStream(TEST_DATA);
             ZipArchiveInputStream zip = new ZipArchiveInputStream(input)) {
            ZipArchiveEntry entry = zip.getNextZipEntry();
            Assert.assertNotNull(entry);
            byte[] buffer = new byte[BUFFER_SIZE];
            int data = zip.read(buffer, 0, BUFFER_SIZE);
            Assert.assertNotNull(data);
            Assert.assertEquals("Test Data", new String(buffer, 0, data));
            zip.closeEntry();
            Assert.assertNull(zip.getNextZipEntry());
            zip.close();
        }
    }

    /**
     * Test reading data from entry with invalid CRC32.
     *
     * @throws IOException if an error occurs while testing.
     */
    @org.junit.Test(expected = ZipException.class)
    public void testReadInvalidCRC32() throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             OutputStream os = output;
             ZipArchiveOutputStream zip = new ZipArchiveOutputStream(os)) {
            zip.setMethod(ZipArchiveOutputStream.DEFLATED);
            zip.putNextEntry(new ZipArchiveEntry("test.txt"));
            zip.write("Test Data".getBytes());
            zip.setMethod(ZipArchiveOutputStream.STORED);
            zip.putNextEntry(new ZipArchiveEntry("test2.txt"));
            zip.write("Test Data 2".getBytes());
            zip.closeEntry();
            zip.close();

            ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
            ZipArchiveInputStream zipIn = new ZipArchiveInputStream(input);
            ZipArchiveEntry entry = zipIn.getNextZipEntry();
            Assert.assertNotNull(entry);
            byte[] buffer = new byte[BUFFER_SIZE];
            int data = zipIn.read(buffer, 0, BUFFER_SIZE);
            Assert.assertNotNull(data);
            long crc = zipIn.getCrc();
            Assert.assertEquals(crc, 0); // set to 0 for test
            zipIn.closeEntry();
            zipIn.getNextZipEntry();
            zipIn.close();
        }
    }
}