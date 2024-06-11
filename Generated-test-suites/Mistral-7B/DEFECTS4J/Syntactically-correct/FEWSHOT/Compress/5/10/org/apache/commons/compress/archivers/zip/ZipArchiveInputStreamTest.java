package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ZipArchiveInputStreamTest {

    private InputStream inputStream;
    private ZipArchiveInputStream archiveInputStream;
    private byte[] testData;

    @Before
    public void setup() throws IOException {
        byte[] lfh = new byte[30];
        System.arraycopy(ZipLong.LFH_SIG.bytes, 0, lfh, 0, ZipLong.LFH_SIG.length);

        testData = new byte[] {
            0x00, 0x02, // version made by
            0, (byte) 0x12, 0, (byte) 0x34, // general purpose bit flag
            0x08, 0x08, // compression method
            0, (byte) 0x56, 0, (byte) 0x78, // last mod file time
            0, (byte) 0x42, 0, (byte) 0x60, // last mod file date
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // crc-32
            0x04, 0x05, 0x06, 0x07, // compressed size
            0x08, 0x09, 0x0a, 0x0b, // uncompressed size
            0x0c, 0x0d, // file name length
            0x0e // extra field length
        };
        System.arraycopy(testData, 0, lfh, 22, testData.length);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipUtil.storeHeader(bos, lfh, "test.txt".getBytes("UTF-8"), (byte) 0,
                new byte[0], null, null, 0, false);
        byte[] header = bos.toByteArray();

        inputStream = new ByteArrayInputStream(header);
        archiveInputStream = new ZipArchiveInputStream(inputStream);
        archiveInputStream.getNextZipEntry();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ArchiveEntry entry = archiveInputStream.getNextZipEntry();
        assertSame(archiveInputStream.getClass(), entry.getClass());
        assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testGetNextZipEntry_null() throws IOException {
        ArchiveEntry entry = archiveInputStream.getNextZipEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextZipEntry_closed() throws IOException {
        archiveInputStream.close();
        ArchiveEntry entry = archiveInputStream.getNextZipEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextZipEntry_hitCentralDirectory() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] cd = new byte[] {
            (byte) 0x50, 0x4b, 0x35, 0x06, // central directory signature
            (byte) 0x01, 0x02, // version made by
            // central directory records
        };
        bos.write(cd, 0, cd.length);
        inputStream = new ByteArrayInputStream(bos.toByteArray());
        archiveInputStream = new ZipArchiveInputStream(inputStream);
        archiveInputStream.getNextZipEntry();
        archiveInputStream.getNextZipEntry();
        ArchiveEntry entry = archiveInputStream.getNextZipEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextZipEntry_hasDataDescriptor() throws IOException {
        byte[] lfh = new byte[30];
        System.arraycopy(ZipLong.LFH_SIG.bytes, 0, lfh, 0, ZipLong.LFH_SIG.length);

        testData = new byte[] {
            0x00, 0x02, // version made by
            0x08, 0x08, // general purpose bit flag
            0x08, 0x08, // compression method
            0, (byte) 0x56, 0, (byte) 0x78, // last mod file time
            0, (byte) 0x42, 0, (byte) 0x60, // last mod file date
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // crc-32
            0x04, 0x05, 0x06, 0x07, // compressed size
            0x08, 0x09, 0x0a, 0x0b, // uncompressed size
            0x0c, 0x0d, // file name length
            0x0e // extra field length
        };
        System.arraycopy(testData, 0, lfh, 22, testData.length);
        System.arraycopy(new byte[]{(byte) 0x54, (byte) 0x58, (byte) 0x4d, (byte) 0x53,
                (byte) 0x53, (byte) 0x42, (byte) 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00}, 22, lfh, 22); // local file header extra field

        testData = new byte[] {
            0x00, 0x01, // central directory record signature
            0x0c, 0x0d, // file name length
            0x0e, // extra field length
            0x00, 0x00, 0x00, 0x00, // disk number start
            0x00, 0x00, // internal file attributes
            0x00, 0x00, // external file attributes
            0, (byte) 0x56, 0, (byte) 0x78, // local header time
            0, (byte) 0x42, 0, (byte) 0x60, // local header date
            (byte) 0x08, // compression method
            (byte) 0x00, // wName Length
            (byte) 0x00 // wExtraFieldLength
        };
        System.arraycopy(testData, 0, lfh, 26, testData.length);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(lfh, 0, lfh.length);

        inputStream = new ByteArrayInputStream(lfh);
        archiveInputStream = new ZipArchiveInputStream(inputStream);
        archiveInputStream.getNextZipEntry();
        ArchiveEntry entry = archiveInputStream.getNextZipEntry();
        assertEquals("test.txt", entry.getName());
        assertTrue(entry.getExtra().length > 0);
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_nullByteArrayInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(null);
        ZipArchiveInputStream archiveInputStream = new ZipArchiveInputStream(inputStream);
        archiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_emptyByteArrayInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream archiveInputStream = new ZipArchiveInputStream(inputStream);
        archiveInputStream.getNextZipEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[5];
        int read = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(30, read);

        read = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, read);

        read = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, read);

        archiveInputStream.close();
    }

    @Test
    public void testRead_validData() throws IOException {
        byte[] buffer = new byte[5];
        int read = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(30, read);

        assertArrayEquals(ZipLong.LFH_SIG.bytes, buffer, 0, ZipLong.LFH_SIG.length);

        archiveInputStream.getNextZipEntry();

        read = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, read);

        archiveInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = archiveInputStream.skip(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, skipped);

        // Test with a smaller value
        skipped = archiveInputStream.skip(10);
        assertEquals(10, skipped);

        archiveInputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkip_negativeValue() throws IOException {
        archiveInputStream.skip(-1);
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        archiveInputStream.close();
        ArchiveEntry entry = archiveInputStream.getNextZipEntry();
        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testClose_alreadyClosed() throws IOException {
        archiveInputStream.close();
        archiveInputStream.close();
    }

    @Test
    public void testCloseEntry() throws IOException {
        archiveInputStream.getNextZipEntry();
        archiveInputStream.closeEntry();
        ArchiveEntry entry = archiveInputStream.getNextZipEntry();
        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testCloseEntry_closed() throws IOException {
        archiveInputStream.close();
        archiveInputStream.closeEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseEntry_null() throws IOException {
        archiveInputStream.closeEntry();
    }

    @Test
    public void testFill() throws IOException {
        archiveInputStream.fill();
        assertFalse(inf.needsInput());
    }

    @Test
    public void testFill_needsInput() throws IOException {
        archiveInputStream.fill();
        inf.setInput(new byte[0]);
        assertTrue(inf.needsInput());
    }

    @Test
    public void testMatchesSignature() throws IOException {
        byte[] signature = new byte[ZipLong.LFH_SIG.length];
        assertFalse(ZipArchiveInputStream.matches(signature, 0));

        System.arraycopy(ZipLong.LFH_SIG.bytes, 0, signature, 0,
                ZipLong.LFH_SIG.length);
        assertTrue(ZipArchiveInputStream.matches(signature, ZipLong.LFH_SIG.length));

        System.arraycopy(ZipLong.EOCD_SIG.bytes, 0, signature, 0,
                ZipLong.EOCD_SIG.length);
        assertTrue(ZipArchiveInputStream.matches(signature, ZipLong.EOCD_SIG.length));
    }
}