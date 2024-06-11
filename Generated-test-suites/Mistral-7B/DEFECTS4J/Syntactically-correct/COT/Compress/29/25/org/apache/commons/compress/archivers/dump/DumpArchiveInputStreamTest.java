package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.test.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * Tests for DumpArchiveInputStream class.
 */
public class DumpArchiveInputStreamTest {

    private static final String TEST_DATA = "testData.dump";
    private static final String TEST_DATA_ENCODED = "testData_encoded.dump";
    private static final String TEST_DATA_INVALID = "testData_invalid.dump";

    private ArchiveInputStream archiveInputStream;
    private ArchiveInputStream archiveInputStreamInvalid;
    private DumpArchiveEntry entry;
    private DumpArchiveEntry entryInvalid;
    private DumpArchiveSummary summary;
    private ZipEncoding encoding;
    private ZipEncoding encodingInvalid;

    @Before
    public void setUp() throws Exception {
        byte[] testData = TestUtils.getResourceAsBytes(TEST_DATA);
        byte[] testDataEncoded = TestUtils.getResourceAsBytes(TEST_DATA_ENCODED);
        byte[] testDataInvalid = TestUtils.getResourceAsBytes(TEST_DATA_INVALID);

        InputStream is = new ByteArrayInputStream(testData);
        InputStream isEncoded = new ByteArrayInputStream(testDataEncoded);
        InputStream isInvalid = new ByteArrayInputStream(testDataInvalid);

        encoding = ZipEncodingHelper.getZipEncoding(null);
        encodingInvalid = ZipEncodingHelper.getZipEncoding("UTF-8");

        archiveInputStream = new DumpArchiveInputStream(is, encoding);
        archiveInputStreamInvalid = new DumpArchiveInputStream(isInvalid, encodingInvalid);

        summary = archiveInputStream.getSummary();

        assertEquals(summary.getMagic(), DumpArchiveConstants.NFS_MAGIC);
        assertEquals(summary.getVolumeLabel(), "testVolumeLabel");
        assertEquals(summary.getVolumeName(), "testVolumeName");
        assertEquals(summary.getBlockSize(), 512);
        assertEquals(summary.isCompressed(), false);
        assertEquals(summary.getNumberOfRecords(), 10);

        entry = archiveInputStream.getNextEntry();
        entryInvalid = archiveInputStreamInvalid.getNextEntry();
    }

    @Test
    public void testGetSummary() throws IOException, ArchiveException {
        assertNotNull(summary);
    }

    @Test
    public void testGetNextEntry() throws IOException, ArchiveException {
        // Test getting next entries
        assertEquals("testDir1/", entry.getName());
        assertEquals("testDir1", entry.getSimpleName());
        assertEquals(0, entry.getSize());
        assertEquals(0, entry.getOffset());

        // Test getting next entries
        assertEquals("testFile1", archiveInputStream.getNextEntry().getName());
        assertEquals("testFile1", archiveInputStream.getNextEntry().getSimpleName());
        assertEquals(17, archiveInputStream.getNextEntry().getSize());
        assertEquals(1024, archiveInputStream.getNextEntry().getOffset());

        // Test getting next entries
        assertEquals("testDir2/testDir2/testFile2",
                archiveInputStream.getNextEntry().getName());
        assertEquals("testDir2/testDir2/testFile2",
                archiveInputStream.getNextEntry().getSimpleName());
        assertEquals(17, archiveInputStream.getNextEntry().getSize());
        assertEquals(2048, archiveInputStream.getNextEntry().getOffset());

        // Test getting next entries
        assertEquals("testFile3", archiveInputStream.getNextEntry().getName());
        assertEquals("testFile3", archiveInputStream.getNextEntry().getSimpleName());
        assertEquals(12, archiveInputStream.getNextEntry().getSize());
        assertEquals(3072, archiveInputStream.getNextEntry().getOffset());

        // Test getting next entries
        assertEquals("testDir3/testFile4", archiveInputStream.getNextEntry().getName());
        assertEquals("testDir3/testFile4", archiveInputStream.getNextEntry().getSimpleName());
        assertEquals(21, archiveInputStream.getNextEntry().getSize());
        assertEquals(4096, archiveInputStream.getNextEntry().getOffset());

        assertEquals(null, archiveInputStream.getNextEntry());

        // Test getting next entries from invalid archive
        assertNull(archiveInputStreamInvalid.getNextEntry());

        // Test getting next entries
        archiveInputStream.close();
        archiveInputStream = new DumpArchiveInputStream(is, encoding);
        entry = archiveInputStream.getNextEntry();
        assertEquals("testDir1/", entry.getName());
        assertEquals("testDir1", entry.getSimpleName());
        assertEquals(0, entry.getSize());
        assertEquals(0, entry.getOffset());
    }

