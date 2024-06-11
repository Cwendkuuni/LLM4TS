package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

/**
 * TarArchiveOutputStreamTest class for testing TarArchiveOutputStream methods.
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();
        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);
        byte[] actualEOFRecord = outputStream.toByteArray();
        assertArrayEquals(expectedEOFRecord, actualEOFRecord);
        assertArrayEquals(expectedEOFRecord, tarArchiveOutputStream.getBuffer().getRecord(0));
        assertArrayEquals(expectedEOFRecord, tarArchiveOutputStream.getBuffer().getRecord(1));
    }

    @Test
    public void testClose() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithTooLongName() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        String tooLongName = "a".repeat(TarConstants.NAMELEN + 1);
        ArchiveEntry archiveEntry = new JarEntry(tooLongName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        File file = new File("testFile.txt");
        String entryName = "testFile.txt";
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] expectedHeader = archiveEntry.getHeader();
        byte[] actualHeader = tarArchiveOutputStream.getBuffer().getRecord(0);
        assertArrayEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testWrite() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] data = "test data".getBytes();
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("testFile.txt"));
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        int numBytesWritten = tarArchiveOutputStream.write(data, 0, data.length);
        byte[] expectedData = data;
        byte[] actualData = new byte[numBytesWritten];
        System.arraycopy(tarArchiveOutputStream.getBuffer().getRecord(1), 0, actualData, 0, numBytesWritten);
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        File file = new File("testFile.txt");
        String entryName = "testFile.txt";
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[10]);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);
        byte[] actualEOFRecord = tarArchiveOutputStream.getBuffer().getRecord(0);
        assertArrayEquals(expectedEOFRecord, actualEOFRecord);
        assertNull(tarArchiveOutputStream.getBuffer().getRecord(1));
    }

    @Test
    public void testWriteWithLongFileName() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        File file = new File("testLongFileName.txt");
        String entryName = "testLongFileName.txt";
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("long file name test data".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedHeader = archiveEntry.getHeader();
        byte[] actualHeader = tarArchiveOutputStream.getBuffer().getRecord(0);
        byte[] expectedLongLinkName = entryName.getBytes();
        byte[] actualLongLinkName = new byte[expectedLongLinkName.length + 2]; // +2 for null byte
        System.arraycopy(expectedLongLinkName, 0, actualLongLinkName, 0, expectedLongLinkName.length);
        actualLongLinkName[expectedLongLinkName.length] = 0;
        actualLongLinkName[expectedLongLinkName.length + 1] = 0;
        byte[] expectedLongLinkRecord = new byte[TarConstants.RECORD_LENGTH];
        System.arraycopy(expectedLongLinkName, 0, expectedLongLinkRecord, 0, expectedLongLinkName.length);
        System.arraycopy(expectedLongLinkRecord, 0, actualLongLinkRecord, 0, expectedLongLinkRecord.length);
        assertArrayEquals(expectedHeader, actualHeader);
        assertArrayEquals(expectedLongLinkRecord, tarArchiveOutputStream.getBuffer().getRecord(1));
    }
}