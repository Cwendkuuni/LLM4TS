package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testConstructorWithOutputStream() {
        // Test constructor with OutputStream
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assertNull(tarArchiveOutputStream.getBuffer());
        assertNull(tarArchiveOutputStream.getAssemBuf());
        assertNull(tarArchiveOutputStream.getRecordBuf());
        assertNull(tarArchiveOutputStream.getAssemLen());
        assertNull(tarArchiveOutputStream.getLongFileMode());
        assertFalse(tarArchiveOutputStream.isClosed());
        assertNull(tarArchiveOutputStream.getOut());
    }

    @Test
    public void testConstructorWithOutputStreamAndBlockSize() {
        // Test constructor with OutputStream and block size
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 128);
        assertNotNull(tarArchiveOutputStream.getBuffer());
        assertNotNull(tarArchiveOutputStream.getAssemBuf());
        assertNotNull(tarArchiveOutputStream.getRecordBuf());
        assertEquals(128, tarArchiveOutputStream.getBuffer().getBlockSize());
        assertEquals(128, tarArchiveOutputStream.getBuffer().getRecordSize());
        assertNull(tarArchiveOutputStream.getAssemLen());
        assertNull(tarArchiveOutputStream.getLongFileMode());
        assertFalse(tarArchiveOutputStream.isClosed());
        assertSame(outputStream, tarArchiveOutputStream.getOut());
    }

    @Test
    public void testConstructorWithOutputStreamAndBlockSizeAndRecordSize() {
        // Test constructor with OutputStream, block size, and record size
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 128, 512);
        assertNotNull(tarArchiveOutputStream.getBuffer());
        assertNotNull(tarArchiveOutputStream.getAssemBuf());
        assertNotNull(tarArchiveOutputStream.getRecordBuf());
        assertEquals(128, tarArchiveOutputStream.getBuffer().getBlockSize());
        assertEquals(512, tarArchiveOutputStream.getBuffer().getRecordSize());
        assertNull(tarArchiveOutputStream.getAssemLen());
        assertNull(tarArchiveOutputStream.getLongFileMode());
        assertFalse(tarArchiveOutputStream.isClosed());
        assertSame(outputStream, tarArchiveOutputStream.getOut());
    }

    @Test
    public void testSetLongFileMode() {
        // Test setLongFileMode method
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());

        // Test setLongFileMode with invalid mode
        assertThrows(IllegalArgumentException.class, () -> tarArchiveOutputStream.setLongFileMode(4));
    }

    @Test
    public void testFinish() throws IOException {
        // Test finish method
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt", "test content"));
        tarArchiveOutputStream.write(new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'n', 't', 'e', 'n', 't'});
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedHeader = new byte[512];
        byte[] expectedData1 = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'n', 't', 'e', 'n', 't'};
        byte[] expectedData2 = new byte[0];
        byte[] expectedEOF1 = new byte[512];
        byte[] expectedEOF2 = new byte[512];

        byte[] actualRecords = ((ByteArrayOutputStream) outputStream).toByteArray();

        System.arraycopy(expectedHeader, 0, actualRecords, 0, 512);
        System.arraycopy(expectedData1, 0, actualRecords, 512, expectedData1.length);
        System.arraycopy(expectedData2, 0, actualRecords, 564, expectedData2.length);
        System.arraycopy(expectedEOF1, 0, actualRecords, 1024, expectedEOF1.length);
        System.arraycopy(expectedEOF2, 0, actualRecords, 1536, expectedEOF2.length);

        assertArrayEquals(expectedRecords, actualRecords);
    }

    @Test
    public void testClose() throws IOException {
        // Test close method
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt", "test content"));
        tarArchiveOutputStream.write(new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'n', 't', 'e', 'n', 't'});
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] expectedEOF = new byte[512];
        byte[] actualRecords = ((ByteArrayOutputStream) outputStream).toByteArray();

        byte[] header = new byte[512];
        byte[] data = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'n', 't', 'e', 'n', 't'};

        // Write header and data to ensure they are written before close
        tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt", "test content"));
        tarArchiveOutputStream.write(data);

        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        System.arraycopy(header, 0, actualRecords, 0, 512);
        System.arraycopy(data, 0, actualRecords, 512, data.length);
        System.arraycopy(expectedEOF, 0, actualRecords, 1024, expectedEOF.length);

        assertArrayEquals(actualRecords, ((ByteArrayOutputStream) outputStream).toByteArray());
    }

    @Test
    public void testGetRecordSize() {
        // Test getRecordSize method
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Test putArchiveEntry method
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt", "test content"));
        tarArchiveOutputStream.write(new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'n', 't', 'e', 'n', 't'});
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedData = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'n', 't', 'e', 'n', 't'};
        byte[] actualRecords = ((ByteArrayOutputStream) outputStream).toByteArray();

        int headerPosition = 0;
        int dataPosition = 512;

        // Check header
        assertArrayEquals(TarConstants.MAGIC_NUMBER, actualRecords, headerPosition, 512);
        headerPosition += 512;

        // Check name
        assertEquals("test.txt", new String(actualRecords, headerPosition, TarConstants.NAMELEN));
        headerPosition += TarConstants.NAMELEN;

        // Check size
        assertEquals(expectedData.length, tarArchiveOutputStream.getSize());
        assertEquals(expectedData.length, ByteArrayUtils.bytesToLong(actualRecords, headerPosition, 8));
        headerPosition += 8;

        // Check mode
        assertEquals("0644", new String(actualRecords, headerPosition, 8));
        headerPosition += 8;

        // Check modification time
        assertEquals(0, ByteArrayUtils.bytesToInt(actualRecords, headerPosition, 4));
        headerPosition += 4;

        // Check checksum
        assertEquals(0, ByteArrayUtils.bytesToInt(actualRecords, headerPosition, 4));
        headerPosition += 4;

        // Check data
        assertArrayEquals(expectedData, actualRecords, dataPosition, expectedData.length);
    }

    @Test
    public void testCloseArchiveEntryWithoutWrite() throws IOException {
        // Test closeArchiveEntry without write
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt", "test content"));
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedEOF = new byte[512];
        byte[] actualRecords = ((ByteArrayOutputStream) tarArchiveOutputStream.getOut()).toByteArray();

        System.arraycopy(expectedEOF, 0, actualRecords, 0, expectedEOF.length);

        assertArrayEquals(actualRecords, ((ByteArrayOutputStream) tarArchiveOutputStream.getOut()).toByteArray());
    }

    @Test
    public void testCloseArchiveEntryWithWrite() throws IOException {
        // Test closeArchiveEntry with write
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'n', 't', 'e', 'n', 't'});
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[512];
        byte[] expectedData = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'n', 't', 'e', 'n', 't'};
        byte[] expectedEOF1 = new byte[512];
        byte[] expectedEOF2 = new byte[512];

        byte[] actualRecords = ((ByteArrayOutputStream) tarArchiveOutputStream.getOut()).toByteArray();

        System.arraycopy(expectedHeader, 0, actualRecords, 0, 512);
        System.arraycopy(expectedData, 0, actualRecords, 512, expectedData.length);
        System.arraycopy(expectedEOF1, 0, actualRecords, 1024, expectedEOF1.length);
        System.arraycopy(expectedEOF2, 0, actualRecords, 1536, expectedEOF2.length);

        assertArrayEquals(actualRecords, ((ByteArrayOutputStream) tarArchiveOutputStream.getOut()).toByteArray());
    }

    @Test
    public void testWrite() throws IOException {
        // Test write method
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] data = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'n', 't', 'e', 'n', 't'};
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[512];
        byte[] expectedData = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'n', 't', 'e', 'n', 't'};
        byte[] expectedEOF1 = new byte[512];
        byte[] expectedEOF2 = new byte[512];

        byte[] actualRecords = ((ByteArrayOutputStream) tarArchiveOutputStream.getOut()).toByteArray();

        System.arraycopy(expectedHeader, 0, actualRecords, 0, 512);
        System.arraycopy(expectedData, 0, actualRecords, 512, expectedData.length);
        System.arraycopy(expectedEOF1, 0, actualRecords, 1024, expectedEOF1.length);
        System.arraycopy(expectedEOF2, 0, actualRecords, 1536, expectedEOF2.length);

        assertArrayEquals(actualRecords, ((ByteArrayOutputStream) tarArchiveOutputStream.getOut()).toByteArray());
    }

}