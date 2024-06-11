package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assertSame(outputStream, tarArchiveOutputStream.getOutputStream());
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntryWithSmallFileName() throws IOException {
        String fileName = "test.txt";
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        ArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) tarArchiveOutputStream.getNextEntry();
        assertEquals(fileName, tarArchiveEntry.getName());
        assertArrayEquals(fileContent, tarArchiveOutputStream.getBuffer().getDataToEndOfStream());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String fileName = "longFileNameThatIsTooLongToBeStoredDirectlyInTarFile.txt";
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream()) {
            @Override
            public void setLongFileMode(int longFileMode) {
                super.setLongFileMode(longFileMode);
                assertEquals(longFileMode, TarArchiveOutputStream.LONGFILE_GNU);
            }
        };

        ArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) tarArchiveOutputStream.getNextEntry();
        assertEquals(fileName, tarArchiveEntry.getName());
        assertArrayEquals(fileContent, tarArchiveOutputStream.getBuffer().getDataToEndOfStream());
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        String dirName = "testDir";
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry(dirName, false);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();

        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) tarArchiveOutputStream.getNextEntry();
        assertEquals(dirName, tarArchiveEntry.getName());
        assertTrue(tarArchiveEntry.isDirectory());
    }

    @Test
    public void testFinishWithNoDataWritten() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(tarArchiveOutputStream.getBuffer().getDataToEndOfStream());
        assertFalse(inputStream.read() >= 0);
    }

    @Test
    public void testFinishWithDataWritten() throws IOException {
        String fileName = "test.txt";
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        ArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(tarArchiveOutputStream.getBuffer().getDataToEndOfStream());
        byte[] expectedContent = new byte[fileContent.length + TarConstants.STANDARD_RECORD_LENGTH];
        Arrays.fill(expectedContent, (byte) 0);
        System.arraycopy(fileContent, 0, expectedContent, 0, fileContent.length);
        assertArrayEquals(expectedContent, inputStream.readAllBytes());
    }

    @Test
    public void testFinishWithUnclosedEntry() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertThrows(IllegalStateException.class, tarArchiveOutputStream::finish);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testNextEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(tarArchiveOutputStream.getNextEntry());
        String fileName = "test.txt";
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) tarArchiveOutputStream.getNextEntry();
        assertEquals(fileName, tarArchiveEntry.getName());
        assertArrayEquals(fileContent, tarArchiveOutputStream.getBuffer().getDataToEndOfStream());
        assertNull(tarArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testWriteWithSmallBufferSize() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);
        int bufferSize = 2;

        for (int i = 0; i < fileContent.length; i += bufferSize) {
            tarArchiveOutputStream.write(fileContent, i, Math.min(bufferSize, fileContent.length - i));
        }
        tarArchiveOutputStream.close();

        byte[] expectedContent = new byte[fileContent.length];
        System.arraycopy(fileContent, 0, expectedContent, 0, fileContent.length);
        assertArrayEquals(expectedContent, byteArrayOutputStream.toByteArray());
    }
}