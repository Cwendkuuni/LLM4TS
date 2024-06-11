package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test cases for {@link DeflateCompressorInputStream}.
 */
public class DeflateCompressorInputStreamTest {

    @Test
    public void testNewInstanceWithInputStreamOnly() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x78, 0xDA, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x14, 0x00, 0x02, 0x00, 0x00, 0x0F, 0x40, 0x08, 0x00, 0x00, 0x0B, 0x41, 0x08, 0x00, 0x00, 0x06, 0x42, 0x08, 0x00, 0x00, 0x02, 0x43, 0x00, 0x00, 0x44, 0x00, 0x00, 0x01, 0x45, 0x00, 0x00, 0x5D });
        DeflateCompressorInputStream inputStreamWithZlibHeader = new DeflateCompressorInputStream(inputStream);
        assertNotNull(inputStreamWithZlibHeader);
        assertTrue(inputStreamWithZlibHeader.supportsRead());
        assertTrue(inputStreamWithZlibHeader.supportsReadNBytes());
        assertTrue(inputStreamWithZlibHeader.supportsWrite());
        assertTrue(inputStreamWithZlibHeader.supportsWriteNBytes());
        assertTrue(inputStreamWithZlibHeader.supportsClose());
        assertTrue(inputStreamWithZlibHeader.supportsMarkSupported());
        assertFalse(inputStreamWithZlibHeader.supportsDataOutput());
        assertSame(DeflateCompressorInputStream.class, inputStreamWithZlibHeader.getClass());
        assertNull(inputStreamWithZlibHeader.getChannel());
        assertEquals(-1, inputStreamWithZlibHeader.read());
        try {
            inputStreamWithZlibHeader.write(new byte[] { 0x01 });
            fail("Should not be able to write to a decompressor input stream");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testNewInstanceWithInputStreamAndParameters() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x78, 0xDA, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x14, 0x00, 0x02, 0x00, 0x00, 0x0F, 0x40, 0x08, 0x00, 0x00, 0x0B, 0x41, 0x08, 0x00, 0x00, 0x06, 0x42, 0x08, 0x00, 0x00, 0x02, 0x43, 0x00, 0x00, 0x44, 0x00, 0x00, 0x01, 0x45, 0x00, 0x00, 0x5D });
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream inputStreamWithoutZlibHeader = new DeflateCompressorInputStream(inputStream, parameters);
        assertNotNull(inputStreamWithoutZlibHeader);
        assertTrue(inputStreamWithoutZlibHeader.supportsRead());
        assertTrue(inputStreamWithoutZlibHeader.supportsReadNBytes());
        assertTrue(inputStreamWithoutZlibHeader.supportsWrite());
        assertTrue(inputStreamWithoutZlibHeader.supportsWriteNBytes());
        assertTrue(inputStreamWithoutZlibHeader.supportsClose());
        assertTrue(inputStreamWithoutZlibHeader.supportsMarkSupported());
        assertFalse(inputStreamWithoutZlibHeader.supportsDataOutput());
        assertSame(DeflateCompressorInputStream.class, inputStreamWithoutZlibHeader.getClass());
        assertNull(inputStreamWithoutZlibHeader.getChannel());
        assertEquals(-1, inputStreamWithoutZlibHeader.read());
        try {
            inputStreamWithoutZlibHeader.write(new byte[] { 0x01 });
            fail("Should not be able to write to a decompressor input stream");
        } catch (IOException e) {
            // expected
        }
        assertEquals(parameters, inputStreamWithoutZlibHeader.getParameters());
    }

    @Test
    public void testRead() throws IOException {
        String testData = "testing 12345";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(testData.getBytes()), new DeflateParameters());
        byte[] buffer = new byte[10];
        int length;
        while ((length = inputStream.read(buffer)) >= 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        byte[] expected = testData.getBytes();
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testReadNBytes() throws IOException {
        String testData = "testing 12345";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(testData.getBytes()), new DeflateParameters());
        byte[] buffer = new byte[10];
        int length = inputStream.read(buffer);
        outputStream.write(buffer, 0, length);
        length = inputStream.read(buffer);
        outputStream.write(buffer, 0, length);
        outputStream.flush();
        byte[] expected = testData.getBytes();
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        String testData = "testing 12345";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(testData.getBytes()), new DeflateParameters());
        long bytesToSkip = 5;
        long skipped = inputStream.skip(bytesToSkip);
        assertEquals(bytesToSkip, skipped);
        byte[] expected = new byte[testData.length() - 5];
        int length;
        byte[] buffer = new byte[10];
        while ((length = inputStream.read(buffer)) >= 0) {
            System.arraycopy(buffer, 0, expected, 0, length);
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testAvailable() throws IOException {
        String testData = "testing 12345";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(testData.getBytes()), new DeflateParameters());
        assertEquals(-1, inputStream.available());
        byte[] buffer = new byte[10];
        int length;
        while ((length = inputStream.read(buffer)) >= 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        byte[] expected = testData.getBytes();
        assertEquals(expected.length, inputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        String testData = "testing 12345";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(testData.getBytes()), new DeflateParameters());
        byte[] buffer = new byte[10];
        int length;
        while ((length = inputStream.read(buffer)) >= 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        byte[] expected = testData.getBytes();
        assertArrayEquals(expected, outputStream.toByteArray());
        inputStream.close();
    }

    @Test
    public void testMarkSupported() {
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]), new DeflateParameters());
        assertFalse(inputStream.markSupported(true));
        try {
            inputStream.mark(0);
            fail("Should not be able to mark a decompressor input stream");
        } catch (IOException e) {
            // expected
        }
    }
}