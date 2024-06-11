package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_NEW);
    }

    @Test
    public void testConstructor() throws Exception {
        assertNotNull(cpioOutputStream);
    }

    @Test
    public void testPutNextEntry() throws Exception {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName);
        cpioOutputStream.putNextEntry(entry);
        assertNotNull(cpioOutputStream.getCpioEntry());
        assertEquals(entryName, cpioOutputStream.getCpioEntry().getName());
    }

    @Test
    public void testWrite() throws Exception {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName);
        String testData = "testData";

        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write(testData.getBytes());
        cpioOutputStream.closeArchiveEntry();

        byte[] actualData = ((ByteArrayOutputStream) outputStream).toByteArray();
        byte[] expectedData = (testData + "\0").getBytes();

        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testFinish() throws Exception {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("testData".getBytes());
        cpioOutputStream.finish();

        byte[] actualData = ((ByteArrayOutputStream) outputStream).toByteArray();
        byte[] expectedData = new byte[entry.getSize() + getCpioFileTrailerSize()];

        System.arraycopy(getCpioFileHeader(), 0, expectedData, 0, getCpioFileHeader().length);
        System.arraycopy(entry.getName().getBytes(), 0, expectedData, getCpioFileHeader().length, entry.getName().length() + 1);
        System.arraycopy("testData\0".getBytes(), 0, expectedData, getCpioFileHeader().length + entry.getName().length() + 1, "testData\0".length());

        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testClose() throws Exception {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.close();

        assertNull(cpioOutputStream.getCpioEntry());
        assertFalse(cpioOutputStream.isOpen());
    }

    @Test
    public void testWriteWithDuplicateEntry() throws Exception {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.closeArchiveEntry();

        assertThrows(IOException.class, () -> cpioOutputStream.putNextEntry(entry));
    }

    @Test
    public void testWriteWithNullEntry() throws Exception {
        assertThrows(NullPointerException.class, () -> cpioOutputStream.putNextEntry(null));
    }

    @Test
    public void testWriteWithInvalidFormat() throws Exception {
        cpioOutputStream = new CpioArchiveOutputStream(outputStream);

        assertThrows(IllegalArgumentException.class, () -> cpioOutputStream.putNextEntry(new CpioArchiveEntry("testEntry", -1)));
    }

    @Test
    public void testWriteWithInvalidSize() throws Exception {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName);

        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write(new byte[1024 * 1024 * 2]); // write more than maximum entry size

        assertThrows(IOException.class, () -> cpioOutputStream.closeArchiveEntry());
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws Exception {
        cpioOutputStream.close();
        cpioOutputStream.write("testData".getBytes());
    }

    @Test(expected = IOException.class)
    public void testWriteWithNoEntry() throws Exception {
        cpioOutputStream.write("testData".getBytes());
    }

    @Test
    public void testCloseWithUnwrittenData() throws Exception {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.close();

        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_NEW);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("testData".getBytes());
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.close();

        byte[] actualData = ((ByteArrayOutputStream) outputStream).toByteArray();
        byte[] expectedData = new byte[entry.getSize() + getCpioFileTrailerSize()];

        System.arraycopy(getCpioFileHeader(), 0, expectedData, 0, getCpioFileHeader().length);
        System.arraycopy(entry.getName().getBytes(), 0, expectedData, getCpioFileHeader().length, entry.getName().length() + 1);
        System.arraycopy("testData\0".getBytes(), 0, expectedData, getCpioFileHeader().length + entry.getName().length() + 1, "testData\0".length());

        assertArrayEquals(expectedData, actualData);
    }

    private byte[] getCpioFileHeader() {
        byte[] magicNew = CpioConstants.MAGIC_NEW;
        short format = (short) CpioArchiveOutputStream.FORMAT_NEW;
        return new byte[] { magicNew[0], magicNew[1], (byte) format, (byte) (format >> 8) };
    }

    private int getCpioFileTrailerSize() {
        int magicNewSize = CpioConstants.MAGIC_NEW.length;
        int nameSize = 1;
        int size = 1;
        short format = (short) CpioArchiveOutputStream.FORMAT_NEW;
        return magicNewSize + nameSize + size + Short.BYTES + Short.BYTES
                + Short.BYTES + Short.BYTES + Short.BYTES + Short.BYTES + Short.BYTES + Short.BYTES
                + Short.BYTES;
    }
}