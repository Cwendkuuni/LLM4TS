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
import java.io.InputStream;
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
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(tarArchiveOutputStream.getBuffer());
        assertEquals(0, tarArchiveOutputStream.getRecordSize());
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntryWithShortFileName() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        String fileName = "test.txt";
        byte[] fileContent = "Hello World".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertEquals(fileName, archiveEntry.getName());
        assertFalse(archiveEntry.isDirectory());

        InputStream inputStream = tarArchiveOutputStream.getInputStream();
        byte[] actualContent = new byte[fileContent.length];
        inputStream.read(actualContent);
        assertArrayEquals(fileContent, actualContent);

        inputStream.close();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        String fileName = "veryVeryLongFileName.txt";
        byte[] fileContent = "Hello World".getBytes(StandardCharsets.UTF_8);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(fileName.getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertEquals(fileName, archiveEntry.getName());
        assertFalse(archiveEntry.isDirectory());

        InputStream inputStream = tarArchiveOutputStream.getInputStream();
        byte[] actualContent = new byte[fileContent.length];
        inputStream.read(actualContent);
        assertArrayEquals(fileContent, actualContent);

        inputStream.close();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeError() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        String fileName = "veryVeryLongFileName.txt";
        byte[] fileContent = "Hello World".getBytes(StandardCharsets.UTF_8);

        // Set longFileModeError to handle long file names by throwing an exception

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        assertThrows(RuntimeException.class, () -> tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry));

        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        String dirName = "testDir";

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(dirName);
        tarArchiveEntry.setDirectory(true);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        ArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertEquals(dirName, archiveEntry.getName());
        assertTrue(archiveEntry.isDirectory());
    }

    @Test
    public void testWriteWithRemainingBytes() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        String fileName = "test.txt";
        byte[] fileContent = "Hello World".getBytes(StandardCharsets.UTF_8);
        byte[] remainingBytes = new byte[] { 'A', 'B' };

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.write(remainingBytes, 0, remainingBytes.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertEquals(fileName, archiveEntry.getName());
        assertFalse(archiveEntry.isDirectory());

        InputStream inputStream = tarArchiveOutputStream.getInputStream();
        byte[] actualContent = new byte[fileContent.length + remainingBytes.length];
        inputStream.read(actualContent);
        assertArrayEquals(Arrays.copyOf(fileContent, fileContent.length + remainingBytes.length), actualContent);

        inputStream.close();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testFinish() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        String fileName = "test.txt";
        byte[] fileContent = "Hello World".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        ArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertEquals(fileName, archiveEntry.getName());
        assertFalse(archiveEntry.isDirectory());

        InputStream inputStream = tarArchiveOutputStream.getInputStream();
        byte[] actualContent = new byte[fileContent.length];
        inputStream.read(actualContent);
        assertArrayEquals(fileContent, actualContent);

        inputStream.close();
        tarArchiveOutputStream.close();

        archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertNull(archiveEntry);
    }

    @Test
    public void testClose() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        String fileName = "test.txt";
        byte[] fileContent = "Hello World".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.close();

        ArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertNull(archiveEntry);
    }

    @Test
    public void testFinishAndClose() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        String fileName = "test.txt";
        byte[] fileContent = "Hello World".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        ArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertNull(archiveEntry);
    }
}