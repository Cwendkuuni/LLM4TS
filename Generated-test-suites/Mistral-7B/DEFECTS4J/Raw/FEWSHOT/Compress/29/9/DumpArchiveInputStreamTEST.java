package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class DumpArchiveInputStreamTest {

    @Test
    public void testMatches() {
        byte[] buffer = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(buffer, (byte) 0x00);
        DumpArchiveConstants.writeMagic(buffer, 0);
        assertTrue(DumpArchiveInputStream.matches(buffer, buffer.length));

        byte[] invalidBuffer = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(invalidBuffer, (byte) 0x01);
        assertFalse(DumpArchiveInputStream.matches(invalidBuffer, invalidBuffer.length));

        byte[] bufferWithChecksum = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(bufferWithChecksum, (byte) 0x00);
        DumpArchiveConstants.writeMagic(bufferWithChecksum, 0);
        DumpArchiveConstants.writeChecksum(bufferWithChecksum, 0, bufferWithChecksum.length);
        assertTrue(DumpArchiveInputStream.matches(bufferWithChecksum, bufferWithChecksum.length));

        byte[] invalidChecksumBuffer = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(invalidChecksumBuffer, (byte) 0x00);
        DumpArchiveConstants.writeMagic(invalidChecksumBuffer, 0);
        DumpArchiveConstants.writeChecksum(invalidChecksumBuffer, 0, invalidChecksumBuffer.length - 4);
        assertFalse(DumpArchiveInputStream.matches(invalidChecksumBuffer, invalidChecksumBuffer.length));
    }

    @Test
    public void testGetSummary() throws IOException, ArchiveException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(DumpArchiveConstants.writeMagic(new byte[DumpArchiveConstants.TP_SIZE], 0));
        outputStream.write(DumpArchiveConstants.writeDumpArchiveSummary(new DumpArchiveSummary(1, 2, 3, 4), ZipEncodingHelper.getDefaultEncoding()));

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DumpArchiveInputStream testee = new DumpArchiveInputStream(inputStream, null);

        DumpArchiveSummary summary = testee.getSummary();
        assertEquals(1, summary.getNumberOfFiles());
        assertEquals(2, summary.getNumberOfDirectories());
        assertEquals(3, summary.getBlockSize());
        assertEquals(4, summary.getTotalFilesize());
    }

    @Test
    public void testGetNextEntry() throws IOException, ArchiveException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(DumpArchiveConstants.writeMagic(new byte[DumpArchiveConstants.TP_SIZE], 0));
        outputStream.write(DumpArchiveConstants.writeDumpArchiveSummary(new DumpArchiveSummary(1, 2, 3, 4), ZipEncodingHelper.getDefaultEncoding()));
        outputStream.write(DumpArchiveUtil.writeDirectoryEntry(new Dirent(1, 1, 1, "dir1"), 4));
        outputStream.write(DumpArchiveUtil.writeDumpArchiveEntry(new DumpArchiveEntry(1, 1, 1, "file1.txt", 5, 10, false), 4));

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DumpArchiveInputStream testee = new DumpArchiveInputStream(inputStream, null);

        DumpArchiveEntry entry = testee.getNextEntry();
        assertEquals("file1.txt", entry.getName());
        assertEquals("file1.txt", entry.getSimpleName());
        assertEquals(1, entry.getIno());
        assertEquals(10, entry.getEntrySize());
        assertEquals(0, entry.getOffset());

        entry = testee.getNextEntry();
        assertEquals("dir1", entry.getName());
        assertNull(entry.getSimpleName());
        assertEquals(1, entry.getIno());
        assertEquals(4, entry.getEntrySize());
        assertEquals(-1, entry.getOffset());

        entry = testee.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testRead() throws IOException, ArchiveException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(DumpArchiveConstants.writeMagic(new byte[DumpArchiveConstants.TP_SIZE], 0));
        outputStream.write(DumpArchiveConstants.writeDumpArchiveSummary(new DumpArchiveSummary(1, 2, 3, 4), ZipEncodingHelper.getDefaultEncoding()));
        outputStream.write(DumpArchiveUtil.writeDirectoryEntry(new Dirent(1, 1, 1, "dir1"), 4));
        outputStream.write(DumpArchiveUtil.writeDumpArchiveEntry(new DumpArchiveEntry(1, 1, 1, "file1.txt", 5, 10, false), 4));
        outputStream.write(new byte[5]); // File content

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DumpArchiveInputStream testee = new DumpArchiveInputStream(inputStream, null);

        byte[] buffer = new byte[5];
        int read = testee.read(buffer, 0, buffer.length);
        assertEquals(5, read);
        assertArrayEquals(new byte[]{"file1.txt".getBytes()}, buffer);

        testee.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testReadWithoutEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(DumpArchiveConstants.writeMagic(new byte[DumpArchiveConstants.TP_SIZE], 0));
        outputStream.write(DumpArchiveConstants.writeDumpArchiveSummary(new DumpArchiveSummary(1, 2, 3, 4), ZipEncodingHelper.getDefaultEncoding()));

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DumpArchiveInputStream testee = new DumpArchiveInputStream(inputStream, null);

        byte[] buffer = new byte[5];
        testee.read(buffer, 0, buffer.length);

        testee.getNextEntry();

        testee.read(buffer, 0, buffer.length);
    }

    @Test(expected = EOFException.class)
    public void testReadAtEOF() throws IOException, ArchiveException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(DumpArchiveConstants.writeMagic(new byte[DumpArchiveConstants.TP_SIZE], 0));
        outputStream.write(DumpArchiveConstants.writeDumpArchiveSummary(new DumpArchiveSummary(1, 2, 3, 4), ZipEncodingHelper.getDefaultEncoding()));
        outputStream.write(DumpArchiveUtil.writeDirectoryEntry(new Dirent(1, 1, 1, "dir1"), 4));
        outputStream.write(DumpArchiveUtil.writeDumpArchiveEntry(new DumpArchiveEntry(1, 1, 1, "file1.txt", 5, 10, false), 4));

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DumpArchiveInputStream testee = new DumpArchiveInputStream(inputStream, null);

        byte[] buffer = new byte[5];
        testee.read(buffer, 0, buffer.length);

        testee.close();

        testee.read(buffer, 0, buffer.length);
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidArchive() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[DumpArchiveConstants.TP_SIZE]);
        outputStream.write("InvalidMagic".getBytes());

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DumpArchiveInputStream testee = new DumpArchiveInputStream(inputStream, null);

        testee.close();
    }
}