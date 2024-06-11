package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructors() {
        OutputStream os = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        assertTrue(tarOut instanceof TarArchiveOutputStream);
        assertEquals(1024, tarOut.getRecordSize());

        TarArchiveOutputStream tarOut2 = new TarArchiveOutputStream(os, 512);
        assertEquals(512, tarOut2.getRecordSize());

        TarArchiveOutputStream tarOut3 = new TarArchiveOutputStream(os, 512, 1024);
        assertEquals(512, tarOut3.getRecordSize());
        assertEquals(1024, tarOut3.getBlockSize());
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOut.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeIllegalArgument() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        tarOut.setLongFileMode(-1);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        tarOut.finish();
        byte[] expected = new byte[1024 * 2];
        assertArrayEquals(expected, tarOut.getBuffer().getBuffer());
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        tarOut.close();
        byte[] expected = new byte[1024 * 2];
        assertArrayEquals(expected, tarOut.getBuffer().getBuffer());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        tarOut.write(data, 0, data.length);
        byte[] expected = new byte[1024];
        System.arraycopy(data, 0, expected, 0, data.length);
        assertArrayEquals(expected, tarOut.getBuffer().getBuffer());
    }

    @Test(expected = IOException.class)
    public void testWriteExceedsSize() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        byte[] data = new byte[1025];
        tarOut.write(data, 0, data.length);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveEntry;
        tarOut.putArchiveEntry(archiveEntry);
        byte[] expected = new byte[1024 + tarArchiveEntry.getSize()];
        System.arraycopy(tarArchiveEntry.getHeader(), 0, expected, 0, tarArchiveEntry.getHeader().length);
        System.arraycopy(tarArchiveEntry.getSize(), 0, expected, 1024, tarArchiveEntry.getSize());
        assertArrayEquals(expected, tarOut.getBuffer().getBuffer());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryInvalidEntry() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        ArchiveEntry archiveEntry = new Object();
        tarOut.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarOut.putArchiveEntry(tarArchiveEntry);
        tarOut.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryBeforePut() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        tarOut.closeArchiveEntry();
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarOut.putArchiveEntry(tarArchiveEntry);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        File file = new File("test.txt");
        ArchiveEntry archiveEntry = tarOut.createArchiveEntry(file, "test.txt");
        assertNull(archiveEntry);
    }

    @Test
    public void testCreateArchiveEntryFileDoesNotExist() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        File file = new File("test.txt");
        ArchiveEntry archiveEntry = tarOut.createArchiveEntry(file, "test.txt");
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveEntry;
        tarOut.putArchiveEntry(archiveEntry);
        byte[] expected = new byte[1024 + tarArchiveEntry.getSize()];
        System.arraycopy(tarArchiveEntry.getHeader(), 0, expected, 0, tarArchiveEntry.getHeader().length);
        System.arraycopy(tarArchiveEntry.getSize(), 0, expected, 1024, tarArchiveEntry.getSize());
        assertArrayEquals(expected, tarOut.getBuffer().getBuffer());
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryFileExists() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        File file = new File("test.txt");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        ArchiveEntry archiveEntry = tarOut.createArchiveEntry(file, "test.txt");
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveEntry;
        tarOut.putArchiveEntry(archiveEntry);
        byte[] expected = new byte[1024 + tarArchiveEntry.getSize()];
        System.arraycopy(tarArchiveEntry.getHeader(), 0, expected, 0, tarArchiveEntry.getHeader().length);
        System.arraycopy(tarArchiveEntry.getSize(), 0, expected, 1024, tarArchiveEntry.getSize());
        assertArrayEquals(expected, tarOut.getBuffer().getBuffer());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), TarArchiveOutputStream.LONGFILE_GNU);
        String longFileName = "test/long/file/name.txt";
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileName.getBytes(StandardCharsets.UTF_8), longFileName.getBytes(StandardCharsets.UTF_8), TarConstants.GNU_LONGLINK);
        tarOut.putArchiveEntry(archiveEntry);
        byte[] expected = new byte[1024 + TarConstants.NAMELEN + archiveEntry.getSize()];
        System.arraycopy(archiveEntry.getHeader(), 0, expected, 0, archiveEntry.getHeader().length);
        System.arraycopy(longFileName.getBytes(StandardCharsets.UTF_8), 0, expected, 1024, longFileName.getBytes(StandardCharsets.UTF_8).length);
        assertArrayEquals(expected, tarOut.getBuffer().getBuffer());
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameLongFileModeError() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        String longFileName = "test/long/file/name.txt";
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileName.getBytes(StandardCharsets.UTF_8), longFileName.getBytes(StandardCharsets.UTF_8), TarConstants.GNU_LONGLINK);
        tarOut.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testFinishAndClose() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarOut.putArchiveEntry(tarArchiveEntry);
        tarOut.closeArchiveEntry();
        tarOut.finish();
        byte[] expected = new byte[1024 * 2];
        assertArrayEquals(expected, tarOut.getBuffer().getBuffer());
        assertFalse(tarOut.isOpen());
    }
}