package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream out;

    @Before
    public void setUp() throws IOException {
        out = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(out, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
    }

    /**
     * Test for constructor TarArchiveOutputStream(OutputStream os).
     * @throws IOException on error
     */
    @Test
    public void testConstructor() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    /**
     * Test for constructor TarArchiveOutputStream(OutputStream os, int blockSize).
     * @throws IOException on error
     */
    @Test
    public void testConstructorWithBlockSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()), 1024);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    /**
     * Test for constructor TarArchiveOutputStream(OutputStream os, int blockSize, int recordSize).
     * @throws IOException on error
     */
    @Test
    public void testConstructorWithBlockSizeAndRecordSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()), 1024, 512);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    /**
     * Test for method setLongFileMode(int longFileMode).
     * @throws IOException on error
     */
    @Test
    public void testSetLongFileMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    /**
     * Test for method finish().
     * @throws IOException on error
     */
    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello Tar!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] bytes = out.toByteArray();
        int eofIndex = findEOFIndex(bytes);
        int headerLength = 512; // size of header

        Assert.assertEquals("Unexpected EOF index or header length", eofIndex + headerLength * 2, bytes.length);
    }

    /**
     * Test for method close().
     * @throws IOException on error
     */
    @Test
    public void testClose() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello Tar!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] bytes = out.toByteArray();
        int eofIndex = findEOFIndex(bytes);
        int headerLength = 512; // size of header

        Assert.assertEquals("Unexpected EOF index or header length", eofIndex + headerLength * 2, bytes.length);
    }

    /**
     * Test for method getRecordSize().
     * @throws IOException on error
     */
    @Test
    public void testGetRecordSize() throws IOException {
        Assert.assertEquals("Unexpected record size", TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    /**
     * Test for method putArchiveEntry(ArchiveEntry archiveEntry).
     * @throws IOException on error
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        Assert.assertEquals("Unexpected name", tarArchiveEntry.getName(), tarArchiveOutputStream.currName);
        Assert.assertEquals("Unexpected size", 0, tarArchiveOutputStream.currSize);
        Assert.assertEquals("Unexpected bytes", 0, tarArchiveOutputStream.currBytes);
    }

    /**
     * Test for method closeArchiveEntry().
     * @throws IOException on error
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello Tar!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();

        Assert.assertEquals("Unexpected name", null, tarArchiveOutputStream.currName);
        Assert.assertEquals("Unexpected size", 0, tarArchiveOutputStream.currSize);
        Assert.assertEquals("Unexpected bytes", 0, tarArchiveOutputStream.currBytes);
    }

    /**
     * Test for method write(byte[] wBuf, int wOffset, int numToWrite).
     * @throws IOException on error
     */
    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] wBuf = "Hello Tar!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(wBuf, 0, wBuf.length);

        Assert.assertEquals("Unexpected name", tarArchiveEntry.getName(), tarArchiveOutputStream.currName);
        Assert.assertEquals("Unexpected size", tarArchiveEntry.getSize(), tarArchiveOutputStream.currSize);
        Assert.assertEquals("Unexpected bytes", wBuf.length, tarArchiveOutputStream.currBytes);
    }

    /**
     * Test for method write(byte[] wBuf, int wOffset, int numToWrite) with
     * buffer overflow.
     * @throws IOException on error
     */
    @Test(expected = IOException.class)
    public void testWriteWithBufferOverflow() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] wBuf = "Hello Tar!".getBytes(StandardCharsets.UTF_8);
        int numToWrite = wBuf.length + 1;
        tarArchiveOutputStream.write(wBuf, 0, numToWrite);
    }

    /**
     * Test for method write(byte[] wBuf, int wOffset, int numToWrite) with
     * unclosed entry.
     * @throws IOException on error
     */
    @Test(expected = IOException.class)
    public void testWriteWithUnclosedEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello Tar!".getBytes(StandardCharsets.UTF_8));

        byte[] wBuf = "World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(wBuf, 0, wBuf.length);
    }

    /**
     * Test for method write(byte[] wBuf, int wOffset, int numToWrite) with
     * directory entry.
     * @throws IOException on error
     */
    @Test(expected = IOException.class)
    public void testWriteWithDirectoryEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testDir");
        tarArchiveEntry.setDirectory(true);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] wBuf = "Hello Tar!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(wBuf, 0, wBuf.length);
    }

    private int findEOFIndex(byte[] bytes) {
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] == 0 && bytes[i + 1] == 0) {
                return i;
            }
        }
        return -1;
    }
}