package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Stack;

public class DumpArchiveInputStreamTest {
    private static final ZipEncoding ENCODING = ZipEncodingHelper.getDefaultEncoding();

    private DumpArchiveInputStream inputStream;
    private ArchiveInputStream originalInputStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream testInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] headerBytes = new byte[]{
                0x62, 0x69, 0x61, 0x73, 0x73, 0x6F, 0x63, 0x68, 0x69, 0x6E, 0x67,
                0x31, 0x33, 0x31, 0x30, 0x33, 0x30, 0x32, 0x33, 0x31, 0x38, 0x31, 0x38,
                0x32, 0x31, 0x31, 0x32, 0x32, 0x31, 0x20, 0x63, 0x61, 0x6D, 0x65, 0x74, 0x20,
                0x64, 0x6F, 0x6D, 0x65, 0x6D, 0x70, 0x6C, 0x65, 0x2E, 0x64, 0x61, 0x74, 0x61,
                0x30, 0x30, 0x36, 0x0, 0x2, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x2, 0x0,
                0x0, 0x1, 0x0, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0,
                0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0,
                0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x2, 0x0, 0x0, 0x0,
                0x0, 0x1, 0x0, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0,
                0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0,
                0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x1, 0x0, 0x0, 0x2, 0x0, 0x0,
                0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
                0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
                0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0
        };

        this.outputStream = new ByteArrayOutputStream();
        this.testInputStream = new ByteArrayInputStream(this.outputStream.toByteArray());
        this.originalInputStream = new DumpArchiveInputStream(this.testInputStream, ENCODING);
        this.inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(headerBytes), ENCODING);
    }

    @Test
    public void testMatches() {
        byte[] buffer = new byte[32];
        System.arraycopy(headerBytes, 0, buffer, 0, 32);
        assertTrue(DumpArchiveInputStream.matches(buffer, 32));
    }

    @Test
    public void testGetCount() throws IOException {
        assertEquals(0, inputStream.getCount());
    }

    @Test
    public void testGetBytesRead() throws IOException {
        assertEquals(0, inputStream.getBytesRead());
    }

    @Test
    public void testGetSummary() throws IOException {
        DumpArchiveSummary summary = inputStream.getSummary();
        assertEquals(2, summary.getMagicNumber());
        assertEquals(13, summary.getBlockSize());
        assertEquals(47132, summary.getNumberOfRecords());
        assertEquals(47132, summary.getTotalSize());
        assertEquals(255, summary.getRecordSize());
    }

    @Test
    public void testReadCLRI() throws IOException {
        inputStream.readCLRI();
        assertNotNull(inputStream.getActive());
        assertEquals(DumpArchiveConstants.SEGMENT_TYPE.CLRI,
                inputStream.getActive().getHeaderType());
    }

    @Test(expected = IOException.class)
    public void testReadCLRIWithEOF() throws IOException {
        inputStream.hasHitEOF = true;
        inputStream.readCLRI();
    }

    @Test
    public void testReadBITS() throws IOException {
        inputStream.readBITS();
        assertNotNull(inputStream.getActive());
        assertEquals(DumpArchiveConstants.SEGMENT_TYPE.BITS,
                inputStream.getActive().getHeaderType());
    }

    @Test(expected = IOException.class)
    public void testReadBITSWithEOF() throws IOException {
        inputStream.hasHitEOF = true;
        inputStream.readBITS();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = inputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals(DumpArchiveConstants.SEGMENT_TYPE.INODE, entry.getHeaderType());
        assertEquals(".", entry.getName());

        entry = inputStream.getNextEntry();
        assertEquals(DumpArchiveConstants.SEGMENT_TYPE.INODE, entry.getHeaderType());
        assertEquals("..", entry.getName());

        entry = inputStream.getNextEntry();
        assertNull(entry);

        // test case for a directory
        entry = inputStream.getNextEntry();
        assertNotNull(entry);
        assertTrue(entry.isDirectory());
        assertEquals("foo", entry.getName());

        // test case for a file
        entry = inputStream.getNextEntry();
        assertNotNull(entry);
        assertFalse(entry.isDirectory());
        assertEquals("bar.txt", entry.getName());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWithEOF() throws IOException {
        inputStream.hasHitEOF = true;
        inputStream.getNextEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int read = inputStream.read(buffer);
        assertEquals(0, read);

        buffer = new byte[10];
        read = inputStream.read(buffer, 5, 5);
        assertEquals(0, read);

        buffer = new byte[10];
        read = inputStream.read(buffer);
        assertEquals(-1, read);

        byte[] data = new byte[]{0x61, 0x62, 0x63, 0x64};

        buffer = new byte[data.length];
        read = inputStream.read(buffer);
        assertArrayEquals(data, buffer);

        buffer = new byte[data.length + 5];
        read = inputStream.read(buffer);
        assertArrayEquals(data, Arrays.copyOfRange(buffer, 0, data.length));
    }

    @Test(expected = IOException.class)
    public void testReadWithEOF() throws IOException {
        inputStream.hasHitEOF = true;
        inputStream.read(new byte[10]);
    }

    @Test
    public void testClose() throws IOException {
        inputStream.close();
        assertFalse(inputStream.isOpen());

        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(headerBytes), ENCODING);
        inputStream.open();
        assertTrue(inputStream.isOpen());
    }
}