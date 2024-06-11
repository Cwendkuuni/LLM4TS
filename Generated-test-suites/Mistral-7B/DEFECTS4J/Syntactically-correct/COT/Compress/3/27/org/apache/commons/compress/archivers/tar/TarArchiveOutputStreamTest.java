package org.apache.commons.compress.archivers.tar; // make sure to import all necessary packages

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.TarConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * JUnit 4 test cases for TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    private static final int LONG_FILE_NAME_LENGTH = TarConstants.NAMELEN + 1;
    private static final int RECORD_SIZE = TarBuffer.DEFAULT_RCDSIZE;
    private static final int BLOCK_SIZE = TarBuffer.DEFAULT_BLKSIZE;
    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream(1024);
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        byte[] eofRecord = new byte[RECORD_SIZE * 2];
        Arrays.fill(eofRecord, (byte) 0);
        byte[] expectedEofRecord = new byte[RECORD_SIZE * 2];
        Arrays.fill(expectedEofRecord, (byte) 0);
        Assert.assertArrayEquals(expectedEofRecord, outputStream.toByteArray());
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        byte[] eofRecord = new byte[RECORD_SIZE * 2];
        Arrays.fill(eofRecord, (byte) 0);
        byte[] outputStreamBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(Arrays.concat(eofRecord, eofRecord), outputStreamBytes);
    }

    @Test
    public void testGetRecordSize() {
        Assert.assertEquals(RECORD_SIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntryWithShortFileName() throws IOException {
        String fileName = "testFile.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] record = new byte[RECORD_SIZE];
        tarArchiveEntry.writeEntryHeader(record);
        Assert.assertArrayEquals(record, tarArchiveOutputStream.buffer.getBuffer());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a" + new String(new char[LONG_FILE_NAME_LENGTH]).replace("\0", "a");
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] longFileNameBytes = longFileName.getBytes(StandardCharsets.UTF_8);
        byte[] longLinkRecord = new byte[RECORD_SIZE];
        Arrays.fill(longLinkRecord, (byte) 0);
        longLinkRecord[0] = TarConstants.GNU_LONGLINK;
        longLinkRecord[1] = TarConstants.LF_GNUTYPE_LONGNAME;
        longLinkRecord[2] = (byte) (longFileNameBytes.length + 1);
        writeBytes(longLinkRecord, 0, longFileNameBytes.length + 1, tarArchiveOutputStream.buffer);
        byte[] expectedRecord = new byte[2 * RECORD_SIZE];
        System.arraycopy(longLinkRecord, 0, expectedRecord, 0, longLinkRecord.length);
        System.arraycopy(tarArchiveEntry.getName().getBytes(StandardCharsets.UTF_8), 0, expectedRecord, longLinkRecord.length, tarArchiveEntry.getName().length() + 1);
        Assert.assertArrayEquals(expectedRecord, tarArchiveOutputStream.buffer.getBuffer());
    }

    @Test
    public void testWriteWithShortFileName() throws IOException {
        String fileName = "testFile.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] record = new byte[RECORD_SIZE];
        tarArchiveEntry.writeEntryHeader(record);
        writeBytes(record, 0, RECORD_SIZE, tarArchiveOutputStream.buffer);
        byte[] content = "This is a test file".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(content, 0, content.length);
        byte[] expectedContent = new byte[content.length];
        System.arraycopy(content, 0, expectedContent, 0, content.length);
        byte[] expectedRecordWithContent = new byte[RECORD_SIZE + content.length];
        System.arraycopy(record, 0, expectedRecordWithContent, 0, RECORD_SIZE);
        System.arraycopy(content, 0, expectedRecordWithContent, RECORD_SIZE, content.length);
        Assert.assertArrayEquals(expectedRecordWithContent, tarArchiveOutputStream.buffer.getBuffer());
    }

    @Test
    public void testWriteWithLongFileName() throws IOException {
        String longFileName = "a" + new String(new char[LONG_FILE_NAME_LENGTH]).replace("\0", "a");
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] longFileNameBytes = longFileName.getBytes(StandardCharsets.UTF_8);
        byte[] longLinkRecord = new byte[RECORD_SIZE];
        // write longLinkRecord
        writeBytes(longLinkRecord, 0, longLinkRecord.length, tarArchiveOutputStream.buffer);
        // write longFileName
        writeBytes(longFileNameBytes, 0, longFileNameBytes.length + 1, tarArchiveOutputStream.buffer);
        String content = "This is a test file with a long file name.";
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(contentBytes, 0, contentBytes.length);
        byte[] expectedRecordWithLongFileNameAndContent = new byte[2 * RECORD_SIZE + content.length()];
        System.arraycopy(longLinkRecord, 0, expectedRecordWithLongFileNameAndContent, 0, longLinkRecord.length);
        System.arraycopy(longFileNameBytes, 0, expectedRecordWithLongFileNameAndContent, longLinkRecord.length, longFileNameBytes.length + 1);
        System.arraycopy(contentBytes, 0, expectedRecordWithLongFileNameAndContent, longLinkRecord.length + longFileNameBytes.length + 1, contentBytes.length);
        Assert.assertArrayEquals(expectedRecordWithLongFileNameAndContent, tarArchiveOutputStream.buffer.getBuffer());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        String fileName = "testFile.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] record = new byte[RECORD_SIZE];
        tarArchiveEntry.writeEntryHeader(record);
        writeBytes(record, 0, RECORD_SIZE, tarArchiveOutputStream.buffer);
        byte[] content = "This is a test file".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(content, 0, content.length);
        byte[] expectedRecordWithContent = new byte[RECORD_SIZE + content.length];
        System.arraycopy(record, 0, expectedRecordWithContent, 0, RECORD_SIZE);
        System.arraycopy(content, 0, expectedRecordWithContent, RECORD_SIZE, content.length);
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertArrayEquals(expectedRecordWithContent, tarArchiveOutputStream.buffer.getBuffer());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndTruncateMode() throws IOException {
        String longFileName = "a" + new String(new char[LONG_FILE_NAME_LENGTH]).replace("\0", "a");
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        Assert.fail("Expected IOException with long file name and LONGFILE_TRUNCATE mode");
    }

    private void writeBytes(byte[] buffer, int offset, int length, TarBuffer tarBuffer) throws IOException {
        for (int i = offset; i < offset + length; ++i) {
            tarBuffer.writeByte(buffer[i]);
        }
    }
}