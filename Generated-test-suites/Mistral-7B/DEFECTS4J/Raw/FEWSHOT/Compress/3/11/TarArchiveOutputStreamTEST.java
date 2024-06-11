package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Test cases for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(out);
        assertSame(out, tarOS.getOutputStream());
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new ByteArrayOutputStream());
        // default value
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOS.getLongFileMode());

        // set LONGFILE_TRUNCATE
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOS.getLongFileMode());

        // set LONGFILE_GNU
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOS.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeNegative() {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOS.setLongFileMode(-1);
    }

    @Test
    public void testFinish() throws IOException {
        byte[] eofRecord = new byte[TarConstants.RECORD_SIZE];
        for (int i = 0; i < TarConstants.RECORD_SIZE; ++i) {
            eofRecord[i] = 0;
        }
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(out);
        tarOS.finish();
        assertArrayEquals(eofRecord, tarOS.getBuffer().getRecordBuf());
        tarOS.finish();
    }

    @Test
    public void testClose() throws IOException {
        byte[] eofRecord = new byte[TarConstants.RECORD_SIZE];
        for (int i = 0; i < TarConstants.RECORD_SIZE; ++i) {
            eofRecord[i] = 0;
        }
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(out);
        tarOS.finish();
        tarOS.close();
        assertArrayEquals(eofRecord, out.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        byte[] eofRecord = new byte[TarConstants.RECORD_SIZE];
        for (int i = 0; i < TarConstants.RECORD_SIZE; ++i) {
            eofRecord[i] = 0;
        }
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(out);
        tarOS.finish();
        tarOS.close();
        tarOS.close();
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(TarConstants.RECORD_SIZE, tarOS.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(out);
        File emptyFile = new File("empty.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry(emptyFile, "test.txt");
        TarArchiveEntry tarEntry = (TarArchiveEntry) archiveEntry;
        tarOS.putArchiveEntry(archiveEntry);

        byte[] expectedHeader = tarEntry.getHeaderBytes();
        byte[] expectedRecord = "test.txt\0\0\0".getBytes(StandardCharsets.US_ASCII);
        byte[] record = tarOS.getBuffer().getRecordBuf();

        // header
        assertEquals(expectedHeader.length, record.length);
        assertArrayEquals(expectedHeader, record, 0, expectedHeader.length);
        // record
        assertEquals(expectedRecord.length, tarOS.getBuffer().getRecordSize() - TarConstants.HEADER_LENGTH);
        assertArrayEquals(expectedRecord, tarOS.getBuffer().getRecordBuf(),
                TarConstants.HEADER_LENGTH, expectedRecord.length);
    }

    @Test
    public void testPutArchiveEntryWithLargeFileName() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(out);
        File file = new File("long_filename.txt");
        String longFileName = "long_filename.txt";
        long fileNameLength = longFileName.length();

        // set longFileMode to LONGFILE_GNU
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        ArchiveEntry archiveEntry = new TarArchiveEntry(file, longFileName);
        TarArchiveEntry tarEntry = (TarArchiveEntry) archiveEntry;

        // write longlink entry
        tarOS.putArchiveEntry(archiveEntry);
        byte[] longLinkEntryBytes = tarEntry.getHeaderBytes();
        assertFalse(longLinkEntryBytes[0] == TarConstants.GNU_LONGLINK);

        // write longname record
        byte[] longNameRecord = longFileName.getBytes(StandardCharsets.US_ASCII);
        tarOS.write(longNameRecord);
        byte[] record = tarOS.getBuffer().getRecordBuf();
        assertArrayEquals(longNameRecord, record, 0, longNameRecord.length);

        // write data record
        byte[] data = new byte[1];
        tarOS.write(data, 0, 1);
        tarOS.closeArchiveEntry();

        byte[] expectedRecord = new byte[longNameRecord.length + data.length + TarConstants.RECORD_SIZE];
        System.arraycopy(longNameRecord, 0, expectedRecord, 0, longNameRecord.length);
        System.arraycopy(data, 0, expectedRecord, longNameRecord.length, data.length);
        assertArrayEquals(expectedRecord, tarOS.getBuffer().getRecordBuf(), 0, expectedRecord.length);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLargeFileNameWithoutGNU() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(out);
        File file = new File("long_filename.txt");
        String longFileName = "long_filename.txt";
        long fileNameLength = longFileName.length();

        // set longFileMode to LONGFILE_TRUNCATE
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        ArchiveEntry archiveEntry = new TarArchiveEntry(file, longFileName);
        TarArchiveEntry tarEntry = (TarArchiveEntry) archiveEntry;

        // write longname record
        byte[] longNameRecord = longFileName.getBytes(StandardCharsets.US_ASCII);
        tarOS.putArchiveEntry(archiveEntry);
        tarOS.write(longNameRecord);

        // write data record
        byte[] data = new byte[1];
        tarOS.write(data, 0, 1);
        tarOS.closeArchiveEntry();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(out);
        tarOS.putArchiveEntry(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(out);
        File file = new File("test.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, "test.txt");
        TarArchiveEntry tarEntry = (TarArchiveEntry) archiveEntry;
        tarOS.putArchiveEntry(archiveEntry);
        tarOS.finish();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(out);
        File file = new File("test.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, "test.txt");
        TarArchiveEntry tarEntry = (TarArchiveEntry) archiveEntry;
        tarOS.putArchiveEntry(archiveEntry);
        tarOS.closeArchiveEntry();
        byte[] record = tarOS.getBuffer().getRecordBuf();
        assertTrue(record[0] == TarConstants.REFCNT || record[0] == TarConstants.VERSION_WITHOUT_WILDCARDS);
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(out);
        byte[] data = "test data".getBytes(StandardCharsets.US_ASCII);
        tarOS.putArchiveEntry(new TarArchiveEntry("test.txt"), "test.txt");
        tarOS.write(data, 0, data.length);
        tarOS.closeArchiveEntry();
        byte[] expected = new byte[data.length + TarConstants.RECORD_SIZE];
        System.arraycopy(data, 0, expected, 0, data.length);
        assertArrayEquals(expected, tarOS.getBuffer().getRecordBuf(), 0, expected.length);
    }

    @Test
    public void testWriteWithPartialRecord() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(out);
        byte[] data = "test data".getBytes(StandardCharsets.US_ASCII);
        tarOS.putArchiveEntry(new TarArchiveEntry("test.txt"), "test.txt");
        tarOS.write(data, 0, 3);
        tarOS.closeArchiveEntry();
        byte[] expected = new byte[3];
        System.arraycopy(data, 0, expected, 0, 3);
        assertArrayEquals(expected, tarOS.getBuffer().getRecordBuf(), 0, expected.length);
    }

    @Test
    public void testWriteMultipleRecords() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(out);
        byte[] data1 = "test data 1".getBytes(StandardCharsets.US_ASCII);
        byte[] data2 = "test data 2".getBytes(StandardCharsets.US_ASCII);
        tarOS.putArchiveEntry(new TarArchiveEntry("test1.txt"), "test1.txt");
        tarOS.write(data1, 0, data1.length);
        tarOS.putArchiveEntry(new TarArchiveEntry("test2.txt"), "test2.txt");
        tarOS.write(data2, 0, data2.length);
        tarOS.closeArchiveEntry();
        tarOS.close();

        byte[] expected = new byte[data1.length + data2.length + 2 * TarConstants.RECORD_SIZE];
        System.arraycopy(data1, 0, expected, 0, data1.length);
        System.arraycopy(data2, 0, expected, data1.length, data2.length);
        assertArrayEquals(expected, out.toByteArray(), 0, expected.length);
    }
}