package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

public class ZipArchiveInputStreamTest {
    @Test
    public void testNewInstance() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testNewInstanceWithEncodingAndUnicodeExtraFields() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String encoding = "UTF-8";
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, encoding, true);
        assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test(expected = NullPointerException.class)
    public void testNewInstanceWithNullInputStream() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNewInstanceWithNullEncoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String encoding = null;
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, encoding, true);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertNotNull(entry);

        ArchiveEntry nextEntry = zipArchiveInputStream.getNextZipEntry();
        assertNotNull(nextEntry);

        // Check that the last entry is the end of central directory
        ArchiveEntry endOfCentralDirectoryEntry = zipArchiveInputStream.getNextZipEntry();
        assertNull(endOfCentralDirectoryEntry);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testGetNextZipEntryWithEmptyFile() throws IOException {
        String testFile = "test_empty.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertNull(entry);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testGetNextZipEntryWithUnknownSignature() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(new byte[]{0x01, 0x01, 0x0b, 0x0b, 0x04, 0x03, 0x4b, 0x50, 0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});
        InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertNull(entry);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[10];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(bytesRead, 10);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadWithEOF() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[10];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(bytesRead, 10);

        zipArchiveInputStream.close();
        inputStream.close();

        inputStream = new FileInputStream(testFile);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(bytesRead, -1);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadWithNullBuffer() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        int bytesRead = zipArchiveInputStream.read(null, 0, 10);
        assertNull(bytesRead);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadWithBufferSizeLessThanZero() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        int bytesRead = zipArchiveInputStream.read(new byte[1], 0, -10);
        assertEquals(bytesRead, -1);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadWithBufferSizeGreaterThanAvailableBytes() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[20];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, 10);
        assertEquals(bytesRead, 10);

        zipArchiveInputStream.close();
        inputStream.close();

        inputStream = new FileInputStream(testFile);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        bytesRead = zipArchiveInputStream.read(buffer, 0, 20);
        assertEquals(bytesRead, 10);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCloseEntry() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();
        assertEquals(entry, zipArchiveInputStream.getNextZipEntry());

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCloseEntryWithNullEntry() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        zipArchiveInputStream.closeEntry();

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCloseEntryTwice() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();
        zipArchiveInputStream.closeEntry();
        assertSame(entry, zipArchiveInputStream.getNextZipEntry());

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        long skipped = zipArchiveInputStream.skip(10);
        assertEquals(skipped, 10);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testSkipWithNegativeValue() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        long skipped = zipArchiveInputStream.skip(-10);
        assertEquals(skipped, 0);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testSkipWithValueGreaterThanAvailableBytes() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        long skipped = zipArchiveInputStream.skip(Long.MAX_VALUE);
        assertEquals(skipped, Long.MAX_VALUE);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testFill() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[10];
        zipArchiveInputStream.fill();
        assertFalse(zipArchiveInputStream.inf.needsInput());

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testFillWithEOF() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[10];
        zipArchiveInputStream.fill();
        assertTrue(zipArchiveInputStream.inf.needsInput());

        zipArchiveInputStream.close();
        inputStream.close();

        inputStream = new FileInputStream(testFile);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        zipArchiveInputStream.fill();
        assertTrue(zipArchiveInputStream.inf.needsInput());

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadFully() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[10];
        zipArchiveInputStream.readFully(buffer);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = EOFException.class)
    public void testReadFullyWithEOF() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[10];
        zipArchiveInputStream.readFully(buffer);

        inputStream = new FileInputStream(testFile);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        zipArchiveInputStream.readFully(buffer);

        zipArchiveInputStream.close();
        inputStream.close();
    }
}