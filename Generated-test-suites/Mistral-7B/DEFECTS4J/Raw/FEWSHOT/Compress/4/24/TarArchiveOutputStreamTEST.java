package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.apache.commons.compress.archivers.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testBasicFunctionality() throws IOException {
        String testFileName = "test_file.txt";

        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                new ByteArrayOutputStream());

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(testFileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] testFileContent = "This is a test file".getBytes(StandardCharsets.UTF_8);

        tarArchiveOutputStream.write(testFileContent, 0, testFileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) tarArchiveOutputStream.getOutputStream();
        byte[] tarFileBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(tarFileBytes);

        ArchiveOutputStream testArchiveOutputStream = new TarArchiveOutputStream(inputStream);

        ArchiveEntry entry = testArchiveOutputStream.getNextEntry();
        assertNotNull(entry);
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals(testFileName, ((TarArchiveEntry) entry).getName());

        byte[] readContent = new byte[testFileContent.length];
        int length = testArchiveOutputStream.read(readContent, 0, readContent.length);
        assertEquals(testFileContent.length, length);
        assertArrayEquals(testFileContent, readContent);

        testArchiveOutputStream.close();
    }

    @Test
    public void testWritingLongFileName() throws IOException {
        String testFileName = "test_file_with_long_name.txt";

        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(testFileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.write("Long file name".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.write(0); // NUL terminator
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) tarArchiveOutputStream.getOutputStream();
        byte[] tarFileBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(tarFileBytes);

        ArchiveOutputStream testArchiveOutputStream = new TarArchiveOutputStream(inputStream);

        ArchiveEntry entry = testArchiveOutputStream.getNextEntry();
        assertNotNull(entry);
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals(testFileName, ((TarArchiveEntry) entry).getName());

        testArchiveOutputStream.close();
    }

    @Test
    public void testWritingLongFileNameWithLongLink() throws IOException {
        String longFileName = "long_file_name_with_long_link.txt";
        String linkName = "link_name.txt";

        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(linkName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.write(longFileName.getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.write(0); // NUL terminator
        tarArchiveOutputStream.closeArchiveEntry();

        TarArchiveEntry longLinkEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK,
                TarConstants.LF_GNUTYPE_LONGNAME);

        longLinkEntry.setSize(longFileName.length() + 1); // +1 for NUL terminator
        tarArchiveOutputStream.putArchiveEntry(longLinkEntry);
        tarArchiveOutputStream.write(longFileName.getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.write(0); // NUL terminator
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) tarArchiveOutputStream.getOutputStream();
        byte[] tarFileBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(tarFileBytes);

        ArchiveOutputStream testArchiveOutputStream = new TarArchiveOutputStream(inputStream);

        ArchiveEntry entry = testArchiveOutputStream.getNextEntry();
        assertNotNull(entry);
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals(linkName, ((TarArchiveEntry) entry).getName());

        testArchiveOutputStream.getNextEntry();
        TarArchiveEntry longLinkEntryFromArchive = (TarArchiveEntry) testArchiveOutputStream.getNextEntry();
        assertNotNull(longLinkEntryFromArchive);
        assertEquals(longFileName, longLinkEntryFromArchive.getName());

        testArchiveOutputStream.close();
    }

    @Test
    public void testHandlingLargeFiles() throws IOException {
        String testFileName = "test_large_file.bin";
        byte[] largeFileContent = new byte[1024 * 1024]; // 1 MB

        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                new ByteArrayOutputStream());

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(testFileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.write(largeFileContent, 0, largeFileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) tarArchiveOutputStream.getOutputStream();
        byte[] tarFileBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(tarFileBytes);

        ArchiveOutputStream testArchiveOutputStream = new TarArchiveOutputStream(inputStream);

        ArchiveEntry entry = testArchiveOutputStream.getNextEntry();
        assertNotNull(entry);
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals(testFileName, ((TarArchiveEntry) entry).getName());

        byte[] readContent = new byte[largeFileContent.length];
        int length = testArchiveOutputStream.read(readContent, 0, readContent.length);
        assertEquals(largeFileContent.length, length);
        assertArrayEquals(largeFileContent, readContent);

        testArchiveOutputStream.close();
    }

    @Test
    public void testClosingUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                new ByteArrayOutputStream());

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test_file.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.write(new byte[1024]);

        assertThrows(IOException.class, () -> tarArchiveOutputStream.close());

        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testWritingZeroLengthEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                new ByteArrayOutputStream());

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test_file.txt");
        tarArchiveEntry.setSize(0);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.write(new byte[0]);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) tarArchiveOutputStream.getOutputStream();
        byte[] tarFileBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(tarFileBytes);

        ArchiveOutputStream testArchiveOutputStream = new TarArchiveOutputStream(inputStream);

        ArchiveEntry entry = testArchiveOutputStream.getNextEntry();
        assertNotNull(entry);
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals("test_file.txt", ((TarArchiveEntry) entry).getName());
        assertTrue(entry.getSize() == 0);

        testArchiveOutputStream.close();
    }

    @Test
    public void testHandlingDirectoryEntry() throws IOException {
        String testDirectoryName = "test_directory";

        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                new ByteArrayOutputStream());

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(testDirectoryName);
        tarArchiveEntry.setSize(0);
        tarArchiveEntry.setDirectory(true);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) tarArchiveOutputStream.getOutputStream();
        byte[] tarFileBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(tarFileBytes);

        ArchiveOutputStream testArchiveOutputStream = new TarArchiveOutputStream(inputStream);

        ArchiveEntry entry = testArchiveOutputStream.getNextEntry();
        assertNotNull(entry);
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals(testDirectoryName, ((TarArchiveEntry) entry).getName());
        assertTrue(entry.isDirectory());

        testArchiveOutputStream.close();
    }

    @Test
    public void testHandlingMalformedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                new ByteArrayOutputStream());

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test_file.txt");

        assertThrows(IllegalArgumentException.class, () -> tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry));

        tarArchiveEntry = new TarArchiveEntry("test_file.txt");
        tarArchiveEntry.setName(null);
        assertThrows(NullPointerException.class, () -> tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry));

        tarArchiveOutputStream.close();
    }
}