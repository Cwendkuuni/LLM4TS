package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.Test;

public class DumpArchiveInputStreamTest {

    @Test
    public void testMatches() {
        byte[] data = new byte[DumpArchiveConstants.TP_SIZE];
        // valid dump archive
        System.arraycopy(DumpArchiveConstants.MAGIC.getBytes(), 0, data, 0, DumpArchiveConstants.MAGIC.length);
        assertTrue(DumpArchiveInputStream.matches(data, DumpArchiveConstants.TP_SIZE));
        // invalid magic
        data[24] = (byte) (DumpArchiveConstants.NFS_MAGIC & 0xFF);
        data[25] = (byte) ((DumpArchiveConstants.NFS_MAGIC >> 8) & 0xFF);
        data[26] = (byte) ((DumpArchiveConstants.NFS_MAGIC >> 16) & 0xFF);
        data[27] = (byte) ((DumpArchiveConstants.NFS_MAGIC >> 24) & 0xFF);
        assertFalse(DumpArchiveInputStream.matches(data, DumpArchiveConstants.TP_SIZE));
        // invalid size
        data[28] = (byte) 0;
        assertFalse(DumpArchiveInputStream.matches(data, DumpArchiveConstants.TP_SIZE));
    }

    @Test
    public void testReadCLRI() throws IOException {
        InputStream input = new ByteArrayInputStream(DumpArchiveConstants.CLRI_HEADER.getBytes());
        DumpArchiveInputStream stream = new DumpArchiveInputStream(input, null);
        DumpArchiveEntry entry = stream.getNextEntry();
        assertEquals(DumpArchiveConstants.SEGMENT_TYPE.CLRI, entry.getHeaderType());
    }

    @Test
    public void testReadBITS() throws IOException {
        InputStream input = new ByteArrayInputStream(DumpArchiveConstants.BITS_HEADER.getBytes());
        DumpArchiveInputStream stream = new DumpArchiveInputStream(input, null);
        DumpArchiveEntry entry = stream.getNextEntry();
        assertEquals(DumpArchiveConstants.SEGMENT_TYPE.BITS, entry.getHeaderType());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        InputStream input = new ByteArrayInputStream(DumpArchiveConstants.DUMP_ARCHIVE.getBytes());
        DumpArchiveInputStream stream = new DumpArchiveInputStream(input, null);

        DumpArchiveEntry entry = stream.getNextEntry();
        assertEquals(1, entry.getHeaderType());
        assertSame(entry, stream.getNextEntry());
        assertNull(stream.getNextEntry());

        entry = stream.getNextEntry();
        assertEquals(2, entry.getHeaderType());
        assertSame(entry, stream.getNextEntry());
        assertNull(stream.getNextEntry());

        entry = stream.getNextEntry();
        assertEquals(3, entry.getHeaderType());
        assertSame(entry, stream.getNextEntry());
        assertNull(stream.getNextEntry());
    }

    @Test
    public void testReadDirectoryEntry() throws IOException {
        InputStream input = new ByteArrayInputStream(DumpArchiveConstants.DUMP_ARCHIVE.getBytes());
        DumpArchiveInputStream stream = new DumpArchiveInputStream(input, null);
        DumpArchiveEntry entry = null;

        while ((entry = stream.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                readDirectoryEntry(stream, entry);
                break;
            }
        }

        assertEquals(1, entry.getHeaderType());
        assertEquals(".", entry.getName());
        assertEquals(".", entry.getSimpleName());
        assertEquals(0L, entry.getOffset());

        entry = stream.getNextEntry();
        assertEquals(2, entry.getHeaderType());
        assertEquals("..", entry.getName());
        assertEquals("..", entry.getSimpleName());
        assertEquals(0L, entry.getOffset());
    }

    private void readDirectoryEntry(DumpArchiveInputStream stream, DumpArchiveEntry entry)
        throws IOException {
        int size = entry.getEntrySize();
        StringBuilder sb = new StringBuilder();

        while (size > 0) {
            int len = Math.min(size, readBuf.length - recordOffset);
            int read = stream.read(readBuf, 0, len);

            if (read < 0) {
                throw new IOException("Unexpected EOF");
            }

            size -= read;
            recordOffset += read;

            byte[] nameBytes = new byte[readBuf.length - recordOffset];
            System.arraycopy(readBuf, recordOffset, nameBytes, 0, nameBytes.length);
            String name = new String(nameBytes, ZipEncoding.UTF_8);
            sb.append(name).append("/");
            DumpArchiveEntry parentEntry = stream.getNextEntry();
            int parentIno = parentEntry != null ? parentEntry.getIno() : 0;
            sb.append(parentIno).append(" ");
        }

        assertEquals("/1 dir\n", sb.toString());
    }

    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream(DumpArchiveConstants.DATA.getBytes());
        DumpArchiveInputStream stream = new DumpArchiveInputStream(input, null);
        byte[] buffer = new byte[10];

        int totalRead = 0;
        while (totalRead < buffer.length) {
            int read = stream.read(buffer, totalRead, buffer.length - totalRead);
            totalRead += read;
        }

        assertArrayEquals(new byte[] { 0x61, 0x62, 0x63, 0x64, 0x65 }, buffer);
    }

    @Test
    public void testClose() throws IOException {
        InputStream input = new ByteArrayInputStream(DumpArchiveConstants.DUMP_ARCHIVE.getBytes());
        DumpArchiveInputStream stream = new DumpArchiveInputStream(input, null);
        stream.close();

        try {
            stream.getNextEntry();
            fail();
        } catch (IOException e) {
            // expected
        }
    }
}