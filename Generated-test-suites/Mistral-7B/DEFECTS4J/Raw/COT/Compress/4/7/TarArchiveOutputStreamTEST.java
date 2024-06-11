package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * JUnit 4 test cases for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream testOutputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        testOutputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(testOutputStream);
    }

    @Test
    public void testConstructorWithOutputStream() throws IOException {
        // Verify that the constructor with OutputStream works correctly.
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(testOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSize() throws IOException {
        // Verify that the constructor with OutputStream and block size works correctly.
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(testOutputStream, TarBuffer.DEFAULT_BLKSIZE);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSize() throws IOException {
        // Verify that the constructor with OutputStream, block size, and record size works correctly.
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(testOutputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        // Verify that setLongFileMode() sets the longFileMode field correctly.
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        // Verify that finish() writes the correct number of EOF records.
        tarArchiveOutputStream.finish();
        byte[] eofBytes = testOutputStream.toByteArray();
        int numEofRecords = TarConstants.NUM_EOF_RECORDS;
        int eofRecordSize = TarConstants.RECORD_SIZE;
        assertEquals(numEofRecords * eofRecordSize, eofBytes.length);
    }

    @Test
    public void testClose() throws IOException {
        // Verify that close() writes the contents of any unclosed entries.
        TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Test entry content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] outputBytes = testOutputStream.toByteArray();
        byte[] expectedBytes = new byte[]{'T', 'e', 's', 't', ' ', 'e', 'n', 't', 'r', 'y', ' ', 'c', 'o', 'n', 't', 'e', 'n', 't', '\r', '\n', 0};
        assertEquals(expectedBytes.length, outputBytes.length);
        assertArrayEquals(expectedBytes, outputBytes, 0, expectedBytes.length);
    }

    @Test
    public void testWrite() throws IOException {
        // Verify that write() writes the correct number of bytes to the archive.
        byte[] testBytes = "Test string".getBytes();
        tarArchiveOutputStream.write(testBytes, 0, testBytes.length);
        tarArchiveOutputStream.finish();

        byte[] outputBytes = testOutputStream.toByteArray();
        byte[] expectedBytes = new byte[testBytes.length + TarConstants.RECORD_SIZE];
        System.arraycopy(testBytes, 0, expectedBytes, 0, testBytes.length);
        assertEquals(expectedBytes.length, outputBytes.length);
        assertArrayEquals(expectedBytes, outputBytes, 0, expectedBytes.length);
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        // Verify that write() throws an exception if an entry is unclosed.
        TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertThrows(IOException.class, () -> tarArchiveOutputStream.write("Test entry content".getBytes()));
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        // Verify that putArchiveEntry() can handle long file names correctly.
        String longFileName = "a/long/path/to/test/file.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Test content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] outputBytes = testOutputStream.toByteArray();
        int fileNameLength = longFileName.length();
        int fileNameRecordSize = TarConstants.RECORD_SIZE;
        int expectedLength = fileNameLength + TarConstants.NAMELEN
                + TarConstants.RECORD_SIZE + TarConstants.RECORD_SIZE;
        assertEquals(expectedLength, outputBytes.length);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameLongLink() throws IOException {
        // Verify that putArchiveEntry() can handle long file names and long links correctly.
        TarArchiveEntry entry = new TarArchiveEntry("a/long/path/to/test/file.txt");
        TarArchiveEntry longLinkEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK,
                TarConstants.LF_GNUTYPE_LONGNAME);
        longLinkEntry.setSize(entry.getName().length() + 1);
        tarArchiveOutputStream.putArchiveEntry(longLinkEntry);
        writeBytes(testOutputStream, entry.getName().getBytes());
        writeByte(testOutputStream, (byte) 0);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Test content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] outputBytes = testOutputStream.toByteArray();
        int expectedLength = entry.getName().length() + TarConstants.NAMELEN
                + TarConstants.RECORD_SIZE + TarConstants.RECORD_SIZE;
        assertEquals(expectedLength, outputBytes.length);
    }
}