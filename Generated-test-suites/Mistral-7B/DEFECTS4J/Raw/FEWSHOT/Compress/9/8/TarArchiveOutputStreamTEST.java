package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CountingInputStream;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();
        byte[] expectedEOFRecord = new byte[TarConstants.TAR_RECORD_SIZE];
        Arrays.fill(expectedEFofRecord, (byte) 0);
        byte[] eof1 = tarArchiveOutputStream.getBuffer().getRecord();
        byte[] eof2 = tarArchiveOutputStream.getBuffer().getRecord();
        assertArrayEquals(expectedEOFRecord, eof1);
        assertArrayEquals(expectedEOFRecord, eof2);
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.close();
        assertFalse(tarArchiveOutputStream.isOpen());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] record = tarArchiveOutputStream.getBuffer().getRecord();
        byte[] expectedRecord = new byte[TarConstants.TAR_RECORD_SIZE];
        TarArchiveEntry tarEntry = (TarArchiveEntry) entry;
        tarEntry.writeEntryHeader(expectedRecord);
        assertArrayEquals(expectedRecord, record);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        TarArchiveEntry tarEntry = (TarArchiveEntry) entry;
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] record = tarArchiveOutputStream.getBuffer().getRecord();
        byte[] expectedRecord = new byte[TarConstants.TAR_RECORD_SIZE];
        tarEntry.writeEntryHeader(expectedRecord);
        Arrays.fill(expectedRecord, (byte) 0, TarConstants.TAR_HEADER_SIZE, (byte) 0x05);
        assertArrayEquals(expectedRecord, record);
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        String data = "Hello World!";
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        TarArchiveEntry tarEntry = (TarArchiveEntry) entry;
        tarEntry.setSize(data.length());
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data.getBytes(), 0, data.length());
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedRecord = new byte[TarConstants.TAR_RECORD_SIZE];
        tarEntry.writeEntryHeader(expectedRecord);
        byte[] dataBytes = data.getBytes();
        System.arraycopy(dataBytes, 0, expectedRecord, TarConstants.TAR_HEADER_SIZE, data.length());
        byte[] record = tarArchiveOutputStream.getBuffer().getRecord();
        assertArrayEquals(expectedRecord, record);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        String longFileName = "longFileNameThatIsLongerThanNameLen.txt";
        TarArchiveEntry tarEntry = (TarArchiveEntry) entry;
        tarEntry.setName(longFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        byte[] record = tarArchiveOutputStream.getBuffer().getRecord();
        int expectedNameLength = longFileName.getBytes().length + 1;
        byte[] expectedLongLinkRecord = new byte[TarConstants.TAR_LONG_LINK_SIZE];
        System.arraycopy(ArchiveUtils.toAsciiBytes(longFileName), 0, expectedLongLinkRecord, 0, expectedNameLength);
        expectedLongLinkRecord[expectedNameLength] = (byte) 0x00;
        assertArrayEquals(expectedLongLinkRecord, record);
    }

    @Test(expected = RuntimeException.class)
    public void testSetLongFileModeInvalidMode() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        String longFileName = "longFileNameThatIsLongerThanNameLen.txt";
        TarArchiveEntry tarEntry = (TarArchiveEntry) entry;
        tarEntry.setName(longFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.setLongFileMode(3);
    }

    @Test
    public void testGetRecordSize() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        int expectedRecordSize = TarConstants.TAR_RECORD_SIZE;
        assertEquals(expectedRecordSize, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        File file = new File("test.txt");
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");
        assertEquals("test.txt", archiveEntry.getName());
        assertTrue(archiveEntry instanceof TarArchiveEntry);
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry1 = new TarArchiveEntry("test1.txt");
        ArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        TarArchiveEntry tarEntry1 = (TarArchiveEntry) entry1;
        TarArchiveEntry tarEntry2 = (TarArchiveEntry) entry2;
        tarEntry1.setSize(5);
        tarEntry2.setSize(3);
        tarArchiveOutputStream.putArchiveEntry(entry1);
        tarArchiveOutputStream.write(new byte[] { 1, 2, 3, 4 }, 0, 4);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(entry2);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedRecord1 = new byte[TarConstants.TAR_RECORD_SIZE];
        tarEntry1.writeEntryHeader(expectedRecord1);
        System.arraycopy(new byte[] { 1, 2, 3, 4 }, 0, expectedRecord1, TarConstants.TAR_HEADER_SIZE, 4);
        byte[] expectedRecord2 = new byte[TarConstants.TAR_RECORD_SIZE];
        tarEntry2.writeEntryHeader(expectedRecord2);
        byte[] record1 = tarArchiveOutputStream.getBuffer().getRecord();
        byte[] record2 = tarArchiveOutputStream.getBuffer().getRecord();
        assertArrayEquals(expectedRecord1, record1);
        assertArrayEquals(expectedRecord2, record2);
    }

    @Test
    public void testWriteMultipleEntriesWithPadding() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry1 = new TarArchiveEntry("test1.txt");
        ArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        TarArchiveEntry tarEntry1 = (TarArchiveEntry) entry1;
        TarArchiveEntry tarEntry2 = (TarArchiveEntry) entry2;
        tarEntry1.setSize(5);
        tarEntry2.setSize(3);
        tarArchiveOutputStream.putArchiveEntry(entry1);
        tarArchiveOutputStream.write(new byte[] { 1, 2, 3, 4 }, 0, 4);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(entry2);
        tarArchiveOutputStream.write(new byte[5], 0, 5);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedRecord1 = new byte[TarConstants.TAR_RECORD_SIZE];
        tarEntry1.writeEntryHeader(expectedRecord1);
        System.arraycopy(new byte[] { 1, 2, 3, 4 }, 0, expectedRecord1, TarConstants.TAR_HEADER_SIZE, 4);
        byte[] expectedRecord2 = new byte[TarConstants.TAR_RECORD_SIZE];
        tarEntry2.writeEntryHeader(expectedRecord2);
        byte[] record1 = tarArchiveOutputStream.getBuffer().getRecord();
        byte[] record2 = tarArchiveOutputStream.getBuffer().getRecord();
        assertArrayEquals(expectedRecord1, record1);
        assertArrayEquals(expectedRecord2, record2);
    }

    @Test
    public void testWriteLargeFile() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        TarArchiveEntry tarEntry = (TarArchiveEntry) entry;
        tarEntry.setSize(1024 * 1024); // 1MB
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = new byte[1024 * 1024];
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] record = tarArchiveOutputStream.getBuffer().getRecord();
        byte[] expectedRecord = new byte[TarConstants.TAR_RECORD_SIZE];
        tarEntry.writeEntryHeader(expectedRecord);
        byte[] dataBytes = data;
        System.arraycopy(dataBytes, 0, expectedRecord, TarConstants.TAR_HEADER_SIZE, data.length);
        assertArrayEquals(expectedRecord, record);
    }

    @Test
    public void testWriteMultipleLargeFiles() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        setLongFileMode(tarArchiveOutputStream, TarArchiveOutputStream.LONGFILE_GNU);
        ArchiveEntry entry1 = new TarArchiveEntry("test1.txt");
        ArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        TarArchiveEntry tarEntry1 = (TarArchiveEntry) entry1;
        TarArchiveEntry tarEntry2 = (TarArchiveEntry) entry2;
        tarEntry1.setSize(1024 * 1024); // 1MB
        tarEntry2.setSize(1024 * 1024); // 1MB
        tarArchiveOutputStream.putArchiveEntry(entry1);
        byte[] data1 = new byte[1024 * 1024];
        tarArchiveOutputStream.write(data1, 0, data1.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(entry2);
        byte[] data2 = new byte[1024 * 1024];
        tarArchiveOutputStream.write(data2, 0, data2.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] record1 = tarArchiveOutputStream.getBuffer().getRecord();
        byte[] record2 = tarArchiveOutputStream.getBuffer().getRecord();
        byte[] expectedRecord1 = new byte[TarConstants.TAR_RECORD_SIZE];
        tarEntry1.writeEntryHeader(expectedRecord1);
        byte[] expectedRecord2 = new byte[TarConstants.TAR_RECORD_SIZE];
        tarEntry2.writeEntryHeader(expectedRecord2);
        assertArrayEquals(expectedRecord1, record1);
        assertArrayEquals(expectedRecord2, record2);
    }

    private static void setLongFileMode(TarArchiveOutputStream tarArchiveOutputStream, int longFileMode) {
        tarArchiveOutputStream.setLongFileMode(longFileMode);
    }

    private static void assertArrayEquals(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

}