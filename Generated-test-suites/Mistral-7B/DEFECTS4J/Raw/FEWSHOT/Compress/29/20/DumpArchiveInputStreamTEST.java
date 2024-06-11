package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test cases for {@link DumpArchiveInputStream} class.
 */
public class DumpArchiveInputStreamTest {

    private static final int TEST_DATA_SIZE = 1024;

    private static final byte[] TEST_DATA = new byte[TEST_DATA_SIZE];

    private static final ZipEncoding TEST_ENCODING = ZipEncodingHelper.getZipEncoding("UTF-8");

    private InputStream inputStream;
    private DumpArchiveInputStream dumpArchiveInputStream;

    @Before
    public void setUp() throws IOException {
        // Create test data with a valid dump archive header.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(TEST_DATA_SIZE);
        outputStream.write(DumpArchiveConstants.MAGIC_NUMBER);
        outputStream.write(DumpArchiveConstants.VERSION_NUMBER);

        // Add test data to the dump archive.
        outputStream.write(TestUtils.getBytes("test-entry.txt", TEST_ENCODING));
        outputStream.write(TestUtils.getBytes("test-subdir/test-subentry.txt", TEST_ENCODING));
        outputStream.write(TestUtils.getBytes("test-subdir/test-subdir/test-subsubentry.txt", TEST_ENCODING));

        byte[] headerBytes = outputStream.toByteArray();
        DumpArchiveSummary summary = new DumpArchiveSummary(headerBytes, TEST_ENCODING);

        // Create test input stream.
        ByteArrayInputStream testInput = new ByteArrayInputStream(headerBytes);
        this.inputStream = new TapeInputStream(testInput);
        this.dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
    }

    @Test
    public void testMatches() {
        assertTrue(DumpArchiveInputStream.matches(TEST_DATA, TEST_DATA_SIZE));
    }

    @Test(expected = UnrecognizedFormatException.class)
    public void testUnrecognizedFormatException() throws IOException {
        // Create test input stream with an invalid dump archive header.
        byte[] invalidData = new byte[TEST_DATA_SIZE];
        // Set the first byte to an invalid value.
        invalidData[0] = (byte) 0xAB;
        InputStream invalidInput = new ByteArrayInputStream(invalidData);
        DumpArchiveInputStream invalidStream = new DumpArchiveInputStream(invalidInput);
        invalidStream.getNextEntry();
    }

    @Test
    public void testGetSummary() {
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        assertEquals("test-entry.txt", entry.getName());
        assertEquals("test-entry.txt", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        assertEquals(TEST_DATA_SIZE, entry.getSize());
        assertEquals(0, entry.getCompressedSize());
        assertEquals(0, entry.getCrc());
        assertEquals(DumpArchiveEntry.TYPE_FILE, entry.getType());
        assertEquals("test-entry.txt", entry.getPath());

        entry = dumpArchiveInputStream.getNextEntry();
        assertEquals("test-subdir/test-subentry.txt", entry.getName());
        assertEquals("test-subentry.txt", entry.getSimpleName());
        assertEquals(TEST_DATA_SIZE * 2, entry.getOffset());
        assertEquals(TEST_DATA_SIZE, entry.getSize());
        assertEquals(0, entry.getCompressedSize());
        assertEquals(0, entry.getCrc());
        assertEquals(DumpArchiveEntry.TYPE_FILE, entry.getType());
        assertEquals("test-subdir/test-subentry.txt", entry.getPath());

        entry = dumpArchiveInputStream.getNextEntry();
        assertEquals("test-subdir/test-subdir/test-subsubentry.txt", entry.getName());
        assertEquals("test-subsubentry.txt", entry.getSimpleName());
        assertEquals(TEST_DATA_SIZE * 3, entry.getOffset());
        assertEquals(TEST_DATA_SIZE, entry.getSize());
        assertEquals(0, entry.getCompressedSize());
        assertEquals(0, entry.getCrc());
        assertEquals(DumpArchiveEntry.TYPE_FILE, entry.getType());
        assertEquals("test-subdir/test-subdir/test-subsubentry.txt", entry.getPath());
    }

    @Test
    public void testReadDirectoryEntry() throws IOException {
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        assertEquals("test-entry.txt", entry.getName());
        assertEquals("test-entry.txt", entry.getSimpleName());
        assertNull(entry.getPath());

        entry = dumpArchiveInputStream.getNextEntry();
        assertEquals("test-subdir/test-subentry.txt", entry.getName());
        assertEquals("test-subentry.txt", entry.getSimpleName());
        assertEquals("test-subdir", entry.getPath());

        entry = dumpArchiveInputStream.getNextEntry();
        assertEquals("test-subdir/test-subdir/test-subsubentry.txt", entry.getName());
        assertEquals("test-subsubentry.txt", entry.getSimpleName());
        assertEquals("test-subdir/test-subdir", entry.getPath());
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] testBuffer = new byte[TEST_DATA_SIZE];
        int read = dumpArchiveInputStream.read(testBuffer, 0, TEST_DATA_SIZE);
        assertArrayEquals(TEST_DATA, testBuffer);
        assertEquals(TEST_DATA_SIZE, read);

        read = dumpArchiveInputStream.read(testBuffer, TEST_DATA_SIZE, TEST_DATA_SIZE);
        assertEquals(-1, read);
    }

    @Test
    public void testClose() throws IOException {
        dumpArchiveInputStream.close();
        assertFalse(inputStream.isOpen());
    }
}