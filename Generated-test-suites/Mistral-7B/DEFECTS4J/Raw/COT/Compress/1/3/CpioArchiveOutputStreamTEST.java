package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static org.apache.commons.compress.utils.CpioConstants.*;

public class CpioArchiveOutputStreamTest {

    private OutputStream testOutputStream;
    private CpioArchiveOutputStream testCpioOutputStream;

    @Before
    public void setUp() {
        testOutputStream = new ByteArrayOutputStream();
        testCpioOutputStream = new CpioArchiveOutputStream(testOutputStream, FORMAT_NEW);
    }

    @Test
    public void testConstructorNewFormat() {
        Assert.assertNotNull(testCpioOutputStream);
    }

    @Test
    public void testConstructorOldFormat() {
        CpioArchiveOutputStream testCpioOutputStreamOld = new CpioArchiveOutputStream(testOutputStream);
        Assert.assertNotNull(testCpioOutputStreamOld);
        Assert.assertEquals(FORMAT_OLD, testCpioOutputStreamOld.getFormat());
    }

    @Test
    public void testPutNextEntryWithValidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        testCpioOutputStream.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNullEntry() {
        testCpioOutputStream.putNextEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        testCpioOutputStream.putNextEntry(entry1);
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        testCpioOutputStream.putNextEntry(entry2);
        testCpioOutputStream.putNextEntry(entry1);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        testCpioOutputStream.setFormat((short) 0xFF);
        testCpioOutputStream.putNextEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] testBytes = "Hello World!".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        testCpioOutputStream.putNextEntry(entry);
        testCpioOutputStream.write(testBytes, 0, testBytes.length);
        testCpioOutputStream.closeArchiveEntry();
        byte[] outputBytes = ((ByteArrayOutputStream) testOutputStream).toByteArray();
        Assert.assertArrayEquals(testBytes, outputBytes);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        testCpioOutputStream.putNextEntry(entry);
        testCpioOutputStream.write(null, 0, 0);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeOffset() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        testCpioOutputStream.putNextEntry(entry);
        testCpioOutputStream.write(-1, 0, 1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeLength() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        testCpioOutputStream.putNextEntry(entry);
        testCpioOutputStream.write(new byte[1], -1, 1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        testCpioOutputStream.putNextEntry(entry);
        testCpioOutputStream.write(new byte[1], 0, 1);
        testCpioOutputStream.closeArchiveEntry();
        testCpioOutputStream.putNextEntry(null);
        testCpioOutputStream.write(new byte[1], 0, 1);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        testCpioOutputStream.putNextEntry(entry);
        testCpioOutputStream.write("Hello World!".getBytes());
        testCpioOutputStream.closeArchiveEntry();
        Assert.assertNull(testCpioOutputStream.getCpioEntry());
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        testCpioOutputStream.putNextEntry(entry);
        testCpioOutputStream.write("Hello World!".getBytes());
        testCpioOutputStream.finish();
        CpioArchiveEntry nextEntry = new CpioArchiveEntry();
        testCpioOutputStream.putNextEntry(nextEntry);
        testCpioOutputStream.write("Test Entry 2!".getBytes());
        testCpioOutputStream.finish();

        byte[] outputBytes = ((ByteArrayOutputStream) testOutputStream).toByteArray();
        HashMap names = testCpioOutputStream.getNames();
        Assert.assertEquals(2, names.size());
        Assert.assertEquals("Test Entry 2!", names.get(2));
        Assert.assertEquals("Hello World!", (String) names.get(1));

        // Check the first entry
        int position = 0;
        byte[] firstEntryBytes = new byte[ENTRY_HEADER_SIZE + 1 + "Hello World!".length()];
        System.arraycopy(outputBytes, position, firstEntryBytes, 0, firstEntryBytes.length);
        CpioArchiveEntry firstEntry = new CpioArchiveEntry();
        firstEntry.decodeHeader(firstEntryBytes);
        Assert.assertEquals(FORMAT_NEW, firstEntry.getFormat());
        Assert.assertEquals(NAME_DESCRIPTOR_LENGTH + 1, firstEntry.getName().length());
        Assert.assertEquals("Hello World!", firstEntry.getName());
        Assert.assertEquals(0, firstEntry.getDeviceMaj());
        Assert.assertEquals(0, firstEntry.getDeviceMin());
        Assert.assertEquals(0, firstEntry.getInode());
        Assert.assertEquals(0, firstEntry.getMode());
        Assert.assertEquals(0, firstEntry.getUID());
        Assert.assertEquals(0, firstEntry.getGID());
        Assert.assertEquals(1, firstEntry.getNumberOfLinks());
        Assert.assertEquals(10, firstEntry.getSize());
        Assert.assertEquals(0L, firstEntry.getRemoteDeviceMaj());
        Assert.assertEquals(0L, firstEntry.getRemoteDeviceMin());
        Assert.assertEquals(0L, firstEntry.getTime());
        Assert.assertEquals(0L, firstEntry.getChksum());

        // Check the second entry
        position += firstEntryBytes.length;
        byte[] secondEntryBytes = new byte[ENTRY_HEADER_SIZE + 1 + "Test Entry 2!".length()];
        System.arraycopy(outputBytes, position, secondEntryBytes, 0, secondEntryBytes.length);
        CpioArchiveEntry secondEntry = new CpioArchiveEntry();
        secondEntry.decodeHeader(secondEntryBytes);
        Assert.assertEquals(FORMAT_NEW, secondEntry.getFormat());
        Assert.assertEquals(NAME_DESCRIPTOR_LENGTH + 1, secondEntry.getName().length());
        Assert.assertEquals("Test Entry 2!", secondEntry.getName());
        Assert.assertEquals(0, secondEntry.getDeviceMaj());
        Assert.assertEquals(0, secondEntry.getDeviceMin());
        Assert.assertEquals(2, secondEntry.getInode());
        Assert.assertEquals(0, secondEntry.getMode());
        Assert.assertEquals(0, secondEntry.getUID());
        Assert.assertEquals(0, secondEntry.getGID());
        Assert.assertEquals(1, secondEntry.getNumberOfLinks());
        Assert.assertEquals(13, secondEntry.getSize());
        Assert.assertEquals(0L, secondEntry.getRemoteDeviceMaj());
        Assert.assertEquals(0L, secondEntry.getRemoteDeviceMin());
        Assert.assertEquals(0L, secondEntry.getTime());
        Assert.assertEquals(0L, secondEntry.getChksum());
    }
}