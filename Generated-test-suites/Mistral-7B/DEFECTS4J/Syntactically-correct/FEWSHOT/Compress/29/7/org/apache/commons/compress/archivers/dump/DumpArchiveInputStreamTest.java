package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Unit tests for {@link DumpArchiveInputStream}.
 */
public class DumpArchiveInputStreamTest {

    private static final ZipEncoding ENCODING = ZipEncodingHelper.getZipEncoding(null);

    private ByteArrayOutputStream output;
    private ByteArrayInputStream input;
    private DumpArchiveSummary summary;
    private DumpArchiveInputStream stream;

    @Before
    public void setUp() throws IOException {
        output = new ByteArrayOutputStream();
        input = new ByteArrayInputStream(output.toByteArray());
        summary = DumpArchiveSummary.create("test.dmp", 10, ENCODING);
        stream = new DumpArchiveInputStream(input, ENCODING);
    }

    @After
    public void tearDown() throws IOException {
        if (input != null) {
            input.close();
        }
        if (output != null) {
            output.close();
        }
    }

    @Test
    public void testMatches() {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.DUMPED_ARCHIVE), header, 0, header.length);
        assertTrue(DumpArchiveInputStream.matches(header, header.length));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesWithNegativeLength() {
        assertFalse(DumpArchiveInputStream.matches(new byte[0], -1));
    }

    @Test
    public void testMatchesWithShortHeader() {
        byte[] header = new byte[1];
        assertFalse(DumpArchiveInputStream.matches(header, header.length));
    }

    @Test
    public void testReadCLRI() throws IOException {
        byte[] record = new byte[DumpArchiveConstants.TP_SIZE];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.CLRI_RECORD), record, 0, record.length);

        DumpArchiveEntry entry = DumpArchiveEntry.parse(record);
        assertEquals(DumpArchiveConstants.SEGMENT_TYPE.CLRI, entry.getHeaderType());
        assertEquals(3, entry.getHeaderCount());

        stream.readCLRI();
        assertSame(entry, stream.active);
    }

    @Test
    public void testReadBITS() throws IOException {
        byte[] record = new byte[DumpArchiveConstants.TP_SIZE];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.BITS_RECORD), record, 0, record.length);

        DumpArchiveEntry entry = DumpArchiveEntry.parse(record);
        assertEquals(DumpArchiveConstants.SEGMENT_TYPE.BITS, entry.getHeaderType());
        assertEquals(1, entry.getHeaderCount());

        stream.readBITS();
        assertSame(entry, stream.active);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        // test empty archive
        assertNull(stream.getNextEntry());

        // test valid entry
        byte[] record = new byte[DumpArchiveConstants.TP_SIZE];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.DIRECTORY_RECORD), record, 0, record.length);
        DumpArchiveEntry directoryEntry = DumpArchiveEntry.parse(record);
        summary.addDirectoryEntry(".", directoryEntry);

        stream.readRecord(); // read CLRI record
        stream.readRecord(); // read BITS record

        assertEquals(directoryEntry, stream.getNextEntry());

        // test directory entry with child entries
        byte[] directoryRecord = new byte[DumpArchiveConstants.TP_SIZE * 2];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.DIRECTORY_RECORD2), directoryRecord, 0, directoryRecord.length);
        DumpArchiveEntry childDirectory = DumpArchiveEntry.parse(directoryRecord);
        summary.addDirectoryEntry(".", directoryEntry);
        summary.addDirectoryEntry("dir", childDirectory);

        stream.readRecord(); // read CLRI record
        stream.readRecord(); // read BITS record
        stream.readRecord(); // read directory record for .
        stream.readRecord(); // read directory record for dir

        assertEquals(childDirectory, stream.getNextEntry());

        // test file entry
        byte[] fileRecord = new byte[DumpArchiveConstants.TP_SIZE];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.FILE_RECORD), fileRecord, 0, fileRecord.length);
        DumpArchiveEntry fileEntry = DumpArchiveEntry.parse(fileRecord);
        summary.addDirectoryEntry(".", directoryEntry);
        summary.addDirectoryEntry("dir", childDirectory);
        summary.addFileEntry("file", fileEntry, 0);

        stream.readRecord(); // read CLRI record
        stream.readRecord(); // read BITS record
        stream.readRecord(); // read directory record for .
        stream.readRecord(); // read directory record for dir
        stream.readRecord(); // read file record for file

        assertEquals(fileEntry, stream.getNextEntry());

        // test end-of-archive marker
        byte[] endRecord = new byte[DumpArchiveConstants.TP_SIZE];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.END_RECORD), endRecord, 0, endRecord.length);
        DumpArchiveEntry endEntry = DumpArchiveEntry.parse(endRecord);

        stream.readRecord(); // read CLRI record
        stream.readRecord(); // read BITS record
        stream.readRecord(); // read directory record for .
        stream.readRecord(); // read directory record for dir
        stream.readRecord(); // read file record for file
        stream.readRecord(); // read end record

        assertNull(stream.getNextEntry());
    }

    @Test
    public void testGetName() throws IOException {
        byte[] record = new byte[DumpArchiveConstants.TP_SIZE];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.DIRECTORY_RECORD), record, 0, record.length);
        DumpArchiveEntry directoryEntry = DumpArchiveEntry.parse(record);

        assertEquals(".", directoryEntry.getName());
        assertEquals("", directoryEntry.getSimpleName());

        record = new byte[DumpArchiveConstants.TP_SIZE * 2];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.DIRECTORY_RECORD2), record, 0, record.length);
        DumpArchiveEntry childDirectory = DumpArchiveEntry.parse(record);

        assertEquals("dir", childDirectory.getName());
        assertEquals("dir", childDirectory.getSimpleName());

        record = new byte[DumpArchiveConstants.TP_SIZE];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.FILE_RECORD), record, 0, record.length);
        DumpArchiveEntry fileEntry = DumpArchiveEntry.parse(record);

        assertEquals("file", fileEntry.getName());
        assertEquals("file", fileEntry.getSimpleName());
    }

    @Test
    public void testGetPath() throws IOException {
        byte[] record = new byte[DumpArchiveConstants.TP_SIZE * 3];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.DIRECTORY_RECORD2), record, 0, record.length);
        DumpArchiveEntry childDirectory = DumpArchiveEntry.parse(record);

        record = new byte[DumpArchiveConstants.TP_SIZE * 2];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.DIRECTORY_RECORD), record, 0, record.length);
        DumpArchiveEntry parentDirectory = DumpArchiveEntry.parse(record);

        record = new byte[DumpArchiveConstants.TP_SIZE];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.FILE_RECORD), record, 0, record.length);
        DumpArchiveEntry fileEntry = DumpArchiveEntry.parse(record);

        summary.addDirectoryEntry(".", parentDirectory);
        summary.addDirectoryEntry(".", directoryEntry);
        summary.addDirectoryEntry("dir", childDirectory);
        summary.addFileEntry("file", fileEntry, 0);

        String expectedPath = "dir/file";
        String actualPath = stream.getNextEntry().getPath();

        assertEquals(expectedPath, actualPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPathNull() {
        assertNull(stream.getNextEntry().getPath());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPathBeforeFirstEntry() throws IOException {
        assertNull(stream.getNextEntry().getPath());
        stream.getNextEntry();
        assertNull(stream.getNextEntry().getPath());
    }

    @Test
    public void testClose() throws IOException {
        stream.close();
        assertFalse(stream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        stream.close();
        stream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseBeforeFirstEntry() throws IOException {
        stream.close();
        assertNull(stream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseAfterEndOfArchive() throws IOException {
        // read all records
        byte[] record = new byte[DumpArchiveConstants.TP_SIZE];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.CLRI_RECORD), record, 0, record.length);
        DumpArchiveEntry clri = DumpArchiveEntry.parse(record);
        stream.readCLRI();

        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.BITS_RECORD), record, 0, record.length);
        DumpArchiveEntry bits = DumpArchiveEntry.parse(record);
        stream.readBITS();

        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.DIRECTORY_RECORD), record, 0, record.length);
        DumpArchiveEntry dir = DumpArchiveEntry.parse(record);
        stream.readDirectoryEntry(dir);

        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.DIRECTORY_RECORD2), record, 0, record.length);
        DumpArchiveEntry childDir = DumpArchiveEntry.parse(record);
        stream.readDirectoryEntry(childDir);

        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.FILE_RECORD), record, 0, record.length);
        DumpArchiveEntry file = DumpArchiveEntry.parse(record);
        stream.readDirectoryEntry(file);

        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.END_RECORD), record, 0, record.length);
        DumpArchiveEntry end = DumpArchiveEntry.parse(record);
        stream.readDirectoryEntry(end);

        // try to close
        stream.close();
        assertFalse(stream.isOpen());

        // try to get next entry
        assertNull(stream.getNextEntry());
    }

    @Test(expected = EOFException.class)
    public void testReadPastEndOfArchive() throws IOException {
        byte[] record = new byte[DumpArchiveConstants.TP_SIZE];
        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.CLRI_RECORD), record, 0, record.length);
        DumpArchiveEntry clri = DumpArchiveEntry.parse(record);
        stream.readCLRI();

        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.BITS_RECORD), record, 0, record.length);
        DumpArchiveEntry bits = DumpArchiveEntry.parse(record);
        stream.readBITS();

        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.DIRECTORY_RECORD), record, 0, record.length);
        DumpArchiveEntry dir = DumpArchiveEntry.parse(record);
        stream.readDirectoryEntry(dir);

        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.DIRECTORY_RECORD2), record, 0, record.length);
        DumpArchiveEntry childDir = DumpArchiveEntry.parse(record);
        stream.readDirectoryEntry(childDir);

        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.FILE_RECORD), record, 0, record.length);
        DumpArchiveEntry file = DumpArchiveEntry.parse(record);
        stream.readDirectoryEntry(file);

        ByteStreams.copy(ByteStreams.byteArrayInput(DumpArchiveTestData.END_RECORD), record, 0, record.length);
        DumpArchiveEntry end = DumpArchiveEntry.parse(record);
        stream.readDirectoryEntry(end);

        // read past end
        byte[] emptyRecord = new byte[DumpArchiveConstants.TP_SIZE];
        int bytesRead = 0;
        while (bytesRead >= 0) {
            bytesRead = stream.read(emptyRecord, 0, emptyRecord.length);
        }
        assertTrue(bytesRead == -1);
    }

    @Test(expected = IOException.class)
    public void testReadWithClosedStream() throws IOException {
        stream.close();
        int bytesRead = stream.read(new byte[1], 0, 1);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = 0;

        // read first record
        bytesRead = stream.read(buffer, 0, buffer.length);
        assertEquals("test.dmp", new String(buffer, 0, bytesRead));

        // read second record
        bytesRead = stream.read(buffer, 0, buffer.length);
        assertEquals("10", new String(buffer, 0, bytesRead));

        // read third record
        bytesRead = stream.read(buffer, 0, buffer.length);
        assertEquals("unix\000", new String(buffer, 0, bytesRead));

        // read past end of archive
        bytesRead = stream.read(buffer, 0, buffer.length);
        assertEquals(-1, bytesRead);
    }
}