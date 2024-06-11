package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    @Test
    public void testMatchesSignature() {
        byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length];
        System.arraycopy(ZipArchiveOutputStream.LFH_SIG.bytes, 0, signature, 0, ZipArchiveOutputStream.LFH_SIG.length);
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));

        signature[0] = 0x01;
        assertFalse(ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testGetNextZipEntry_EmptyFile() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(ZipArchiveOutputStream.EOCD_SIG.bytes);
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextZipEntry_SimpleFile() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(ZipArchiveOutputStream.LFH_SIG.bytes);
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(0x08); // general purpose bit flag
        baos.write(ZipShort.getBytes((short) 8)); // compression method
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(ZipLong.getBytes(100L));
        baos.write(ZipLong.getBytes(200L));
        baos.write(ZipLong.getBytes(1000L));
        baos.write(ZipShort.getBytes((short) 12));
        baos.write(ZipShort.getBytes((short) 8));
        baos.write("test.txt".getBytes());

        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertEquals("test.txt", entry.getName());
        assertEquals("application/octet-stream", entry.getMimeType());
    }

    @Test
    public void testGetNextZipEntry_UnicodeExtraFields() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(ZipArchiveOutputStream.LFH_SIG.bytes);
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(0x08); // general purpose bit flag
        baos.write(ZipShort.getBytes((short) 8)); // compression method
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(ZipLong.getBytes(100L));
        baos.write(ZipLong.getBytes(200L));
        baos.write(ZipLong.getBytes(1000L));
        baos.write(ZipShort.getBytes((short) 12));
        baos.write(ZipShort.getBytes((short) 8));
        baos.write("测试文件.txt".getBytes(ZipEncodingHelper.UTF8.getCodePointIndex("测试文件")), 0, 10);
        baos.write(ZipEncodingHelper.UTF8.getBytes("测试文件.txt".substring(10)));

        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, ZipEncodingHelper.UTF8, true);

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertEquals("测试文件.txt", entry.getName());
    }

    @Test
    public void testGetNextZipEntry_CentralDirectory() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(ZipArchiveOutputStream.CDF_SIG.bytes);
        baos.write("Test.zip".getBytes());
        baos.write("0".getBytes());
        baos.writeShort((short) 0);
        baos.writeShort((short) 0);
        baos.writeShort((short) 3);
        baos.writeShort((short) 0);

        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("Test".getBytes());
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] output = new byte[3];
        int length = zipArchiveInputStream.read(output, 0, 3);
        assertEquals(3, length);
        assertArrayEquals("Test".getBytes(), output);
    }

    @Test
    public void testRead_EndOfStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("Test".getBytes());
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] output = new byte[3];
        int length = zipArchiveInputStream.read(output, 0, 3);
        assertEquals(3, length);

        length = zipArchiveInputStream.read(output, 0, 3);
        assertEquals(-1, length);
    }

    @Test
    public void testRead_DataDescriptor() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(ZipArchiveOutputStream.LFH_SIG.bytes);
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(0x08); // general purpose bit flag
        baos.write(ZipShort.getBytes((short) 8)); // compression method
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(ZipLong.getBytes(100L));
        baos.write(ZipLong.getBytes(200L));
        baos.write(ZipLong.getBytes(1000L));
        baos.write(ZipShort.getBytes((short) 12));
        baos.write(ZipShort.getBytes((short) 8));
        baos.write("test.txt".getBytes());
        baos.write(ZipShort.getBytes((short) 10));
        baos.write(ZipShort.getBytes((short) 0));
        baos.write(ZipShort.getBytes((short) 0));
        baos.write(ZipShort.getBytes((short) 0));

        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] output = new byte[10];
        int length = zipArchiveInputStream.read(output, 0, 10);
        assertEquals(10, length);
        assertArrayEquals("test.txt".getBytes(), output, 0, 10);
    }

    @Test
    public void testCloseEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(ZipArchiveOutputStream.LFH_SIG.bytes);
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(0x08); // general purpose bit flag
        baos.write(ZipShort.getBytes((short) 8)); // compression method
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(ZipShort.getBytes((short) 1));
        baos.write(ZipLong.getBytes(100L));
        baos.write(ZipLong.getBytes(200L));
        baos.write(ZipLong.getBytes(1000L));
        baos.write(ZipShort.getBytes((short) 12));
        baos.write(ZipShort.getBytes((short) 8));
        baos.write("test.txt".getBytes());
        baos.write(ZipShort.getBytes((short) 10));
        baos.write(ZipShort.getBytes((short) 0));
        baos.write(ZipShort.getBytes((short) 0));
        baos.write(ZipShort.getBytes((short) 0));

        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        ArchiveEntry expectedEntry = new ZipArchiveEntry("test.txt");
        assertEquals(expectedEntry, entry);

        zipArchiveInputStream.closeEntry();

        entry = zipArchiveInputStream.getNextZipEntry();
        assertNull(entry);

        zipArchiveInputStream.close();
    }

    @Test
    public void testFill() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        zipArchiveInputStream.fill();

        byte[] bytes = new byte[ZipArchiveOutputStream.BUFFER_SIZE];
        int length = zipArchiveInputStream.read(bytes, 0, ZipArchiveOutputStream.BUFFER_SIZE);
        assertEquals(ZipArchiveOutputStream.BUFFER_SIZE, length);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("Test".getBytes());
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] output = new byte[3];
        zipArchiveInputStream.readFully(output, 0, 3);
        assertEquals("Test".getBytes(), output);
    }

    @Test(expected = ZipException.class)
    public void testReadFully_DataFormatException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("Test".getBytes());
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.inf.setInput(new ByteArrayInputStream(new byte[0]));
        zipArchiveInputStream.readFully(new byte[1], 0, 1);
    }
}