import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.ByteStreams;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream input;
    private DumpArchiveSummary summary;
    private static final ZipEncoding zipEncoding = ZipEncodingHelper.getDefaultEncoding();

    @Before
    public void setup() throws IOException, ArchiveException {
        InputStream testData = TestUtils.getResourceAsStream("/test-data/dump-archive.dump");
        byte[] testBytes = ByteStreams.toByteArray(testData);
        input = new DumpArchiveInputStream(new ByteArrayInputStream(testBytes), zipEncoding);
        summary = input.getSummary();
    }

    @Test
    public void testGetNextEntry_ValidEntry() throws IOException {
        DumpArchiveEntry entry = input.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("file1.txt", entry.getName());
        Assert.assertEquals(10, entry.getSize());
    }

    @Test
    public void testGetNextEntry_EndOfArchive() throws IOException {
        while (input.getNextEntry() != null) {
            // consume all entries
        }

        DumpArchiveEntry endOfArchiveEntry = input.getNextEntry();
        Assert.assertNull(endOfArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_EndOfStream() throws IOException {
        input.close();
        DumpArchiveEntry entry = input.getNextEntry();
    }

    @Test
    public void testGetNextEntry_Directory() throws IOException {
        DumpArchiveEntry directoryEntry = input.getNextEntry();
        Assert.assertEquals("dir1", directoryEntry.getName());
        Assert.assertFalse(directoryEntry.isFile());

        // check that all files in the directory are retrieved
        Queue<DumpArchiveEntry> expectedEntries = new PriorityQueue<DumpArchiveEntry>(
                Arrays.asList(
                        new DumpArchiveEntry("file2.txt", 20, 100),
                        new DumpArchiveEntry("file3.txt", 30, 120)
                )
        );
        DumpArchiveEntry actualEntry;
        while ((actualEntry = input.getNextEntry()) != null) {
            Assert.assertEquals(expectedEntries.poll(), actualEntry);
        }
        Assert.assertEquals(expectedEntries.size(), 0);
    }

    @Test
    public void testGetSummary_ValidSummary() throws IOException {
        Assert.assertEquals(10, summary.getEntryCount());
        Assert.assertEquals("file1.txt", summary.getEntryName(0));
        Assert.assertEquals(10, summary.getSize());
    }

    @Test
    public void testRead_ValidData() throws IOException {
        byte[] buffer = new byte[15];
        int len = input.read(buffer);
        Assert.assertGreaterEq(len, 0);
        Assert.assertArrayEquals(
                new byte[]{
                        0x66, 0x74, 0x68, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x2e,
                        0x74, 0x78, 0x74, 0x0A
                }, buffer, 0, len);
    }

    @Test
    public void testRead_EndOfStream() throws IOException {
        byte[] buffer = new byte[15];
        int len = input.read(buffer);
        Assert.assertEquals(-1, len);
    }

    @Test(expected = IOException.class)
    public void testRead_ClosedStream() throws IOException {
        input.close();
        byte[] buffer = new byte[15];
        input.read(buffer);
    }

    @Test
    public void testClose() throws IOException {
        input.close();
        Assert.assertFalse(input.isOpen());
    }

    @Test
    public void testMatches_ValidDumpArchive() {
        byte[] testData = TestUtils.getResourceAsByteArray("/test-data/dump-archive.dump");
        Assert.assertTrue(DumpArchiveInputStream.matches(testData, testData.length));
    }

    @Test
    public void testMatches_InvalidData() {
        byte[] testData = TestUtils.getResourceAsByteArray("/test-data/invalid-data.bin");
        Assert.assertFalse(DumpArchiveInputStream.matches(testData, testData.length));
    }
}