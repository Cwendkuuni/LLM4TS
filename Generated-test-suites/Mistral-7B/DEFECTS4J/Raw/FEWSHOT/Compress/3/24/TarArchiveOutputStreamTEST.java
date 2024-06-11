package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.TarConstants;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructors() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os);
        TarArchiveOutputStream tarOutputStreamWithBlockSize = new TarArchiveOutputStream(os, 512);
        TarArchiveOutputStream tarOutputStreamWithBlockSizeAndRecordSize = new TarArchiveOutputStream(os, 512, 1024);

        assertFalse(tarOutputStream.isClosed());
        assertFalse(tarOutputStreamWithBlockSize.isClosed());
        assertFalse(tarOutputStreamWithBlockSizeAndRecordSize.isClosed());
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        assertTrue(tarOutputStream.getLongFileMode() == TarArchiveOutputStream.LONGFILE_ERROR);
    }

    @Test
    public void testWriteEOFRecord() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);

        tarOutputStream.finish();
        byte[] eofRecord = byteArrayOutputStream.toByteArray();
        TarBuffer tarBuffer = new TarBuffer(byteArrayOutputStream, 512, 1024);

        byte[] record = tarBuffer.readRecord();
        for (int i = 0; i < record.length; i++) {
            if (i < TarConstants.MAGIC_TAR) {
                assertEquals("Unexpected byte: " + (byte) i, (byte) 0, record[i]);
            } else {
                assertEquals("Unexpected byte: " + i, (byte) 0, record[i]);
            }
        }

        assertEquals("Unexpected record length: " + (record.length - TarConstants.MAGIC_TAR), (record.length - TarConstants.MAGIC_TAR), eofRecord.length);
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        File testFile = new File("test.txt");
        String testFileName = "test.txt";
        File testDirectory = new File("test_directory");
        String testDirectoryName = "test_directory";

        TarArchiveEntry testEntry = new TarArchiveEntry(testFile, testFileName);
        tarOutputStream.putArchiveEntry(testEntry);

        byte[] testEntryHeader = testEntry.getHeader();
        byte[] headerLength = new byte[4];
        System.arraycopy(testEntryHeader, 0, headerLength, 0, 4);

        long headerSize = ByteBuffer.wrap(headerLength).getLong();
        assertEquals("Unexpected header size: " + headerSize, TarConstants.STANDARD_HEADER_SIZE, headerSize);

        TarArchiveEntry directoryEntry = new TarArchiveEntry(testDirectory, testDirectoryName);
        tarOutputStream.putArchiveEntry(directoryEntry);
        tarOutputStream.closeArchiveEntry(); // close testEntry before putting directoryEntry

        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(testFile, "test_" + TarConstants.NAMELEN + "_long_filename.txt");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(longFileNameEntry);
        tarOutputStream.closeArchiveEntry();

        byte[] buffer = byteArrayOutputStream.toByteArray();
        TarBuffer tarBuffer = new TarBuffer(new ByteArrayInputStream(buffer), 512, 1024);

        TarArchiveEntry readEntry = (TarArchiveEntry) tarOutputStream.getNextEntry();
        assertEquals("Unexpected entry name: " + readEntry.getName(), testFileName, readEntry.getName());
        tarOutputStream.closeArchiveEntry();

        readEntry = (TarArchiveEntry) tarOutputStream.getNextEntry();
        assertEquals("Unexpected entry name: " + readEntry.getName(), testDirectoryName, readEntry.getName());
        tarOutputStream.closeArchiveEntry();

        readEntry = (TarArchiveEntry) tarOutputStream.getNextEntry();
        assertEquals("Unexpected entry name: " + readEntry.getName(), "test_" + TarConstants.NAMELEN + "_long_filename.txt", readEntry.getName());
        tarOutputStream.closeArchiveEntry();

        assertEquals("Unexpected number of entries: " + tarOutputStream.getNextEntry(), null, tarOutputStream.getNextEntry());
    }

    @Test
    public void testClose() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        File testFile = new File("test.txt");
        String testFileName = "test.txt";
        TarArchiveEntry testEntry = new TarArchiveEntry(testFile, testFileName);

        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.close();

        byte[] buffer = byteArrayOutputStream.toByteArray();
        TarBuffer tarBuffer = new TarBuffer(new ByteArrayInputStream(buffer), 512, 1024);

        TarArchiveEntry readEntry = (TarArchiveEntry) tarOutputStream.getNextEntry();
        assertEquals("Unexpected entry name: " + readEntry.getName(), testFileName, readEntry.getName());
        tarOutputStream.closeArchiveEntry();

        assertEquals("Unexpected number of entries: " + tarOutputStream.getNextEntry(), null, tarOutputStream.getNextEntry());

        tarOutputStream.close();
    }
}