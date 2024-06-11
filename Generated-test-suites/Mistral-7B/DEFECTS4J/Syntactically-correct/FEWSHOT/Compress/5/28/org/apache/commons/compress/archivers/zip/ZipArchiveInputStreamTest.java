package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.zip.CRC32;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private static final byte[] LFH_SIG = new byte[] { 0x04, 0x03, 0x4b, 0x50 };
    private static final byte[] EOCD_SIG = new byte[] { 0x06, 0x05, 0x4b, 0x50, 0x02, 0x00 };

    private static final int BUFFER_SIZE = 1024;
    private static final int LFH_LEN = 30;
    private static final int WORD = 4;
    private static final int SHORT = 2;

    private byte[] testData;
    private ByteArrayOutputStream out;
    private ByteArrayInputStream input;
    private ZipArchiveInputStream zipIn;
    private ZipArchiveEntry zipEntry;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        byte[] header = new byte[LFH_LEN];
        ByteBuffer headerBB = ByteBuffer.wrap(header);
        headerBB.putInt(ZipLong.CFH_SIG.toIntValue());
        headerBB.position(0);
        out.write(header);
        out.write("test.txt\0\0\0\0\0\0".getBytes()); // name
        out.write("test comment\0\0\0\0\0\0".getBytes()); // comment
        out.writeInt(ZipEncodingHelper.UTF8.getBytes("test comment").length);
        out.writeInt(0); // crc
        out.writeInt(-1); // compressed size
        out.writeInt(-1); // uncompressed size
        out.writeShort((short) 8); // extra field length
        out.writeShort((short) 0); // flag
        out.writeShort(32000); // time
        out.writeShort(32000); // date

        testData = out.toByteArray();
        out.reset();

        out.write(LFH_SIG);
        out.writeShort((short) 0x0e); // version made by
        out.writeShort((short) 0x01); // version needed
        out.writeShort((short) 0); // general purpose bit flag
        out.writeShort((short) 0); // compression method
        out.writeShort((short) 2); // last mod file time
        out.writeShort((short) 2); // last mod file date
        out.writeInt(0x12345678L); // crc
        out.writeInt(20); // compressed size
        out.writeInt(30); // uncompressed size
        out.writeShort((short) 20); // file name length
        out.writeShort((short) 0); // extra field length

        out.write("test.txt\0\0\0\0\0\0".getBytes()); // name
        out.writeInt(ZipEncodingHelper.UTF8.getBytes("test.txt").length);
        out.writeInt(0); // crc
        out.writeInt(30); // compressed size
        out.writeInt(30); // uncompressed size

        out.write("test2.txt\0\0\0\0\0\0".getBytes()); // name
        out.writeInt(ZipEncodingHelper.UTF8.getBytes("test2.txt").length);
        out.writeInt(40); // crc
        out.writeInt(40); // compressed size
        out.writeInt(40); // uncompressed size

        out.writeLong(0x1abcdef1L); // central directory offset
        out.writeShort((short) 2); // number of entries
        out.writeShort((short) 0); // number of extra fields
        out.writeShort((short) 0); // signature

        out.write(EOCD_SIG);
        out.flush();
        testData = out.toByteArray();

        input = new ByteArrayInputStream(testData);
        zipIn = new ZipArchiveInputStream(input);
    }

    @After
    public void tearDown() throws Exception {
        if (zipIn != null) {
            zipIn.close();
        }
        if (input != null) {
            input.close();
        }
        if (out != null) {
            out.close();
        }
    }

    @Test
    public void testMatches() throws IOException {
        assertTrue(ZipArchiveInputStream.matches(testData, testData.length));
        assertTrue(ZipArchiveInputStream.matches(testData, testData.length
                + BUFFER_SIZE));
        assertFalse(ZipArchiveInputStream.matches(testData, testData.length - LFH_SIG.length));
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = zipIn.getNextZipEntry();
        assertEquals("test.txt", entry.getName());
        assertEquals("test comment", entry.getComment());
        assertNull(entry.getExtra());
        assertEquals(ZipShort.getValue(testData, 12), entry.getPlatform());
        assertEquals(ZipShort.getValue(testData, 14), entry.getMethod());
        assertEquals(32000, entry.getTime());
        assertEquals(ZipShort.getValue(testData, 20), entry.getSize());
        assertEquals(ZipShort.getValue(testData, 22), entry.getCompressedSize());

        entry = zipIn.getNextZipEntry();
        assertEquals("test2.txt", entry.getName());
        assertEquals("", entry.getComment());
        assertNull(entry.getExtra());
        assertEquals(ZipShort.getValue(testData, 32), entry.getPlatform());
        assertEquals(ZipShort.getValue(testData, 34), entry.getMethod());
        assertEquals(32001, entry.getTime());
        assertEquals(40, entry.getSize());
        assertEquals(40, entry.getCompressedSize());

        zipEntry = zipIn.getNextZipEntry();
        assertNull(zipEntry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = zipIn.getNextEntry();
        assertEquals("test.txt", entry.getName());
        assertEquals("test comment", entry.getComment());
        assertNull(entry.getExtra());
        assertEquals(ZipShort.getValue(testData, 12), entry.getPlatform());
        assertEquals(ZipShort.getValue(testData, 14), entry.getMethod());
        assertEquals(32000, entry.getTime());
        assertEquals(ZipShort.getValue(testData, 20), entry.getSize());
        assertEquals(ZipShort.getValue(testData, 22), entry.getCompressedSize());

        entry = zipIn.getNextEntry();
        assertEquals("test2.txt", entry.getName());
        assertEquals("", entry.getComment());
        assertNull(entry.getExtra());
        assertEquals(ZipShort.getValue(testData, 32), entry.getPlatform());
        assertEquals(ZipShort.getValue(testData, 34), entry.getMethod());
        assertEquals(32001, entry.getTime());
        assertEquals(ZipShort.getValue(testData, 38), entry.getSize());
        assertEquals(ZipShort.getValue(testData, 40), entry.getCompressedSize());

        ZipArchiveEntry entry2 = (ZipArchiveEntry) zipIn.getNextZipEntry();
        assertNull(entry2);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        zipIn.getNextZipEntry();
        int len = zipIn.read(buffer);
        assertEquals(20, len);
        assertEquals("test.txt", new String(buffer, 0, len));
        assertEquals(ZipShort.getValue(testData, 22), zipIn.getSize());

        zipIn.getNextZipEntry();
        len = zipIn.read(buffer);
        assertEquals(40, len);
        assertEquals("test2.txt", new String(buffer, 0, len));
        assertEquals(ZipShort.getValue(testData, 38), zipIn.getSize());

        zipIn.getNextZipEntry();
        len = zipIn.read(buffer);
        assertEquals(-1, len);
        assertEquals(0, zipIn.available());
        assertFalse(zipIn.hasNextEntry());
    }

    @Test
    public void testSkip() throws IOException {
        zipIn.getNextZipEntry();
        skip(zipIn, 13);
        assertEquals(13, zipIn.available());
        assertFalse(zipIn.hasNextEntry());

        zipIn.getNextZipEntry();
        skip(zipIn, 15);
        assertEquals(15, zipIn.available());
        assertFalse(zipIn.hasNextEntry());

        zipIn.getNextZipEntry();
        skip(zipIn, 1000);
        assertEquals(0, zipIn.available());
        assertFalse(zipIn.hasNextEntry());
    }

    @Test
    public void testCloseEntry() throws IOException {
        zipIn.getNextZipEntry();
        zipIn.closeEntry();
        zipIn.getNextZipEntry();
        zipIn.closeEntry();
        zipIn.getNextZipEntry();
        zipIn.closeEntry();
        assertFalse(zipIn.hasNextEntry());
    }

    @Test
    public void testFill() throws IOException {
        zipIn.getNextZipEntry();
        zipIn.fill();
        assertEquals(1, zipIn.getSize());
        assertEquals(1, zipIn.available());
        assertFalse(zipIn.hasNextEntry());
    }

    @Test
    public void testReadFully() throws IOException {
        zipIn.getNextZipEntry();
        byte[] buffer = new byte[20];
        zipIn.readFully(buffer);
        assertEquals(20, zipIn.getSize());
        assertEquals(0, zipIn.available());
        assertFalse(zipIn.hasNextEntry());
        assertArrayEquals(new byte[] { 0x04, 0x03, 0x4b, 0x50, 0x12, 0x34, 0x56, 0x78, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, buffer);
    }

    @Test
    public void testClose() throws IOException {
        zipIn.close();
        assertFalse(zipIn.hasNextEntry());
        assertFalse(zipIn.isOpen());
    }

    private void skip(ZipArchiveInputStream zipIn, int length) throws IOException {
        zipIn.skip(length);
    }
}