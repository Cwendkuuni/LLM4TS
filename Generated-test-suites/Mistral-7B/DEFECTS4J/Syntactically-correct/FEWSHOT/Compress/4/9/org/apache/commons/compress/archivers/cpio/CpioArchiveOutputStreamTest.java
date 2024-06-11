package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStreamCreate() {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new ByteArrayOutputStream(1024), FORMAT_NEW);
        assertNotNull(outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCpioArchiveOutputStreamCreateWithInvalidFormat() {
        new CpioArchiveOutputStream(new ByteArrayOutputStream(1024), (short) 0xFF);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new ByteArrayOutputStream(1024), FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        outputStream.putArchiveEntry(entry);
        assertNotNull(entry);
        assertEquals("testFile.txt", entry.getName());
        assertEquals(FORMAT_NEW, entry.getFormat());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithAlreadyAddedEntry() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new ByteArrayOutputStream(1024), FORMAT_NEW);
        CpioArchiveEntry entry1 = new CpioArchiveEntry("testFile1.txt");
        CpioArchiveEntry entry2 = new CpioArchiveEntry("testFile1.txt");
        outputStream.putArchiveEntry(entry1);
        outputStream.putArchiveEntry(entry2);
    }

    @Test
    public void testWriteHeader() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                new FilterOutputStream(outputStream), FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.writeHeader(entry);
        byte[] magicNewBytes = ArchiveUtils.toAsciiBytes(MAGIC_NEW);
        byte[] headerBytes = outputStream.toByteArray();
        assertTrue(Arrays.equals(magicNewBytes, Arrays.copyOfRange(headerBytes, 0, 8)));
    }

    @Test
    public void testWriteNewEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                new FilterOutputStream(outputStream), FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.writeNewEntry(entry);
        byte[] data = outputStream.toByteArray();
        assertEquals(32, data.length);
        byte[] inodeBytes = Arrays.copyOfRange(data, 0, 8);
        byte[] modeBytes = Arrays.copyOfRange(data, 8, 16);
        byte[] uidBytes = Arrays.copyOfRange(data, 16, 24);
        byte[] gidBytes = Arrays.copyOfRange(data, 24, 32);
        assertEquals(-1, Arrays.binarySearch(inodeBytes, (byte) 0xFF));
        assertEquals((byte) 0755, Arrays.getByte(modeBytes, 0));
        assertEquals((byte) 0, Arrays.getByte(uidBytes, 0));
        assertEquals((byte) 0, Arrays.getByte(gidBytes, 0));
    }

    @Test
    public void testWriteOldAsciiEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                new FilterOutputStream(outputStream), FORMAT_OLD_ASCII);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.writeOldAsciiEntry(entry);
        byte[] data = outputStream.toByteArray();
        assertEquals(32, data.length);
        byte[] deviceBytes = Arrays.copyOfRange(data, 0, 6);
        byte[] inodeBytes = Arrays.copyOfRange(data, 6, 12);
        byte[] modeBytes = Arrays.copyOfRange(data, 12, 18);
        byte[] uidBytes = Arrays.copyOfRange(data, 18, 24);
        byte[] gidBytes = Arrays.copyOfRange(data, 24, 30);
        byte[] nameLengthBytes = Arrays.copyOfRange(data, 30, 34);
        byte[] sizeBytes = Arrays.copyOfRange(data, 34, 42);
        byte[] nameBytes = Arrays.copyOfRange(data, 42, data.length);
        assertEquals((byte) 0, Arrays.getByte(deviceBytes, 0));
        assertEquals((byte) 0, Arrays.getByte(deviceBytes, 1));
        assertEquals((byte) 0, Arrays.getByte(deviceBytes, 2));
        assertEquals((byte) 0, Arrays.getByte(deviceBytes, 3));
        assertEquals((byte) 0, Arrays.getByte(inodeBytes, 0));
        assertEquals((byte) 0, Arrays.getByte(inodeBytes, 1));
        assertEquals((byte) 0, Arrays.getByte(inodeBytes, 2));
        assertEquals((byte) 0, Arrays.getByte(inodeBytes, 3));
        assertEquals((byte) 0755, Arrays.getByte(modeBytes, 0));
        assertEquals((byte) 0, Arrays.getByte(modeBytes, 1));
        assertEquals((byte) 0, Arrays.getByte(modeBytes, 2));
        assertEquals((byte) 0, Arrays.getByte(modeBytes, 3));
        assertEquals((byte) 0, Arrays.getByte(uidBytes, 0));
        assertEquals((byte) 0, Arrays.getByte(uidBytes, 1));
        assertEquals((byte) 0, Arrays.getByte(uidBytes, 2));
        assertEquals((byte) 0, Arrays.getByte(uidBytes, 3));
        assertEquals((byte) 0, Arrays.getByte(gidBytes, 0));
        assertEquals((byte) 0, Arrays.getByte(gidBytes, 1));
        assertEquals((byte) 0, Arrays.getByte(gidBytes, 2));
        assertEquals((byte) 0, Arrays.getByte(gidBytes, 3));
        assertEquals((byte) 0x01, Arrays.getByte(nameLengthBytes, 0));
        assertEquals((byte) 0x01, Arrays.getByte(nameLengthBytes, 1));
        assertEquals((byte) 0x00, Arrays.getByte(sizeBytes, 0));
        assertEquals((byte) 0x00, Arrays.getByte(sizeBytes, 1));
        assertEquals((byte) 0x00, Arrays.getByte(sizeBytes, 2));
        assertEquals((byte) 0x00, Arrays.getByte(sizeBytes, 3));
        assertEquals("", new String(nameBytes, 0, nameBytes.length - 1));
    }

    @Test
    public void testWriteOldBinaryEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                new FilterOutputStream(outputStream), FORMAT_OLD_BINARY);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.writeOldBinaryEntry(entry, true);
        byte[] data = outputStream.toByteArray();
        assertEquals(32, data.length);
        byte[] deviceBytes = Arrays.copyOfRange(data, 0, 8);
        byte[] inodeBytes = Arrays.copyOfRange(data, 8, 16);
        byte[] modeBytes = Arrays.copyOfRange(data, 16, 24);
        byte[] uidBytes = Arrays.copyOfRange(data, 24, 32);
        byte[] gidBytes = Arrays.copyOfRange(data, 32, 40);
        byte[] nameLengthBytes = Arrays.copyOfRange(data, 40, 44);
        byte[] sizeBytes = Arrays.copyOfRange(data, 44, 52);
        byte[] nameBytes = Arrays.copyOfRange(data, 52, data.length);
        assertEquals((short) 0, CpioUtil.bytes2Short(deviceBytes));
        assertEquals((short) 0, CpioUtil.bytes2Short(inodeBytes));
        assertEquals((short) 0755, CpioUtil.bytes2Short(modeBytes));
        assertEquals((short) 0, CpioUtil.bytes2Short(uidBytes));
        assertEquals((short) 0, CpioUtil.bytes2Short(gidBytes));
        assertEquals((short) 0x01, CpioUtil.bytes2Short(nameLengthBytes));
        assertEquals((long) 0, CpioUtil.bytes2Long(sizeBytes, true));
        assertEquals("", new String(nameBytes, 0, nameBytes.length - 1));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                new FilterOutputStream(outputStream), FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        byte[] testData = "Test Data".getBytes();
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(testData);
        cpioOutputStream.closeArchiveEntry();
        byte[] data = outputStream.toByteArray();
        assertEquals(32 + testData.length, data.length);
        // Verify that the header and entry data have been written
        // The header verification is already done in testWriteNewEntry()
        byte[] nameBytes = Arrays.copyOfRange(data, 32, data.length);
        assertEquals("testFile.txt", new String(nameBytes, 0, nameBytes.length - 1));
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                new FilterOutputStream(outputStream), FORMAT_NEW);
        cpioOutputStream.finish();
        byte[] trailerBytes = Arrays.copyOfRange(outputStream.toByteArray(), 0, 32);
        byte[] magicNewTrailerBytes = ArchiveUtils.toAsciiBytes(MAGIC_NEW_TRAILER);
        assertTrue(Arrays.equals(magicNewTrailerBytes, trailerBytes));
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                new FilterOutputStream(outputStream), FORMAT_NEW);
        cpioOutputStream.close();
        assertFalse(outputStream.hasRemaining());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testFile.txt");
        String entryName = "testFile.txt";
        ArchiveEntry archiveEntry = cpioOutputStream.createArchiveEntry(inputFile, entryName);
        assertTrue(archiveEntry instanceof CpioArchiveEntry);
        assertEquals(entryName, ((CpioArchiveEntry) archiveEntry).getName());
        assertEquals(FORMAT_NEW, ((CpioArchiveEntry) archiveEntry).getFormat());
    }
}