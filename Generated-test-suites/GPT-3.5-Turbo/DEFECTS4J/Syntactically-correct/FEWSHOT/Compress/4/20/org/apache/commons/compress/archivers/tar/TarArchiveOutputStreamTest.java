
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;
import org.junit.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        // Test case 1: Verify if the output stream is set correctly
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        assertSame(os, tao.out);
        
        // Test case 2: Verify if the block size and record size are set correctly
        int blockSize = TarBuffer.DEFAULT_BLKSIZE;
        int recordSize = TarBuffer.DEFAULT_RCDSIZE;
        TarBuffer buffer = new TarBuffer(os, blockSize, recordSize);
        assertSame(buffer, tao.buffer);
        assertEquals(0, tao.assemLen);
        assertArrayEquals(new byte[recordSize], tao.assemBuf);
        assertArrayEquals(new byte[recordSize], tao.recordBuf);
    }

    @Test
    public void testSetLongFileMode() {
        // Test case 1: Verify if the long file mode is set correctly
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tao.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tao.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        // Test case 1: Verify if an exception is thrown when there are unclosed entries
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("entry1");
        tao.putArchiveEntry(entry);
        tao.finish();

        try {
            tao.finish();
            fail("IOException is not thrown");
        } catch (IOException e) {
            assertEquals("This archives contains unclosed entries.", e.getMessage());
        }
    }

    @Test
    public void testClose() throws IOException {
        // Test case 1: Verify if the output stream and buffer are closed
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tao.close();
        assertTrue(tao.closed);
    }

    @Test
    public void testGetRecordSize() {
        // Test case 1: Verify if the record size is returned correctly
        int blockSize = TarBuffer.DEFAULT_BLKSIZE;
        int recordSize = TarBuffer.DEFAULT_RCDSIZE;
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new ByteArrayOutputStream(), blockSize, recordSize);
        assertEquals(recordSize, tao.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Test case 1: Verify if the entry's header record is written correctly
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("entry1");
        tao.putArchiveEntry(entry);
        assertEquals(0, os.size());
        assertArrayEquals(new byte[TarArchiveEntry.HEADERLEN], os.toByteArray());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        // Test case 1: Verify if the assembled buffer is written correctly and assemLen is reset to 0
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        byte[] wBuf = new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE];
        int wOffset = 0;
        int numToWrite = wBuf.length;
        tao.write(wBuf, wOffset, numToWrite);
        assertEquals(0, tao.assemLen);
        
        tao.assemLen = 10;
        tao.closeArchiveEntry();
        assertEquals(0, tao.assemLen);
    }

    @Test
    public void testWrite() throws IOException {
        // Test case 1: Verify if the write method writes bytes to the output stream correctly
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        byte[] wBuf = new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE];
        int wOffset = 0;
        int numToWrite = wBuf.length;
        tao.write(wBuf, wOffset, numToWrite);
        assertArrayEquals(wBuf, os.toByteArray());
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        // Test case 1: Verify if the EOF record is written correctly
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        tao.writeEOFRecord();
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, os.toByteArray().length);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        // Test case 1: Verify if TarArchiveEntry is created correctly with the given inputFile and entryName
        File inputFile = new File("test.txt");
        String entryName = "entry1";
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = (TarArchiveEntry) tao.createArchiveEntry(inputFile, entryName);
        assertEquals(inputFile, entry.getFile());
        assertEquals(entryName, entry.getName());
    }
}
