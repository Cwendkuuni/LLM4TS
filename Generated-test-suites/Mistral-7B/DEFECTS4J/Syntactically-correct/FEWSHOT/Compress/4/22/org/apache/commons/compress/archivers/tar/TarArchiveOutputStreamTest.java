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
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.apache.commons.compress.archivers.TarConstants;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finish();
        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);
        assertArrayEquals(expectedEOFRecord, outputStream.toByteArray(), 0, TarConstants.RECORD_LENGTH);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.close();
        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);
        assertArrayEquals(expectedEOFRecord, outputStream.toByteArray(), 0, TarConstants.RECORD_LENGTH);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidParam() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(3);
    }

    @Test
    public void testWriteAndCloseEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] testData = "Test Data".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
        byte[] expectedEntryData = new byte[testData.length + 512]; // 512 is the size of a record
        System.arraycopy(testData, 0, expectedEntryData, 0, testData.length);
        byte[] expectedHeader = entry.getHeader().getHeaderBytes();
        System.arraycopy(expectedHeader, 0, expectedEntryData, 0, expectedHeader.length);
        assertArrayEquals(expectedEntryData, outputStream.toByteArray(), 0, expectedEntryData.length);
    }

    @Test(expected = IOException.class)
    public void testWriteToClosedStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.close();
        byte[] testData = "Test Data".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);
        String longFileName = "Test File with a long name. This name has more than " + TarConstants.NAMELEN + " characters.";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);
        byte[] expectedLongFileNameEntry = new byte[entry.getHeader().getHeaderBytes().length + TarConstants.NAME_LENGTH + 1]; // 1 for the NUL terminator
        System.arraycopy(entry.getHeader().getHeaderBytes(), 0, expectedLongFileNameEntry, 0, entry.getHeader().getHeaderBytes().length);
        byte[] longFileNameBytes = longFileName.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(longFileNameBytes, 0, expectedLongFileNameEntry, entry.getHeader().getHeaderBytes().length, longFileNameBytes.length);
        assertArrayEquals(expectedLongFileNameEntry, outputStream.toByteArray(), 0, expectedLongFileNameEntry.length);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameLongFileModeError() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        String longFileName = "Test File with a long name. This name has more than " + TarConstants.NAMELEN + " characters.";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameLongFileTruncate() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        String longFileName = "Test File with a long name. This name has more than " + TarConstants.NAMELEN + " characters.";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);
        byte[] expectedLongFileNameEntry = new byte[entry.getHeader().getHeaderBytes().length + TarConstants.NAME_LENGTH]; // no need for the NUL terminator, as it will be truncated
        System.arraycopy(entry.getHeader().getHeaderBytes(), 0, expectedLongFileNameEntry, 0, entry.getHeader().getHeaderBytes().length);
        assertArrayEquals(expectedLongFileNameEntry, outputStream.toByteArray(), 0, expectedLongFileNameEntry.length);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameLongFileGNU() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        String longFileName = "Test File with a long name. This name has more than " + TarConstants.NAMELEN + " characters.";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName, TarConstants.GNU_LONGLINK);
        tarOutputStream.putArchiveEntry(entry);
        byte[] expectedLongFileNameEntry = new byte[entry.getHeader().getHeaderBytes().length + TarConstants.NAME_LENGTH + 1]; // 1 for the NUL terminator
        System.arraycopy(entry.getHeader().getHeaderBytes(), 0, expectedLongFileNameEntry, 0, entry.getHeader().getHeaderBytes().length);
        byte[] longFileNameBytes = longFileName.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(longFileNameBytes, 0, expectedLongFileNameEntry, entry.getHeader().getHeaderBytes().length, longFileNameBytes.length);
        assertArrayEquals(expectedLongFileNameEntry, outputStream.toByteArray(), 0, expectedLongFileNameEntry.length);
    }

    @Test
    public void testCloseArchiveBeforePuttingAnotherEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry1 = new TarArchiveEntry("test1.txt");
        TarArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        tarOutputStream.putArchiveEntry(entry1);
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.haveUnclosedEntry);
        tarOutputStream.putArchiveEntry(entry2);
        byte[] expectedEntryData1 = new byte[entry1.getSize() + 512]; // 512 is the size of a record
        byte[] expectedHeader1 = entry1.getHeader().getHeaderBytes();
        System.arraycopy(expectedHeader1, 0, expectedEntryData1, 0, expectedHeader1.length);
        byte[] expectedEntryData2 = new byte[entry2.getSize() + 512];
        assertArrayEquals(expectedEntryData1, outputStream.toByteArray(), 0, expectedEntryData1.length);
        outputStream.reset();
        byte[] expectedHeader2 = entry2.getHeader().getHeaderBytes();
        System.arraycopy(expectedHeader2, 0, expectedEntryData2, 0, expectedHeader2.length);
        assertArrayEquals(expectedEntryData2, outputStream.toByteArray(), 0, expectedEntryData2.length);
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry directoryEntry = new TarArchiveEntry("test_dir");
        directoryEntry.setDirectory(true);
        tarOutputStream.putArchiveEntry(directoryEntry);
        byte[] expectedDirectoryEntry = new byte[512]; // size of a record
        assertArrayEquals(expectedDirectoryEntry, outputStream.toByteArray(), 0, expectedDirectoryEntry.length);
    }

    @Test
    public void testWriteEmptyEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[0], 0, 0);
        tarOutputStream.closeArchiveEntry();
        byte[] expectedEntryData = new byte[512]; // size of a record
        assertArrayEquals(expectedEntryData, outputStream.toByteArray(), 0, expectedEntryData.length);
    }

    @Test
    public void testWriteToArchiveWithDataLargerThanRecordSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] data = new byte[TarConstants.DEFAULT_RECORD_SIZE * 3 + 1]; // data larger than a record size
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        byte[] expectedEntryData = new byte[data.length + 512]; // size of a record
        assertArrayEquals(expectedEntryData, outputStream.toByteArray(), 0, expectedEntryData.length);
    }

    @Test
    public void testWriteToArchiveWithDataSmallerThanRecordSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] data = new byte[TarConstants.DEFAULT_RECORD_SIZE / 2]; // data smaller than a record size
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.write(data, 0, data.length); // write the data again to fill a record
        tarOutputStream.closeArchiveEntry();
        byte[] expectedEntryData = new byte[data.length * 2 + 512]; // size of 2 records
        assertArrayEquals(expectedEntryData, outputStream.toByteArray(), 0, expectedEntryData.length);
    }

    @Test
    public void testWriteToArchiveWithDataMixedWithRecords() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] record = new byte[TarConstants.RECORD_LENGTH];
        Arrays.fill(record, (byte) 0);
        byte[] data = new byte[TarConstants.RECORD_LENGTH / 2];
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.write(record, 0, TarConstants.RECORD_LENGTH);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        byte[] expectedEntryData = new byte[data.length * 2 + TarConstants.RECORD_LENGTH];
        assertArrayEquals(expectedEntryData, outputStream.toByteArray(), 0, expectedEntryData.length);
    }

    @Test
    public void testWriteToArchiveWithDataMixedWithRecordsLargerThanRecordSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] record = new byte[TarConstants.RECORD_LENGTH];
        Arrays.fill(record, (byte) 0);
        byte[] data = new byte[TarConstants.RECORD_LENGTH * 2];
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.write(record, 0, TarConstants.RECORD_LENGTH);
        tarOutputStream.write(record, 0, TarConstants.RECORD_LENGTH);
        tarOutputStream.closeArchiveEntry();
        byte[] expectedEntryData = new byte[data.length + 2 * TarConstants.RECORD_LENGTH];
        assertArrayEquals(expectedEntryData, outputStream.toByteArray(), 0, expectedEntryData.length);
    }

    @Test
    public void testWriteToArchiveWithDataMixedWithRecordsAndLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] record = new byte[TarConstants.RECORD_LENGTH];
        Arrays.fill(record, (byte) 0);
        byte[] data = new byte[TarConstants.RECORD_LENGTH / 2];
        String longFileName = "Test File with a long name. This name has more than " + TarConstants.NAMELEN + " characters.";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.write(record, 0, TarConstants.RECORD_LENGTH);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        byte[] expectedLongFileNameEntry = new byte[entry.getHeader().getHeaderBytes().length + TarConstants.NAME_LENGTH];
        System.arraycopy(entry.getHeader().getHeaderBytes(), 0, expectedLongFileNameEntry, 0, entry.getHeader().getHeaderBytes().length);
        byte[] longFileNameBytes = longFileName.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(longFileNameBytes, 0, expectedLongFileNameEntry, entry.getHeader().getHeaderBytes().length, longFileNameBytes.length);
        byte[] expectedEntryData = new byte[data.length * 2 + TarConstants.RECORD_LENGTH];
        assertArrayEquals(expectedLongFileNameEntry, outputStream.toByteArray(), 0, expectedLongFileNameEntry.length);
        outputStream.reset();
        assertArrayEquals(expectedEntryData, outputStream.toByteArray(), 0, expectedEntryData.length);
    }
}