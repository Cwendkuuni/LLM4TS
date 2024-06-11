package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;

public class TarArchiveOutputStreamTest {

    @Test
    public void testWriteTarArchiveWithDefaultSettings() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] entryContent = "Test content".getBytes();
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(entryContent, 0, entryContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedTarArchive = getExpectedTarArchive();
        assertArrayEquals(expectedTarArchive, outputStream.toByteArray());
    }

    @Test
    public void testWriteTarArchiveWithLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        String longFileName = "a/long/file/name.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        byte[] entryContent = "Test content".getBytes();
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(entryContent, 0, entryContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedTarArchive = getExpectedTarArchiveWithLongFileName();
        assertArrayEquals(expectedTarArchive, outputStream.toByteArray());
    }

    @Test
    public void testWriteTarArchiveWithTruncatedFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String longFileName = "a/long/file/name.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        byte[] entryContent = "Test content".getBytes();
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish(); // No write or closeArchiveEntry call

        byte[] expectedTarArchive = getExpectedTarArchiveWithTruncatedFileName();
        assertArrayEquals(expectedTarArchive, outputStream.toByteArray());
    }

    @Test
    public void testWriteTarArchiveWithDirectoryEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String dirName = "test_directory/";
        TarArchiveEntry directoryEntry = new TarArchiveEntry(dirName);
        directoryEntry.setDirectory(true);
        tarArchiveOutputStream.putArchiveEntry(directoryEntry);
        tarArchiveOutputStream.finish();

        byte[] expectedTarArchive = getExpectedTarArchiveWithDirectoryEntry();
        assertArrayEquals(expectedTarArchive, outputStream.toByteArray());
    }

    @Test
    public void testCloseArchiveEntryWithoutWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry(); // No write call

        assertThrows(IOException.class, () -> tarArchiveOutputStream.closeArchiveEntry());
    }

    private byte[] getExpectedTarArchive() {
        // The expected test tar archive byte array for the default settings
        // Use the TarArchiveOutputStreamTestExpectedValues.java file
        // for the expected byte array content
        return new byte[0]; // To be implemented
    }

    private byte[] getExpectedTarArchiveWithLongFileName() {
        // The expected test tar archive byte array for the long file name setting
        // Use the TarArchiveOutputStreamTestExpectedValues.java file
        // for the expected byte array content
        return new byte[0]; // To be implemented
    }

    private byte[] getExpectedTarArchiveWithTruncatedFileName() {
        // The expected test tar archive byte array for the truncated file name setting
        // Use the TarArchiveOutputStreamTestExpectedValues.java file
        // for the expected byte array content
        return new byte[0]; // To be implemented
    }

    private byte[] getExpectedTarArchiveWithDirectoryEntry() {
        // The expected test tar archive byte array for the directory entry
        // Use the TarArchiveOutputStreamTestExpectedValues.java file
        // for the expected byte array content
        return new byte[0]; // To be implemented
    }
}