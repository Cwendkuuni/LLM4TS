package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarOutputStream;

/**
 * Test class for TarArchiveOutputStream
 */
public class TarArchiveOutputStreamTest {

    /**
     * Test constructor with default arguments
     */
    @org.junit.Test
    public void testTarArchiveOutputStreamDefaultConstructor() {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(tarOutputStream.getBuffer());
    }

    /**
     * Test constructor with custom block size and record size
     */
    @org.junit.Test
    public void testTarArchiveOutputStreamCustomBlockAndRecordSizeConstructor() {
        int blockSize = 1024;
        int recordSize = 512;
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), blockSize, recordSize);
        TarBuffer tarBuffer = tarOutputStream.getBuffer();
        assertEquals(blockSize, tarBuffer.getBlockSize());
        assertEquals(recordSize, tarBuffer.getRecordSize());
    }

    /**
     * Test setLongFileMode method
     */
    @org.junit.Test
    public void testSetLongFileMode() {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    /**
     * Test finish method with no unclosed entry
     */
    @org.junit.Test
    public void testFinishNoUnclosedEntry() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.finish();
    }

    /**
     * Test finish method with an unclosed entry
     */
    @org.junit.Test
    public void testFinishWithUnclosedEntry() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry("TestEntry.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        assertThrows(IOException.class, () -> tarOutputStream.finish());
    }

    /**
     * Test close method with no unclosed entry
     */
    @org.junit.Test
    public void testCloseNoUnclosedEntry() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    /**
     * Test close method with an unclosed entry
     */
    @org.junit.Test
    public void testCloseWithUnclosedEntry() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry("TestEntry.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.close();
        assertFalse(tarOutputStream.isClosed());
    }

    /**
     * Test getRecordSize method
     */
    @org.junit.Test
    public void testGetRecordSize() {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(TarOutputStream.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    /**
     * Test putArchiveEntry method with valid entry
     */
    @org.junit.Test
    public void testPutArchiveEntryValidEntry() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry("TestEntry.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        assertFalse(tarOutputStream.isClosed());
    }

    /**
     * Test putArchiveEntry method with long file name
     */
    @org.junit.Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry("TestEntry/with/long/name.txt");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(archiveEntry);
        assertFalse(tarOutputStream.isClosed());
    }

    /**
     * Test closeArchiveEntry method with valid entry
     */
    @org.junit.Test
    public void testCloseArchiveEntryValidEntry() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry("TestEntry.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        assertTrue(tarOutputStream.isClosed());
    }

    /**
     * Test write method with valid data
     */
    @org.junit.Test
    public void testWriteValidData() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] data = "Hello, World!".getBytes();
        ArchiveEntry archiveEntry = new TarArchiveEntry("TestEntry.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(data);
        tarOutputStream.closeArchiveEntry();
        byte[] output = ((ByteArrayOutputStream) tarOutputStream.getOutputStream()).toByteArray();
        byte[] expected = new byte[data.length + 512]; // 512 bytes for record size
        System.arraycopy(data, 0, expected, 0, data.length);
        assertEquals(expected, output);
    }

    /**
     * Test write method with large data
     */
    @org.junit.Test
    public void testWriteLargeData() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024 * 1024);
        byte[] data = new byte[1024 * 1024]; // 1MB
        ArchiveEntry archiveEntry = new TarArchiveEntry("TestEntry.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(data);
        tarOutputStream.closeArchiveEntry();

        byte[] output = ((ByteArrayOutputStream) tarOutputStream.getOutputStream()).toByteArray();
        assertEquals(data.length + 512, output.length); // 512 bytes for record size
    }

    /**
     * Test write method with long file name
     */
    @org.junit.Test
    public void testWriteLongFileName() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024, 512);
        byte[] data = "Hello, World!".getBytes();
        ArchiveEntry archiveEntry = new TarArchiveEntry("TestEntry/with/long/name.txt");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(data);
        tarOutputStream.write(0); // NUL terminator
        tarOutputStream.write(archiveEntry.getName().getBytes());
        tarOutputStream.closeArchiveEntry();

        byte[] output = ((ByteArrayOutputStream) tarOutputStream.getOutputStream()).toByteArray();
        int nameBytesLength = archiveEntry.getName().length() + 1;
        byte[] expected = new byte[data.length + nameBytesLength + 512]; // 512 bytes for record size
        System.arraycopy(data, 0, expected, 0, data.length);
        System.arraycopy(archiveEntry.getName().getBytes(), 0, expected, data.length, nameBytesLength);
        assertEquals(expected, output);
    }

    /**
     * Test write method with invalid data size
     */
    @org.junit.Test
    public void testWriteInvalidDataSize() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] data = "Hello, World!".getBytes();
        ArchiveEntry archiveEntry = new TarArchiveEntry("TestEntry.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        int size = data.length + 1;
        assertThrows(IOException.class, () -> tarOutputStream.write(data, size, data.length));
    }

    /**
     * Test close method with no output stream
     */
    @org.junit.Test(expected = IOException.class)
    public void testCloseNoOutputStream() {
        new TarArchiveOutputStream(null);
    }

    /**
     * Test putArchiveEntry method with null entry
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testPutArchiveEntryNullEntry() {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.putArchiveEntry(null);
    }

    /**
     * Test putArchiveEntry method with null entry name
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testPutArchiveEntryNullEntryName() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry("TestEntry.txt");
        archiveEntry.setName(null);
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    /**
     * Test putArchiveEntry method with invalid entry size
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryInvalidEntrySize() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry("TestEntry.txt");
        archiveEntry.setSize(-1);
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    /**
     * Test write method with null output stream
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testWriteNullOutputStream() {
        OutputStream outputStream = null;
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] data = "Hello, World!".getBytes();
        tarOutputStream.write(data);
    }

    /**
     * Test write method with null data
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testWriteNullData() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] data = null;
        ArchiveEntry archiveEntry = new TarArchiveEntry("TestEntry.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(data);
        tarOutputStream.closeArchiveEntry();
    }
}