package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Test cases for DumpArchiveInputStream class.
 */
public class DumpArchiveInputStreamTest {

    @Test
    public void testMatches() {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(header, (byte) 0x4E);
        assertTrue(DumpArchiveInputStream.matches(header, header.length));

        byte[] headerWithChecksum = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(headerWithChecksum, (byte) 0x4E);
        byte[] checksum = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(checksum, (byte) 0xDE);
        System.arraycopy(header, 0, headerWithChecksum, 0, DumpArchiveConstants.TP_SIZE - DumpArchiveConstants.CHECKSUM_LENGTH);
        System.arraycopy(checksum, 0, headerWithChecksum, DumpArchiveConstants.TP_SIZE - DumpArchiveConstants.CHECKSUM_LENGTH, DumpArchiveConstants.CHECKSUM_LENGTH);
        assertTrue(DumpArchiveInputStream.matches(headerWithChecksum, headerWithChecksum.length));

        byte[] invalidHeader = new byte[32];
        Arrays.fill(invalidHeader, (byte) 0x4F);
        assertFalse(DumpArchiveInputStream.matches(invalidHeader, invalidHeader.length));

        byte[] tooShortHeader = new byte[31];
        Arrays.fill(tooShortHeader, (byte) 0x4E);
        assertFalse(DumpArchiveInputStream.matches(tooShortHeader, tooShortHeader.length));

        byte[] data = new byte[1024];
        assertFalse(DumpArchiveInputStream.matches(data, data.length));
    }

    @Test
    public void testGetNextEntryWithEmptyArchive() throws IOException, ArchiveException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DumpArchiveInputStream is = new DumpArchiveInputStream(new ByteArrayInputStream(bos.toByteArray()));

        DumpArchiveEntry entry = is.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntryWithOneEntryArchive() throws IOException, ArchiveException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(header, (byte) 0x4E);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(header);
        DumpArchiveSummary summary = new DumpArchiveSummary(header, ZipEncodingHelper.getDefaultEncoding());
        DumpArchiveEntry entry = new DumpArchiveEntry("test.txt", 1, summary.getMagic());
        entry.setDirectory(false);
        entry.setOffset(0);
        bos.write(entry.getHeaderBytes());
        byte[] archiveData = bos.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(archiveData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);

