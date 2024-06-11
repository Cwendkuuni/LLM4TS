package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructorWithOutputStream() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assertSame(outputStream, tarArchiveOutputStream.getOutputStream());
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeAndRecordSize()
            throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        int blockSize = 512;
        int recordSize = 1024;
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                outputStream, blockSize, recordSize);
        assertEquals(blockSize, tarArchiveOutputStream.getBuffer().getBlockSize());
        assertEquals(recordSize, tarArchiveOutputStream.getBuffer().getRecordSize());
    }

    @Test
    public void testSetLongFileMode() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidParameter() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                outputStream);
        tarArchiveOutputStream.setLongFileMode(-1);
    }

    @Test
    public void testFinish() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                outputStream);
        tarArchiveOutputStream.finish();
        byte[] eofRecord = new byte[512 * 2];
        Arrays.fill(eofRecord, (byte) 0);
        byte[] header = new byte[512];
        Arrays.fill(header, (byte) 0);
        TarEntryHeader header1 = new TarEntryHeader();
        header1.setMagic(TarConstants.MAGIC);
        header1.setVersion(TarConstants.VERSION_STANDARD);
        header1.setUserID(0);
        header1.setGroupID(0);
        header1.setFileMode(0755);
        header1.setSize(1024);
        header1.setChecksum(0L);
        header1.setName("test.txt".getBytes(StandardCharsets.UTF_8));
        byte[] headerBytes = header1.getBytes();
        System.arraycopy(headerBytes, 0, header, 0, headerBytes.length);
        byte[] entryBytes = new byte[1024];
        Arrays.fill(entryBytes, (byte) 0);
        byte[] record = new byte[512];
        Arrays.fill(record, (byte) 0);
        InputStream inputStream = new ByteArrayInputStream(headerBytes);
        ArchiveEntry testEntry = new TarArchiveEntry("test.txt", inputStream);
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.write(entryBytes, 0, entryBytes.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        byte[] output = outputStream.toByteArray();
        byte[] expected = new byte[headerBytes.length + entryBytes.length * 2 + eofRecord.length];
        System.arraycopy(headerBytes, 0, expected, 0, headerBytes.length);
        System.arraycopy(entryBytes, 0, expected, headerBytes.length, entryBytes.length);
        System.arraycopy(headerBytes, 0, expected, headerBytes.length + entryBytes.length, headerBytes.length);
        System.arraycopy(entryBytes, 0, expected, headerBytes.length * 2 + entryBytes.length, entryBytes.length);
        System.arraycopy(eofRecord, 0, expected, headerBytes.length * 2 + entryBytes.length * 2, eofRecord.length);
        assertArrayEquals(expected, output);
    }

    @Test
    public void testClose() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                outputStream);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        assertFalse(tarArchiveOutputStream.isOpen());
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryAfterClose() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                outputStream);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryAndWriteData() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] output = outputStream.toByteArray();
        byte[] expectedHeader = new byte[512];
        byte[] expectedData = new byte[data.length];
        TarEntryHeader header = new TarEntryHeader();
        header.setMagic(TarConstants.MAGIC);
        header.setVersion(TarConstants.VERSION_STANDARD);
        header.setUserID(0);
        header.setGroupID(0);
        header.setFileMode(0755);
        header.setSize(data.length);
        header.setChecksum(0L);
        header.setName("test.txt".getBytes(StandardCharsets.UTF_8));
        byte[] headerBytes = header.getBytes();
        System.arraycopy(headerBytes, 0, expectedHeader, 0, headerBytes.length);
        System.arraycopy(data, 0, expectedData, 0, data.length);
        byte[] expected = new byte[headerBytes.length + data.length];
        System.arraycopy(headerBytes, 0, expected, 0, headerBytes.length);
        System.arraycopy(data, 0, expected, headerBytes.length, data.length);
        assertArrayEquals(expected, output);
    }

    @Test
    public void testWriteDataWithUnclosedEntry() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length);
        byte[] expected = new byte[data.length];
        System.arraycopy(data, 0, expected, 0, data.length);
        byte[] output = tarArchiveOutputStream.toByteArray();
        assertArrayEquals(expected, output);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteDataWithClosedEntry() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        assertTrue(tarArchiveOutputStream.haveUnclosedEntry());
    }

    @Test
    public void testCloseArchiveEntryAfterFinish() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.closeArchiveEntry();
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry());
    }
}