    @Test(expected = EOFException.class)
    public void testGetNextEntryAfterEndOfStream() throws IOException,
            ArchiveException {
        assertEquals(null, archiveInputStream.getNextEntry());
    }

    @Test(expected = ArchiveException.class)
    public void testGetNextEntryInvalid() throws IOException,
            ArchiveException {
        // Test getting next entries from invalid archive
        assertNull(archiveInputStreamInvalid.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[12];

        int length = archiveInputStream.read(buffer);
        assertEquals(12, length);
        Arrays.equals(TestUtils.getResourceAsBytes("testFile3"), buffer);

        length = archiveInputStream.read(buffer, 0, 12);
        assertEquals(12, length);
        Arrays.equals(TestUtils.getResourceAsBytes("testFile3"), buffer);

        length = archiveInputStream.read(buffer);
        assertEquals(-1, length);

        archiveInputStream.close();

        byte[] buffer2 = new byte[21];

        length = archiveInputStream = new DumpArchiveInputStream(is, encoding)
                .read(buffer2);
        assertEquals(21, length);
        Arrays.equals(TestUtils.getResourceAsBytes("testDir3/testFile4"), buffer2);

        length = archiveInputStream.read(buffer2, 0, 21);
        assertEquals(21, length);
        Arrays.equals(TestUtils.getResourceAsBytes("testDir3/testFile4"), buffer2);

        length = archiveInputStream.read(buffer2);
        assertEquals(-1, length);

        archiveInputStream.close();

        archiveInputStream = new DumpArchiveInputStream(is, encoding);
        length = archiveInputStream.read(buffer2);
        assertEquals(21, length);
        Arrays.equals(TestUtils.getResourceAsBytes("testDir3/testFile4"), buffer2);

        length = archiveInputStream.read(buffer2);
        assertEquals(-1, length);

        archiveInputStream.close();
    }

    @Test(expected = EOFException.class)
    public void testReadAfterEndOfStream() throws IOException {
        byte[] buffer = new byte[21];
        archiveInputStream.read(buffer);
        archiveInputStream.read(buffer);
        archiveInputStream.read(buffer);

        archiveInputStream.close();

        archiveInputStream = new DumpArchiveInputStream(is, encoding);

        archiveInputStream.read(buffer);
        archiveInputStream.read(buffer);
        archiveInputStream.read(buffer);

        archiveInputStream.close();

        archiveInputStream = new DumpArchiveInputStream(is, encoding);

        archiveInputStream.read(buffer);
        archiveInputStream.read(buffer);
        archiveInputStream.read(buffer);

        assertNull(archiveInputStream.read(buffer));
    }

    @Test
    public void testClose() throws IOException {
        archiveInputStream.close();
        archiveInputStream = new DumpArchiveInputStream(is, encoding);

        archiveInputStream.close();
        archiveInputStream = new DumpArchiveInputStream(is, encoding);

        archiveInputStream.close();
        archiveInputStream = new DumpArchiveInputStream(is, encoding);

        archiveInputStream.getNextEntry();
        archiveInputStream.close();

        archiveInputStream = new DumpArchiveInputStream(is, encoding);
        archiveInputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEncoding() throws IOException {
        new DumpArchiveInputStream(is, null);
    }

    @Test
    public void testMatches() throws IOException {
        assertTrue(DumpArchiveInputStream.matches(TestUtils.getResourceAsBytes(TEST_DATA),
                TestUtils.getResourceAsBytes(TEST_DATA).length));
    }

    @Test
    public void testMatchesInvalidData() throws IOException {
        assertFalse(DumpArchiveInputStream.matches(TestUtils.getResourceAsBytes(TEST_DATA_INVALID),
                TestUtils.getResourceAsBytes(TEST_DATA_INVALID).length));
    }

    @Test
    public void testMatchesInvalidLength() throws IOException {
        assertFalse(DumpArchiveInputStream.matches(TestUtils.getResourceAsBytes(TEST_DATA),
                10));
    }
}