import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * TarArchiveOutputStreamTest
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
    }

    @Test
    public void testConstructorWithOutputStream() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(tarOutputStream.getRecordSize());
        tarOutputStream.close();
    }

    @Test
    public void testConstructorWithOutputStreamAndBlockSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        assertEquals(512, tarOutputStream.getRecordSize());
        tarOutputStream.close();
    }

    @Test
    public void testConstructorWithOutputStreamAndBlockSizeAndRecordSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        assertEquals(1024, tarOutputStream.getRecordSize());
        tarOutputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOutputStream() {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_LENGTH * 2];
        for (int i = 0; i < expectedEOFRecord.length; ++i) {
            expectedEOFRecord[i] = 0;
        }
        byte[] actualEOFRecord = outputStream.toByteArray();
        assertArrayEquals(expectedEOFRecord, actualEOFRecord);
        assertEquals(outputStream.size(), 2 * TarConstants.RECORD_LENGTH);
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        assertFalse(tarOutputStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(new Object());
    }

    @Test
    public void testPutArchiveEntryWithValidArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        assertFalse(tarOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithClosedStream() throws IOException {
        tarOutputStream.close();
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        assertTrue(tarOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        tarOutputStream.close();
        byte[] data = new byte[10];
        tarOutputStream.write(data, 0, data.length);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);

        byte[] data = new byte[10];
        tarOutputStream.write(data, 0, data.length);
        byte[] actualData = outputStream.toByteArray();
        assertArrayEquals(data, actualData);

        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testWriteWithLongFileName() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test" + new String(new char[TarConstants.NAMELEN]) + ".txt");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(archiveEntry);

        byte[] data = new byte[1];
        tarOutputStream.write(data, 0, data.length);
        byte[] actualData = outputStream.toByteArray();

        int longLinkLength = archiveEntry.getName().length();
        int longLinkNameBytesLength = (longLinkLength + 1) >> 1;
        int nameBytesLength = TarConstants.NAMELEN - longLinkNameBytesLength;
        byte[] longLinkNameBytes = archiveEntry.getName().substring(0, TarConstants.NAMELEN - longLinkNameBytesLength).getBytes();
        byte[] expectedLongLinkRecord = new byte[TarConstants.RECORD_LENGTH];
        System.arraycopy(TarConstants.GNU_LONGLINK.getBytes(), 0, expectedLongLinkRecord, 0, TarConstants.GNU_LONGLINK.length);
        System.arraycopy(longLinkNameBytes, 0, expectedLongLinkRecord, TarConstants.GNU_LONGLINK.length, longLinkNameBytes.length);
        System.arraycopy(TarConstants.LF_GNUTYPE_LONGNAME.getBytes(), 0, expectedLongLinkRecord, TarConstants.GNU_LONGLINK.length + longLinkNameBytes.length, TarConstants.LF_GNUTYPE_LONGNAME.length);

        int recordLength = TarConstants.RECORD_LENGTH;
        int dataLength = Long.SIZE + nameBytesLength + data.length;
        byte[] expectedHeader = new byte[recordLength];
        int headerIndex = 0;
        System.arraycopy(TarConstants.MAGIC.getBytes(), 0, expectedHeader, headerIndex, TarConstants.MAGIC.length);
        headerIndex += TarConstants.MAGIC.length;
        System.arraycopy(Long.toString(TarConstants.VERSION).getBytes(), 0, expectedHeader, headerIndex, Long.SIZE);
        headerIndex += Long.SIZE;
        System.arraycopy(Long.toString(dataLength).getBytes(), 0, expectedHeader, headerIndex, Long.SIZE);
        headerIndex += Long.SIZE;
        System.arraycopy(expectedLongLinkRecord, 0, expectedHeader, headerIndex, expectedLongLinkRecord.length);
        headerIndex += expectedLongLinkRecord.length;

        int headerIndexBeforeData = headerIndex;
        for (int i = 0; i < data.length; ++i) {
            expectedHeader[headerIndex++] = data[i];
        }

        byte[] actualData = outputStream.toByteArray();
        assertArrayEquals(expectedHeader, actualData, 0, headerIndexBeforeData);
        assertEquals(outputStream.size(), headerIndex + data.length);

        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
    }
}