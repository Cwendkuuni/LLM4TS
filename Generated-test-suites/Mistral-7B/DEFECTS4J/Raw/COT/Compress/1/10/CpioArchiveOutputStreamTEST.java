package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Test cases for CpioArchiveOutputStream class
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private Map<String, CpioArchiveEntry> entries;

    @Before
    public void setUp() throws IOException {
        outputStream = new FilterOutputStream(new ByteArrayOutputStream());
        byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        entries = new HashMap<>();
    }

    @Test
    public void testConstructor() {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                new FilterOutputStream(new ByteArrayOutputStream()), CpioConstants.FORMAT_NEW);
        Assert.assertNotNull(cpioOutputStream);
    }

    @Test
    public void testPutNextEntry() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putNextEntry(entry);
        Assert.assertEquals("testfile", cpioOutputStream.cpioEntry.getName());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.putNextEntry(entry);
    }

    @Test
    public void testWriteHeader() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.writeHeader(entry);
        byte[] header = byteArrayOutputStream.toByteArray();
        byte[] expectedHeader = new byte[header.length];
        System.arraycopy(header, 0, expectedHeader, 0, header.length);
        byte[] newFormatHeader = CpioUtil.newFormatMagic.getBytes();
        System.arraycopy(newFormatHeader, 0, expectedHeader, 0, newFormatHeader.length);
        Assert.assertArrayEquals("Incorrect header", expectedHeader, header);
    }

    @Test
    public void testWriteAsciiLong() throws IOException {
        cpioOutputStream.writeAsciiLong(12345, 6, 10);
        byte[] expectedBytes = "12345\u0000\u0000\u0000\u0000\u0000".getBytes();
        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        Assert.assertArrayEquals("Incorrectly written AsciiLong", expectedBytes, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testWriteAsciiLongWithNegativeLength() throws IOException {
        cpioOutputStream.writeAsciiLong(-1, 8, 16);
    }

    @Test(expected = IOException.class)
    public void testWriteAsciiLongWithNegativeNumber() throws IOException {
        cpioOutputStream.writeAsciiLong(-1, 8, 16);
    }

    @Test
    public void testWriteCString() throws IOException {
        cpioOutputStream.writeCString("teststring");
        byte[] expectedBytes = "teststring\u0000".getBytes();
        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        Assert.assertArrayEquals("Incorrectly written CString", expectedBytes, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testWriteCStringWithNullName() throws IOException {
        cpioOutputStream.writeCString(null);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] testBytes = "testbytes".getBytes();
        cpioOutputStream.putNextEntry(new CpioArchiveEntry());
        cpioOutputStream.write(testBytes, 0, testBytes.length);
        Assert.assertEquals(testBytes.length, cpioOutputStream.written);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        cpioOutputStream.close();
        cpioOutputStream.write(1, 0, 1);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("testdata".getBytes());
        cpioOutputStream.closeArchiveEntry();
        Assert.assertNull(cpioOutputStream.cpioEntry);
        Assert.assertEquals(0, cpioOutputStream.written);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithNullEntry() throws IOException {
        cpioOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("testdata".getBytes());
        cpioOutputStream.finish();
        byte[] expectedTrailer = new byte[32];
        System.arraycopy(CpioUtil.magicTrailer.getBytes(), 0, expectedTrailer, 0, CpioUtil.magicTrailer.length);
        byte[] actualTrailer = byteArrayOutputStream.toByteArray();
        Assert.assertArrayEquals("Incorrect trailer", expectedTrailer, actualTrailer);
    }

    @Test(expected = IOException.class)
    public void testFinishWithClosedStream() throws IOException {
        cpioOutputStream.close();
        cpioOutputStream.finish();
    }

    @Test
    public void testPutArchiveEntry() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putArchiveEntry(entry);
        Assert.assertEquals("testfile", cpioOutputStream.cpioEntry.getName());
    }
}