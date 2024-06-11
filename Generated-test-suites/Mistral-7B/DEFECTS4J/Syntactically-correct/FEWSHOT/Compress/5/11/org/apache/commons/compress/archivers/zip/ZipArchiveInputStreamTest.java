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
import java.io.PushbackInputStream;
import java.util.zip.CRC32;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.ZipLong;

public class ZipArchiveInputStreamTest {

    private static final int BUFFER_SIZE = 128;

    @Test
    public void testMatches() {
        byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length];
        System.arraycopy(ZipArchiveOutputStream.LFH_SIG, 0, signature, 0, ZipArchiveOutputStream.LFH_SIG.length);
        assertTrue(ZipArchiveInputStream.matches(signature, ZipArchiveOutputStream.LFH_SIG.length));
        assertFalse(ZipArchiveInputStream.matches(signature, ZipArchiveOutputStream.LFH_SIG.length + 1));
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        byte[] bytes = createTestZipFileBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(input, null, false);

        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        assertEquals("test.txt", entry.getName());
        assertEquals(ZipArchiveOutputStream.DEFLATED, entry.getMethod());

        entry = zipInputStream.getNextZipEntry();
        assertNull(entry);

        entry = zipInputStream.getNextZipEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] bytes = createTestZipFileBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(input, null, false);

        ArchiveEntry archiveEntry = zipInputStream.getNextEntry();
        assertEquals("test.txt", archiveEntry.getName());
        assertTrue(archiveEntry instanceof ZipArchiveEntry);
        assertEquals(ZipArchiveOutputStream.DEFLATED, ((ZipArchiveEntry) archiveEntry).getMethod());

        archiveEntry = zipInputStream.getNextEntry();
        assertNull(archiveEntry);

        archiveEntry = zipInputStream.getNextEntry();
        assertNull(archiveEntry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] bytes = createTestZipFileBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(input, null, false);

        byte[] buffer = new byte[BUFFER_SIZE];
        int length = zipInputStream.read(buffer, 0, BUFFER_SIZE);
        assertEquals(-1, length);

        length = zipInputStream.read(buffer, 0, BUFFER_SIZE);
        assertEquals(ZipArchiveOutputStream.LFH_SIG.length, length);
        assertArrayEquals(ZipArchiveOutputStream.LFH_SIG, buffer, 0, ZipArchiveOutputStream.LFH_SIG.length);

        length = zipInputStream.read(buffer, 0, BUFFER_SIZE);
        assertArrayEquals(new byte[ZipArchiveOutputStream.LFH_LEN - ZipArchiveOutputStream.LFH_SIG.length], buffer, 0, ZipArchiveOutputStream.LFH_SIG.length, ZipArchiveOutputStream.LFH_LEN - ZipArchiveOutputStream.LFH_SIG.length);

        length = zipInputStream.read(buffer, 0, BUFFER_SIZE);
        assertArrayEquals(new byte[8], buffer, 0, 8);

        length = zipInputStream.read(buffer, 0, BUFFER_SIZE);
        assertEquals(3, length);
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);

        length = zipInputStream.read(buffer, 0, BUFFER_SIZE);
        assertEquals(4, length);
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('.', buffer[3]);

        length = zipInputStream.read(buffer, 0, BUFFER_SIZE);
        assertEquals(2, length);
        assertEquals('0', buffer[0]);
        assertEquals('x', buffer[1]);

        length = zipInputStream.read(buffer, 0, BUFFER_SIZE);
        assertEquals(2, length);
        assertEquals('0', buffer[0]);
        assertEquals('x', buffer[1]);

        length = zipInputStream.read(buffer, 0, BUFFER_SIZE);
        assertEquals(2, length);
        assertEquals('0', buffer[0]);
        assertEquals('0', buffer[1]);

        length = zipInputStream.read(buffer, 0, BUFFER_SIZE);
        assertArrayEquals(new byte[10], buffer, 0, 10);

        length = zipInputStream.read(buffer, 0, BUFFER_SIZE);
        assertEquals(17, length);
        assertEquals(' ', buffer[0]);
        assertEquals(' ', buffer[1]);
        assertEquals(' ', buffer[2]);
        assertEquals(' ', buffer[3]);
        assertEquals(' ', buffer[4]);
        assertEquals(' ', buffer[5]);
        assertEquals(' ', buffer[6]);
        assertEquals(' ', buffer[7]);
        assertEquals(' ', buffer[8]);
        assertEquals(' ', buffer[9]);
        assertEquals(' ', buffer[10]);
        assertEquals(' ', buffer[11]);
        assertEquals(' ', buffer[12]);
        assertEquals(' ', buffer[13]);
        assertEquals(' ', buffer[14]);
        assertEquals(' ', buffer[15]);
        assertEquals(' ', buffer[16]);

        length = zipInputStream.read(buffer, 0, BUFFER_SIZE);
        assertEquals(-1, length);
    }

    @Test
    public void testClose() throws IOException {
        byte[] bytes = createTestZipFileBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(input, null, false);
        zipInputStream.close();

        try {
            zipInputStream.getNextZipEntry();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCloseEntry() throws IOException {
        byte[] bytes = createTestZipFileBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(input, null, false);

        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        zipInputStream.closeEntry();

        try {
            zipInputStream.getNextZipEntry();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        entry = zipInputStream.getNextZipEntry();
        assertNull(entry);

        entry = zipInputStream.getNextZipEntry();
        assertNull(entry);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] bytes = createTestZipFileBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(input, null, false);

        long bytesSkipped = zipInputStream.skip(21 + ZipArchiveOutputStream.LFH_SIG.length);
        assertEquals(21, bytesSkipped);

        bytesSkipped = zipInputStream.skip(Long.MAX_VALUE - 21);
        assertEquals(Long.MAX_VALUE - 21, bytesSkipped);

        bytesSkipped = zipInputStream.skip(-1);
        assertEquals(0, bytesSkipped);
    }

    @Test
    public void testFill() throws IOException {
        byte[] bytes = new byte[10];
        InputStream input = new ByteArrayInputStream(new byte[1]);
        PushbackInputStream pushbackInput = new PushbackInputStream(input, bytes.length);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(pushbackInput, null, false);

        zipInputStream.fill();
        assertEquals(0, pushbackInput.available());

        pushbackInput.unread(bytes);
        zipInputStream.fill();
        assertEquals(bytes.length, pushbackInput.available());
    }

    private byte[] createTestZipFileBytes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream archiveOutputStream = new ZipArchiveOutputStream(outputStream, null, false);

        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipArchiveOutputStream.DEFLATED);
        archiveOutputStream.putNextEntry(entry);
        archiveOutputStream.write("This is a test file.", 0, "This is a test file.".length());
        archiveOutputStream.finishEntry();

        archiveOutputStream.close();

        return outputStream.toByteArray();
    }
}