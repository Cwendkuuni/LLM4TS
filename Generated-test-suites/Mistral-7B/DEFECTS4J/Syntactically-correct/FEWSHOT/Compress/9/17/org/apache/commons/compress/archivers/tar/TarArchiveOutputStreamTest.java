package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.ArchiveUtils;

public class TarArchiveOutputStreamTest {

    private static final int RECORD_SIZE = 512;

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, outputStream.getLongFileMode());
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, outputStream.getLongFileMode());
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, outputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);

        outputStream.finish();

        byte[] expectedEofRecord = new byte[2 * RECORD_SIZE];
        Arrays.fill(expectedEofRecord, (byte) 0);
        byte[] actualEofRecord = byteArrayOutputStream.toByteArray();

        assertArrayEquals(expectedEofRecord, actualEofRecord);
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);
        outputStream.finish();

        outputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);

        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(archiveEntry);

        outputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);

        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseAlreadyClosed() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);

        outputStream.close();
        outputStream.close();
    }

    @Test
    public void testGetRecordSize() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        assertEquals(RECORD_SIZE, outputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);

        String entryName = "test.txt";
        TarArchiveEntry archiveEntry = new TarArchiveEntry(entryName);

        outputStream.putArchiveEntry(archiveEntry);

        byte[] expectedEntryHeader = TarArchiveEntry.getHeader(archiveEntry);

        byte[] actualEntryHeader = byteArrayOutputStream.toByteArray();

        assertArrayEquals(expectedEntryHeader, actualEntryHeader);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAlreadyFinished() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);
        outputStream.finish();

        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");

        outputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNullName() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);

        TarArchiveEntry archiveEntry = new TarArchiveEntry(null);

        outputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNullArchiveEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);

        outputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryNonTarArchiveEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);

        ArchiveEntry archiveEntry = new testNonTarArchiveEntry();

        outputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");

        outputStream.putArchiveEntry(archiveEntry);
        outputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryAlreadyClosed() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");

        outputStream.putArchiveEntry(archiveEntry);
        outputStream.closeArchiveEntry();
        outputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");

        outputStream.putArchiveEntry(archiveEntry);

        byte[] dataToWrite = "Test data".getBytes(StandardCharsets.UTF_8);
        int numToWrite = dataToWrite.length;

        outputStream.write(dataToWrite, 0, numToWrite);

        byte[] expectedData = new byte[numToWrite];
        System.arraycopy(dataToWrite, 0, expectedData, 0, numToWrite);

        byte[] actualData = byteArrayOutputStream.toByteArray();

        assertArrayEquals(expectedData, actualData, 0, numToWrite);
    }

    @Test(expected = ArchiveException.class)
    public void testWriteBeyondEntrySize() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");

        outputStream.putArchiveEntry(archiveEntry);

        byte[] dataToWrite = "Test data".getBytes(StandardCharsets.UTF_8);
        int numToWrite = dataToWrite.length + archiveEntry.getSize();

        outputStream.write(dataToWrite, 0, numToWrite);
    }

    @Test
    public void testWriteMultipleBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");

        outputStream.putArchiveEntry(archiveEntry);

        byte[] dataToWrite1 = "Test data1".getBytes(StandardCharsets.UTF_8);
        int numToWrite1 = dataToWrite1.length;
        outputStream.write(dataToWrite1, 0, numToWrite1);

        byte[] dataToWrite2 = "Test data2".getBytes(StandardCharsets.UTF_8);
        int numToWrite2 = dataToWrite2.length;
        outputStream.write(dataToWrite2, 0, numToWrite2);

        byte[] expectedData = new byte[numToWrite1 + numToWrite2];
        System.arraycopy(dataToWrite1, 0, expectedData, 0, numToWrite1);
        System.arraycopy(dataToWrite2, 0, expectedData, numToWrite1, numToWrite2);

        byte[] actualData = byteArrayOutputStream.toByteArray();

        assertArrayEquals(expectedData, actualData, 0, expectedData.length);
    }

    @Test
    public void testWriteMultipleRecords() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);

        TarArchiveEntry archiveEntry1 = new TarArchiveEntry("test1.txt");
        outputStream.putArchiveEntry(archiveEntry1);
        outputStream.closeArchiveEntry();

        TarArchiveEntry archiveEntry2 = new TarArchiveEntry("test2.txt");
        outputStream.putArchiveEntry(archiveEntry2);
        outputStream.closeArchiveEntry();

        byte[] dataToWrite1 = "Test data1".getBytes(StandardCharsets.UTF_8);
        int numToWrite1 = dataToWrite1.length;
        outputStream.write(dataToWrite1, 0, numToWrite1);

        byte[] dataToWrite2 = "Test data2".getBytes(StandardCharsets.UTF_8);
        int numToWrite2 = dataToWrite2.length;
        outputStream.write(dataToWrite2, 0, numToWrite2);

        byte[] expectedData = new byte[numToWrite1 + numToWrite2];
        System.arraycopy(dataToWrite1, 0, expectedData, 0, numToWrite1);
        System.arraycopy(dataToWrite2, 0, expectedData, numToWrite1, numToWrite2);

        byte[] actualData = byteArrayOutputStream.toByteArray();

        assertArrayEquals(expectedData, actualData, 0, expectedData.length);
    }

    @Test
    public void testFlush() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");

        outputStream.putArchiveEntry(archiveEntry);

        byte[] dataToWrite = "Test data".getBytes(StandardCharsets.UTF_8);
        int numToWrite = dataToWrite.length;

        outputStream.write(dataToWrite, 0, numToWrite);
        outputStream.flush();

        byte[] actualData = byteArrayOutputStream.toByteArray();

        byte[] expectedData = new byte[numToWrite];
        System.arraycopy(dataToWrite, 0, expectedData, 0, numToWrite);

        assertArrayEquals(expectedData, actualData, 0, numToWrite);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);

        File inputFile = new File("testInputFile.txt");
        inputFile.createNewFile();

        TarArchiveEntry archiveEntry = outputStream.createArchiveEntry(inputFile, "test.txt");

        byte[] expectedEntryHeader = TarArchiveEntry.getHeader(archiveEntry);

        byte[] actualEntryHeader = byteArrayOutputStream.toByteArray();

        assertArrayEquals(expectedEntryHeader, actualEntryHeader);

        inputFile.delete();
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryNullName() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);

        File inputFile = new File("testInputFile.txt");
        inputFile.createNewFile();

        TarArchiveEntry archiveEntry = outputStream.createArchiveEntry(inputFile, null);
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryNullInputFile() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream, RECORD_SIZE);

        TarArchiveEntry archiveEntry = outputStream.createArchiveEntry(null, "test.txt");
    }

    private static class testNonTarArchiveEntry implements ArchiveEntry {
        @Override
        public String getName() {
            return null;
        }

        @Override
        public long getSize() {
            return 0;
        }

        @Override
        public void writeEntryHeader(byte[] buffer) throws IOException {

        }

        @Override
        public void writeEntryTrailer(byte[] buffer) throws IOException {

        }

        @Override
        public long getCompressedSize() {
            return 0;
        }

        @Override
        public String getComment() {
            return null;
        }

        @Override
        public long getTimeStamp() {
            return 0;
        }

        @Override
        public boolean isDirectory() {
            return false;
        }
    }
}