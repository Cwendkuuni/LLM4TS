package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;

/**
 * Test cases for CpioArchiveOutputStream class
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out);
        assertEquals(FORMAT_NEW, stream.getEntryFormat());
    }

    /**
     * Test constructor with specified format
     */
    @Test
    public void testConstructorFormat() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out, FORMAT_NEW_CRC);
        assertEquals(FORMAT_NEW_CRC, stream.getEntryFormat());
    }

    /**
     * Test putArchiveEntry() with valid input
     * @throws IOException
     */
    @Test
    public void testPutArchiveEntryValid() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out);
        String entryName = "testEntry.txt";
        byte[] entryContent = "Test entry content".getBytes();
        ArchiveEntry archiveEntry = new CpioArchiveEntry(entryName, "text/plain", (long) entryContent.length);
        stream.putArchiveEntry(archiveEntry);
        stream.write(entryContent, 0, entryContent.length);
        stream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        assertEquals(entryName, entry.getName());
        assertEquals("text/plain", entry.getSize(), entry.getCompressedSize());
        assertNull(entry.getCompressionMethodName());
        byte[] readEntryContent = new byte[entry.getSize()];
        archiveInputStream.read(readEntryContent);
        assertArrayEquals(entryContent, readEntryContent);
    }

    /**
     * Test putArchiveEntry() with invalid input
     * @throws IOException
     */
    @Test
    public void testPutArchiveEntryInvalid() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out);

        // Test null input
        ArchiveEntry archiveEntry = null;
        assertThrows(IllegalArgumentException.class, () -> stream.putArchiveEntry(archiveEntry));

        // Test non-CpioArchiveEntry input
        String nonCpioArchiveEntryName = "nonCpioArchiveEntry.txt";
        ArchiveEntry nonCpioArchiveEntry = new JarEntry(nonCpioArchiveEntryName);
        assertThrows(ClassCastException.class, () -> stream.putArchiveEntry(nonCpioArchiveEntry));

        // Test duplicate entry name
        CpioArchiveEntry duplicateEntry = new CpioArchiveEntry(new File("testEntry.txt"), "testEntry.txt");
        stream.putArchiveEntry(duplicateEntry);
        assertThrows(IOException.class, () -> stream.putArchiveEntry(duplicateEntry));
    }

    /**
     * Test writeHeader() with new format
     * @throws IOException
     */
    @Test
    public void testWriteHeaderNewFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out);
        String entryName = "testEntry.txt";
        byte[] entryContent = "Test entry content".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName, "text/plain", (long) entryContent.length);
        stream.putArchiveEntry(entry);
        stream.write(entryContent, 0, entryContent.length);
        stream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);
        ArchiveEntry entryFromArchive = archiveInputStream.getNextEntry();

        byte[] magicBytesNew = ArchiveUtils.toAsciiBytes(MAGIC_NEW);
        byte[] magicBytesNewEntry = new byte[magicBytesNew.length + entryName.length() + 1];
        System.arraycopy(magicBytesNew, 0, magicBytesNewEntry, 0, magicBytesNew.length);
        System.arraycopy(entryName.getBytes(), 0, magicBytesNewEntry, magicBytesNew.length, entryName.length() + 1);
        assertArrayEquals(magicBytesNewEntry, ArchiveUtils.readNBytes(inputStream, magicBytesNew.length + entryName.length() + 1));
    }

    /**
     * Test writeHeader() with new format and CRC
     * @throws IOException
     */
    @Test
    public void testWriteHeaderNewFormatCRC() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out, FORMAT_NEW_CRC);
        String entryName = "testEntry.txt";
        byte[] entryContent = "Test entry content".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName, "text/plain", (long) entryContent.length);
        stream.putArchiveEntry(entry);
        stream.write(entryContent, 0, entryContent.length);
        stream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);
        ArchiveEntry entryFromArchive = archiveInputStream.getNextEntry();

        byte[] magicBytesNewCRC = ArchiveUtils.toAsciiBytes(MAGIC_NEW_CRC);
        byte[] magicBytesNewEntryCRC = new byte[magicBytesNewCRC.length + entryName.length() + 1];
        System.arraycopy(magicBytesNewCRC, 0, magicBytesNewEntryCRC, 0, magicBytesNewCRC.length);
        System.arraycopy(entryName.getBytes(), 0, magicBytesNewEntryCRC, magicBytesNewCRC.length, entryName.length() + 1);
        assertArrayEquals(magicBytesNewEntryCRC, ArchiveUtils.readNBytes(inputStream, magicBytesNewCRC.length + entryName.length() + 1));
    }

    /**
     * Test writeHeader() with old format
     * @throws IOException
     */
    @Test
    public void testWriteHeaderOldFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out);
        String entryName = "testEntry.txt";
        byte[] entryContent = "Test entry content".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName, "text/plain", (long) entryContent.length);
        stream.putArchiveEntry(entry);
        stream.write(entryContent, 0, entryContent.length);
        stream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);
        ArchiveEntry entryFromArchive = archiveInputStream.getNextEntry();

        byte[] magicBytesOldAscii = ArchiveUtils.toAsciiBytes(MAGIC_OLD_ASCII);
        byte[] magicBytesOldEntry = new byte[magicBytesOldAscii.length + entryName.length() + 1 + entryContent.length];
        System.arraycopy(magicBytesOldAscii, 0, magicBytesOldEntry, 0, magicBytesOldAscii.length);
        System.arraycopy(entryName.getBytes(), 0, magicBytesOldEntry, magicBytesOldAscii.length, entryName.length() + 1);
        System.arraycopy(entryContent, 0, magicBytesOldEntry, magicBytesOldAscii.length + entryName.length() + 1, entryContent.length);
        assertArrayEquals(magicBytesOldEntry, ArchiveUtils.readNBytes(inputStream, magicBytesOldEntry.length));
    }

    /**
     * Test writeHeader() with old format and CRC
     * @throws IOException
     */
    @Test
    public void testWriteHeaderOldFormatCRC() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out, FORMAT_OLD_BINARY);
        String entryName = "testEntry.txt";
        byte[] entryContent = "Test entry content".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName, "text/plain", (long) entryContent.length);
        stream.putArchiveEntry(entry);
        stream.write(entryContent, 0, entryContent.length);
        stream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);
        ArchiveEntry entryFromArchive = archiveInputStream.getNextEntry();

        HashMap<Short, Byte[]> expectedCrcTable = new HashMap<>();
        expectedCrcTable.put((short) 0, ArchiveUtils.long2byteArray(0L, 4));
        expectedCrcTable.put((short) 1, ArchiveUtils.long2byteArray(1L, 4));
        expectedCrcTable.put((short) 2, ArchiveUtils.long2byteArray(2L, 4));
        expectedCrcTable.put((short) 3, ArchiveUtils.long2byteArray(3L, 4));
        expectedCrcTable.put((short) 4, ArchiveUtils.long2byteArray(4L, 4));
        expectedCrcTable.put((short) 5, ArchiveUtils.long2byteArray(5L, 4));
        expectedCrcTable.put((short) 6, ArchiveUtils.long2byteArray(6L, 4));
        expectedCrcTable.put((short) 7, ArchiveUtils.long2byteArray(7L, 4));
        expectedCrcTable.put((short) 8, ArchiveUtils.long2byteArray(8L, 4));
        expectedCrcTable.put((short) 9, ArchiveUtils.long2byteArray(9L, 4));
        expectedCrcTable.put((short) 10, ArchiveUtils.long2byteArray(10L, 4));
        expectedCrcTable.put((short) 11, ArchiveUtils.long2byteArray(11L, 4));
        expectedCrcTable.put((short) 12, ArchiveUtils.long2byteArray(12L, 4));
        expectedCrcTable.put((short) 13, ArchiveUtils.long2byteArray(13L, 4));
        expectedCrcTable.put((short) 14, ArchiveUtils.long2byteArray(14L, 4));
        expectedCrcTable.put((short) 15, ArchiveUtils.long2byteArray(15L, 4));

        byte[] magicBytesOldEntryCRC = new byte[magicBytesOldEntry.length + 2 * expectedCrcTable.size()];
        int index = 0;
        for (Short key : expectedCrcTable.keySet()) {
            System.arraycopy(expectedCrcTable.get(key), 0, magicBytesOldEntryCRC, index, expectedCrcTable.get(key).length);
            index += expectedCrcTable.get(key).length;
        }
        System.arraycopy(ArchiveUtils.long2byteArray(entryName.length() + 1L, 4), 0, magicBytesOldEntryCRC, index, 4);
        index += 4;
        System.arraycopy(ArchiveUtils.long2byteArray(entryContent.length, 4), 0, magicBytesOldEntryCRC, index, 4);
        index += 4;
        System.arraycopy(entryContent, 0, magicBytesOldEntryCRC, index, entryContent.length);
        assertArrayEquals(magicBytesOldEntryCRC, ArchiveUtils.readNBytes(inputStream, magicBytesOldEntryCRC.length));
    }
}