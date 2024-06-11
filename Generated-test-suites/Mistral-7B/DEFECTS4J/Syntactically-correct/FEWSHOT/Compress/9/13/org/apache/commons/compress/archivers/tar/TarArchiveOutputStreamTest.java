package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinishAndClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finish();
        tarOutputStream.close();
        assertEquals("", outputStream.toString());
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.close();
        tarOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testCloseWithUnclosedEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        String longFileName = "a" + TarConstants.NAMELEN + "LongFileName";
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();
        String expectedTar = "0000000000000000" + TarConstants.MAGIC_TAR + "00" + Long.toUnsignedLong(entry.getCrc())
                + Long.toUnsignedLong(entry.getSize()) + Long.toUnsignedLong(entry.getModTime())
                + TarConstants.NAME_FIELD_LENGTH + TarConstants.FILE_NAME_NULL + ArchiveUtils.toAsciiBytes(entry.getName())[0]
                + '0';
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        assertFalse(archiveEntry instanceof TarArchiveEntry);
        assertNull(archiveEntry);
        archiveInputStream.close();
        assertEquals(expectedTar, ArchiveUtils.toHexDump(outputStream.toByteArray()));
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();
        String expectedTar = "0000000000000000" + TarConstants.MAGIC_TAR + "00" + Long.toUnsignedLong(entry.getCrc())
                + Long.toUnsignedLong(entry.getSize()) + Long.toUnsignedLong(entry.getModTime())
                + TarConstants.NAME_FIELD_LENGTH + TarConstants.FILE_NAME_NULL + ArchiveUtils.toAsciiBytes(entry.getName())[0]
                + '0';
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        assertTrue(archiveEntry instanceof TarArchiveEntry);
        assertEquals(entry, archiveEntry);
        archiveInputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        String longFileName = "a" + TarConstants.NAMELEN + "LongFileName";
        entry.setName(longFileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();
        String expectedTar = "0000000000000000" + TarConstants.MAGIC_LONGNAME + "00" + Long.toUnsignedLong(entry.getCrc())
                + Long.toUnsignedLong(entry.getSize()) + Long.toUnsignedLong(entry.getModTime())
                + Long.toUnsignedLong(longFileName.length() + 1) + '0'
                + TarConstants.NAME_FIELD_LENGTH + TarConstants.FILE_NAME_NULL + ArchiveUtils.toAsciiBytes(longFileName)[0]
                + '0';
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        assertTrue(archiveEntry instanceof TarArchiveEntry);
        assertEquals(entry, archiveEntry);
        archiveInputStream.close();
        assertArrayEquals(ArchiveUtils.toAsciiBytes(longFileName), ArchiveUtils.toAsciiBytes(archiveEntry.getName()));
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello, world!".getBytes();
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
        String expectedTar = "0000000000000000" + TarConstants.MAGIC_TAR + "00" + Long.toUnsignedLong(entry.getCrc())
                + Long.toUnsignedLong(entry.getSize()) + Long.toUnsignedLong(entry.getModTime())
                + TarConstants.NAME_FIELD_LENGTH + TarConstants.FILE_NAME_NULL + ArchiveUtils.toAsciiBytes(entry.getName())[0]
                + '0';
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        assertTrue(archiveEntry instanceof TarArchiveEntry);
        assertEquals(entry, archiveEntry);
        byte[] result = new byte[data.length];
        inputStream.read(result);
        assertArrayEquals(data, result);
        archiveInputStream.close();
    }

    @Test
    public void testWriteWithDataLeftOver() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello, world!".getBytes();
        int dataLength = data.length;
        tarOutputStream.write(data, 0, dataLength - 1);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
        String expectedTar = "0000000000000000" + TarConstants.MAGIC_TAR + "00" + Long.toUnsignedLong(entry.getCrc())
                + Long.toUnsignedLong(entry.getSize()) + Long.toUnsignedLong(entry.getModTime())
                + TarConstants.NAME_FIELD_LENGTH + TarConstants.FILE_NAME_NULL + ArchiveUtils.toAsciiBytes(entry.getName())[0]
                + '0';
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        assertTrue(archiveEntry instanceof TarArchiveEntry);
        assertEquals(entry, archiveEntry);
        byte[] result = new byte[dataLength];
        inputStream.read(result, 0, result.length - 1);
        assertArrayEquals(data, 0, dataLength - 1, result, 0, result.length - 1);
        archiveInputStream.close();
    }

    @Test
    public void testWriteWithLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        String longFileName = "a" + TarConstants.NAMELEN + "LongFileName";
        entry.setName(longFileName);
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello, world!".getBytes();
        int dataLength = data.length;
        tarOutputStream.write(data, 0, dataLength - 1);
        tarOutputStream.write(ArchiveUtils.toAsciiBytes(longFileName), 0, longFileName.length() + 1);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
        String expectedTar = "0000000000000000" + TarConstants.MAGIC_LONGNAME + "00" + Long.toUnsignedLong(entry.getCrc())
                + Long.toUnsignedLong(entry.getSize()) + Long.toUnsignedLong(entry.getModTime())
                + (longFileName.length() + 1) + '0'
                + TarConstants.NAME_FIELD_LENGTH + TarConstants.FILE_NAME_NULL + ArchiveUtils.toAsciiBytes(longFileName)[0]
                + '0';
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        assertTrue(archiveEntry instanceof TarArchiveEntry);
        assertEquals(entry, archiveEntry);
        byte[] result = new byte[dataLength + longFileName.length() + 1];
        inputStream.read(result, 0, result.length);
        assertArrayEquals(data, 0, dataLength - 1, result, 0, dataLength - 1);
        assertArrayEquals(ArchiveUtils.toAsciiBytes(longFileName), result, dataLength, result.length - dataLength);
        archiveInputStream.close();
    }
}