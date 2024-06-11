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
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructors() {
        // Test default constructor
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(tarStream.getOutputStream());
        assertEquals(TarConstants.DEFAULT_BLKSIZE, tarStream.getBuffer().getBlockSize());
        assertEquals(TarConstants.DEFAULT_RCDSIZE, tarStream.getBuffer().getRecordSize());

        // Test constructor with OutputStream
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream2 = new TarArchiveOutputStream(os);
        assertEquals(os, tarStream2.getOutputStream());

        // Test constructor with OutputStream, block size, and record size
        OutputStream os2 = new ByteArrayOutputStream();
        int blockSize = 1024;
        int recordSize = 512;
        TarArchiveOutputStream tarStream3 = new TarArchiveOutputStream(os2, blockSize, recordSize);
        assertEquals(os2, tarStream3.getOutputStream());
        assertEquals(blockSize, tarStream3.getBuffer().getBlockSize());
        assertEquals(recordSize, tarStream3.getBuffer().getRecordSize());
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // Test setting longFileMode to LONGFILE_ERROR
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        // Test setting longFileMode to LONGFILE_TRUNCATE
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        // Test setting longFileMode to LONGFILE_GNU
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // Test setting longFileMode to an invalid value
        assertThrows(IllegalArgumentException.class, () -> tarStream.setLongFileMode(3));
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // Test finishing an empty archive
        tarStream.finish();
        assertEquals(2 * TarConstants.RECORD_LENGTH, tarStream.getBuffer().getPosition());

        // Test finishing an archive with a single entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putNextEntry(entry);
        byte[] data = "Test data".getBytes();
        tarStream.write(data, 0, data.length);
        tarStream.closeArchiveEntry();
        tarStream.finish();
        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_LENGTH];
        assertArrayEquals(expectedEOFRecord, tarStream.getBuffer().getBuffer(), 0, TarConstants.RECORD_LENGTH);
        assertEquals(tarStream.getBuffer().getPosition(), 4 * TarConstants.RECORD_LENGTH);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // Test closing an empty archive
        tarStream.close();
        assertEquals(0, tarStream.getBuffer().getPosition());

        // Test closing an archive with a single entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putNextEntry(entry);
        byte[] data = "Test data".getBytes();
        tarStream.write(data, 0, data.length);
        tarStream.closeArchiveEntry();
        tarStream.close();
        assertEquals(4 * TarConstants.RECORD_LENGTH, tarStream.getBuffer().getPosition());
    }

    @Test
    public void testGetRecordSize() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(TarConstants.RECORD_SIZE, tarStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // Test putting an empty entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        assertEquals(0, tarStream.getBuffer().getPosition());

        // Test putting an entry with a name longer than the name length limit
        TarArchiveEntry longEntry = new TarArchiveEntry("test" + new String(new char[TarConstants.NAME_LENGTH + 1]) + ".txt");
        assertThrows(IllegalArgumentException.class, () -> tarStream.putArchiveEntry(longEntry));

        // Test putting an entry with a non-TarArchiveEntry object
        ArchiveEntry nonTarEntry = new File("test.txt").toPath().toFile();
        assertThrows(ClassCastException.class, () -> tarStream.putArchiveEntry(nonTarEntry));

        // Test putting an entry with data
        entry = new TarArchiveEntry("test.txt");
        byte[] data = "Test data".getBytes();
        tarStream.putArchiveEntry(entry);
        tarStream.write(data, 0, data.length);
        tarStream.closeArchiveEntry();
        byte[] expectedEntryRecord = entry.getHeader().getBytes();
        assertArrayEquals(expectedEntryRecord, tarStream.getBuffer().getBuffer(), 0, expectedEntryRecord.length);
        assertEquals(expectedEntryRecord.length + data.length, tarStream.getBuffer().getPosition());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // Test closing an entry before putting one
        assertThrows(IllegalStateException.class, () -> tarStream.closeArchiveEntry());

        // Test closing an entry after putting an empty one
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.closeArchiveEntry();
        assertEquals(0, tarStream.getBuffer().getPosition());

        // Test closing an entry after writing data to it
        entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        byte[] data = "Test data".getBytes();
        tarStream.write(data, 0, data.length);
        tarStream.closeArchiveEntry();
        byte[] expectedEntryRecord = entry.getHeader().getBytes();
        assertArrayEquals(expectedEntryRecord, tarStream.getBuffer().getBuffer(), 0, expectedEntryRecord.length + data.length);
        assertEquals(expectedEntryRecord.length + data.length, tarStream.getBuffer().getPosition());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // Test writing to an empty archive
        byte[] data = "Test data".getBytes();
        tarStream.write(data, 0, data.length);
        assertEquals(0, tarStream.getBuffer().getPosition());

        // Test writing to an archive with a single entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.write(data, 0, data.length);
        tarStream.closeArchiveEntry();
        byte[] expectedEntryRecord = entry.getHeader().getBytes();
        assertArrayEquals(expectedEntryRecord, tarStream.getBuffer().getBuffer(), 0, expectedEntryRecord.length + data.length);
        assertEquals(expectedEntryRecord.length + data.length, tarStream.getBuffer().getPosition());

        // Test writing to an archive with multiple entries
        TarArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        tarStream.putArchiveEntry(entry2);
        byte[] data2 = "Test data 2".getBytes();
        tarStream.write(data2, 0, data2.length);
        tarStream.closeArchiveEntry();
        byte[] expectedEntry2Record = entry2.getHeader().getBytes();
        assertArrayEquals(expectedEntry2Record, tarStream.getBuffer().getBuffer(), 0, expectedEntry2Record.length + data2.length);
        assertEquals(expectedEntry2Record.length + data2.length, tarStream.getBuffer().getPosition());
    }

    @Test
    public void testWriteWithAssembly() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // Test writing a single record without assembly
        byte[] data = "Test data".getBytes();
        tarStream.write(data, 0, data.length);
        byte[] expectedRecord = new byte[TarConstants.RECORD_LENGTH];
        System.arraycopy(data, 0, expectedRecord, 0, data.length);
        assertArrayEquals(expectedRecord, tarStream.getBuffer().getBuffer(), 0, expectedRecord.length);
        assertEquals(expectedRecord.length, tarStream.getBuffer().getPosition());

        // Test writing multiple records with assembly
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        data = "Test data 1".getBytes();
        byte[] firstRecord = tarStream.getBuffer().getBuffer();
        tarStream.write(data, 0, data.length);
        data = "Test data 2".getBytes();
        byte[] secondRecord = tarStream.getBuffer().getBuffer();
        tarStream.write(data, 0, data.length);
        tarStream.closeArchiveEntry();

        // Check that the first record was written correctly
        byte[] expectedFirstRecord = new byte[TarConstants.RECORD_LENGTH];
        System.arraycopy(entry.getHeader().getBytes(), 0, expectedFirstRecord, 0, entry.getHeader().length);
        System.arraycopy(data, 0, expectedFirstRecord, entry.getHeader().length, data.length);
        assertArrayEquals(expectedFirstRecord, firstRecord, 0, expectedFirstRecord.length);

        // Check that the second record was assembled and written correctly
        int assembleLength = TarConstants.RECORD_LENGTH - data.length;
        byte[] assembledRecord = new byte[TarConstants.RECORD_LENGTH];
        System.arraycopy(secondRecord, 0, assembledRecord, 0, assembleLength);
        byte[] expectedSecondRecord = new byte[TarConstants.RECORD_LENGTH];
        System.arraycopy(entry.getHeader().getBytes(), 0, expectedSecondRecord, 0, entry.getHeader().length);
        System.arraycopy(data, 0, expectedSecondRecord, entry.getHeader().length, data.length);
        System.arraycopy(assembledRecord, assembleLength, expectedSecondRecord, expectedFirstRecord.length, assembleLength);
        assertArrayEquals(expectedSecondRecord, tarStream.getBuffer().getBuffer(), 0, expectedSecondRecord.length);
        assertEquals(expectedSecondRecord.length, tarStream.getBuffer().getPosition());
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // Test flushing an empty archive
        tarStream.flush();
        assertEquals(0, tarStream.getBuffer().getPosition());

        // Test flushing an archive with a single entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        byte[] data = "Test data".getBytes();
        tarStream.write(data, 0, data.length);
        tarStream.closeArchiveEntry();
        tarStream.flush();
        assertEquals(entry.getHeader().length + data.length, tarStream.getBuffer().getPosition());

        // Test flushing an archive with multiple entries
        TarArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        tarStream.putArchiveEntry(entry2);
        byte[] data2 = "Test data 2".getBytes();
        tarStream.write(data2, 0, data2.length);
        tarStream.closeArchiveEntry();
        tarStream.flush();
        assertEquals(entry.getHeader().length + data.length + entry2.getHeader().length + data2.length, tarStream.getBuffer().getPosition());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // Test creating an archive entry for a file
        File file = new File("test.txt");
        ArchiveEntry entry = tarStream.createArchiveEntry(file, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertEquals(file.length(), entry.getSize());

        // Test creating an archive entry for a directory
        File dir = new File("test_dir");
        if (!dir.exists()) {
            dir.mkdir();
        }
        entry = tarStream.createArchiveEntry(dir, "test_dir/");
        assertEquals("test_dir/", entry.getName());
        assertFalse(entry.isDirectory());
        assertFalse(entry.isFile());

        // Test creating an archive entry for a non-existent file
        File nonExistentFile = new File("non_existent_file.txt");
        entry = tarStream.createArchiveEntry(nonExistentFile, "non_existent_file.txt");
        assertEquals("non_existent_file.txt", entry.getName());
        assertEquals(0, entry.getSize());

        // Test creating an archive entry for a non-existent directory
        File nonExistentDir = new File("non_existent_dir");
        entry = tarStream.createArchiveEntry(nonExistentDir, "non_existent_dir/");
        assertEquals("non_existent_dir/", entry.getName());
        assertFalse(entry.isDirectory());
        assertFalse(entry.isFile());
    }
}