package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
    }

    /**
     * Test constructor TarArchiveOutputStream(OutputStream os).
     */
    @Test
    public void testConstructor1() {
        TarArchiveOutputStream tarOutputStream1 = new TarArchiveOutputStream(outputStream);
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarOutputStream1.getRecordSize());
    }

    /**
     * Test constructor TarArchiveOutputStream(OutputStream os, int blockSize).
     */
    @Test
    public void testConstructor2() {
        int blockSize = 1024;
        TarArchiveOutputStream tarOutputStream2 = new TarArchiveOutputStream(outputStream, blockSize);
        assertEquals(blockSize, tarOutputStream2.getRecordSize());
    }

    /**
     * Test constructor TarArchiveOutputStream(OutputStream os, int blockSize, int recordSize).
     */
    @Test
    public void testConstructor3() {
        int blockSize = 1024;
        int recordSize = 512;
        TarArchiveOutputStream tarOutputStream3 = new TarArchiveOutputStream(outputStream, blockSize, recordSize);
        assertEquals(recordSize, tarOutputStream3.getRecordSize());
    }

    /**
     * Test method setLongFileMode(int longFileMode).
     */
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    /**
     * Test method finish() throws IOException.
     */
    @Test(expected = IOException.class)
    public void testFinishWithUnopenedEntry() throws IOException {
        tarOutputStream.finish();
    }

    /**
     * Test method finish() throws IOException.
     */
    @Test
    public void testFinishWithOpenedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
    }

    /**
     * Test method close() throws IOException.
     */
    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();
    }

    /**
     * Test method getRecordSize().
     */
    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    /**
     * Test method putArchiveEntry(ArchiveEntry archiveEntry) throws IOException.
     */
    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidEntry() throws IOException {
        TarArchiveEntry invalidEntry = new TarArchiveEntry("test.txt");
        invalidEntry.setSize(-1);
        tarOutputStream.putArchiveEntry(invalidEntry);
    }

    /**
     * Test method putArchiveEntry(ArchiveEntry archiveEntry) throws IOException.
     */
    @Test
    public void testPutArchiveEntryWithValidEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        assertNull(tarOutputStream.currName);
    }

    /**
     * Test method closeArchiveEntry() throws IOException.
     */
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithUnopenedEntry() throws IOException {
        tarOutputStream.closeArchiveEntry();
    }

    /**
     * Test method closeArchiveEntry() throws IOException.
     */
    @Test
    public void testCloseArchiveEntryWithOpenedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
    }

    /**
     * Test method write(byte[] wBuf, int wOffset, int numToWrite) throws IOException.
     */
    @Test
    public void testWrite() throws IOException {
        byte[] data = "Hello World!".getBytes();
        tarOutputStream.write(data, 0, data.length);
        byte[] outputData = outputStream.toByteArray();
        byte[] expectedData = new byte[data.length + 512];
        System.arraycopy(data, 0, expectedData, 0, data.length);
        assertEquals(expectedData, outputData);
    }

    /**
     * Test method write(byte[] wBuf, int wOffset, int numToWrite) throws IOException
     * when assembling record.
     */
    @Test
    public void testWriteWithRecordAssembly() throws IOException {
        byte[] data = "Hello World!".getBytes();
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.write(data, 0, data.length);
        byte[] outputData = outputStream.toByteArray();
        byte[] expectedData = new byte[data.length * 2 + 512];
        System.arraycopy(data, 0, expectedData, 0, data.length * 2);
        assertEquals(expectedData, outputData);
    }

    /**
     * Test method write(byte[] wBuf, int wOffset, int numToWrite) throws IOException
     * when writing to full archive.
     */
    @Test
    public void testWriteWithFullArchive() throws IOException {
        byte[] data = "Hello World!".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        byte[] outputData = outputStream.toByteArray();
        byte[] expectedData = new byte[entry.getSize() + data.length + 2 * 512];
        System.arraycopy(data, 0, expectedData, entry.getSize(), data.length);
        assertEquals(expectedData, outputData);
    }

    /**
     * Test method createArchiveEntry(File inputFile, String entryName).
     */
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        inputFile.createNewFile();
        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertEquals(inputFile.length(), entry.getSize());
        assertEquals(TarConstants.MAGIC, entry.getHeader().getMagic());
    }
}