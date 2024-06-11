package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DeflateCompressorInputStreamTest {

    private static final byte[] ZLIB_HEADER = new byte[]{0x7F, 0x4E, 0x45, 0x4C, 0x44, 0x58, 0xDA, 0xDB};

    @Test
    public void testNewInstanceWithInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertEquals(CompressorInputStream.class, compressorInputStream.getClass());
        assertEquals(inputStream.getClass(), compressorInputStream.getDelegateStream().getClass());
    }

    @Test
    public void testNewInstanceWithInputStreamAndParameters() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        assertEquals(CompressorInputStream.class, compressorInputStream.getClass());
        assertEquals(inputStream.getClass(), compressorInputStream.getDelegateStream().getClass());
        assertEquals(false, compressorInputStream.isZlibHeaderPresent());
    }

    @Test
    public void testIsZlibHeaderPresent() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x7F, 0x4E, 0x45, 0x4C, 0x44, 0x58, 0xDA, 0xDB, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertTrue(compressorInputStream.isZlibHeaderPresent());

        inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        compressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertFalse(compressorInputStream.isZlibHeaderPresent());
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        int expected = 0;
        int read;
        while ((read = compressorInputStream.read()) != -1) {
            outputStream.write(read);
            expected++;
        }

        assertArrayEquals(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05}, outputStream.toByteArray());
        assertEquals(6, expected);
    }

    @Test
    public void testReadByteArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[6];
        int length = compressorInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05}, buffer);
        assertEquals(6, length);
    }

    @Test
    public void testReadZeroBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[0];
        int length = compressorInputStream.read(buffer, 0, buffer.length);
        assertEquals(0, length);
    }

    @Test
    public void testReadByteArrayWithLessBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[3];
        int length = compressorInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0x00, 0x01, 0x02}, buffer);
        assertEquals(3, length);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        long n = 3;
        long skipped = compressorInputStream.skip(n);
        assertEquals(n, skipped);

        byte[] buffer = new byte[4];
        int read = compressorInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0x03, 0x04, 0x05}, buffer);
        assertEquals(3, read);
    }

    @Test
    public void testAvailable() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        assertEquals(5, compressorInputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        compressorInputStream.close();
        assertNull(compressorInputStream.getDelegateStream());
    }

    @Test
    public void testReadFromZlibFile() throws IOException {
        String zlibFileName = "target/test-classes/test_zlib_file.bin";
        try (ZipFile zipFile = new ZipFile(zlibFileName); ZipEntry entry = zipFile.getEntry("test_zlib_file.bin")) {
            InputStream inputStream = zipFile.getInputStream(entry);
            DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

            byte[] buffer = new byte[6];
            int length = compressorInputStream.read(buffer, 0, buffer.length);
            assertArrayEquals(new byte[]{0x0, 0x1, 0x2, 0x3, 0x4, 0x5}, buffer);
            assertEquals(6, length);
        }
    }
}