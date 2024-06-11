package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TarConstants;

public class TarArchiveOutputStreamTest {

    @Test
    public void testNewInstance() {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os);
        assertSame(os, tarStream.getOutputStream());
        assertEquals(TarConstants.DEFAULT_BLKSIZE, tarStream.getBlockSize());
        assertEquals(TarConstants.DEFAULT_RCDSIZE, tarStream.getRecordSize());
    }

    @Test
    public void testNewInstanceWithBlockSizeAndRecordSize() {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 1024);
        assertSame(os, tarStream.getOutputStream());
        assertEquals(512, tarStream.getBlockSize());
        assertEquals(1024, tarStream.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarStream.getLongFileMode());
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarStream.getLongFileMode());
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os);
        assertThrows(IllegalStateException.class, tarStream::finish);
        assertFalse(tarStream.isFinished());

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.finish();
        assertTrue(tarStream.isFinished());

        ByteArrayOutputStream output = (ByteArrayOutputStream) tarStream.getOutputStream();
        byte[] bytes = output.toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ArchiveInputStream in = new TarInputStream(input);
        ArchiveEntry nextEntry = in.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());

        nextEntry = in.getNextEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testFinishWithUnclosedEntry() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        assertThrows(IllegalStateException.class, tarStream::finish);
        assertFalse(tarStream.isFinished());

        byte[] record = new byte[TarConstants.STANDARD_HEADER_SIZE];
        TarHeader header = new TarHeader(new ByteArrayInputStream(record));
        assertEquals(0, header.getMagic());

        tarStream.finish();
        assertTrue(tarStream.isFinished());

        byte[] bytes = output.toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ArchiveInputStream in = new TarInputStream(input);
        ArchiveEntry nextEntry = in.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());

        nextEntry = in.getNextEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        assertThrows(IllegalStateException.class, tarStream::finish);
        assertFalse(tarStream.isFinished());
        assertNull(tarStream.getCurrentEntry());

        TarArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        tarStream.putArchiveEntry(entry2);
        tarStream.finish();
        assertTrue(tarStream.isFinished());
        assertSame(entry2, tarStream.getCurrentEntry());

        TarArchiveEntry entry3 = new TarArchiveEntry("test3.txt");
        assertThrows(IllegalStateException.class, () -> tarStream.putArchiveEntry(entry3));

        ByteArrayOutputStream output = (ByteArrayOutputStream) tarStream.getOutputStream();
        byte[] bytes = output.toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ArchiveInputStream in = new TarInputStream(input);
        ArchiveEntry nextEntry = in.getNextEntry();
        assertEquals("test3.txt", nextEntry.getName());
        assertNull(tarStream.getCurrentEntry());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        assertThrows(IllegalStateException.class, tarStream::finish);
        assertFalse(tarStream.isFinished());
        assertNull(tarStream.getCurrentEntry());

        byte[] data = new byte[1000];
        byte[] header = new byte[TarConstants.STANDARD_HEADER_SIZE];
        System.arraycopy(TarConstants.STANDARD_MAGIC, 0, header, 0, TarConstants.STANDARD_MAGIC.length);
        TarHeader testHeader = new TarHeader(header);
        testHeader.setName("test.txt");
        testHeader.setSize(data.length);
        testHeader.setMode(0644);
        testHeader.setChecksum(new CRC32().value(data) & 0xFFFFFFFFL);

        int written = 0;
        for (int i = 0; i < data.length; i++) {
            written += tarStream.write(data, i, 1);
            tarStream.closeArchiveEntry();
        }
        tarStream.finish();
        assertTrue(tarStream.isFinished());
        assertNull(tarStream.getCurrentEntry());

        ByteArrayOutputStream output = (ByteArrayOutputStream) tarStream.getOutputStream();
        byte[] bytes = output.toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ArchiveInputStream in = new TarInputStream(input);
        ArchiveEntry nextEntry = in.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());
    }

    @Test
    public void testWriteWithDataLessThanRecordSize() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.finish();

        byte[] smallData = new byte[511];
        int written = tarStream.write(smallData, 0, smallData.length);
        assertEquals(smallData.length, written);

        byte[] record = new byte[TarConstants.STANDARD_HEADER_SIZE];
        TarHeader header = new TarHeader(new ByteArrayInputStream(record));
        byte[] data = new byte[1024];
        System.arraycopy(TarConstants.STANDARD_MAGIC, 0, record, 0, TarConstants.STANDARD_MAGIC.length);
        header.setName("test.txt");
        header.setSize(1024);
        header.setMode(0644);
        header.setChecksum(new CRC32().value(data) & 0xFFFFFFFFL);

        int assemblyLength = 0;
        written = 0;
        while (written < smallData.length) {
            int blockSize = Math.min(TarConstants.DEFAULT_BLKSIZE, smallData.length - written);
            byte[] block = new byte[blockSize];
            System.arraycopy(smallData, written, block, 0, blockSize);
            written += blockSize;
            assemblyLength += blockSize;
            tarStream.write(block, 0, blockSize);
            tarStream.closeArchiveEntry();
        }

        byte[] finalData = new byte[TarConstants.STANDARD_HEADER_SIZE + smallData.length];
        System.arraycopy(record, 0, finalData, 0, TarConstants.STANDARD_HEADER_SIZE);
        System.arraycopy(smallData, 0, finalData, TarConstants.STANDARD_HEADER_SIZE, smallData.length);

        byte[] bytes = ((ByteArrayOutputStream) tarStream.getOutputStream()).toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ArchiveInputStream in = new TarInputStream(input);
        ArchiveEntry nextEntry = in.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());
        byte[] readData = new byte[nextEntry.getSize()];
        in.read(readData, 0, readData.length);
        assertArrayEquals(smallData, readData);

        nextEntry = in.getNextEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testWriteWithDataMoreThanRecordSize() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.finish();

        byte[] data = new byte[1025];
        int written = 0;
        while (written < data.length) {
            int blockSize = Math.min(TarConstants.DEFAULT_BLKSIZE, data.length - written);
            byte[] block = new byte[blockSize];
            System.arraycopy(data, written, block, 0, blockSize);
            written += blockSize;
            tarStream.write(block, 0, blockSize);
            tarStream.closeArchiveEntry();
        }

        ByteArrayOutputStream output = (ByteArrayOutputStream) tarStream.getOutputStream();
        byte[] bytes = output.toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ArchiveInputStream in = new TarInputStream(input);
        ArchiveEntry nextEntry = in.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());
        byte[] readData = new byte[nextEntry.getSize()];
        in.read(readData, 0, readData.length);
        assertArrayEquals(data, readData);

        nextEntry = in.getNextEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testFlush() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.finish();
        tarStream.flush();

        ByteArrayOutputStream output = (ByteArrayOutputStream) tarStream.getOutputStream();
        byte[] bytes = output.toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ArchiveInputStream in = new TarInputStream(input);
        ArchiveEntry nextEntry = in.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());
        byte[] readData = new byte[nextEntry.getSize()];
        in.read(readData, 0, readData.length);
        assertArrayEquals(IOUtils.toByteArray(entry.getInputStream()), readData);

        nextEntry = in.getNextEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os);

        File file = new File("test.txt");
        TarArchiveEntry entry = tarStream.createArchiveEntry(file, "test.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.finish();

        ByteArrayOutputStream output = (ByteArrayOutputStream) tarStream.getOutputStream();
        byte[] bytes = output.toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ArchiveInputStream in = new TarInputStream(input);
        ArchiveEntry nextEntry = in.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());
        byte[] readData = new byte[nextEntry.getSize()];
        in.read(readData, 0, readData.length);
        assertArrayEquals(IOUtils.toByteArray(new FileInputStream(file)), readData);

        nextEntry = in.getNextEntry();
        assertNull(nextEntry);
    }
}