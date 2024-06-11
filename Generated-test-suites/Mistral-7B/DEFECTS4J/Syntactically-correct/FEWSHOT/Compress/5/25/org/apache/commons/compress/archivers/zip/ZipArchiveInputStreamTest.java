package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveEntry;
import org.apache.commons.compress.utils.ZipLong;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {
    private static final byte[] LFH_SIG = new byte[]{
            0x04, 0x03, 0x4b, 0x50,
            0x00, 0x00, 0x00, 0x02,
            0x08, 0x07, 0x00, 0x00,
            0x00, 0x00, 0x0c, 0xcc,
            0x21, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x02, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x02,
            0x04, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x04,
            0x00, 0x00, 0x00, 0x04,
            0x00, 0x00, 0x00, 0x02,
            0x00, 0x00, 0x00, 0x00
    };

    private static final byte[] EOCD_SIG = new byte[]{
            0x06, 0x05, 0x4e, 0x4b,
            0x01, 0x02, 0x00, 0x00
    };

    private static final int LFH_LEN = LFH_SIG.length;

    @Test
    public void testMatchesSignature() {
        assertTrue(ZipArchiveInputStream.matches(LFH_SIG, LFH_SIG.length));
        assertTrue(ZipArchiveInputStream.matches(EOCD_SIG, EOCD_SIG.length));
        assertFalse(ZipArchiveInputStream.matches(LFH_SIG, EOCD_SIG.length));
    }

    @Test
    public void testNewInstance() {
        InputStream inputStream = new ByteArrayInputStream(LFH_SIG);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        assertNull(zipArchiveInputStream.getNextZipEntry());
        inputStream.close();
    }

    @Test
    public void testNewInstanceUTF8() {
        InputStream inputStream = new ByteArrayInputStream(LFH_SIG);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        assertNull(zipArchiveInputStream.getNextZipEntry());
        inputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceNullEncoding() {
        InputStream inputStream = new ByteArrayInputStream(LFH_SIG);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, true);
        inputStream.close();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        byte[] buffer = new byte[ZipArchiveOutputStream.BUFFER_SIZE];
        InputStream inputStream = new ByteArrayInputStream(LFH_SIG);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertEquals("org.apache.commons.compress.archivers.zip.ZipArchiveEntry", entry.getClass().getName());
        assertNull(entry);
        inputStream.close();

        // Test with a file that has a Local File Header (LFH)
        byte[] lfh = new byte[LFH_LEN];
        System.arraycopy(LFH_SIG, 0, lfh, 0, LFH_SIG.length);
        inputStream = new ByteArrayInputStream(lfh);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        entry = zipArchiveInputStream.getNextZipEntry();
        assertNotNull(entry);
        assertEquals("testEntry.txt", entry.getName());
        assertSame(ZipArchiveEntry.STORED, entry.getMethod());
        inputStream.close();

        // Test with a Local File Header that does not have a file name
        byte[] lfhNoName = new byte[LFH_LEN];
        System.arraycopy(LFH_SIG, 0, lfhNoName, 0, LFH_SIG.length);
        inputStream = new ByteArrayInputStream(lfhNoName);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        entry = zipArchiveInputStream.getNextZipEntry();
        assertNull(entry);
        inputStream.close();

        // Test with an End of Central Directory Record (EOCD)
        byte[] eocd = new byte[] {0x00, 0x04, 0x54, 0x50, 0x07, 0x00, 0x00, 0x00, 0x14, 0x40, 0x00, 0x02, 0x00, 0x00};
        inputStream = new ByteArrayInputStream(eocd);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        entry = zipArchiveInputStream.getNextZipEntry();
        assertNull(entry);
        inputStream.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] buffer = new byte[ZipArchiveOutputStream.BUFFER_SIZE];
        InputStream inputStream = new ByteArrayInputStream(LFH_SIG);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        ArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        assertNull(entry);
        inputStream.close();

        // Test with a file that has a Local File Header (LFH)
        byte[] lfh = new byte[LFH_LEN];
        System.arraycopy(LFH_SIG, 0, lfh, 0, LFH_SIG.length);
        inputStream = new ByteArrayInputStream(lfh);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        entry = zipArchiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("testEntry.txt", entry.getName());
        assertSame(ZipArchiveEntry.STORED, entry.getMethod());
        inputStream.close();

        // Test with a Local File Header that does not have a file name
        byte[] lfhNoName = new byte[LFH_LEN];
        System.arraycopy(LFH_SIG, 0, lfhNoName, 0, LFH_SIG.length);
        inputStream = new ByteArrayInputStream(lfhNoName);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        entry = zipArchiveInputStream.getNextEntry();
        assertNull(entry);
        inputStream.close();

        // Test with an End of Central Directory Record (EOCD)
        byte[] eocd = new byte[] {0x00, 0x04, 0x54, 0x50, 0x07, 0x00, 0x00, 0x00, 0x14, 0x40, 0x00, 0x02, 0x00, 0x00};
        inputStream = new ByteArrayInputStream(eocd);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        entry = zipArchiveInputStream.getNextEntry();
        assertNull(entry);
        inputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        byte[] input = new byte[] {0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(input);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        ArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        assertEquals("testEntry.txt", entry.getName());
        assertSame(ZipArchiveEntry.STORED, entry.getMethod());
        int read = zipArchiveInputStream.read(new byte[3]);
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, input);
        assertEquals(3, read);
        inputStream.close();
    }

    @Test
    public void testCloseEntry() throws IOException {
        byte[] buffer = new byte[ZipArchiveOutputStream.BUFFER_SIZE];
        byte[] input = new byte[] {0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(input);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        ArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        assertEquals("testEntry.txt", entry.getName());
        assertSame(ZipArchiveEntry.STORED, entry.getMethod());
        int read = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(input.length, read);
        zipArchiveInputStream.closeEntry();
        assertNull(zipArchiveInputStream.getNextEntry());
        inputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] buffer = new byte[ZipArchiveOutputStream.BUFFER_SIZE];
        byte[] input = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(input);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        ArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        assertEquals("testEntry.txt", entry.getName());
        assertSame(ZipArchiveEntry.STORED, entry.getMethod());
        int read = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(input.length, read);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] input = new byte[] {0x01, 0x02, 0x03, 0x01, 0x02, 0x03, 0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(input);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        ArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        assertNotNull(entry);
        long skipped = zipArchiveInputStream.skip(12);
        assertEquals(12, skipped);
        int read = zipArchiveInputStream.read(new byte[3]);
        assertEquals(3, read);
        inputStream.close();
    }

    @Test
    public void testCloseAfterEndOfStream() throws IOException {
        byte[] input = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(input);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8", true);
        ArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        assertEquals("testEntry.txt", entry.getName());
        assertSame(ZipArchiveEntry.STORED, entry.getMethod());
        int read = zipArchiveInputStream.read(new byte[input.length]);
        assertEquals(input.length, read);
        zipArchiveInputStream.close();
        inputStream.close();
    }
}