package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntryAndWriteShortFile() throws IOException {
        final String fileName = "test_entry.txt";
        final String fileContent = "Content of the test file";
        final byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileContentBytes, 0, fileContentBytes.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedTarArchive = createExpectedTarArchive(fileName, fileContentBytes.length);
        assertEquals("Tar archive doesn't match expected output", expectedTarArchive, outputStream.toByteArray());
    }

    @Test
    public void testPutArchiveEntryAndWriteLongFile() throws IOException {
        final String fileName = "test_long_entry.txt";
        final String fileContent = "Content of the long test file";
        final byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] buffer = new byte[1024];
        int bytesRead;

        OutputStream fileOutputStream = new ByteArrayOutputStream(fileContentBytes.length);
        for (int i = 0; i < fileContentBytes.length; i += buffer.length) {
            bytesRead = fileContentBytes.length > i + buffer.length
                    ? buffer.length
                    : fileContentBytes.length - i;
            fileOutputStream.write(fileContentBytes, i, bytesRead);
            tarArchiveOutputStream.write(buffer, 0, buffer.length);
        }
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedTarArchive = createExpectedTarArchiveWithLongFileName(fileName, fileContentBytes.length, fileContentBytes);
        assertEquals("Tar archive doesn't match expected output", expectedTarArchive, outputStream.toByteArray());
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameButTruncateMode() throws IOException {
        final String fileName = "test_long_entry_truncated.tar";
        final String longFileName = "this_is_a_very_long_filename.txt";
        final String fileContent = "Content of the long test file";
        final byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] buffer = new byte[1024];
        int bytesRead;

        OutputStream fileOutputStream = new ByteArrayOutputStream(fileContentBytes.length);
        for (int i = 0; i < fileContentBytes.length; i += buffer.length) {
            bytesRead = fileContentBytes.length > i + buffer.length
                    ? buffer.length
                    : fileContentBytes.length - i;
            fileOutputStream.write(fileContentBytes, i, bytesRead);
            tarArchiveOutputStream.write(buffer, 0, buffer.length);
        }
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedTarArchive = createExpectedTarArchiveWithTruncatedLongFileName(longFileName, fileContentBytes.length, fileContentBytes);
        assertEquals("Tar archive doesn't match expected output", expectedTarArchive, outputStream.toByteArray());
    }

    @Test
    public void testLongFileNameWithErrorMode() throws IOException {
        final String fileName = "test_long_entry_error.tar";
        final String longFileName = "this_is_a_very_long_filename.txt";
        final String fileContent = "Content of the long test file";
        final byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        assertThrows(RuntimeException.class, () -> tarArchiveOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testCloseWithoutPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();
        assertFalse("close should fail when no putArchiveEntry was called", tarArchiveOutputStream.close());
    }

    @Test
    public void testWriteWithoutPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assertFalse("write should fail when no putArchiveEntry was called", tarArchiveOutputStream.write(new byte[1024], 0, 1024));
    }

    private byte[] createExpectedTarArchive(String fileName, int fileSize) {
        byte[] header = new TarArchiveEntry(fileName, (short) 0, (short) 0,
                (short) 0, (short) 0, fileSize).getHeader();
        byte[] record = new byte[TarConstants.STD_RECORD_SIZE];
        System.arraycopy(header, 0, record, 0, TarConstants.STD_RECORD_SIZE);

        byte[] fileContent = new byte[fileSize];
        System.arraycopy(fileContent, 0, record, TarConstants.NAME_LENGTH, fileSize);

        byte[] tarArchive = new byte[TarConstants.STD_RECORD_SIZE + fileSize];
        System.arraycopy(record, 0, tarArchive, 0, TarConstants.STD_RECORD_SIZE + fileSize);
        return tarArchive;
    }

    private byte[] createExpectedTarArchiveWithLongFileName(String fileName, int fileSize, byte[] fileContent) {
        byte[] nameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        int nameLength = nameBytes.length;
        byte[] header = new TarArchiveEntry(fileName, (short) 0, (short) 0,
                (short) (nameLength + TarConstants.NAME_LENGTH),
                (short) (TarConstants.STD_RECORD_SIZE + fileSize), fileSize).getHeader();
        byte[] record = new byte[TarConstants.STD_RECORD_SIZE + fileSize];
        System.arraycopy(header, 0, record, 0, TarConstants.STD_RECORD_SIZE);
        System.arraycopy(nameBytes, 0, record, TarConstants.NAME_LENGTH, nameLength);
        System.arraycopy(fileContent, 0, record, TarConstants.NAME_LENGTH + TarConstants.STD_RECORD_SIZE, fileSize);
        return record;
    }

    private byte[] createExpectedTarArchiveWithTruncatedLongFileName(String fileName, int fileSize, byte[] fileContent) {
        byte[] nameBytes = fileName.substring(0, TarConstants.NAMELEN).getBytes(StandardCharsets.UTF_8);
        byte[] record = new byte[TarConstants.STD_RECORD_SIZE + fileSize];
        System.arraycopy(new TarArchiveEntry(fileName, (short) 0, (short) 0,
                (short) (TarConstants.NAMELEN + TarConstants.STD_RECORD_SIZE + fileSize),
                (short) (TarConstants.STD_RECORD_SIZE + fileSize), fileSize).getHeader(), 0, TarConstants.STD_RECORD_SIZE);
        System.arraycopy(nameBytes, 0, record, TarConstants.NAME_LENGTH, TarConstants.NAMELEN);
        System.arraycopy(fileContent, 0, record, TarConstants.NAME_LENGTH + TarConstants.STD_RECORD_SIZE, fileSize);
        return record;
    }
}