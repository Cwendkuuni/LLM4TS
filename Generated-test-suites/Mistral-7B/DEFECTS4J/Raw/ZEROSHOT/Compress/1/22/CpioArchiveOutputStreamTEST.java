package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Test method for {@link CpioArchiveOutputStream#CpioArchiveOutputStream(OutputStream, short)}
     */
    @org.junit.Test
    public void testCpioArchiveOutputStreamConstructor() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#CpioArchiveOutputStream(OutputStream)}
     */
    @org.junit.Test
    public void testCpioArchiveOutputStreamConstructorWithDefaultFormat() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out);
        assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#ensureOpen()}
     */
    @org.junit.Test
    public void testEnsureOpen() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out);
        cpioArchiveOutputStream.ensureOpen();
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)}
     */
    @org.junit.Test
    public void testPutNextEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putNextEntry(entry);

        assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());
        assertEquals(entry, cpioArchiveOutputStream.getCpioEntry());
        assertFalse(cpioArchiveOutputStream.isClosed());
        assertFalse(cpioArchiveOutputStream.isFinished());
        assertNull(cpioArchiveOutputStream.getNames().get(""));

        String name = "test";
        entry = new CpioArchiveEntry();
        entry.setName(name);
        cpioArchiveOutputStream.putNextEntry(entry);

        assertEquals(name, (String) cpioArchiveOutputStream.getNames().get(name));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)} with
     * invalid format
     */
    @org.junit.Test
    public void testPutNextEntryWithInvalidFormat() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat((short) 0xFF);
        assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putNextEntry(entry));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)} with
     * duplicate name
     */
    @org.junit.Test
    public void testPutNextEntryWithDuplicateName() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out);

        String name = "test";
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(name);
        cpioArchiveOutputStream.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(name);
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putNextEntry(entry2));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#closeArchiveEntry()}
     */
    @org.junit.Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.closeArchiveEntry();

        assertEquals(null, cpioArchiveOutputStream.getCpioEntry());
        assertTrue(cpioArchiveOutputStream.isClosed());
        assertFalse(cpioArchiveOutputStream.isFinished());
        assertNull(cpioArchiveOutputStream.getNames().get(""));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)}
     */
    @org.junit.Test
    public void testWrite() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "test".getBytes();

        cpioArchiveOutputStream.write(data, 0, data.length);

        assertEquals(data, out2.toByteArray());
        assertEquals(data.length, cpioArchiveOutputStream.written);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#finish()}
     */
    @org.junit.Test
    public void testFinish() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "test".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);

        cpioArchiveOutputStream.finish();

        assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());
        assertEquals(null, cpioArchiveOutputStream.getCpioEntry());
        assertTrue(cpioArchiveOutputStream.isFinished());
        assertFalse(cpioArchiveOutputStream.isClosed());
        assertNull(cpioArchiveOutputStream.getNames().get(""));

        byte[] expected = new byte[1 + 1 + data.length + 1];
        System.arraycopy(CpioUtil.long2byteArray(0, 1), 0, expected, 0, 1);
        expected[1] = (byte) 0x20;
        System.arraycopy(data, 0, expected, 2, data.length);
        expected[data.length + 1] = (byte) 0x00;

        assertEquals(expected, out2.toByteArray());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#close()}
     */
    @org.junit.Test
    public void testClose() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out);
        cpioArchiveOutputStream.close();

        assertEquals(null, cpioArchiveOutputStream.getCpioEntry());
        assertTrue(cpioArchiveOutputStream.isClosed());
        assertFalse(cpioArchiveOutputStream.isFinished());
        assertNull(cpioArchiveOutputStream.getNames().get(""));
    }
}