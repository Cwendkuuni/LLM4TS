package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;

/**
 * Tests for {@link DumpArchiveInputStream}.
 */
public class DumpArchiveInputStreamTest {

    private static final String TEST_DATA = "testData.dump";
    private static final String TEST_DATA_GZ = "testData.gz";
    private File testFile;
    private File testFileGz;
    private InputStream testIs;
    private InputStream testIsGz;
    private DumpArchiveInputStream testDumpIs;
    private DumpArchiveInputStream testDumpIsGz;

    @Before
    public void setUp() throws IOException {
        testFile = new File("testData.dump");
        testFileGz = new File("testData.gz");
        testIs = new FileInputStream(testFile);
        testIsGz = new GZIPInputStream(new FileInputStream(testFileGz));
        testDumpIs = new DumpArchiveInputStream(testIs);
        testDumpIsGz = new DumpArchiveInputStream(testIsGz);
    }

    @Test
    public void testMatches() throws IOException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        testIs.read(header);
        assertTrue(DumpArchiveInputStream.matches(header, header.length));
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = testDumpIs.getNextEntry();
        assertNotNull(entry);
        testDumpIs.close();
        testDumpIs = new DumpArchiveInputStream(testIs);

        entry = testDumpIs.getNextEntry();
        assertNotNull(entry);

        // Test getNextEntry with EOF.
        testDumpIs.getNextEntry();
        testDumpIs.close();
        testDumpIs = new DumpArchiveInputStream(testIs);

        entry = testDumpIs.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        DumpArchiveEntry entry = testDumpIs.getNextEntry();
        assertNotNull(entry);

