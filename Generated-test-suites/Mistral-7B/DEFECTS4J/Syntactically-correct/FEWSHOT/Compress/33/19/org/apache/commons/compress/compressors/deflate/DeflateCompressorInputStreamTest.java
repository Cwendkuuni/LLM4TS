package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testCreateWithoutParameters() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 1, 2, 3, 4});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertNotNull(deflateCompressorInputStream);
        assertEquals(inputStream, deflateCompressorInputStream.getInputStream());
    }

    @Test
    public void testCreateWithParameters() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 8, 0, 1, 2, 3, 4});
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        assertNotNull(deflateCompressorInputStream);
        assertEquals(inputStream, deflateCompressorInputStream.getInputStream());
        assertEquals(false, deflateCompressorInputStream.isReadingZlibHeader());
    }

    @Test
    public void testRead() throws IOException {
        byte[] sourceData = {-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Deflater deflater = new Deflater();
        deflater.setInput(sourceData);
        deflater.finish();
        byte[] compressedData = new byte[deflater.getTotalOutputSize()];
        deflater.deflate(compressedData);
        InputStream inputStream = new ByteArrayInputStream(compressedData);

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] targetData = new byte[10];

        int read = deflateCompressorInputStream.read(targetData, 0, targetData.length);
        assertEquals(read, targetData.length);
        assertArrayEquals(sourceData, targetData);

        IOUtils.closeQuietly(deflateCompressorInputStream);
    }

    @Test
    public void testReadWithPartiallyFilledBuffer() throws IOException {
        byte[] sourceData = {-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Deflater deflater = new Deflater();
        deflater.setInput(sourceData);
        deflater.finish();
        byte[] compressedData = new byte[deflater.getTotalOutputSize()];
        deflater.deflate(compressedData);
        InputStream inputStream = new ByteArrayInputStream(compressedData);

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] targetData = new byte[5];

        int read = deflateCompressorInputStream.read(targetData, 0, targetData.length);
        assertEquals(read, targetData.length);
        assertArrayEquals(new byte[]{5, 6, 7, 8, 9}, targetData);

        IOUtils.closeQuietly(deflateCompressorInputStream);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] sourceData = {-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Deflater deflater = new Deflater();
        deflater.setInput(sourceData);
        deflater.finish();
        byte[] compressedData = new byte[deflater.getTotalOutputSize()];
        deflater.deflate(compressedData);
        InputStream inputStream = new ByteArrayInputStream(compressedData);

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long skipped = deflateCompressorInputStream.skip(5L);
        assertEquals(skipped, 5);

        IOUtils.closeQuietly(deflateCompressorInputStream);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] sourceData = {-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Deflater deflater = new Deflater();
        deflater.setInput(sourceData);
        deflater.finish();
        byte[] compressedData = new byte[deflater.getTotalOutputSize()];
        deflater.deflate(compressedData);
        InputStream inputStream = new ByteArrayInputStream(compressedData);

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long available = deflateCompressorInputStream.available();
        assertEquals(available, compressedData.length - deflateCompressorInputStream.getBytesRead());

        IOUtils.closeQuietly(deflateCompressorInputStream);
    }

    @Test
    public void testClose() throws IOException {
        byte[] sourceData = {-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Deflater deflater = new Deflater();
        deflater.setInput(sourceData);
        deflater.finish();
        byte[] compressedData = new byte[deflater.getTotalOutputSize()];
        deflater.deflate(compressedData);
        InputStream inputStream = new ByteArrayInputStream(compressedData);

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();
        assertNull(deflateCompressorInputStream.getInputStream());

        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testIsReadingZlibHeader() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x7F, 0x45, 0x4C, 0x4B, 0x08, 0x01, 0x01});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertTrue(deflateCompressorInputStream.isReadingZlibHeader());

        InputStream inputStream2 = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 1, 2, 3, 4});
        DeflateCompressorInputStream deflateCompressorInputStream2 = new DeflateCompressorInputStream(inputStream2);
        assertFalse(deflateCompressorInputStream2.isReadingZlibHeader());
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        if (!java.util.Arrays.equals(expected, actual)) {
            throw new AssertionError("Expected:<" + new String(expected) + "> but was:<" + new String(actual) + ">");
        }
    }

    private void assertNotNull(Object reference) {
        if (reference == null) {
            throw new AssertionError("Reference should not be null");
        }
    }
}