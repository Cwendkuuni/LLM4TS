package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private CpioArchiveEntry cpioEntry;

    @Before
    public void setup() throws Exception {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioEntry = new CpioArchiveEntry();
    }

    @After
    public void tearDown() throws Exception {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
    }

    @Test
    public void testConstructorWithFormat() throws IOException {
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY);
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, cpioArchiveOutputStream.getFormat());
    }

    @Test
    public void testConstructorWithoutFormat() throws IOException {
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 123);
    }

    @Test
    public void testPutNextEntry() throws IOException {
        cpioEntry.setName("test-file");
        cpioEntry.setSize(5);

        cpioArchiveOutputStream.putNextEntry(cpioEntry);

        assertEquals("test-file", cpioArchiveOutputStream.getCpioEntry().getName());
        assertEquals(5, cpioArchiveOutputStream.getCpioEntry().getSize());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateName() throws IOException {
        cpioArchiveOutputStream.putNextEntry(cpioEntry);
        cpioArchiveOutputStream.putNextEntry(cpioEntry);
    }

    @Test
    public void testWriteString() throws IOException {
        String testString = "test";
        cpioEntry.setName("test-file");
        cpioEntry.setSize(testString.length());

        cpioArchiveOutputStream.putNextEntry(cpioEntry);
        cpioArchiveOutputStream.write(testString.getBytes());

        byte[] bytes = outputStream.toByteArray();
        assertEquals(testString, new String(bytes, 0, testString.length()));
    }

    @Test
    public void testWriteArray() throws IOException {
        byte[] testBytes = {1, 2, 3};
        cpioEntry.setName("test-file");
        cpioEntry.setSize(testBytes.length);

        cpioArchiveOutputStream.putNextEntry(cpioEntry);
        cpioArchiveOutputStream.write(testBytes);

        byte[] bytes = outputStream.toByteArray();
        assertArrayEquals(testBytes, new byte[testBytes.length][], bytes, 0, testBytes.length);
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        cpioArchiveOutputStream.close();
        cpioArchiveOutputStream.write(1);
    }

    @Test
    public void testFinish() throws IOException {
        cpioArchiveOutputStream.putNextEntry(cpioEntry);
        cpioArchiveOutputStream.write("test".getBytes());
        cpioArchiveOutputStream.finish();

        byte[] bytes = outputStream.toByteArray();
        assertEquals(12 + "test".length(), bytes.length);
    }

    @Test
    public void testClose() throws IOException {
        cpioArchiveOutputStream.close();
        assertFalse(cpioArchiveOutputStream.isOpen());
    }

    @Test
    public void testGetCpioEntry() {
        cpioEntry.setName("test-file");
        cpioEntry.setSize(5);

        cpioArchiveOutputStream.putNextEntry(cpioEntry);

        CpioArchiveEntry entry = cpioArchiveOutputStream.getCpioEntry();

        assertEquals("test-file", entry.getName());
        assertEquals(5, entry.getSize());
    }

    @Test
    public void testGetFormat() {
        assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());
    }

    @Test
    public void testGetNames() {
        cpioEntry.setName("test-file-1");
        cpioArchiveOutputStream.putNextEntry(cpioEntry);

        cpioEntry.setName("test-file-2");
        cpioArchiveOutputStream.putNextEntry(cpioEntry);

        assertEquals(2, cpioArchiveOutputStream.getNames().size());

        assertSame("test-file-1", cpioArchiveOutputStream.getNames().get("test-file-1"));
        assertSame("test-file-2", cpioArchiveOutputStream.getNames().get("test-file-2"));
    }

    @Test
    public void testSetFormat() throws IOException {
        cpioArchiveOutputStream.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        assertEquals(CpioConstants.FORMAT_OLD_ASCII, cpioArchiveOutputStream.getFormat());
    }

    @Test(expected = IOException.class)
    public void testSetFormatWithInvalidFormat() throws IOException {
        cpioArchiveOutputStream.setFormat((short) 123);
    }

    @Test
    public void testGetWritten() throws IOException {
        cpioArchiveOutputStream.putNextEntry(cpioEntry);
        cpioArchiveOutputStream.write("test".getBytes());
        cpioArchiveOutputStream.finish();

        assertEquals(12 + "test".length(), cpioArchiveOutputStream.getWritten());
    }

    @Test
    public void testGetCrc() throws IOException {
        cpioArchiveOutputStream.putNextEntry(cpioEntry);
        cpioArchiveOutputStream.write("test".getBytes());
        cpioArchiveOutputStream.finish();

        assertEquals(CpioUtil.crc32("test"), cpioArchiveOutputStream.getCrc());
    }

    @Test
    public void testGetClosed() {
        assertFalse(cpioArchiveOutputStream.isClosed());

        cpioArchiveOutputStream.close();

        assertTrue(cpioArchiveOutputStream.isClosed());
    }

    @Test
    public void testGetFinished() {
        assertFalse(cpioArchiveOutputStream.isFinished());

        cpioArchiveOutputStream.finish();

        assertTrue(cpioArchiveOutputStream.isFinished());
    }
}