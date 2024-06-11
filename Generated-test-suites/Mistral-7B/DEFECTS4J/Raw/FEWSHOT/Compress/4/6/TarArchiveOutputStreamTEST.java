package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Test cases for the TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {
    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
    }

    @Test
    public void testEmptyOutput() throws IOException {
        // Test that the empty constructor works
        TarArchiveOutputStream emptyTarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        emptyTarOutputStream.finish();

        // Test that the output stream is empty
        assertArrayEquals(new byte[0], outputStream.toByteArray());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Test that a new entry can be added
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);

        // Test that the file name was written
        assertArrayEquals(testEntry.getName().getBytes(), outputStream.toByteArray());

        // Test that the size was written
        TarArchiveEntry newEntry = new TarArchiveEntry("newEntry.txt");
        newEntry.setSize(42);
        tarOutputStream.putArchiveEntry(newEntry);
        byte[] header = new byte[TarConstants.HEADER_SIZE];
        System.arraycopy(outputStream.toByteArray(), 0, header, 0, TarConstants.HEADER_SIZE);
        assertArrayEquals("0*test.txt\0\0".getBytes(), extractHeader(header, 0));
        assertArrayEquals("0*newEntry.txt\0\0\0\0*42\0", extractHeader(outputStream.toByteArray(), TarConstants.HEADER_SIZE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNullName() throws IOException {
        TarArchiveEntry entry = null;
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryZeroSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(0);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNonTarArchiveEntry() throws IOException {
        Object nonTarEntry = new Object();
        tarOutputStream.putArchiveEntry(nonTarEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.closeArchiveEntry();

        // Test that the correct number of bytes were written
        byte[] header = new byte[TarConstants.HEADER_SIZE];
        System.arraycopy(outputStream.toByteArray(), 0, header, 0, TarConstants.HEADER_SIZE);
        byte[] data = extractData(outputStream.toByteArray(), TarConstants.HEADER_SIZE);
        assertEquals(testEntry.getSize(), data.length);
    }

    @Test
    public void testCloseArchiveEntryBeforePutArchiveEntry() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.putArchiveEntry(testEntry);

        // Test that an exception is thrown
        assertFalse(tarOutputStream.isOpen());
    }

    @Test
    public void testFinish() throws IOException {
        // Test that an empty archive is created
        tarOutputStream.finish();
        assertArrayEquals(new byte[0], outputStream.toByteArray());

        // Test that a non-empty archive is created
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expected = new byte[] {
                0x0, '0', '*', 't', 'e', 's', 't', '.', 't', 'x', 't', '\0',
                0x14, '\0', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0x12, '0', '*', 't', 'e', 's', 't', '.', 't', 'x', 't', '\0'};
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        // Test that an error is thrown when longFileMode is not set
        try {
            tarOutputStream.finish();
            fail();
        } catch (IOException e) {
            // Expected
        }

        // Test that LONGFILE_TRUNCATE mode works
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry testEntry = new TarArchiveEntry("longname.txt");
        testEntry.setSize(100);
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        assertArrayEquals(new byte[0], outputStream.toByteArray());

        // Test that LONGFILE_GNU mode works
        tarOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        testEntry = new TarArchiveEntry("longname.txt");
        testEntry.setSize(100);
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        byte[] expected = new byte[] {
                0x0, '0', '*', 'l', 'o', 'n', 'g', 'n', 'a', 'm', 'e', '.', 't', 'x', 't', '\0',
                0x16, '\0', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0x12, '0', '*', 't', 'e', 's', 't', '.', 't', 'x', 't', '\0'};
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        byte[] testData = "Hello World!".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expected = new byte[TarConstants.HEADER_SIZE + testData.length];
        System.arraycopy(extractHeader(outputStream.toByteArray(), 0), 0, expected, 0, TarConstants.HEADER_SIZE);
        System.arraycopy(testData, 0, expected, TarConstants.HEADER_SIZE, testData.length);
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testWritePartialBuffer() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        byte[] testData = "Hello World!".getBytes();

        tarOutputStream.write(testData, 0, 5);
        tarOutputStream.write(testData, 6, testData.length - 5);

        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expected = new byte[TarConstants.HEADER_SIZE + testData.length];
        System.arraycopy(extractHeader(outputStream.toByteArray(), 0), 0, expected, 0, TarConstants.HEADER_SIZE);
        System.arraycopy(testData, 0, expected, TarConstants.HEADER_SIZE, testData.length);
        System.arraycopy(testData, 5, expected, TarConstants.HEADER_SIZE + 5, testData.length - 5);
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testWriteEmptyBuffer() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write(new byte[0][], 0, 0);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expected = new byte[TarConstants.HEADER_SIZE];
        System.arraycopy(extractHeader(outputStream.toByteArray(), 0), 0, expected, 0, TarConstants.HEADER_SIZE);
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testWriteNullBuffer() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write(null, 0, 0);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        assertArrayEquals(new byte[0], outputStream.toByteArray());
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        byte[] testData = "Hello World!".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.flush();

        // Test that the data was written to the stream
        byte[] expected = new byte[TarConstants.HEADER_SIZE + testData.length];
        System.arraycopy(extractHeader(outputStream.toByteArray(), 0), 0, expected, 0, TarConstants.HEADER_SIZE);
        System.arraycopy(testData, 0, expected, TarConstants.HEADER_SIZE, testData.length);
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(inputFile, "test.txt");
        TarArchiveEntry testEntry = (TarArchiveEntry) archiveEntry;

        assertEquals("test.txt", testEntry.getName());
        assertEquals(inputFile.length(), testEntry.getSize());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.createArchiveEntry(null, "test.txt");
    }

    private byte[] extractHeader(byte[] tarData, int offset) {
        return new byte[] { tarData[offset], tarData[offset + 1], tarData[offset + 2], tarData[offset + 3],
                tarData[offset + 4], tarData[offset + 5], tarData[offset + 6], tarData[offset + 7],
                tarData[offset + 8], tarData[offset + 9], tarData[offset + 10], (byte) 0,
                tarData[offset + 11], (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0};
    }

    private byte[] extractData(byte[] tarData, int offset) {
        return new byte[tarData.length - offset - TarConstants.HEADER_SIZE];
    }
}