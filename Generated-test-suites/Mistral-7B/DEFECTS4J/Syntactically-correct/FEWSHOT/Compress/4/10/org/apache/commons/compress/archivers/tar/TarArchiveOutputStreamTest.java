package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        final OutputStream os = new ByteArrayOutputStream(1024);
        final TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);

        assertEquals(TarConstants.MAGIC_TAR, tarOut.getMagic());
        assertEquals(TarConstants.LONGFILE_ERROR, tarOut.getLongFileMode());
        assertTrue(tarOut instanceof ArchiveOutputStream);
        assertTrue(tarOut instanceof TarArchiveOutputStream);
        assertFalse(tarOut.isClosed());
    }

    @Test
    public void testSetLongFileMode() {
        final OutputStream os = new ByteArrayOutputStream(1024);
        final TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);

        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOut.getLongFileMode());

        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOut.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        final OutputStream os = new ByteArrayOutputStream(1024);
        final TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);

        tarOut.finish();
        final byte[] header = tarOut.getHeader();
        assertArrayEquals(TarConstants.MAGIC_TAR, header, 0, TarConstants.HEADER_SIZE);
        assertTrue(tarOut.isClosed());
    }

    @Test
    public void testFinishWithOpenEntry() {
        final OutputStream os = new ByteArrayOutputStream(1024);
        final TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);

        tarOut.putArchiveEntry(new TarArchiveEntry("test.txt"));
        assertThrows(IOException.class, () -> tarOut.finish());
    }

    @Test
    public void testClose() throws IOException {
        final OutputStream os = new ByteArrayOutputStream(1024);
        final TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);

        tarOut.close();
        assertTrue(tarOut.isClosed());
    }

    @Test
    public void testCloseWithOpenEntry() throws IOException {
        final OutputStream os = new ByteArrayOutputStream(1024);
        final TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);

        tarOut.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOut.close();

        assertThrows(IOException.class, () -> tarOut.putArchiveEntry(new TarArchiveEntry("test.txt")));
    }

    @Test
    public void testCloseWithOpenEntryAndFinish() throws IOException {
        final OutputStream os = new ByteArrayOutputStream(1024);
        final TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);

        tarOut.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOut.finish();
        tarOut.close();

        assertThrows(IOException.class, () -> tarOut.putArchiveEntry(new TarArchiveEntry("test.txt")));
    }

    @Test
    public void testWriteShortFile() throws IOException {
        final OutputStream os = new ByteArrayOutputStream(1024);
        final TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        final byte[] data = "short file".getBytes(StandardCharsets.UTF_8);

        tarOut.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOut.write(data, 0, data.length);
        tarOut.closeArchiveEntry();

        final byte[] expected = new byte[TarConstants.HEADER_SIZE + data.length];
        System.arraycopy(TarConstants.MAGIC_TAR, 0, expected, 0, TarConstants.MAGIC_TAR.length);

        final long headerOffset = TarConstants.MAGIC_TAR.length;
        TarArchiveEntry.writeEntryHeader(data.length, TarConstants.FILE_TYPE_REGULAR, headerOffset + TarConstants.HEADER_SIZE, "test.txt".getBytes(StandardCharsets.UTF_8), expected);
        System.arraycopy(data, 0, expected, headerOffset + TarConstants.HEADER_SIZE, data.length);

        assertArrayEquals(expected, tarOut.getBuffer().getData(), 0, expected.length);
    }

    @Test
    public void testWriteLongFile() throws IOException {
        final OutputStream os = new ByteArrayOutputStream(1024 * 1024); // 1 MB
        final TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        final byte[] data = new byte[1024 * 1024]; // 1 MB

        tarOut.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOut.write(data, 0, data.length);
        tarOut.closeArchiveEntry();

        final byte[] expected = new byte[TarConstants.HEADER_SIZE + data.length];
        System.arraycopy(TarConstants.MAGIC_TAR, 0, expected, 0, TarConstants.MAGIC_TAR.length);

        final long headerOffset = TarConstants.MAGIC_TAR.length;
        TarArchiveEntry.writeEntryHeader(data.length, TarConstants.FILE_TYPE_REGULAR, headerOffset + TarConstants.HEADER_SIZE, "test.txt".getBytes(StandardCharsets.UTF_8), expected);
        System.arraycopy(data, 0, expected, headerOffset + TarConstants.HEADER_SIZE, data.length);

        assertArrayEquals(expected, tarOut.getBuffer().getData(), 0, expected.length);
    }

    @Test
    public void testWriteLongFileWithLongName() throws IOException {
        final OutputStream os = new ByteArrayOutputStream(1024 * 1024); // 1 MB
        final TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        final String fileName = "a/long/path/test.txt";
        final byte[] data = new byte[1024 * 1024]; // 1 MB

        tarOut.putArchiveEntry(new TarArchiveEntry(fileName));
        tarOut.write(data, 0, data.length);
        tarOut.closeArchiveEntry();

        final byte[] expected = new byte[TarConstants.HEADER_SIZE + data.length + TarConstants.LONGNAME_SIZE];
        final byte[] nameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        long nameOffset = 0;

        System.arraycopy(TarConstants.MAGIC_TAR, 0, expected, 0, TarConstants.MAGIC_TAR.length);

        final long headerOffset = TarConstants.MAGIC_TAR.length;
        TarArchiveEntry.writeEntryHeader(data.length, TarConstants.FILE_TYPE_REGULAR, headerOffset + TarConstants.HEADER_SIZE, nameBytes, expected);
        System.arraycopy(data, 0, expected, headerOffset + TarConstants.HEADER_SIZE, data.length);
        nameOffset += TarConstants.HEADER_SIZE + data.length;

        final int nameLength = nameBytes.length;
        final int namePadding = TarConstants.NAMELEN - nameLength;
        for (int i = 0; i < namePadding; ++i) {
            expected[nameOffset++] = (byte) 0;
        }

        assertArrayEquals(expected, tarOut.getBuffer().getData(), 0, expected.length);
    }

    @Test
    public void testWriteLongFileWithTruncatedName() throws IOException {
        final OutputStream os = new ByteArrayOutputStream(1024 * 1024); // 1 MB
        final TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        final String fileName = "a/long/path/test.txt";
        final byte[] data = new byte[1024 * 1024]; // 1 MB

        tarOut.putArchiveEntry(new TarArchiveEntry(fileName));
        tarOut.write(data, 0, data.length);
        tarOut.closeArchiveEntry();

        final byte[] expected = new byte[TarConstants.HEADER_SIZE + data.length];
        final byte[] nameBytes = fileName.substring(0, TarConstants.NAMELEN - 1).getBytes(StandardCharsets.UTF_8);

        System.arraycopy(TarConstants.MAGIC_TAR, 0, expected, 0, TarConstants.MAGIC_TAR.length);

        final long headerOffset = TarConstants.MAGIC_TAR.length;
        TarArchiveEntry.writeEntryHeader(data.length, TarConstants.FILE_TYPE_REGULAR, headerOffset + TarConstants.HEADER_SIZE, nameBytes, expected);
        System.arraycopy(data, 0, expected, headerOffset + TarConstants.HEADER_SIZE, data.length);

        assertArrayEquals(expected, tarOut.getBuffer().getData(), 0, expected.length);
    }

    @Test
    public void testWriteLongFileWithLongNameAndData() throws IOException {
        final OutputStream os = new ByteArrayOutputStream(1024 * 1024 * 2); // 2 MB
        final TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        final String fileName = "a/long/path/test.txt";
        final byte[] data1 = new byte[1024 * 1024]; // 1 MB
        final byte[] data2 = new byte[1024 * 1024]; // 1 MB

        tarOut.putArchiveEntry(new TarArchiveEntry(fileName));
        tarOut.write(data1, 0, data1.length);
        tarOut.write(data2, 0, data2.length);
        tarOut.closeArchiveEntry();

        final byte[] expected = new byte[TarConstants.HEADER_SIZE + data1.length + data2.length + TarConstants.LONGNAME_SIZE];
        final byte[] nameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        long nameOffset = 0;

        System.arraycopy(TarConstants.MAGIC_TAR, 0, expected, 0, TarConstants.MAGIC_TAR.length);

        final long headerOffset = TarConstants.MAGIC_TAR.length;
        TarArchiveEntry.writeEntryHeader(data1.length, TarConstants.FILE_TYPE_REGULAR, headerOffset + TarConstants.HEADER_SIZE, nameBytes, expected);
        System.arraycopy(data1, 0, expected, headerOffset + TarConstants.HEADER_SIZE, data1.length);
        nameOffset += TarConstants.HEADER_SIZE + data1.length;

        final int nameLength = nameBytes.length;
        final int namePadding = TarConstants.NAMELEN - nameLength;
        for (int i = 0; i < namePadding; ++i) {
            expected[nameOffset++] = (byte) 0;
        }

        System.arraycopy(TarConstants.MAGIC_TAR, 0, expected, headerOffset + nameOffset, TarConstants.MAGIC_TAR.length);

        final long dataOffset = nameOffset + TarConstants.MAGIC_TAR.length;
        TarArchiveEntry.writeEntryHeader(data2.length, TarConstants.FILE_TYPE_REGULAR, dataOffset + headerOffset, nameBytes, expected);
        System.arraycopy(data2, 0, expected, dataOffset, data2.length);

        assertArrayEquals(expected, tarOut.getBuffer().getData(), 0, expected.length);
    }
}