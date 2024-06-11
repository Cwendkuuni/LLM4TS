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

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarConstants;

public class TarArchiveOutputStreamTest {

    @Test
    public void testEmptyConstruction() {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(outputStream.getRecordSize());
        assertEquals(0, outputStream.getBufferSize());
        assertNull(outputStream.getBuffer());
        assertNull(outputStream.getAssemBuf());
        assertNull(outputStream.getRecordBuf());
        assertNull(outputStream.getBuffer());
        assertNull(outputStream.getName());
        assertFalse(outputStream.isFinished());
        assertFalse(outputStream.isClosed());
        assertFalse(outputStream.isHaveUnclosedEntry());
    }

    @Test
    public void testBasicConstruction() {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        assertEquals(1024, outputStream.getBufferSize());
        assertNotNull(outputStream.getBuffer());
        assertNull(outputStream.getAssemBuf());
        assertNull(outputStream.getRecordBuf());
        assertNull(outputStream.getName());
        assertFalse(outputStream.isFinished());
        assertFalse(outputStream.isClosed());
        assertFalse(outputStream.isHaveUnclosedEntry());
    }

    @Test
    public void testConstructionWithRecordSize() {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024, 512);
        assertEquals(1024, outputStream.getBufferSize());
        assertEquals(512, outputStream.getRecordSize());
        assertNotNull(outputStream.getBuffer());
        assertNull(outputStream.getAssemBuf());
        assertNull(outputStream.getRecordBuf());
        assertNull(outputStream.getName());
        assertFalse(outputStream.isFinished());
        assertFalse(outputStream.isClosed());
        assertFalse(outputStream.isHaveUnclosedEntry());
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, outputStream.getLongFileMode());
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, outputStream.getLongFileMode());
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, outputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        assertFalse(outputStream.isFinished());
        outputStream.finish();
        assertTrue(outputStream.isFinished());

        outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        outputStream.finish();
        outputStream.finish();
        assertTrue(outputStream.isFinished());
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        assertFalse(outputStream.isClosed());
        outputStream.close();
        assertTrue(outputStream.isClosed());

        outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        outputStream.close();
        outputStream.close();
        assertTrue(outputStream.isClosed());
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        assertEquals(0, outputStream.getRecordSize());

        outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024, 512);
        assertEquals(512, outputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);
        assertSame(entry, outputStream.getCurrentEntry());
        assertNull(outputStream.getName());
        assertEquals(0, outputStream.getBytesWritten());
        assertEquals("test.txt", outputStream.getCurrentEntry().getName());

        outputStream.write(new byte[] { 0x01, 0x02, 0x03 });
        assertEquals(3, outputStream.getBytesWritten());

        outputStream.closeArchiveEntry();
        assertNull(outputStream.getCurrentEntry());
        assertEquals("test.txt", outputStream.getName());
        assertEquals(3, outputStream.getBytesWritten());

        outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        entry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);
        assertSame(entry, outputStream.getCurrentEntry());
        assertNull(outputStream.getName());
        assertEquals(0, outputStream.getBytesWritten());
        assertEquals("test.txt", outputStream.getCurrentEntry().getName());

        outputStream.finish();
        assertNull(outputStream.getCurrentEntry());
        assertNull(outputStream.getName());
        assertEquals(0, outputStream.getBytesWritten());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);
        outputStream.write(new byte[] { 0x01, 0x02, 0x03 });
        outputStream.closeArchiveEntry();
        assertEquals(3, outputStream.getBytesWritten());
        assertNull(outputStream.getCurrentEntry());
        assertSame(entry, outputStream.getPreviousEntry());
        assertNull(outputStream.getName());

        outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        entry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);
        outputStream.write(new byte[] { 0x01, 0x02, 0x03 });
        outputStream.write(new byte[] { 0x04, 0x05, 0x06 });
        outputStream.closeArchiveEntry();
        assertEquals(6, outputStream.getBytesWritten());
        assertNull(outputStream.getCurrentEntry());
        assertSame(entry, outputStream.getPreviousEntry());
        assertNull(outputStream.getName());

        outputStream.finish();
        assertNull(outputStream.getCurrentEntry());
        assertNull(outputStream.getPreviousEntry());
        assertNull(outputStream.getName());
        assertEquals(6, outputStream.getBytesWritten());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        outputStream.write(data, 0, data.length);
        byte[] expected = new byte[data.length];
        System.arraycopy(data, 0, expected, 0, data.length);
        byte[] result = new byte[outputStream.getBytesWritten()];
        outputStream.read(result, 0, result.length);
        assertArrayEquals(expected, result);
        assertNull(outputStream.getCurrentEntry());
        assertNull(outputStream.getName());
        assertEquals(data.length, outputStream.getBytesWritten());

        outputStream.finish();
        assertNull(outputStream.getCurrentEntry());
        assertNull(outputStream.getName());
        assertNull(outputStream.getPreviousEntry());
        assertArrayEquals(expected, ArchiveUtils.toByteArray(outputStream.getInputStream()));
    }

    @Test
    public void testWriteWithBuffer() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        byte[] buffer = new byte[10];
        int offset = 0;
        for (int i = 0; i < data.length; i++) {
            buffer[offset++] = data[i];
            if (offset >= buffer.length) {
                outputStream.write(buffer, 0, buffer.length);
                offset = 0;
            }
        }
        byte[] expected = new byte[data.length];
        System.arraycopy(data, 0, expected, 0, data.length);
        byte[] result = new byte[outputStream.getBytesWritten()];
        outputStream.read(result, 0, result.length);
        assertArrayEquals(expected, result);
        assertNull(outputStream.getCurrentEntry());
        assertNull(outputStream.getName());
        assertEquals(data.length, outputStream.getBytesWritten());

        outputStream.finish();
        assertNull(outputStream.getCurrentEntry());
        assertNull(outputStream.getName());
        assertNull(outputStream.getPreviousEntry());
        assertArrayEquals(expected, ArchiveUtils.toByteArray(outputStream.getInputStream()));
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        outputStream.write(data, 0, data.length);
        assertThrows(IOException.class, () -> outputStream.closeArchiveEntry());
        assertNull(outputStream.getCurrentEntry());
        assertSame(entry, outputStream.getPreviousEntry());
        assertNull(outputStream.getName());
        assertEquals(data.length, outputStream.getBytesWritten());

        outputStream.finish();
        assertNull(outputStream.getCurrentEntry());
        assertNull(outputStream.getPreviousEntry());
        assertNull(outputStream.getName());
        assertArrayEquals(data, ArchiveUtils.toByteArray(outputStream.getInputStream()));
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        outputStream.finish();
        byte[] buffer = new byte[outputStream.getBufferSize()];
        outputStream.read(buffer, 0, buffer.length);
        byte[] expected = new byte[buffer.length];
        for (int i = 0; i < buffer.length; i++) {
            expected[i] = 0;
        }
        assertArrayEquals(expected, buffer);
        outputStream.finish();
        assertArrayEquals(expected, ArchiveUtils.toByteArray(outputStream.getInputStream()));
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        byte[] buffer = new byte[10];
        int offset = 0;
        for (int i = 0; i < data.length; i++) {
            buffer[offset++] = data[i];
            if (offset >= buffer.length) {
                outputStream.write(buffer, 0, buffer.length);
                offset = 0;
                outputStream.flush();
            }
        }
        byte[] expected = new byte[data.length];
        System.arraycopy(data, 0, expected, 0, data.length);
        byte[] result = new byte[outputStream.getBytesWritten()];
        outputStream.read(result, 0, result.length);
        assertArrayEquals(expected, result);
        assertNull(outputStream.getCurrentEntry());
        assertNull(outputStream.getName());
        assertEquals(data.length, outputStream.getBytesWritten());

        outputStream.finish();
        assertNull(outputStream.getCurrentEntry());
        assertNull(outputStream.getName());
        assertNull(outputStream.getPreviousEntry());
        assertArrayEquals(expected, ArchiveUtils.toByteArray(outputStream.getInputStream()));
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        File file = new File("test.txt");
        ArchiveEntry entry = outputStream.createArchiveEntry(file, "test.txt");
        assertNotNull(entry);
        assertTrue(entry instanceof TarArchiveEntry);
        assertSame(file, ((TarArchiveEntry) entry).getFile());
        assertNull(outputStream.getName());
        assertEquals(file.length(), outputStream.getBytesWritten());

        outputStream.finish();
        byte[] expected = new byte[outputStream.getBytesWritten()];
        System.arraycopy(ArchiveUtils.toByteArray(outputStream.getInputStream()), 0, expected, 0, expected.length);
        assertArrayEquals(expected, ArchiveUtils.toByteArray(new File("test.txt").toPath().toFile()));
    }
}