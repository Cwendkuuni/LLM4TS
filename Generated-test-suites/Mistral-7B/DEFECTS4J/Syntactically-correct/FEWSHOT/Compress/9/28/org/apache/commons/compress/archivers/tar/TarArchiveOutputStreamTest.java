package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.CountingInputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testEmptyConstruction() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        tarOutputStream.finish();
        byte[] expectedEOFBytes = new byte[512 * 2]; // Two EOF blocks of 512 bytes
        assertArrayEquals(expectedEOFBytes, tarOutputStream.toByteArray());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        tarOutputStream.finish();
        byte[] expectedEOFBytes = new byte[512 * 2]; // Two EOF blocks of 512 bytes
        assertArrayEquals(expectedEOFBytes, tarOutputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishWithEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] expectedHeaderBytes = entry.getHeaderBytes();
        assertArrayEquals(expectedHeaderBytes, tarOutputStream.toByteArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveEntry entry = null;
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        byte[] expectedEOFBytes = new byte[512 * 2]; // Two EOF blocks of 512 bytes
        byte[] expectedBytes = new byte[entry.getHeaderBytes().length + 512];
        System.arraycopy(entry.getHeaderBytes(), 0, expectedBytes, 0, entry.getHeaderBytes().length);
        assertArrayEquals(expectedBytes, tarOutputStream.toByteArray());
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseArchiveEntryTwice() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello, World!".getBytes();
        tarOutputStream.write(data);
        byte[] expectedBytes = new byte[entry.getHeaderBytes().length + data.length + 512];
        System.arraycopy(entry.getHeaderBytes(), 0, expectedBytes, 0, entry.getHeaderBytes().length);
        System.arraycopy(data, 0, expectedBytes, entry.getHeaderBytes().length, data.length);
        assertArrayEquals(expectedBytes, tarOutputStream.toByteArray());
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteWithoutEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        byte[] data = "Hello, World!".getBytes();
        tarOutputStream.write(data);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello, World!".getBytes();
        tarOutputStream.write(data);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
        byte[] expectedEOFBytes = new byte[512 * 2]; // Two EOF blocks of 512 bytes
        byte[] expectedBytes = new byte[entry.getHeaderBytes().length + data.length + 512];
        System.arraycopy(entry.getHeaderBytes(), 0, expectedBytes, 0, entry.getHeaderBytes().length);
        System.arraycopy(data, 0, expectedBytes, entry.getHeaderBytes().length, data.length);
        assertArrayEquals(expectedBytes, tarOutputStream.toByteArray());
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        assertEquals(512, tarOutputStream.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File testFile = new File("test.txt");
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(testFile, "test.txt");
        assertTrue(archiveEntry instanceof TarArchiveEntry);
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveEntry;
        assertEquals("test.txt", tarArchiveEntry.getName());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateArchiveEntryNullFile() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(null, "test.txt");
        assertNull(archiveEntry);
    }

    @Test
    public void testCreateArchiveEntryEmptyFileName() throws IOException {
        File testFile = new File("");
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(testFile, "");
        assertNull(archiveEntry);
    }

    @Test
    public void testCreateArchiveEntryLongFileName() throws IOException {
        File testFile = new File("test" + new String(new char[255]).replace("\0", "a"));
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(testFile, "test" + new String(new char[255]).replace("\0", "a"));
        assertTrue(archiveEntry instanceof TarArchiveEntry);
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveEntry;
        assertEquals("test" + new String(new char[255]).replace("\0", "a"), tarArchiveEntry.getName());
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello, World!".getBytes();
        tarOutputStream.write(data);
        tarOutputStream.flush();
        byte[] expectedBytes = new byte[entry.getHeaderBytes().length + data.length + 512];
        System.arraycopy(entry.getHeaderBytes(), 0, expectedBytes, 0, entry.getHeaderBytes().length);
        System.arraycopy(data, 0, expectedBytes, entry.getHeaderBytes().length, data.length);
        assertArrayEquals(expectedBytes, tarOutputStream.toByteArray());
    }
}