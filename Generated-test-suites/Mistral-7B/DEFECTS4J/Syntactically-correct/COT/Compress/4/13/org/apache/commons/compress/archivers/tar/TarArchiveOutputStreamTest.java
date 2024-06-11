package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * TarArchiveOutputStream Test cases
 */
public class TarArchiveOutputStreamTest {

    private OutputStream os;
    private TarArchiveOutputStream tarStream;

    @Before
    public void setUp() throws Exception {
        os = new ByteArrayOutputStream();
        tarStream = new TarArchiveOutputStream(os);
    }

    @After
    public void tearDown() throws Exception {
        tarStream.finish();
        tarStream.close();
    }

    /**
     * Test constructor TarArchiveOutputStream(OutputStream os)
     * @throws IOException
     */
    @Test
    public void testTarArchiveOutputStreamConstructor() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(tarStream);
    }

    /**
     * Test constructor TarArchiveOutputStream(OutputStream os, int blockSize)
     * @throws IOException
     */
    @Test
    public void testTarArchiveOutputStreamConstructorWithBlockSize() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 128);
        assertNotNull(tarStream);
    }

    /**
     * Test constructor TarArchiveOutputStream(OutputStream os, int blockSize, int recordSize)
     * @throws IOException
     */
    @Test
    public void testTarArchiveOutputStreamConstructorWithBlockSizeAndRecordSize() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 128, 512);
        assertNotNull(tarStream);
    }

    /**
     * Test method setLongFileMode
     * @throws IOException
     */
    @Test
    public void testSetLongFileMode() throws IOException {
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarStream.getLongFileMode());
    }

    /**
     * Test method finish
     * @throws IOException
     */
    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.finish();
        byte[] expectedEOF = new byte[TarConstants.STD_HEADER_LENGTH * 2];
        assertArrayEquals(expectedEOF, tarStream.getBuffer().getRecord());
    }

    /**
     * Test method close
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.close();
        byte[] expectedEOF = new byte[TarConstants.STD_HEADER_LENGTH * 2];
        assertArrayEquals(expectedEOF, tarStream.getBuffer().getRecord());
    }

    /**
     * Test method getRecordSize
     * @throws IOException
     */
    @Test
    public void testGetRecordSize() throws IOException {
        int expected = TarBuffer.DEFAULT_RCDSIZE;
        assertEquals(expected, tarStream.getRecordSize());
    }

    /**
     * Test method putArchiveEntry
     * @throws IOException
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        assertEquals(entry.getName(), tarStream.currName);
        assertEquals(entry.getSize(), tarStream.currSize);
    }

    /**
     * Test method closeArchiveEntry
     * @throws IOException
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        byte[] buf = new byte[1024];
        tarStream.write(buf, 0, 1024);
        tarStream.closeArchiveEntry();
        byte[] expectedEOF = new byte[TarConstants.STD_HEADER_LENGTH * 2];
        assertArrayEquals(expectedEOF, tarStream.getBuffer().getRecord());
    }

    /**
     * Test method write
     * @throws IOException
     */
    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        byte[] data = "Hello World!".getBytes();
        tarStream.write(data, 0, data.length);
        byte[] expectedData = new byte[data.length];
        System.arraycopy(data, 0, expectedData, 0, data.length);
        byte[] record = tarStream.getBuffer().getRecord();
        int headerLength = TarConstants.STD_HEADER_LENGTH;
        byte[] expectedHeader = new byte[headerLength];
        System.arraycopy(entry.getHeader(), 0, expectedHeader, 0, headerLength);
        byte[] expectedRecord = new byte[headerLength + data.length];
        System.arraycopy(expectedHeader, 0, expectedRecord, 0, headerLength);
        System.arraycopy(data, 0, expectedRecord, headerLength, data.length);
        assertArrayEquals(expectedRecord, record);
    }

    /**
     * Test method write with long file names
     * @throws IOException
     */
    @Test
    public void testLongFileNameWrite() throws IOException {
        String longFileName = "a/long/test/file/name/with/many/levels/test.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarStream.putArchiveEntry(entry);
        byte[] data = "Hello World!".getBytes();
        tarStream.write(data, 0, data.length);
        byte[] expectedRecord = null;
        if (tarStream.getLongFileMode() == TarArchiveOutputStream.LONGFILE_TRUNCATE) {
            // Long paths will be truncated in the archive
            String shortFileName = longFileName.substring(0, TarConstants.NAMELEN);
            TarArchiveEntry shortEntry = new TarArchiveEntry(shortFileName);
            byte[] shortHeader = shortEntry.getHeader();
            int headerLength = TarConstants.STD_HEADER_LENGTH;
            expectedRecord = new byte[headerLength + data.length];
            System.arraycopy(shortHeader, 0, expectedRecord, 0, headerLength);
            System.arraycopy(data, 0, expectedRecord, headerLength, data.length);
        } else if (tarStream.getLongFileMode() == TarArchiveOutputStream.LONGFILE_GNU) {
            // GNU tar extensions are used to store long file names in the archive
            TarArchiveEntry longLinkEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK,
                                                               TarConstants.LF_GNUTYPE_LONGNAME);
            longLinkEntry.setSize(longFileName.getBytes().length + 1); // +1 for NUL
            byte[] longLinkHeader = longLinkEntry.getHeader();
            int longLinkHeaderLength = longLinkEntry.getHeader().length;
            byte[] nameBytes = longFileName.getBytes();
            expectedRecord = new byte[longLinkHeaderLength + nameBytes.length + data.length];
            System.arraycopy(longLinkHeader, 0, expectedRecord, 0, longLinkHeaderLength);
            System.arraycopy(nameBytes, 0, expectedRecord, longLinkHeaderLength, nameBytes.length);
            System.arraycopy(data, 0, expectedRecord, longLinkHeaderLength + nameBytes.length, data.length);
        }
        assertArrayEquals(expectedRecord, tarStream.getBuffer().getRecord());
    }

    /**
     * Test method write with IOException
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testWriteWithIOException() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        byte[] data = "Hello World!".getBytes();
        tarStream.write(data, 0, data.length + 1);
    }
}