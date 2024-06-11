package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream out;
    private TarArchiveOutputStream tarOut;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        tarOut = new TarArchiveOutputStream(out, 512, 512);
    }

    @After
    public void tearDown() throws Exception {
        tarOut.finish();
        tarOut.close();
        out.close();
    }

    /**
     * Test constructor with an OutputStream.
     */
    @Test
    public void testTarArchiveOutputStream() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(512, tarOut.getBlockSize());
        assertEquals(512, tarOut.getRecordSize());
    }

    /**
     * Test constructor with InputStream, blockSize, and recordSize.
     */
    @Test
    public void testTarArchiveOutputStreamWithBlockSizeAndRecordSize() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        assertEquals(512, tarOut.getBlockSize());
        assertEquals(512, tarOut.getRecordSize());
    }

    /**
     * Test setLongFileMode method.
     */
    @Test
    public void testSetLongFileMode() {
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        assertEquals(0, entry.getName().length());
        tarOut.closeArchiveEntry();
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        entry = new TarArchiveEntry("testLongName.txt");
        tarOut.putArchiveEntry(entry);
        assertEquals(16, entry.getName().length());
        tarOut.closeArchiveEntry();
    }

    /**
     * Test write method with a TarArchiveEntry.
     */
    @Test
    public void testWriteWithTarArchiveEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] data = "This is a test.".getBytes();
        tarOut.putArchiveEntry(entry);
        tarOut.write(data, 0, data.length);
        tarOut.closeArchiveEntry();
        byte[] result = ((ByteArrayOutputStream) out).toByteArray();
        byte[] header = entry.getHeaderBytes();
        byte[] dataBytes = new byte[data.length + header.length];
        System.arraycopy(header, 0, dataBytes, 0, header.length);
        System.arraycopy(data, 0, dataBytes, header.length, data.length);
        byte[] expected = ByteStreams.concat(dataBytes);
        assertEquals(expected.length, result.length);
        assertEquals(expected, result);
    }

    /**
     * Test write method with a byte array.
     */
    @Test
    public void testWriteWithByteArray() throws Exception {
        byte[] data = "This is a test.".getBytes();
        tarOut.write(data, 0, data.length);
        byte[] result = ((ByteArrayOutputStream) out).toByteArray();
        byte[] expected = ByteStreams.concat(data);
        assertEquals(expected.length, result.length);
        assertEquals(expected, result);
    }

    /**
     * Test putArchiveEntry with long file name.
     */
    @Test
    public void testPutArchiveEntryWithLongFileName() throws Exception {
        String longFileName = "testLongFileName" + TarConstants.NAMELEN + "Test";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOut.putArchiveEntry(entry);
        assertEquals(longFileName.length(), entry.getName().length());
        tarOut.closeArchiveEntry();
    }

    /**
     * Test putArchiveEntry with long file name and LONGFILE_GNU mode.
     */
    @Test
    public void testPutArchiveEntryWithLongFileNameAndLongFileGNUMode()
            throws Exception {
        String longFileName = "testLongFileName" + TarConstants.NAMELEN + "Test";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOut.putArchiveEntry(entry);
        byte[] result = ((ByteArrayOutputStream) out).toByteArray();
        byte[] longLinkHeader = new byte[TarConstants.GNU_LONGLINK_HEADER_LENGTH];
        longLinkHeader[0] = (byte) 0x54;
        longLinkHeader[1] = (byte) 0x65;
        longLinkHeader[2] = (byte) 0x72;
        longLinkHeader[3] = (byte) 0x73;
        longLinkHeader[4] = (byte) 0x6f;
        longLinkHeader[5] = (byte) 0x75;
        longLinkHeader[6] = (byte) 0x74;
        longLinkHeader[7] = (byte) 0x53;
        longLinkHeader[8] = (byte) 0x74;
        longLinkHeader[9] = (byte) 0x65;
        longLinkHeader[10] = (byte) 0x66;
        longLinkHeader[11] = (byte) 0x69;
        longLinkHeader[12] = (byte) 0x6C;
        longLinkHeader[13] = (byte) 0x65;
        longLinkHeader[14] = (byte) 0x2E;
        longLinkHeader[15] = (byte) 0x2E;
        longLinkHeader[16] = (byte) 0x64;
        longLinkHeader[17] = (byte) 0x65;
        longLinkHeader[18] = (byte) 0x66;
        longLinkHeader[19] = (byte) 0x61;
        longLinkHeader[20] = (byte) 0x74;

        byte[] fileNameBytes = longFileName.getBytes();
        byte[] fileNameLength = ByteStreams.byteArray(longFileName.length());

        byte[] expected = ByteStreams.concat(longLinkHeader, fileNameLength, fileNameBytes);
        byte[] result = ((ByteArrayOutputStream) out).toByteArray();
        assertEquals(expected.length, result.length);
        assertEquals(expected, result);
    }

    /**
     * Test closeArchiveEntry without write method call.
     */
    @Test
    public void testCloseArchiveEntryWithoutWrite() {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();
        assertThrows(IOException.class, () -> tarOut.closeArchiveEntry());
    }

    /**
     * Test closeArchiveEntry after write method call.
     */
    @Test
    public void testCloseArchiveEntryAfterWrite() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] data = "This is a test.".getBytes();
        tarOut.putArchiveEntry(entry);
        tarOut.write(data, 0, data.length);
        tarOut.closeArchiveEntry();
        byte[] result = ((ByteArrayOutputStream) out).toByteArray();
        byte[] expected = ByteStreams.concat(entry.getHeaderBytes(), data);
        assertEquals(expected.length, result.length);
        assertEquals(expected, result);
    }

    /**
     * Test finish method.
     */
    @Test
    public void testFinish() throws Exception {
        TarArchiveEntry entry1 = new TarArchiveEntry("test1.txt");
        TarArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        byte[] data1 = "This is a test1.".getBytes();
        byte[] data2 = "This is a test2.".getBytes();
        tarOut.putArchiveEntry(entry1);
        tarOut.write(data1, 0, data1.length);
        tarOut.putArchiveEntry(entry2);
        tarOut.write(data2, 0, data2.length);
        tarOut.finish();
        byte[] expected = ByteStreams.concat(entry1.getHeaderBytes(), data1,
                entry2.getHeaderBytes(), data2,
                new byte[1024 * 2]); // 2 EOF records
        byte[] result = ((ByteArrayOutputStream) out).toByteArray();
        assertEquals(expected.length, result.length);
        assertEquals(expected, result);
    }

}