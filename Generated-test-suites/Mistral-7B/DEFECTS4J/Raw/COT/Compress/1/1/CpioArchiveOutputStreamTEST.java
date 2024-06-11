package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * JUnit test cases for CpioArchiveOutputStream class
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Test method for {@link CpioArchiveOutputStream#CpioArchiveOutputStream(OutputStream, short)}
     */
    @Test
    public void testCpioArchiveOutputStream() {
        OutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        assertNotNull(cpio);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)}
     */
    @Test
    public void testPutNextEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpio.putNextEntry(entry);
        assertEquals(MAGIC_NEW.length, out.size());
        assertFalse(cpio.isClosed());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)}
     */
    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        byte[] data = "Hello World!".getBytes();
        cpio.putNextEntry(new CpioArchiveEntry());
        cpio.write(data, 0, data.length);
        cpio.closeArchiveEntry();
        assertEquals(data.length + 1 + MAGIC_NEW.length, out.size());

        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());
        byte[] buffer = new byte[data.length];
        int len = input.read(buffer, 0, buffer.length);
        assertEquals(data.length, len);
        assertArrayEquals(data, buffer);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#close()}
     */
    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        cpio.close();
        assertTrue(cpio.isClosed());
        assertEquals(0, out.size());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#finish()}
     */
    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        cpio.putNextEntry(new CpioArchiveEntry());
        cpio.write("Hello World!".getBytes(), 0, "Hello World!".getBytes().length);
        cpio.finish();

        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());
        byte[] buffer = new byte[MAGIC_NEW.length + 1 + "Hello World!".getBytes().length + 1];
        int len = input.read(buffer, 0, buffer.length);
        assertEquals(len, buffer.length);
        assertArrayEquals(MAGIC_NEW, CpioUtil.byteArraySubsequence(buffer, 0, MAGIC_NEW.length));
        assertArrayEquals("Hello World!".getBytes(), CpioUtil.byteArraySubsequence(buffer, MAGIC_NEW.length + 1, "Hello World!".getBytes().length + 1));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)} with invalid input
     */
    @Test
    public void testPutNextEntryWithInvalidInput() {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        assertThrows(IOException.class, () -> cpio.putNextEntry(null));
        assertThrows(IOException.class, () -> cpio.putNextEntry(new CpioArchiveEntry()));
        assertThrows(IOException.class, () -> cpio.putNextEntry(new CpioArchiveEntry()), "Stream is already closed");
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(int)}
     */
    @Test
    public void testWriteInt() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        cpio.putNextEntry(new CpioArchiveEntry());
        cpio.write(0x41);
        cpio.closeArchiveEntry();
        assertEquals(1, out.size());
        assertEquals(0x41, out.toByteArray()[0]);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)}
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpio.putArchiveEntry(entry);
        assertEquals(MAGIC_NEW.length, out.size());
        assertFalse(cpio.isClosed());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#closeArchiveEntry()}
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpio.putNextEntry(entry);
        cpio.write("Hello World!".getBytes(), 0, "Hello World!".getBytes().length);
        cpio.closeArchiveEntry();
        assertEquals(1 + MAGIC_NEW.length, out.size());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#finish()} with no entries
     */
    @Test
    public void testFinishNoEntries() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        cpio.finish();

        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());
        byte[] buffer = new byte[MAGIC_NEW.length];
        int len = input.read(buffer, 0, buffer.length);
        assertEquals(len, buffer.length);
        assertArrayEquals(MAGIC_NEW, buffer);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#setFormat(short)} with invalid input
     */
    @Test
    public void testSetFormatWithInvalidInput() {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        assertThrows(IllegalArgumentException.class, () -> cpio.setFormat((short) 0xFF));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#ensureOpen()}
     */
    @Test
    public void testEnsureOpen() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        assertThrows(IOException.class, () -> cpio.ensureOpen());
        cpio.putNextEntry(new CpioArchiveEntry());
        assertFalse(cpio.isClosed());
        assertTrue(cpio.ensureOpen());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)} with duplicate entry name
     */
    @Test
    public void testPutNextEntryDuplicateEntryName() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test.txt");
        cpio.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test.txt");

        assertThrows(IOException.class, () -> cpio.putNextEntry(entry2), "Duplicate entry name: test.txt");
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#close()} with no entries
     */
    @Test
    public void testCloseNoEntries() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        cpio.close();
        assertTrue(cpio.isClosed());
        assertEquals(0, out.size());
    }
}