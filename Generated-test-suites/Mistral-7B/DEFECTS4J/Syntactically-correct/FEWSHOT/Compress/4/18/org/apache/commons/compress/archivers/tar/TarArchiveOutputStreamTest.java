package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    @Test
    public void testEmptyOutput() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(baos);
        tarArchiveOutputStream.finish();
        byte[] expected = new byte[TarConstants.RECORD_SIZE * 2];
        assertEquals(expected.length, baos.size());
        assertArrayEquals(expected, baos.toByteArray());
    }

    @Test
    public void testPutAndCloseEntryWithNameLessThanNameLengthLimit() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(baos);

        String name = "testEntry";
        ArchiveEntry archiveEntry = new TarArchiveEntry(name);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] expected = new byte[TarConstants.RECORD_SIZE];
        archiveEntry.writeTo(expected);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(expected.length, baos.size());
        assertArrayEquals(expected, baos.toByteArray());
    }

    @Test
    public void testPutAndCloseEntryWithNameLongerThanNameLengthLimit() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(baos);

        String name = "testEntryNameThatIsLongerThanNameLengthLimit";
        ArchiveEntry archiveEntry = new TarArchiveEntry(name);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] longLinkNameBytes = name.getBytes(StandardCharsets.UTF_8);
        long linkNameLength = longLinkNameBytes.length + 1; // +1 for NUL terminator
        byte[] expected = new byte[TarConstants.RECORD_SIZE * 2];

        // write GNU long link entry
        System.arraycopy(TarConstants.GNU_LONGLINK.getBytes(), 0, expected, 0,
                TarConstants.GNU_LONGLINK.length);
        System.arraycopy(longLinkNameBytes, 0, expected,
                TarConstants.GNU_LONGLINK.length, linkNameLength);
        tarArchiveOutputStream.write(longLinkNameBytes);
        tarArchiveOutputStream.write(0); // NUL terminator
        tarArchiveOutputStream.closeArchiveEntry();

        // write entry header
        archiveEntry.writeEntryHeader(tarArchiveOutputStream.recordBuf);
        tarArchiveOutputStream.buffer.writeRecord(tarArchiveOutputStream.recordBuf);
        tarArchiveOutputStream.currBytes += TarConstants.RECORD_SIZE;

        assertEquals(expected.length, baos.size());
        assertArrayEquals(expected, baos.toByteArray());
    }

    @Test
    public void testPutMultipleEntriesWithNameLessThanNameLengthLimit() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(baos);

        String entry1Name = "testEntry1";
        ArchiveEntry entry1 = new TarArchiveEntry(entry1Name);
        tarArchiveOutputStream.putArchiveEntry(entry1);
        byte[] entry1Expected = new byte[TarConstants.RECORD_SIZE];
        entry1.writeTo(entry1Expected);
        tarArchiveOutputStream.closeArchiveEntry();

        String entry2Name = "testEntry2";
        ArchiveEntry entry2 = new TarArchiveEntry(entry2Name);
        tarArchiveOutputStream.putArchiveEntry(entry2);
        byte[] entry2Expected = new byte[TarConstants.RECORD_SIZE];
        entry2.writeTo(entry2Expected);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expected = new byte[TarConstants.RECORD_SIZE * 3];
        System.arraycopy(entry1Expected, 0, expected, 0, TarConstants.RECORD_SIZE);
        System.arraycopy(entry2Expected, 0, expected, TarConstants.RECORD_SIZE, TarConstants.RECORD_SIZE);

        assertEquals(expected.length, baos.size());
        assertArrayEquals(expected, baos.toByteArray());
    }

    @Test
    public void testPutEntryWithoutCloseEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(baos);

        String entryName = "testEntry";
        ArchiveEntry archiveEntry = new TarArchiveEntry(entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        // check that entry header was written
        byte[] expectedEntryHeader = new byte[TarConstants.RECORD_SIZE];
        archiveEntry.writeEntryHeader(tarArchiveOutputStream.recordBuf);
        tarArchiveOutputStream.buffer.writeRecord(tarArchiveOutputStream.recordBuf);
        byte[] expectedEntryHeaderBytes = new byte[TarConstants.RECORD_SIZE];
        System.arraycopy(tarArchiveOutputStream.recordBuf, 0, expectedEntryHeaderBytes, 0, TarConstants.RECORD_SIZE);
        assertArrayEquals(expectedEntryHeader, expectedEntryHeaderBytes);

        // check that no data was written
        assertEquals(TarConstants.RECORD_SIZE, baos.size());
        assertFalse(baos.toByteArray()[0] > 0);

        // check that entry is still open
        assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryBeforePutArchiveEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(baos);

        String entryName = "testEntry";
        ArchiveEntry archiveEntry = new TarArchiveEntry(entryName);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(baos);

        String entryName = "testEntry";
        ArchiveEntry archiveEntry = new TarArchiveEntry(entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] testData = "testData".getBytes(StandardCharsets.UTF_8);
        int offset = 0;
        int numToWrite = testData.length;

        // write test data in chunks smaller than the record size
        while (numToWrite > 0) {
            int numToWriteThisTime = Math.min(numToWrite, TarConstants.RECORD_SIZE - currBytes);
            tarArchiveOutputStream.write(testData, offset + TarConstants.RECORD_SIZE - numToWriteThisTime,
                    numToWriteThisTime);
            offset += numToWriteThisTime;
            numToWrite -= numToWriteThisTime;
        }

        byte[] expected = new byte[TarConstants.RECORD_SIZE * 2 + testData.length];
        System.arraycopy(archiveEntry.getHeader(), 0, expected, 0, TarConstants.RECORD_SIZE);
        System.arraycopy(testData, 0, expected, TarConstants.RECORD_SIZE, testData.length);

        assertEquals(expected.length, baos.size());
        assertArrayEquals(expected, baos.toByteArray());
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(baos);
        tarArchiveOutputStream.finish();

        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_SIZE * 2];
        for (int i = 0; i < TarConstants.RECORD_SIZE * 2; ++i) {
            expectedEFRecord[i] = 0;
        }

        assertEquals(expectedEOFRecord.length, baos.size());
        assertArrayEquals(expectedEOFRecord, baos.toByteArray());
    }
}