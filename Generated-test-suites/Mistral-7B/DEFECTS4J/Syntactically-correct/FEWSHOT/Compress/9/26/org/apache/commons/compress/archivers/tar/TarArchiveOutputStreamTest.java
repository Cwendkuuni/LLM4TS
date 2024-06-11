package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assertEquals(TarConstants.DEFAULT_BLKSIZE, tarArchiveOutputStream.getBuffer().getBlockSize());
        assertEquals(TarConstants.DEFAULT_RCDSIZE, tarArchiveOutputStream.getBuffer().getRecordSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullOutputStream() {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();
        assertFalse(tarArchiveOutputStream.isOpen());
        assertEquals(TarConstants.TAR_RECORD_TYPE_END, tarArchiveOutputStream.getBuffer().getRecordType());
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishAlreadyFinished() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.close();
        assertFalse(tarArchiveOutputStream.isOpen());
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseAlreadyClosed() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testCloseWithEntry() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
        assertFalse(tarArchiveOutputStream.isOpen());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertSame(entry, tarArchiveOutputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryDuplicateEntry() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length);
        byte[] outputData = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertArrayEquals(data, outputData);
    }

    @Test
    public void testWriteWithEntry() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] outputData = ((ByteArrayOutputStream) outputStream).toByteArray();
        TarArchiveEntry outputEntry = new TarArchiveEntry("test.txt");
        outputEntry.setSize(data.length);
        ArchiveEntry expectedEntry = new TarArchiveEntry("test.txt");
        expectedEntry.setSize(data.length);
        TarEntry testEntry = new TarEntry(expectedEntry);
        ArchiveUtils.verifyTarEntry(outputData, testEntry);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        assertEquals(inputFile.getName(), archiveEntry.getName());
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveEntry;
        assertEquals(inputFile.length(), tarArchiveEntry.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryNullInputFile() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        File inputFile = null;
        String entryName = "test.txt";
        tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryNullEntryName() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        File inputFile = new File("test.txt");
        String entryName = null;
        tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
    }
}