package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveEntry;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testReadLFHSignature() throws IOException {
        byte[] signature = new byte[]{0x04, 0x03, 0x4b, 0x50, 0x02, 0x02, 0x08, 0x07, 0xeb, 0xca, 0xfe, 0xff, 0xff, 0xff, 0xff};
        ZipArchiveInputStream zis = new ZipArchiveInputStream(new ByteArrayInputStream(signature));
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
        assertEquals(ZipLong.LFH_SIG, zis.readSignature());
    }

    @Test
    public void testReadEOCDSignature() throws IOException {
        byte[] signature = new byte[]{0x04, 0x04, 0x4d, 0x54, 0x50, 0x20, 0x14, 0x00, 0x00};
        ZipArchiveInputStream zis = new ZipArchiveInputStream(new ByteArrayInputStream(signature));
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
        assertEquals(ZipLong.EOCD_SIG, zis.readSignature());
    }

    @Test
    public void testGetNextZipEntry_emptyZipFile() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream);
        assertNull(zis.getNextZipEntry());
    }

    @Test
    public void testGetNextZipEntry_validEntry() throws IOException {
        byte[] header = new byte[ZipArchiveInputStream.LFH_LEN];
        ZipShort.putValue(header, 0, (short) 1);
        ZipShort.putValue(header, 2, ZipShort.valueOf(ZipArchiveOutputStream.DEFLATED));
        ZipLong.putValue(header, 4, 1231231231231L);
        ZipLong.putValue(header, 14, 111111111111L);
        ZipLong.putValue(header, 22, 1231231231231L);
        ZipShort.putValue(header, 26, (short) 7);
        ZipShort.putValue(header, 28, (short) 7);
        byte[] fileName = "test.txt".getBytes();
        int fileNameLength = fileName.length;
        byte[] extraField = new byte[2];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(fileName);
        byteArrayOutputStream.write(extraField);
        byte[] data = new byte[10];
        byteArrayOutputStream.write(data);
        byte[] entryBytes = new byte[fileNameLength + 2 + ZipArchiveInputStream.LFH_LEN + extraField.length + data.length];
        System.arraycopy(header, 0, entryBytes, 0, ZipArchiveInputStream.LFH_LEN);
        System.arraycopy(fileName, 0, entryBytes, fileNameLength + 2, fileNameLength);
        System.arraycopy(extraField, 0, entryBytes, fileNameLength + 2 + extraField.length, extraField.length);
        System.arraycopy(data, 0, entryBytes, fileNameLength + 2 + ZipArchiveInputStream.LFH_LEN, data.length);
        InputStream inputStream = new ByteArrayInputStream(entryBytes);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry entry = zis.getNextZipEntry();
        assertEquals("test.txt", entry.getName());
        assertTrue(entry instanceof ZipArchiveEntry);
    }

    @Test
    public void testGetNextZipEntry_centralDirectory() throws IOException {
        byte[] header = new byte[ZipArchiveInputStream.LFH_LEN];
        ZipShort.putValue(header, 0, (short) 1);
        ZipShort.putValue(header, 2, ZipShort.valueOf(ZipArchiveOutputStream.STORED));
        ZipLong.putValue(header, 4, 1231231231231L);
        ZipLong.putValue(header, 14, 111111111111L);
        ZipLong.putValue(header, 22, 1231231231231L);
        ZipShort.putValue(header, 26, (short) 7);
        ZipShort.putValue(header, 28, (short) 7);
        byte[] fileName = "test.txt".getBytes();
        int fileNameLength = fileName.length;
        byte[] extraField = new byte[2];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(fileName);
        byteArrayOutputStream.write(extraField);
        byte[] data = new byte[10];
        byteArrayOutputStream.write(data);
        byte[] entryBytes = new byte[fileNameLength + 2 + ZipArchiveInputStream.LFH_LEN + extraField.length + data.length];
        System.arraycopy(header, 0, entryBytes, 0, ZipArchiveInputStream.LFH_LEN);
        System.arraycopy(fileName, 0, entryBytes, fileNameLength + 2, fileNameLength);
        System.arraycopy(extraField, 0, entryBytes, fileNameLength + 2 + extraField.length, extraField.length);
        System.arraycopy(data, 0, entryBytes, fileNameLength + 2 + ZipArchiveInputStream.LFH_LEN, data.length);
        InputStream inputStream = new ByteArrayInputStream(entryBytes);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry entry = zis.getNextZipEntry();
        assertEquals("test.txt", entry.getName());
        assertSame(entry, zis.getNextZipEntry()); // Central directory entry should be the same as the previous one.
        assertEquals(0, zis.getNextZipEntry().getSize()); // End of central directory record.
    }

    @Test(expected = EOFException.class)
    public void testGetNextZipEntry_EOF() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream);
        for (int i = 0; i < 10; i++) {
            zis.getNextZipEntry();
        }
        zis.getNextZipEntry(); // Should throw EOFException.
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = new byte[10];
        byte[] buffer = new byte[20];
        InputStream inputStream = new ByteArrayInputStream(data);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream);
        ArchiveEntry entry = zis.getNextZipEntry();
        assertEquals(ZipArchiveOutputStream.DEFLATED, entry.getMethod());
        int read = zis.read(buffer, 0, buffer.length);
        assertEquals(data.length, read);
        assertArrayEquals(data, buffer);
        zis.close();
    }

    @Test
    public void testReadWithClosedStream() throws IOException {
        byte[] data = new byte[10];
        byte[] buffer = new byte[20];
        InputStream inputStream = new ByteArrayInputStream(data);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream);
        zis.close();
        ArchiveEntry entry = zis.getNextZipEntry();
        int read = zis.read(buffer, 0, buffer.length);
        assertEquals(-1, read);
    }

    @Test
    public void testReadWithEOF() throws IOException {
        byte[] data = new byte[10];
        byte[] buffer = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(data);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream);
        ArchiveEntry entry = zis.getNextZipEntry();
        int read = zis.read(buffer, 0, buffer.length);
        assertEquals(data.length, read);
        assertEquals(data, buffer);
        read = zis.read(buffer, 0, buffer.length);
        assertEquals(-1, read);
        zis.close();
    }

    @Test
    public void testCloseEntry() throws IOException {
        byte[] data = new byte[10];
        byte[] buffer = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(data);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream);
        ArchiveEntry entry = zis.getNextZipEntry();
        int read = zis.read(buffer, 0, buffer.length);
        assertEquals(data.length, read);
        assertEquals(data, buffer);
        zis.closeEntry();
        read = zis.read(buffer, 0, buffer.length);
        assertEquals(-1, read);
        zis.close();
    }

    @Test
    public void testCloseEntryWithEOF() throws IOException {
        byte[] data = new byte[10];
        byte[] buffer = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(data);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream);
        ArchiveEntry entry = zis.getNextZipEntry();
        int read = zis.read(buffer, 0, buffer.length);
        assertEquals(data.length, read);
        assertEquals(data, buffer);
        zis.closeEntry();
        read = zis.read(buffer, 0, buffer.length);
        assertEquals(-1, read);
        zis.close();
    }

    @Test
    public void testFill() throws IOException {
        byte[] buffer = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream);
        zis.fill();
        int read = zis.read(buffer, 0, buffer.length);
        assertEquals(-1, read);
    }

    @Test
    public void testFillWithData() throws IOException {
        byte[] data = new byte[10];
        byte[] buffer = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(data);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream);
        zis.fill();
        int read = zis.read(buffer, 0, buffer.length);
        assertEquals(data.length, read);
        assertEquals(data, buffer);
    }

    @Test(expected = ZipException.class)
    public void testReadWithInvalidSignature() throws IOException {
        byte[] data = new byte[10];
        byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length + data.length];
        System.arraycopy(ZipArchiveOutputStream.LFH_SIG, 0, signature, 0, ZipArchiveOutputStream.LFH_SIG.length);
        System.arraycopy(data, 0, signature, ZipArchiveOutputStream.LFH_SIG.length, data.length);
        InputStream inputStream = new ByteArrayInputStream(signature);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream);
        ArchiveEntry entry = zis.getNextZipEntry();
    }
}