        byte[] buffer = new byte[entry.getSize()];
        int len = testDumpIs.read(buffer);
        assertEquals(entry.getSize(), len);
        assertArrayEquals(entry.getData(), Arrays.copyOfRange(buffer, 0, entry.getSize()));
    }

    @Test
    public void testReadWithLimit() throws IOException {
        DumpArchiveEntry entry = testDumpIs.getNextEntry();
        assertNotNull(entry);

        byte[] buffer = new byte[entry.getSize()];
        int limit = 10;
        int len = testDumpIs.read(buffer, 0, limit);
        assertEquals(limit, len);
        assertArrayEquals(Arrays.copyOfRange(entry.getData(), 0, limit), Arrays.copyOfRange(buffer, 0, limit));
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFormat() throws IOException {
        InputStream invalidIs = new ByteArrayInputStream("invalidData".getBytes());
        DumpArchiveInputStream invalidDumpIs = new DumpArchiveInputStream(invalidIs);
        invalidDumpIs.getNextEntry();
    }

    @Test
    public void testEOF() throws IOException {
        testDumpIs.getNextEntry();
        testDumpIs.getNextEntry();
        testDumpIs.getNextEntry();

        byte[] buffer = new byte[1];
        int len = testDumpIs.read(buffer, 0, buffer.length);
        assertEquals(-1, len);

        testDumpIs.close();
        testDumpIs = new DumpArchiveInputStream(testIs);

        testDumpIs.getNextEntry();
        testDumpIs.getNextEntry();
        testDumpIs.getNextEntry();

        len = testDumpIs.read(buffer, 0, buffer.length);
        assertEquals(-1, len);

        testDumpIs.close();
    }

    @Test
    public void testClose() throws IOException {
        DumpArchiveEntry entry = testDumpIs.getNextEntry();
        assertNotNull(entry);

        testDumpIs.close();
        testDumpIs = new DumpArchiveInputStream(testIs);

        entry = testDumpIs.getNextEntry();
        assertNotNull(entry);

        testDumpIs.close();
    }

    @Test
    public void testGetSummary() throws IOException {
        DumpArchiveSummary summary = testDumpIs.getSummary();
        assertNotNull(summary);
    }

    @Test
    public void testGetBytesRead() throws IOException {
        long bytesRead = testDumpIs.getBytesRead();
        assertEquals(0, bytesRead);

        DumpArchiveEntry entry = testDumpIs.getNextEntry();
        assertNotNull(entry);

        byte[] buffer = new byte[entry.getSize()];
        int len = testDumpIs.read(buffer, 0, buffer.length);
        assertEquals(entry.getSize(), len);

        bytesRead = testDumpIs.getBytesRead();
        assertEquals(entry.getSize(), bytesRead);

        testDumpIs.close();
    }

    @Test
    public void testGzipped() throws IOException {
        testDumpIsGz.getNextEntry();
        testDumpIsGz.getNextEntry();
        testDumpIsGz.getNextEntry();

        byte[] buffer = new byte[1];
        int len = testDumpIsGz.read(buffer, 0, buffer.length);
        assertEquals(-1, len);

        testDumpIsGz.close();
    }

    @Test
    public void testReadWithCompressedData() throws IOException {
        DumpArchiveEntry entry = testDumpIsGz.getNextEntry();
        assertNotNull(entry);

        byte[] buffer = new byte[entry.getSize()];
        int len = testDumpIsGz.read(buffer, 0, buffer.length);
        assertEquals(entry.getSize(), len);
        assertArrayEquals(entry.getData(), Arrays.copyOfRange(buffer, 0, entry.getSize()));
    }

    @Test
    public void testCloseGzipped() throws IOException {
        DumpArchiveEntry entry = testDumpIsGz.getNextEntry();
        assertNotNull(entry);

        testDumpIsGz.close();
    }

    @Test
    public void testGetBytesReadGzipped() throws IOException {
        long bytesRead = testDumpIsGz.getBytesRead();
        assertEquals(0, bytesRead);

        DumpArchiveEntry entry = testDumpIsGz.getNextEntry();
        assertNotNull(entry);

        byte[] buffer = new byte[entry.getSize()];
        int len = testDumpIsGz.read(buffer, 0, buffer.length);
        assertEquals(entry.getSize(), len);

        bytesRead = testDumpIsGz.getBytesRead();
        assertEquals(entry.getSize(), bytesRead);

        testDumpIsGz.close();
    }

    @Test
    public void testReadWithLimitGzipped() throws IOException {
        DumpArchiveEntry entry = testDumpIsGz.getNextEntry();
        assertNotNull(entry);

        byte[] buffer = new byte[entry.getSize()];
        int limit = 10;
        int len = testDumpIsGz.read(buffer, 0, limit);
        assertEquals(limit, len);
        assertArrayEquals(Arrays.copyOfRange(entry.getData(), 0, limit), Arrays.copyOfRange(buffer, 0, limit));
    }

    @Test
    public void testReadWithOffsetGzipped() throws IOException {
        DumpArchiveEntry entry = testDumpIsGz.getNextEntry();
        assertNotNull(entry);

        int offset = 10;
        byte[] buffer = new byte[entry.getSize() - offset];
        int len = testDumpIsGz.read(buffer, offset, buffer.length);
        assertEquals(buffer.length, len);
        assertArrayEquals(Arrays.copyOfRange(entry.getData(), offset, offset + buffer.length), buffer);
    }

    @Test
    public void testReadWithOffsetLimitGzipped() throws IOException {
        DumpArchiveEntry entry = testDumpIsGz.getNextEntry();
        assertNotNull(entry);

        int offset = 10;
        int limit = 20;
        byte[] buffer = new byte[limit];
        int len = testDumpIsGz.read(buffer, offset, limit);
        assertEquals(limit - offset, len);
        assertArrayEquals(Arrays.copyOfRange(entry.getData(), offset, offset + len), buffer);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(testIs);
        IOUtils.closeQuietly(testIsGz);
        IOUtils.closeQuietly(testDumpIs);
        IOUtils.closeQuietly(testDumpIsGz);
        testFile.delete();
        testFileGz.delete();
    }
}