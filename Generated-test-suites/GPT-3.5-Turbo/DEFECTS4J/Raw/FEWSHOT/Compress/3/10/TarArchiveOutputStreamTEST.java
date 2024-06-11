package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {
    @Mock
    private ArchiveEntry entry;

    private TarArchiveOutputStream tarStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        outputStream = new ByteArrayOutputStream();
        tarStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeAndRecordSize() throws IOException {
        assertNotNull(tarStream.buffer);
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarStream.buffer.getBlockSize());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarStream.buffer.getRecordSize());
        assertEquals(0, tarStream.currSize);
        assertNull(tarStream.currName);
        assertEquals(0, tarStream.currBytes);
        assertNotNull(tarStream.recordBuf);
        assertEquals(0, tarStream.assemLen);
        assertNotNull(tarStream.assemBuf);
        assertNotNull(tarStream.buffer);
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarStream.buffer.getRecordSize());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarStream.buffer.getBlockSize());
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarStream.longFileMode);
        assertEquals(false, tarStream.closed);
    }

    @Test
    public void testSetLongFileMode() {
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarStream.longFileMode);
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarStream.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        tarStream.finish();
        assertEquals(2 * TarBuffer.DEFAULT_RCDSIZE, outputStream.size());
    }

    @Test
    public void testClose() throws IOException {
        tarStream.close();
        assertEquals(2 * TarBuffer.DEFAULT_RCDSIZE, outputStream.size());
        assertEquals(true, tarStream.closed);
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        tarStream.putArchiveEntry(entry);
        assertNotNull(tarStream.buffer);
        assertEquals(tarStream.recordBuf.length, outputStream.size());
        assertEquals(0, tarStream.currBytes);
        assertNotNull(tarStream.currName);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarStream.putArchiveEntry(entry);
        tarStream.closeArchiveEntry();
        assertEquals(tarStream.recordBuf.length, outputStream.size());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryThrowsIOException() throws IOException {
        tarStream.putArchiveEntry(entry);
        tarStream.currSize = tarStream.currBytes - 1;
        tarStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = {0x0, 0x1, 0x2};
        tarStream.putArchiveEntry(entry);
        tarStream.write(data);
        tarStream.closeArchiveEntry();
        assertEquals(data.length + tarStream.recordBuf.length, outputStream.size());
    }

    @Test(expected = IOException.class)
    public void testWriteThrowsIOException() throws IOException {
        tarStream.putArchiveEntry(entry);
        tarStream.currSize = tarStream.currBytes - 5;
        byte[] data = {0x0, 0x1, 0x2};
        tarStream.write(data);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testFile.txt");
        String entryName = "testFile.txt";
        TarArchiveEntry tarEntry = (TarArchiveEntry)tarStream.createArchiveEntry(inputFile, entryName);
        assertNotNull(tarEntry);
        assertEquals(inputFile, tarEntry.getFile());
        assertEquals(entryName, tarEntry.getName());
    }
    
    // Additional test cases can be added here to comprehensively test the class
}