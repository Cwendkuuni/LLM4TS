package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * TarArchiveOutputStreamTest class.
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        // Test default constructor
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        assertEquals("Default record size should be TarBuffer.DEFAULT_RCDSIZE",
                TarBuffer.DEFAULT_RCDSIZE, tarOut.getRecordSize());
        tarOut.close();

        // Test constructor with OutputStream
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut2 = new TarArchiveOutputStream(out);
        assertEquals("Record size should be the same as the one passed in constructor",
                TarBuffer.DEFAULT_RCDSIZE, tarOut2.getRecordSize());
        tarOut2.close();

        // Test constructor with OutputStream, blockSize, and recordSize
        OutputStream out2 = new ByteArrayOutputStream();
        int blockSize = 512;
        int recordSize = 1024;
        TarArchiveOutputStream tarOut3 = new TarArchiveOutputStream(out2, blockSize, recordSize);
        assertEquals("Record size should be the one passed in constructor", recordSize, tarOut3.getRecordSize());
        tarOut3.close();
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveEntry;
        tarArchiveEntry.setName("longFileNameThatWillBeTruncated.txt");

        assertThrows(RuntimeException.class, () -> tarOut.putArchiveEntry(archiveEntry));

        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOut.putArchiveEntry(archiveEntry);
        tarOut.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(tarArchiveEntry);

        byte[] expectedHeader = getExpectedHeader("test.txt", (int) TarConstants.NORMAL);
        byte[] record = new byte[TarBuffer.DEFAULT_RCDSIZE];

        tarOut.write(0, record, 0, record.length);
        assertEquals("The header in the written record should match the expected header",
                expectedHeader, record);

        tarOut.close();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(tarArchiveEntry);

        byte[] smallBuffer = new byte[512];
        byte[] largeBuffer = new byte[TarConstants.NAMELEN + 1];
        System.arraycopy("longFileNameThatWillBeTruncated.txt".getBytes(), 0, largeBuffer, 0, largeBuffer.length);

        tarOut.write(0, smallBuffer, 0, smallBuffer.length);
        tarOut.write(0, largeBuffer, 0, largeBuffer.length);
        tarOut.closeArchiveEntry();

        byte[] expectedEOFRecord = new byte[TarBuffer.DEFAULT_RCDSIZE];
        for (int i = 0; i < expectedEOFRecord.length; i++) {
            expectedEOFRecord[i] = 0;
        }
        tarOut.write(0, expectedEOFRecord, 0, expectedEOFRecord.length);

        tarOut.close();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(tarArchiveEntry);

        byte[] data = "Hello World!".getBytes();
        byte[] writeBuffer = new byte[TarBuffer.DEFAULT_RCDSIZE];
        int offset = 0;

        tarOut.write(writeBuffer, offset, data.length);
        byte[] expectedRecord = getExpectedRecord(data);
        assertEquals("The record written to the stream should match the expected record",
                expectedRecord, writeBuffer);

        tarOut.close();
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(tarArchiveEntry);

        byte[] data = "Hello World!".getBytes();
        byte[] writeBuffer = new byte[TarBuffer.DEFAULT_RCDSIZE];
        int offset = 0;

        tarOut.write(writeBuffer, offset, data.length);
        tarOut.finish();

        byte[] expectedEOFRecord = new byte[TarBuffer.DEFAULT_RCDSIZE * 2];
        for (int i = 0; i < expectedEOFRecord.length; i++) {
            expectedEOFRecord[i] = 0;
        }
        byte[] writtenEOFRecord = new byte[TarBuffer.DEFAULT_RCDSIZE];
        tarOut.read(writtenEOFRecord, 0, writtenEOFRecord.length);
        assertEquals("The EOF record written to the stream should match the expected EOF record",
                expectedEOFRecord, writtenEOFRecord);

        tarOut.close();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(tarArchiveEntry);

        byte[] data = "Hello World!".getBytes();
        byte[] writeBuffer = new byte[TarBuffer.DEFAULT_RCDSIZE];
        int offset = 0;

        tarOut.write(writeBuffer, offset, data.length);
        tarOut.close();

        byte[] expectedEOFRecord = new byte[TarBuffer.DEFAULT_RCDSIZE * 2];
        for (int i = 0; i < expectedEOFRecord.length; i++) {
            expectedEOFRecord[i] = 0;
        }
        byte[] writtenEOFRecord = new byte[TarBuffer.DEFAULT_RCDSIZE];
        ((ByteArrayOutputStream) tarOut.getOutputStream()).toByteArray();
        tarOut.read(writtenEOFRecord, 0, writtenEOFRecord.length);
        assertEquals("The EOF record written to the stream should match the expected EOF record",
                expectedEOFRecord, writtenEOFRecord);
    }

    private byte[] getExpectedHeader(String fileName, int fileType) {
        byte[] header = new byte[TarBuffer.HEADER_LENGTH];
        System.arraycopy(TarConstants.MAGIC_NUMBER, 0, header, 0, TarConstants.MAGIC_NUMBER.length);
        int fileSize = fileName.length() + TarConstants.NAMELEN + 1;
        header[TarConstants.HEADER_NAME_LENGTH] = (byte) (fileSize >>> 8);
        header[TarConstants.HEADER_NAME_LENGTH + 1] = (byte) fileSize;
        header[TarConstants.HEADER_NAME_INDEX] = (byte) (fileName.length() & 0xFF);
        header[TarConstants.HEADER_NAME_INDEX + 1] = (byte) ((fileName.length() >>> 8) & 0xFF);
        header[TarConstants.HEADER_FILE_NAME_LENGTH] = (byte) (fileName.length() & 0xFF);
        header[TarConstants.HEADER_NAME_LENGTH + 1] = (byte) ((fileName.length() >>> 8) & 0xFF);
        header[TarConstants.HEADER_FILE_MODE] = (byte) fileType;
        header[TarConstants.HEADER_UNIX_UID] = 0;
        header[TarConstants.HEADER_UNIX_GID] = 0;
        header[TarConstants.HEADER_UNIX_MODE] = 0644;
        header[TarConstants.HEADER_UNIX_ACCESS_TIME_LOW] = 0;
        header[TarConstants.HEADER_UNIX_ACCESS_TIME_HIGH] = 0;
        header[TarConstants.HEADER_UNIX_MODIFICATION_TIME_LOW] = 0;
        header[TarConstants.HEADER_UNIX_MODIFICATION_TIME_HIGH] = 0;
        header[TarConstants.HEADER_UNIX_CHECKSUM_LOW] = 0;
        header[TarConstants.HEADER_UNIX_CHECKSUM_HIGH] = 0;
        header[TarConstants.HEADER_UNIX_CHECKSUM_VERIFY] = 0;
        header[TarConstants.HEADER_UNIX_DEVICE_TYPE] = 0;
        header[TarConstants.HEADER_UNIX_DEVICE_ID_LOW] = 0;
        header[TarConstants.HEADER_UNIX_DEVICE_ID_HIGH] = 0;
        header[TarConstants.HEADER_SIZE] = (byte) (fileName.length() + TarConstants.NAMELEN + 1);
        header[TarConstants.HEADER_CHECKSUM] = 0;
        return header;
    }

    private byte[] getExpectedRecord(byte[] data) {
        byte[] record = new byte[TarBuffer.DEFAULT_RCDSIZE];
        System.arraycopy(TarConstants.MAGIC_NUMBER, 0, record, 0, TarConstants.MAGIC_NUMBER.length);
        int fileSize = data.length + TarConstants.NAMELEN + 1;
        record[TarConstants.HEADER_NAME_LENGTH] = (byte) (fileSize >>> 8);
        record[TarConstants.HEADER_NAME_LENGTH + 1] = (byte) fileSize;
        record[TarConstants.HEADER_NAME_INDEX] = (byte) (data.length & 0xFF);
        record[TarConstants.HEADER_NAME_INDEX + 1] = (byte) ((data.length >>> 8) & 0xFF);
        record[TarConstants.HEADER_FILE_NAME_LENGTH] = (byte) (data.length & 0xFF);
        record[TarConstants.HEADER_NAME_LENGTH + 1] = (byte) ((data.length >>> 8) & 0xFF);
        record[TarConstants.HEADER_SIZE] = (byte) (data.length + TarConstants.NAMELEN + 1);
        long checksum = 0;
        for (int i = 0; i < data.length; i++) {
            checksum += (long) data[i];
        }
        record[TarConstants.HEADER_CHECKSUM_LOW] = (byte) (checksum & 0xFF);
        record[TarConstants.HEADER_CHECKSUM_HIGH] = (byte) ((checksum >>> 8) & 0xFF);
        return record;
    }
}