package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor_OneArgument() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        assertNull(tarArchiveOutputStream.getRecordSize());
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarArchiveOutputStream.getBlockSize());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
        assertFalse(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testConstructor_TwoArguments() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os, TarBuffer.DEFAULT_BLKSIZE);
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarArchiveOutputStream.getBlockSize());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
        assertFalse(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testConstructor_ThreeArguments() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        int blockSize = 10;
        int recordSize = 20;
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os, blockSize, recordSize);
        assertEquals(blockSize, tarArchiveOutputStream.getBlockSize());
        assertEquals(recordSize, tarArchiveOutputStream.getRecordSize());
        assertFalse(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testSetLongFileMode_ValidMode() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testSetLongFileMode_InvalidMode() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        assertThrows(IllegalArgumentException.class, () -> tarArchiveOutputStream.setLongFileMode(42));
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.finish();
        assertEquals(2 * TarBuffer.RECORD_SIZE, os.size());
    }

    @Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.close();
        assertEquals(2 * TarBuffer.RECORD_SIZE, os.size());
    }

    @Test
    public void testGetRecordSize() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry_ValidArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        String entryName = "test.txt";
        byte[] entryContent = "Hello, World!".getBytes();
        ArchiveEntry archiveEntry = new TarArchiveEntry(entryName, entryContent);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] expectedHeader = new byte[TarConstants.HEADER_SIZE];
        archiveEntry.writeEntryHeader(expectedHeader);
        tarArchiveOutputStream.buffer.writeRecord(expectedHeader);
        byte[] expectedContent = new byte[entryContent.length];
        assertEquals(entryContent.length, tarArchiveOutputStream.write(entryContent, 0, entryContent.length));
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] actualData = os.toByteArray();
        byte[] expectedData = new byte[TarConstants.HEADER_SIZE + entryContent.length];
        System.arraycopy(expectedHeader, 0, expectedData, 0, TarConstants.HEADER_SIZE);
        System.arraycopy(entryContent, 0, expectedData, TarConstants.HEADER_SIZE, entryContent.length);
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testPutArchiveEntry_InvalidArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        ArchiveEntry archiveEntry = new Object() {}; // an invalid ArchiveEntry
        assertThrows(ClassCastException.class, () -> tarArchiveOutputStream.putArchiveEntry(archiveEntry));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        String entryName = "test.txt";
        byte[] entryContent = "Hello, World!".getBytes();
        ArchiveEntry archiveEntry = new TarArchiveEntry(entryName, entryContent);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] expectedHeader = new byte[TarConstants.HEADER_SIZE];
        archiveEntry.writeEntryHeader(expectedHeader);
        tarArchiveOutputStream.buffer.writeRecord(expectedHeader);
        byte[] expectedContent = new byte[entryContent.length];
        int bytesWritten = tarArchiveOutputStream.write(entryContent, 0, entryContent.length);
        assertEquals(entryContent.length, bytesWritten);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] actualData = os.toByteArray();
        byte[] expectedData = new byte[TarConstants.HEADER_SIZE + entryContent.length];
        System.arraycopy(expectedHeader, 0, expectedData, 0, TarConstants.HEADER_SIZE);
        System.arraycopy(entryContent, 0, expectedData, TarConstants.HEADER_SIZE, entryContent.length);
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testWrite_ValidData() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        byte[] data = "Hello, World!".getBytes();
        int bytesWritten = tarArchiveOutputStream.write(data, 0, data.length);
        assertEquals(data.length, bytesWritten);
    }

    @Test
    public void testWrite_InvalidData() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        byte[] data = new byte[1025];
        int bytesWritten = tarArchiveOutputStream.write(data, 0, data.length);
        assertEquals(-1, bytesWritten);
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.writeEOFRecord();
        byte[] expectedData = new byte[TarBuffer.RECORD_SIZE];
        Arrays.fill(expectedData, (byte) 0);
        byte[] actualData = os.toByteArray();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testFlush() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.flush();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        assertTrue(archiveEntry instanceof TarArchiveEntry);
    }
}