        DumpArchiveEntry actualEntry = dumpArchiveInputStream.getNextEntry();
        assertEquals(entry, actualEntry);
    }

    @Test
    public void testGetNextEntryWithMultipleEntriesArchive() throws IOException, ArchiveException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(header, (byte) 0x4E);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(header);
        DumpArchiveSummary summary = new DumpArchiveSummary(header, ZipEncodingHelper.getDefaultEncoding());
        DumpArchiveEntry entry1 = new DumpArchiveEntry("test1.txt", 1, summary.getMagic());
        entry1.setDirectory(false);
        entry1.setOffset(0);
        bos.write(entry1.getHeaderBytes());
        DumpArchiveEntry entry2 = new DumpArchiveEntry("test2.txt", 2, summary.getMagic());
        entry2.setDirectory(false);
        entry2.setOffset(1024);
        bos.write(entry2.getHeaderBytes());
        byte[] archiveData = bos.toByteArray();
        ByteArrayInputStream
            is = new ByteArrayInputStream(archiveData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);

        DumpArchiveEntry entry1Actual = dumpArchiveInputStream.getNextEntry();
        assertEquals(entry1, entry1Actual);
        DumpArchiveEntry entry2Actual = dumpArchiveInputStream.getNextEntry();
        assertEquals(entry2, entry2Actual);
        DumpArchiveEntry entry3Actual = dumpArchiveInputStream.getNextEntry();
        assertNull(entry3Actual);
    }

    @Test
    public void testGetNextEntryWithDirectoryAndFileEntriesArchive() throws IOException, ArchiveException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(header, (byte) 0x4E);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(header);
        DumpArchiveSummary summary = new DumpArchiveSummary(header, ZipEncodingHelper.getDefaultEncoding());
        DumpArchiveEntry root = new DumpArchiveEntry(".", 1, summary.getMagic());
        root.setDirectory(true);
        root.setOffset(0);
        bos.write(root.getHeaderBytes());
        DumpArchiveEntry dir1 = new DumpArchiveEntry("dir1/", 2, summary.getMagic());
        dir1.setDirectory(true);
        dir1.setOffset(1024);
        bos.write(dir1.getHeaderBytes());
        DumpArchiveEntry file1 = new DumpArchiveEntry("dir1/test1.txt", 3, summary.getMagic());
        file1.setDirectory(false);
        file1.setOffset(2048);
        bos.write(file1.getHeaderBytes());
        byte[] archiveData = bos.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(archiveData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);

        DumpArchiveEntry entry1Actual = dumpArchiveInputStream.getNextEntry();
        assertEquals(root, entry1Actual);
        DumpArchiveEntry entry2Actual = dumpArchiveInputStream.getNextEntry();
        assertEquals(dir1, entry2Actual);
        DumpArchiveEntry entry3Actual = dumpArchiveInputStream.getNextEntry();
        assertEquals(file1, entry3Actual);
        DumpArchiveEntry entry4Actual = dumpArchiveInputStream.getNextEntry();
        assertNull(entry4Actual);
    }

    @Test
    public void testGetNameAndSimpleName() throws IOException, ArchiveException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(header, (byte) 0x4E);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(header);
        DumpArchiveSummary summary = new DumpArchiveSummary(header, ZipEncodingHelper.getDefaultEncoding());
        DumpArchiveEntry entry = new DumpArchiveEntry("test.txt", 1, summary.getMagic());
        entry.setDirectory(false);
        entry.setOffset(0);
        bos.write(entry.getHeaderBytes());
        byte[] archiveData = bos.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(archiveData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);

        DumpArchiveEntry actualEntry = dumpArchiveInputStream.getNextEntry();
        assertEquals("test.txt", actualEntry.getName());
        assertEquals("test", actualEntry.getSimpleName());
    }

    @Test
    public void testGetNameAndSimpleNameWithDirectoryEntry() throws IOException, ArchiveException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(header, (byte) 0x4E);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(header);
        DumpArchiveSummary summary = new DumpArchiveSummary(header, ZipEncodingHelper.getDefaultEncoding());
        DumpArchiveEntry root = new DumpArchiveEntry(".", 1, summary.getMagic());
        root.setDirectory(true);
        root.setOffset(0);
        bos.write(root.getHeaderBytes());
        DumpArchiveEntry dir = new DumpArchiveEntry("dir/", 2, summary.getMagic());
        dir.setDirectory(true);
        dir.setOffset(1024);
        bos.write(dir.getHeaderBytes());
        byte[] archiveData = bos.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(archiveData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);

        DumpArchiveEntry actualEntry = dumpArchiveInputStream.getNextEntry();
        assertEquals(".", actualEntry.getName());
        assertEquals(".", actualEntry.getSimpleName());
        DumpArchiveEntry nextEntry = dumpArchiveInputStream.getNextEntry();
        assertEquals("dir/", nextEntry.getName());
        assertEquals("dir", nextEntry.getSimpleName());
    }

    @Test
    public void testReadWithOneByte() throws IOException, ArchiveException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(header, (byte) 0x4E);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(header);
        DumpArchiveSummary summary = new DumpArchiveSummary(header, ZipEncodingHelper.getDefaultEncoding());
        DumpArchiveEntry entry = new DumpArchiveEntry("test.txt", 1, summary.getMagic());
        entry.setDirectory(false);
        entry.setOffset(0);
        entry.setEntrySize(1);
        bos.write(entry.getHeaderBytes());
        byte[] archiveData = bos.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(archiveData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);

        byte[] buf = new byte[1];
        int read = dumpArchiveInputStream.read(buf, 0, 1);
        assertEquals(1, read);
        assertEquals((byte) 0x4E, buf[0]);
    }

    @Test
    public void testReadWithMultipleBytes() throws IOException, ArchiveException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(header, (byte) 0x4E);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(header);
        DumpArchiveSummary summary = new DumpArchiveSummary(header, ZipEncodingHelper.getDefaultEncoding());
        DumpArchiveEntry entry = new DumpArchiveEntry("test.txt", 1, summary.getMagic());
        entry.setDirectory(false);
        entry.setOffset(0);
        entry.setEntrySize(3);
        bos.write(entry.getHeaderBytes());
        byte[] archiveData = bos.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(archiveData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);

        byte[] buf = new byte[3];
        int read = dumpArchiveInputStream.read(buf, 0, 3);
        assertEquals(3, read);
        Arrays.assertEquals(new byte[]{0x4E, 0x4E, 0x4E}, buf);
    }

    @Test
    public void testReadWithRemainingBytes() throws IOException, ArchiveException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(header, (byte) 0x4E);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(header);
        DumpArchiveSummary summary = new DumpArchiveSummary(header, ZipEncodingHelper.getDefaultEncoding());
        DumpArchiveEntry entry = new DumpArchiveEntry("test.txt", 1, summary.getMagic());
        entry.setDirectory(false);
        entry.setOffset(0);
        entry.setEntrySize(4);
        bos.write(entry.getHeaderBytes());
        byte[] archiveData = bos.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(archiveData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);

        byte[] buf = new byte[4];
        int read = dumpArchiveInputStream.read(buf, 0, 4);
        assertEquals(4, read);
        Arrays.assertEquals(new byte[]{0x4E, 0x4E, 0x4E, 0x4E}, buf);

        read = dumpArchiveInputStream.read(buf, 0, 1);
        assertEquals(-1, read);
    }

    @Test
    public void testClose() throws IOException, ArchiveException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(header, (byte) 0x4E);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(header);
        DumpArchiveSummary summary = new DumpArchiveSummary(header, ZipEncodingHelper.getDefaultEncoding());
        DumpArchiveEntry entry = new DumpArchiveEntry("test.txt", 1, summary.getMagic());
        entry.setDirectory(false);
        entry.setOffset(0);
        entry.setEntrySize(1);
        bos.write(entry.getHeaderBytes());
        byte[] archiveData = bos.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(archiveData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);

        dumpArchiveInputStream.getNextEntry();

        dumpArchiveInputStream.close();

        // verify that the underlying input stream was closed
        try (InputStream inputStream = is) {
            assertFalse(inputStream.isOpen());
        }
    }
}