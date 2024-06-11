package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CountingInputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarBuffer;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {
    /**
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        final int blockSize = TarBuffer.DEFAULT_BLKSIZE;
        final int recordSize = TarBuffer.DEFAULT_RCDSIZE;
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, blockSize, recordSize);
        assertEquals(blockSize, tarStream.getBlockSize());
        assertEquals(recordSize, tarStream.getRecordSize());
        assertSame(os, tarStream.getOutputStream());
    }

    /**
     * Test constructor with custom parameters.
     */
    @Test
    public void testCustomConstructor() {
        final int blockSize = 1024;
        final int recordSize = 512;
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, blockSize, recordSize);
        assertEquals(blockSize, tarStream.getBlockSize());
        assertEquals(recordSize, tarStream.getRecordSize());
        assertSame(os, tarStream.getOutputStream());
    }

    /**
     * Test setting long file mode.
     */
    @Test
    public void testSetLongFileMode() {
        final TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);

        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarStream.getLongFileMode());

        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarStream.getLongFileMode());
    }

    /**
     * Test writing EOF records.
     */
    @Test
    public void testWriteEOFRecords() throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 512);
        tarStream.finish();

        final byte[] eofRecord = new byte[512 * 2];
        Arrays.fill(eofRecord, (byte) 0);

        final byte[] buffer = new byte[512];
        for (int i = 0; i < eofRecord.length; i += buffer.length) {
            System.arraycopy(eofRecord, i, buffer, 0, buffer.length);
            tarStream.write(buffer);
        }

        final ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        final ArchiveInputStream archiveStream = new TarArchiveInputStream(is);
        final ArchiveEntry entry = archiveStream.getNextEntry();
        assertTrue(entry instanceof TarArchiveEntry);

        tarStream.close();
        archiveStream.close();
    }

    /**
     * Test writing an entry with a short name.
     */
    @Test
    public void testWriteShortEntry() throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 512);

        final String entryName = "test";
        final String fileName = "testFile.txt";
        final byte[] entryNameBytes = ArchiveUtils.toAsciiBytes(entryName);
        final byte[] fileNameBytes = ArchiveUtils.toAsciiBytes(fileName);

        final File inputFile = new File("testFile.txt");
        final ArchiveEntry entry = new TarArchiveEntry(inputFile, entryName);

        tarStream.putArchiveEntry(entry);
        tarStream.write(fileNameBytes);
        tarStream.write(new byte[] { 0 });
        tarStream.closeArchiveEntry();

        final ArchiveInputStream archiveStream = new TarArchiveInputStream(new ByteArrayInputStream(os.toByteArray()));
        final ArchiveEntry readEntry = archiveStream.getNextEntry();
        assertEquals(entryName, readEntry.getName());

        tarStream.close();
        archiveStream.close();
    }

    /**
     * Test writing an entry with a long name.
     */
    @Test
    public void testWriteLongEntry() throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 512);

        final String entryName = "a/long/entry/name.txt";
        final String fileName = "a/long/entry/name.txt";
        final byte[] entryNameBytes = ArchiveUtils.toAsciiBytes(entryName);
        final byte[] fileNameBytes = ArchiveUtils.toAsciiBytes(fileName);

        final File inputFile = new File("a/long/entry/name.txt");
        final ArchiveEntry entry = new TarArchiveEntry(inputFile, entryName);

        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarStream.putArchiveEntry(entry);
        tarStream.write(entryNameBytes);
        tarStream.write(new byte[entryNameBytes.length + 1]); // NUL terminator
        tarStream.closeArchiveEntry();

        final ArchiveInputStream archiveStream = new TarArchiveInputStream(new ByteArrayInputStream(os.toByteArray()));
        final ArchiveEntry readEntry = archiveStream.getNextEntry();
        assertEquals(entryName, readEntry.getName());

        tarStream.close();
        archiveStream.close();
    }

    /**
     * Test writing a directory entry.
     */
    @Test
    public void testWriteDirectoryEntry() throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 512);

        final String entryName = "test";
        final String fileName = "testDir";
        final byte[] entryNameBytes = ArchiveUtils.toAsciiBytes(entryName);
        final byte[] fileNameBytes = ArchiveUtils.toAsciiBytes(fileName);

        final File inputFile = new File(fileName);
        final ArchiveEntry entry = new TarArchiveEntry(inputFile, entryName);

        tarStream.putArchiveEntry(entry);
        tarStream.write(fileNameBytes);
        tarStream.write(new byte[fileNameBytes.length + 1]); // NUL terminator
        tarStream.write(TarConstants.MAGIC_DIRECTORY);
        tarStream.closeArchiveEntry();

        final ArchiveInputStream archiveStream = new TarArchiveInputStream(new ByteArrayInputStream(os.toByteArray()));
        final ArchiveEntry readEntry = archiveStream.getNextEntry();
        assertTrue(readEntry.isDirectory());

        tarStream.close();
        archiveStream.close();
    }

    /**
     * Test writing data to an entry.
     */
    @Test
    public void testWriteDataToEntry() throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 512);

        final String entryName = "test";
        final String fileName = "testData.txt";
        final byte[] entryNameBytes = ArchiveUtils.toAsciiBytes(entryName);
        final byte[] fileNameBytes = ArchiveUtils.toAsciiBytes(fileName);

        final File inputFile = new File(fileName);
        final ArchiveEntry entry = new TarArchiveEntry(inputFile, entryName);

        final byte[] data = "Test data".getBytes();

        tarStream.putArchiveEntry(entry);
        tarStream.write(fileNameBytes);
        tarStream.write(new byte[fileNameBytes.length + 1]); // NUL terminator
        tarStream.write(data, 0, data.length);
        tarStream.closeArchiveEntry();

        final ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        final ArchiveInputStream archiveStream = new TarArchiveInputStream(is);
        final ArchiveEntry readEntry = archiveStream.getNextEntry();
        assertEquals(entryName, readEntry.getName());
        final byte[] readData = new byte[data.length];
        archiveStream.read(readData, 0, data.length);
        assertArrayEquals(data, readData);

        tarStream.close();
        archiveStream.close();
    }

    /**
     * Test writing multiple entries.
     */
    @Test
    public void testWriteMultipleEntries() throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 512);

        final String entryName1 = "test1";
        final String fileName1 = "test1File.txt";
        final byte[] entryNameBytes1 = ArchiveUtils.toAsciiBytes(entryName1);
        final byte[] fileNameBytes1 = ArchiveUtils.toAsciiBytes(fileName1);

        final File inputFile1 = new File(fileName1);
        final ArchiveEntry entry1 = new TarArchiveEntry(inputFile1, entryName1);

        final String entryName2 = "test2";
        final String fileName2 = "test2File.txt";
        final byte[] entryNameBytes2 = ArchiveUtils.toAsciiBytes(entryName2);
        final byte[] fileNameBytes2 = ArchiveUtils.toAsciiBytes(fileName2);

        final File inputFile2 = new File(fileName2);
        final ArchiveEntry entry2 = new TarArchiveEntry(inputFile2, entryName2);

        tarStream.putArchiveEntry(entry1);
        tarStream.write(fileNameBytes1);
        tarStream.write(new byte[fileNameBytes1.length + 1]); // NUL terminator
        tarStream.write(data("Test data 1".getBytes()));
        tarStream.closeArchiveEntry();

        tarStream.putArchiveEntry(entry2);
        tarStream.write(fileNameBytes2);
        tarStream.write(new byte[fileNameBytes2.length + 1]); // NUL terminator
        tarStream.write(data("Test data 2".getBytes()));
        tarStream.closeArchiveEntry();

        final ArchiveInputStream archiveStream = new TarArchiveInputStream(new ByteArrayInputStream(os.toByteArray()));
        final ArchiveEntry readEntry1 = archiveStream.getNextEntry();
        assertEquals(entryName1, readEntry1.getName());
        final byte[] readData1 = new byte[data("Test data 1".getBytes()).length];
        archiveStream.read(readData1, 0, readData1.length);
        assertArrayEquals(data("Test data 1".getBytes()), readData1);

        final ArchiveEntry readEntry2 = archiveStream.getNextEntry();
        assertEquals(entryName2, readEntry2.getName());
        final byte[] readData2 = new byte[data("Test data 2".getBytes()).length];
        archiveStream.read(readData2, 0, readData2.length);
        assertArrayEquals(data("Test data 2".getBytes()), readData2);

        tarStream.close();
        archiveStream.close();
    }

    /**
     * Test closing the stream before finishing.
     */
    @Test(expected = IOException.class)
    public void testCloseBeforeFinish() throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 512);

        tarStream.close();

        tarStream.finish();
    }

    /**
     * Test reading an archive.
     */
    @Test
    public void testReadArchive() throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 512);

        final String entryName1 = "test1";
        final String fileName1 = "test1File.txt";
        final byte[] entryNameBytes1 = ArchiveUtils.toAsciiBytes(entryName1);
        final byte[] fileNameBytes1 = ArchiveUtils.toAsciiBytes(fileName1);

        final File inputFile1 = new File(fileName1);
        final ArchiveEntry entry1 = new TarArchiveEntry(inputFile1, entryName1);

        final String entryName2 = "test2";
        final String fileName2 = "test2File.txt";
        final byte[] entryNameBytes2 = ArchiveUtils.toAsciiBytes(entryName2);
        final byte[] fileNameBytes2 = ArchiveUtils.toAsciiBytes(fileName2);

        final File inputFile2 = new File(fileName2);
        final ArchiveEntry entry2 = new TarArchiveEntry(inputFile2, entryName2);

        tarStream.putArchiveEntry(entry1);
        tarStream.write(fileNameBytes1);
        tarStream.write(new byte[fileNameBytes1.length + 1]); // NUL terminator
        tarStream.write(data("Test data 1".getBytes()));
        tarStream.closeArchiveEntry();

        tarStream.putArchiveEntry(entry2);
        tarStream.write(fileNameBytes2);
        tarStream.write(new byte[fileNameBytes2.length + 1]); // NUL terminator
        tarStream.write(data("Test data 2".getBytes()));
        tarStream.closeArchiveEntry();

        tarStream.finish();

        final ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        final ArchiveInputStream archiveStream = new TarArchiveInputStream(is);

        final ArchiveEntry readEntry1 = archiveStream.getNextEntry();
        assertEquals(entryName1, readEntry1.getName());
        final byte[] readData1 = new byte[data("Test data 1".getBytes()).length];
        archiveStream.read(readData1, 0, readData1.length);
        assertArrayEquals(data("Test data 1".getBytes()), readData1);

        final ArchiveEntry readEntry2 = archiveStream.getNextEntry();
        assertEquals(entryName2, readEntry2.getName());
        final byte[] readData2 = new byte[data("Test data 2".getBytes()).length];
        archiveStream.read(readData2, 0, readData2.length);
        assertArrayEquals(data("Test data 2".getBytes()), readData2);

        archiveStream.close();
    }

    /**
     * Test writing large data to an entry.
     */
    private byte[] data(byte[] bytes) {
        final int length = bytes.length;
        byte[] result = new byte[length * 2];

        for (int i = 0; i < length; ++i) {
            result[i * 2] = (byte) (bytes[i] & 0xFF);
            result[i * 2 + 1] = (byte) ((bytes[i] >> 8) & 0xFF);
        }

        return result;
    }
}