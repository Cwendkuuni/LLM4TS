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
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.IOUtils;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinish() throws IOException {
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(recordSize * 2);
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os, recordSize, recordSize);
        tarOS.finish();
        byte[] eofBytes = new byte[recordSize];
        Arrays.fill(eofBytes, (byte) 0);
        byte[] eofBytes2 = new byte[recordSize];
        Arrays.fill(eofBytes2, (byte) 0);
        assertArrayEquals(eofBytes, tarOS.getBuffer().getBuffer(), 0, eofBytes.length);
        assertArrayEquals(eofBytes2, tarOS.getBuffer().getBuffer(), eofBytes.length, eofBytes2.length);
    }

    @Test
    public void testClose() throws IOException {
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(recordSize * 2);
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os, recordSize, recordSize);
        tarOS.close();
        byte[] eofBytes = new byte[recordSize * 2];
        assertTrue(os.available() >= eofBytes.length);
        os.read(eofBytes);
        byte[] eofBytes2 = new byte[recordSize * 2];
        Arrays.fill(eofBytes2, (byte) 0);
        assertArrayEquals(eofBytes, eofBytes2);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new ByteArrayOutputStream(1), TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOS.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidValues() {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new ByteArrayOutputStream(1), TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOS.setLongFileMode(4);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(recordSize * 2);
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os, recordSize, recordSize);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
        tarOS.putArchiveEntry(entry);

        byte[] headerBytes = new byte[recordSize];
        byte[] nameBytes = entry.getName().getBytes();
        entry.writeHeader(headerBytes);

        assertArrayEquals(headerBytes, tarOS.getBuffer().getBuffer(), 0, headerBytes.length);

        byte[] nameBytes2 = new byte[TarConstants.NAMELEN + 1];
        System.arraycopy(nameBytes, 0, nameBytes2, 0, nameBytes.length);
        assertEquals(nameBytes2[nameBytes2.length - 1], (byte) 0);

        assertArrayEquals(nameBytes2, tarOS.getBuffer().getBuffer(), headerBytes.length, nameBytes2.length);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(recordSize * 2);
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os, recordSize, recordSize);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
        tarOS.putArchiveEntry(entry);

        byte[] headerBytes = new byte[recordSize];
        byte[] nameBytes = entry.getName().getBytes();
        entry.writeHeader(headerBytes);
        assertArrayEquals(headerBytes, tarOS.getBuffer().getBuffer(), 0, headerBytes.length);

        tarOS.write(new byte[]{"test content".getBytes()});
        tarOS.closeArchiveEntry();

        byte[] recordBytes = new byte[recordSize];
        tarOS.getBuffer().read(recordBytes);
        byte[] expected = new byte[recordSize];
        System.arraycopy(headerBytes, 0, expected, 0, headerBytes.length);
        System.arraycopy(entry.getContent().getBytes(), 0, expected, headerBytes.length, entry.getContent().length);
        assertArrayEquals(expected, recordBytes, 0, expected.length);
    }

    @Test
    public void testWrite() throws IOException {
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(recordSize * 2);
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os, recordSize, recordSize);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
        tarOS.putArchiveEntry(entry);

        byte[] headerBytes = new byte[recordSize];
        byte[] nameBytes = entry.getName().getBytes();
        entry.writeHeader(headerBytes);
        assertArrayEquals(headerBytes, tarOS.getBuffer().getBuffer(), 0, headerBytes.length);

        byte[] content = "test content".getBytes();
        tarOS.write(content);
        tarOS.closeArchiveEntry();

        byte[] expected = new byte[recordSize * 2];
        System.arraycopy(headerBytes, 0, expected, 0, headerBytes.length);
        System.arraycopy(content, 0, expected, headerBytes.length, content.length);

        byte[] recordBytes = new byte[recordSize];
        tarOS.getBuffer().read(recordBytes);
        assertArrayEquals(expected, recordBytes, 0, expected.length);
    }

    @Test
    public void testWritePartialContent() throws IOException {
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(recordSize * 2);
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os, recordSize, recordSize);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
        tarOS.putArchiveEntry(entry);

        byte[] headerBytes = new byte[recordSize];
        byte[] nameBytes = entry.getName().getBytes();
        entry.writeHeader(headerBytes);
        assertArrayEquals(headerBytes, tarOS.getBuffer().getBuffer(), 0, headerBytes.length);

        byte[] content = "test content".getBytes();
        tarOS.write(content, 0, 5);
        tarOS.closeArchiveEntry();

        byte[] expected = new byte[recordSize];
        System.arraycopy(headerBytes, 0, expected, 0, headerBytes.length);
        System.arraycopy(content, 0, expected, headerBytes.length, 5);

        byte[] recordBytes = new byte[recordSize];
        tarOS.getBuffer().read(recordBytes);
        assertArrayEquals(expected, recordBytes, 0, expected.length);
    }

    @Test
    public void testWriteLargeContent() throws IOException {
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(recordSize * 2);
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os, recordSize, recordSize);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt", new ByteArrayInputStream("test content of size 10000".getBytes()).readAllBytes());
        tarOS.putArchiveEntry(entry);

        byte[] headerBytes = new byte[recordSize];
        byte[] nameBytes = entry.getName().getBytes();
        entry.writeHeader(headerBytes);
        assertArrayEquals(headerBytes, tarOS.getBuffer().getBuffer(), 0, headerBytes.length);

        byte[] content = new byte[10000];
        tarOS.write(content);
        tarOS.closeArchiveEntry();

        byte[] expected = new byte[recordSize * 2];
        System.arraycopy(headerBytes, 0, expected, 0, headerBytes.length);
        System.arraycopy(content, 0, expected, headerBytes.length, content.length);

        byte[] recordBytes = new byte[recordSize];
        tarOS.getBuffer().read(recordBytes);
        assertArrayEquals(expected, recordBytes, 0, expected.length);
    }

    @Test
    public void testCloseWithoutCloseArchiveEntry() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(outputStream, 512, 512);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOS.putArchiveEntry(entry);
        tarOS.close();
        assertNull(entry.isClosed());
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(outputStream, 512, 512);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOS.putArchiveEntry(entry);
        tarOS.close();
        tarOS.write(new byte[]{"test content"[0]});
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("test.txt");
        file.createNewFile();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new ByteArrayOutputStream(512), 512, 512);
        ArchiveEntry entry = tarOS.createArchiveEntry(file, "test.txt");
        assertEquals(TarArchiveEntry.class, entry.getClass());
        tarOS.putArchiveEntry(entry);
        byte[] headerBytes = new byte[512];
        entry.writeHeader(headerBytes);
        assertArrayEquals(headerBytes, tarOS.getBuffer().getBuffer(), 0, headerBytes.length);
        tarOS.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryNullFile() throws IOException {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new ByteArrayOutputStream(512), 512, 512);
        assertFalse(tarOS.createArchiveEntry(null, "test.txt") instanceof TarArchiveEntry);
    }
}