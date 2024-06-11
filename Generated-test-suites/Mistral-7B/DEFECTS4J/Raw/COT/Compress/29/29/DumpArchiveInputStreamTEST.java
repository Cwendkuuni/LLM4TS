package org.apache.commons.compress.test.archivers.dump;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.PriorityQueue;
import java.util.Stack;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.test.utils.TestUtils;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Before;
import org.junit.Test;

public class DumpArchiveInputStreamTest {
    // 

    private static final byte[] DUMP_ARCHIVE_HEADER = TestUtils.loadResourceAsBytes("dumparchive.bin");
    private static final byte[] EMPTY_ARC_HEADER = TestUtils.loadResourceAsBytes("emptyarc.bin");
    private static final byte[] SYMBOLIC_LINK_HEADER = TestUtils.loadResourceAsBytes("symlink.bin");

    private static final int DUMP_ARCHIVE_HEADER_LENGTH = DUMP_ARCHIVE_HEADER.length;
    private static final int EMPTY_ARC_HEADER_LENGTH = EMPTY_ARC_HEADER.length;
    private static final int SYMBOLIC_LINK_HEADER_LENGTH = SYMBOLIC_LINK_HEADER.length;

    private static final int DUMP_ARCHIVE_VALID_MAGIC = 0x64636174; // "dtar"
    private static final int EMPTY_ARC_INVALID_MAGIC = 0xdeadbeef;
    private static final int SYMBOLIC_LINK_VALID_MAGIC = 0x64636172; // "dtr"

    private DumpArchiveInputStream inputStream;
    private ArchiveInputStream referenceArchiveInputStream;
    private Queue<DumpArchiveEntry> expectedQueue;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteStreams.write(DUMP_ARCHIVE_HEADER, outputStream);

        InputStream inputStreamWithHeader = new ByteArrayInputStream(outputStream.toByteArray());
        referenceArchiveInputStream = new DumpArchiveInputStream(inputStreamWithHeader, null);

        expectedQueue = new PriorityQueue<DumpArchiveEntry>(10,
                new Comparator<DumpArchiveEntry>() {
                    public int compare(DumpArchiveEntry p, DumpArchiveEntry q) {
                        if (p.getOriginalName() == null || q.getOriginalName() == null) {
                            return Integer.MAX_VALUE;
                        }

                        return p.getOriginalName().compareTo(q.getOriginalName());
                    }
                });
        expectedQueue.add(new DumpArchiveEntry(2, 2, ".", 0, 0, ".", ".", ".", 0, 0, DumpArchiveEntry.ENTRY_TYPE_DIR, null));
        expectedQueue.add(new DumpArchiveEntry(3, 2, ".", 0, 0, ".", ".", ".", 0, 0, DumpArchiveEntry.ENTRY_TYPE_DIR, null));
        expectedQueue.add(new DumpArchiveEntry(4, 3, "testdir", 0, 0, "testdir", ".", ".", 0, 0, DumpArchiveEntry.ENTRY_TYPE_DIR, null));
        expectedQueue.add(new DumpArchiveEntry(5, 4, "testfile1", 0, 2, "testdir", "testfile1", "testfile1", 0, 2, DumpArchiveEntry.ENTRY_TYPE_FILE, null));
        expectedQueue.add(new DumpArchiveEntry(6, 5, "testfile2", 0, 3, "testdir", "testfile2", "testfile2", 0, 3, DumpArchiveEntry.ENTRY_TYPE_FILE, null));
    }

    @Test
    public void testMatchesValidDumpArchive() {
        assertTrue(DumpArchiveInputStream.matches(DUMP_ARCHIVE_HEADER, DUMP_ARCHIVE_HEADER_LENGTH));
    }

    @Test
    public void testMatchesEmptyArchive() {
        assertFalse(DumpArchiveInputStream.matches(EMPTY_ARC_HEADER, EMPTY_ARC_HEADER_LENGTH));
    }

    @Test
    public void testMatchesSymbolicLinkArchive() {
        assertTrue(DumpArchiveInputStream.matches(SYMBOLIC_LINK_HEADER, SYMBOLIC_LINK_HEADER_LENGTH));
    }

    @Test
    public void testGetNextEntry_ValidDumpArchive() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(DUMP_ARCHIVE_HEADER), null);

        DumpArchiveEntry entry;
        while ((entry = inputStream.getNextEntry()) != null) {
            assertEquals(expectedQueue.poll(), entry);
        }

        assertNull(inputStream.getNextEntry());
    }

    @Test(expected = ArchiveException.class)
    public void testGetNextEntry_InvalidArchive() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(EMPTY_ARC_HEADER), null);
        inputStream.getNextEntry();
    }

    @Test(expected = ArchiveException.class)
    public void testGetNextEntry_SymbolicLinkArchive() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(SYMBOLIC_LINK_HEADER), null);
        inputStream.getNextEntry();
    }

    @Test
    public void testGetNextEntry_EndOfArchive() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(DUMP_ARCHIVE_HEADER), null);

        DumpArchiveEntry entry;
        while ((entry = inputStream.getNextEntry()) != null) {
            assertEquals(expectedQueue.poll(), entry);
        }

        assertNull(inputStream.getNextEntry());
    }

    @Test
    public void testGetNextEntry_HasHitEOF() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(DUMP_ARCHIVE_HEADER), null);
        inputStream.hasHitEOF = true;

        assertNull(inputStream.getNextEntry());
    }

    @Test(expected = EOFException.class)
    public void testGetNextEntry_ReachedEOF() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(DUMP_ARCHIVE_HEADER), null);

        DumpArchiveEntry entry;
        while ((entry = inputStream.getNextEntry()) != null) {
            assertEquals(expectedQueue.poll(), entry);
        }

        inputStream.getNextEntry();
    }

    @Test
    public void testGetSummary_ValidDumpArchive() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(DUMP_ARCHIVE_HEADER), null);

        DumpArchiveSummary summary = inputStream.getSummary();
        assertEquals(3, summary.getNumberOfFiles());
        assertEquals(2, summary.getNumberOfDirectories());
        assertEquals(DUMP_ARCHIVE_HEADER_LENGTH, summary.getSize());
    }

    @Test(expected = IOException.class)
    public void testGetSummary_InvalidArchive() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(EMPTY_ARC_HEADER), null);
        inputStream.getSummary();
    }

    @Test(expected = IOException.class)
    public void testGetSummary_SymbolicLinkArchive() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(SYMBOLIC_LINK_HEADER), null);
        inputStream.getSummary();
    }

    @Test
    public void testGetBytesRead() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(DUMP_ARCHIVE_HEADER), null);

        long expectedBytesRead = DUMP_ARCHIVE_HEADER_LENGTH;

        assertEquals(expectedBytesRead, inputStream.getBytesRead());

        DumpArchiveEntry entry;
        while ((entry = inputStream.getNextEntry()) != null) {
            // Read all available data from the entry
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                expectedBytesRead += length;
            }
        }

        assertEquals(DUMP_ARCHIVE_HEADER_LENGTH + 13 * 512, expectedBytesRead);
    }

    // 
}