package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(tarArchiveOutputStream.getBuffer());
        assertNotNull(tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.finish();
        byte[] eofRecord = tarArchiveOutputStream.getBuffer().getBuffer();
        assertArrayEquals(new byte[TarConstants.RECORD_SIZE], eofRecord);
        tarArchiveOutputStream.finish();
        eofRecord = tarArchiveOutputStream.getBuffer().getBuffer();
        assertArrayEquals(new byte[TarConstants.RECORD_SIZE], eofRecord);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        assertEquals(true, tarArchiveOutputStream.isClosed());
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] testByteArray = "test".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(testByteArray.length);
        tarArchiveEntry.setName("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(testByteArray, 0, testByteArray.length);
        assertFalse(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] testByteArray = "test".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(testByteArray.length);
        tarArchiveEntry.setName("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(testByteArray, 0, testByteArray.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] record = tarArchiveOutputStream.getBuffer().getBuffer();
        byte[] nameBytes = tarArchiveEntry.getName().getBytes(StandardCharsets.UTF_8);
        byte[] header = tarArchiveEntry.getHeader();
        int nameLength = nameBytes.length;
        int namePadding = TarConstants.NAMELEN - nameLength;
        int headerLength = TarConstants.HEADER_SIZE;
        assertArrayEquals(new byte[TarConstants.RECORD_SIZE], record, 0, TarConstants.RECORD_SIZE);
        for (int i = 0; i < nameLength; i++) {
            assertEquals(nameBytes[i], record[i + TarConstants.NAME_INDEX]);
        }
        for (int i = 0; i < namePadding; i++) {
            assertEquals(0, record[i + TarConstants.NAME_INDEX + nameLength]);
        }
        for (int i = 0; i < headerLength; i++) {
            assertEquals(header[i], record[i + nameLength + namePadding]);
        }
        tarArchiveOutputStream.close();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(10);
        tarArchiveEntry.setName("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] testByteArray = "test".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testByteArray, 0, testByteArray.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] record = tarArchiveOutputStream.getBuffer().getBuffer();
        byte[] nameBytes = tarArchiveEntry.getName().getBytes(StandardCharsets.UTF_8);
        int nameLength = nameBytes.length;
        int namePadding = TarConstants.NAMELEN - nameLength;
        int headerLength = TarConstants.HEADER_SIZE;
        int dataLength = 10;
        assertArrayEquals(new byte[TarConstants.RECORD_SIZE], record, 0, TarConstants.RECORD_SIZE);
        for (int i = 0; i < nameLength; i++) {
            assertEquals(nameBytes[i], record[i + TarConstants.NAME_INDEX]);
        }
        for (int i = 0; i < namePadding; i++) {
            assertEquals(0, record[i + TarConstants.NAME_INDEX + nameLength]);
        }
        for (int i = 0; i < headerLength; i++) {
            assertEquals(tarArchiveEntry.getHeader()[i], record[i + nameLength + namePadding]);
        }
        for (int i = 0; i < dataLength; i++) {
            assertEquals(testByteArray[i], record[i + TarConstants.HEADER_SIZE + nameLength + namePadding + headerLength]);
        }
        tarArchiveOutputStream.close();
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(10);
        tarArchiveEntry.setName("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] testByteArray = "test".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testByteArray, 0, testByteArray.length);
        try {
            tarArchiveOutputStream.write(testByteArray, 0, testByteArray.length);
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.write(testByteArray, 0, testByteArray.length);
        tarArchiveOutputStream.close();
    }

    @Test
    public void testWriteWithLongFileName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test/test2/test3.txt");
        tarArchiveEntry.setSize(10);
        tarArchiveEntry.setName("test/test2/test3.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] testByteArray = "test".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testByteArray, 0, testByteArray.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] record = tarArchiveOutputStream.getBuffer().getBuffer();
        byte[] nameBytes = tarArchiveEntry.getName().getBytes(StandardCharsets.UTF_8);
        int nameLength = nameBytes.length;
        int namePadding = TarConstants.NAMELEN - nameLength;
        int headerLength = TarConstants.HEADER_SIZE;
        int dataLength = 10;
        assertArrayEquals(new byte[TarConstants.RECORD_SIZE], record, 0, TarConstants.RECORD_SIZE);
        for (int i = 0; i < nameLength; i++) {
            assertEquals(nameBytes[i], record[i + TarConstants.NAME_INDEX]);
        }
        for (int i = 0; i < namePadding; i++) {
            assertEquals(0, record[i + TarConstants.NAME_INDEX + nameLength]);
        }
        for (int i = 0; i < headerLength; i++) {
            assertEquals(tarArchiveEntry.getHeader()[i], record[i + nameLength + namePadding]);
        }
        for (int i = 0; i < dataLength; i++) {
            assertEquals(testByteArray[i], record[i + TarConstants.HEADER_SIZE + nameLength + namePadding + headerLength]);
        }
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithTooLongFileName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String tooLongFileName = "test".repeat(TarConstants.NAMELEN + 1);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(tooLongFileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(10);
        tarArchiveEntry.setName("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] testByteArray = "test".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testByteArray, 0, testByteArray.length);
        tarArchiveOutputStream.flush();
        byte[] record = tarArchiveOutputStream.getBuffer().getBuffer();
        byte[] nameBytes = tarArchiveEntry.getName().getBytes(StandardCharsets.UTF_8);
        int nameLength = nameBytes.length;
        int namePadding = TarConstants.NAMELEN - nameLength;
        int headerLength = TarConstants.HEADER_SIZE;
        int dataLength = 10;
        assertArrayEquals(new byte[TarConstants.RECORD_SIZE], record, 0, TarConstants.RECORD_SIZE);
        for (int i = 0; i < nameLength; i++) {
            assertEquals(nameBytes[i], record[i + TarConstants.NAME_INDEX]);
        }
        for (int i = 0; i < namePadding; i++) {
            assertEquals(0, record[i + TarConstants.NAME_INDEX + nameLength]);
        }
        for (int i = 0; i < headerLength; i++) {
            assertEquals(tarArchiveEntry.getHeader()[i], record[i + nameLength + namePadding]);
        }
        for (int i = 0; i < dataLength; i++) {
            assertEquals(testByteArray[i], record[i + TarConstants.HEADER_SIZE + nameLength + namePadding + headerLength]);
        }
        tarArchiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File testFile = new File("test.txt");
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = tarArchiveOutputStream.createArchiveEntry(testFile, "test.txt");
        assertEquals("test.txt", tarArchiveEntry.getName());
        assertEquals(testFile.length(), tarArchiveEntry.getSize());
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.close();
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateArchiveEntryWithNonExistentFile() throws IOException {
        File nonExistentFile = new File("nonExistentFile.txt");
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.createArchiveEntry(nonExistentFile, "test.txt");
    }

}