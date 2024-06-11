package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.test.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Test DumpArchiveInputStream class.
 */
public class DumpArchiveInputStreamTest {

    private static final String TEST_DATA = "test_data.bin";
    private static final String ENCODING = "UTF-8";
    private static final ZipEncoding ZIP_ENCODING = ZipEncodingHelper.getDefaultEncoding();
    private ArchiveInputStream expected;
    private DumpArchiveInputStream actual;

    @Before
    public void setUp() throws Exception {
        InputStream inputStream = TestUtils.getResourceAsStream(TEST_DATA);
        expected = new DumpArchiveInputStream(inputStream, ENCODING);
        actual = new DumpArchiveInputStream(new ByteArrayInputStream(TestUtils.toByteArray(inputStream)), ENCODING);
    }

    @Test
    public void testMatches() {
        byte[] data = new byte[1024];
        byte[] header = new byte[32];
        byte[] invalidHeader = new byte[32];

        // Test with valid header.
        System.arraycopy(TestUtils.getResourceAsBytes("dump_archive_header.bin"), 0, header, 0, header.length);
        assertTrue(DumpArchiveInputStream.matches(header, header.length));

        // Test with invalid header.
        Arrays.fill(invalidHeader, (byte) 0x55);
        assertFalse(DumpArchiveInputStream.matches(invalidHeader, invalidHeader.length));

        // Test with insufficient data.
        assertFalse(DumpArchiveInputStream.matches(data, 10));

        // Test with partial header.
        System.arraycopy(header, 0, data, 0, header.length);
        assertFalse(DumpArchiveInputStream.matches(data, data.length));
    }

    @Test
    public void testConstructor() throws IOException {
        InputStream inputStream = TestUtils.getResourceAsStream(TEST_DATA);

        // Test with default encoding.
        DumpArchiveInputStream actual = new DumpArchiveInputStream(inputStream);
        assertNotNull(actual);
        assertEquals(expected, actual);

        // Test with null encoding.
        DumpArchiveInputStream actual = new DumpArchiveInputStream(inputStream, null);
        assertNotNull(actual);
        assertEquals(expected, actual);

        // Test with invalid encoding.
        String invalidEncoding = "IN VALID ENCODING";
        try {
            DumpArchiveInputStream actual = new DumpArchiveInputStream(inputStream, invalidEncoding);
            fail("ArchiveException expected");
        } catch (ArchiveException e) {
            // Expected.
        }
    }

    @Test
    public void testGetCount() throws IOException {
        assertEquals(expected.getBytesRead(), actual.getBytesRead());
    }

    @Test
    public void testGetBytesRead() throws IOException {
        assertEquals(expected.getBytesRead(), actual.getBytesRead());
    }

    @Test
    public void testGetSummary() throws IOException {
        DumpArchiveSummary expectedSummary = expected.getSummary();
        DumpArchiveSummary actualSummary = actual.getSummary();

        assertEquals(expectedSummary.toString(), actualSummary.toString());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry expectedEntry = expected.getNextEntry();
        DumpArchiveEntry actualEntry = actual.getNextEntry();

        assertEquals(expectedEntry, actualEntry);
    }

    @Test
    public void testReadDirectoryEntry() throws IOException {
        Map<Integer, Dirent> names = actual.names;

        // Test with empty map.
        assertEquals(0, names.size());

        // Test with non-empty map.
        DumpArchiveEntry entry = expected.getNextEntry();
        assertEquals(entry.getIno(), names.get(entry.getIno()).getIno());
    }

    @Test
    public void testGetPath() throws IOException {
        DumpArchiveEntry entry = expected.getNextEntry();

        // Test with empty queue.
        assertEquals(null, actual.getPath(entry));

        // Test with non-empty queue.
        actual.readDirectoryEntry(entry);
        assertEquals("/file1.txt", actual.getPath(entry));
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int offset = 0;
        int len = 10;

        // Test with EOF.
        actual.hasHitEOF = true;
        int bytesRead = actual.read(buffer, offset, len);
        assertEquals(-1, bytesRead);

        // Test with closed stream.
        actual.isClosed = true;
        bytesRead = actual.read(buffer, offset, len);
        assertEquals(-1, bytesRead);

        // Test with invalid offset.
        offset = -1;
        bytesRead = actual.read(buffer, offset, len);
        assertEquals(-1, bytesRead);

        // Test with invalid length.
        len = -1;
        bytesRead = actual.read(buffer, offset, len);
        assertEquals(-1, bytesRead);

        // Test with insufficient data.
        len = 11;
        bytesRead = actual.read(buffer, offset, len);
        assertEquals(-1, bytesRead);

        // Test with valid read.
        len = 5;
        int dataLength = 10;
        byte[] data = new byte[dataLength];
        actual.read(data, 0, dataLength);
        System.arraycopy(data, 0, buffer, 0, len);
        assertEquals(len, bytesRead);

        // Test with sparse record.
        actual.readIdx = 5;
        bytesRead = actual.read(buffer, offset, len);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TestUtils.toByteArray(expected.getNextEntry().getData()));
        DumpArchiveInputStream actual = new DumpArchiveInputStream(inputStream, ENCODING);

        // Test with not closed stream.
        assertFalse(actual.isClosed);
        actual.close();
        assertTrue(actual.isClosed);

        // Test with already closed stream.
        actual.isClosed = true;
        actual.close();
        assertTrue(actual.isClosed);

        // Test with invalid close.
        expected.close();
        actual.close();
        assertTrue(actual.isClosed);
    }

    @Test(expected = EOFException.class)
    public void testReadPastEnd() throws IOException {
        byte[] buffer = new byte[10];
        int offset = 0;
        int len = 11;

        actual.read(buffer, offset, len);
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFormat() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("invalid_format".getBytes(ENCODING));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DumpArchiveInputStream actual = new DumpArchiveInputStream(inputStream, ENCODING);
        actual.read(new byte[10], 0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeByteLimit() throws IOException {
        DumpArchiveInputStream actual = new DumpArchiveInputStream(new ByteArrayInputStream(new byte[0]), ENCODING);
        actual.read(new byte[10], -1, 10);
    }
}