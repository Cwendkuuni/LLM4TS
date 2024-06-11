package org.apache.commons.compress.compressors.deflate.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflateParameters;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for DeflateCompressorInputStream.
 */
public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateCompressorInputStream testee;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x45, (byte) 0x4C, (byte) 0x4B, (byte) 0x06, (byte) 0x08});
        testee = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
    }

    @Test
    public void testConstructorWithInputStream() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(mock(InputStream.class).asMock(), new DeflateParameters());
        assertNull(deflateCompressorInputStream.getInputStream());
    }

    @Test
    public void testConstructorWithInputStreamAndParameters() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        assertEquals(inputStream, deflateCompressorInputStream.getInputStream());
    }

    @Test
    public void testRead() throws IOException {
        byte[] result = new byte[1];
        int read = testee.read(result, 0, result.length);
        assertEquals(1, read);
        assertEquals((byte) 0x78, result[0]);
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        byte[] result = new byte[0];
        int read = testee.read(result, 0, result.length);
        assertEquals(0, read);
    }

    @Test
    public void testReadWithNullBuffer() throws IOException {
        assertThrows(NullPointerException.class, () -> testee.read(null, 0, 1));
    }

    @Test
    public void testReadWithNegativeOffset() throws IOException {
        assertThrows(IndexOutOfBoundsException.class, () -> testee.read(new byte[1], -1, 1));
    }

    @Test
    public void testReadWithNegativeLength() throws IOException {
        assertThrows(IndexOutOfBoundsException.class, () -> testee.read(new byte[1], 0, -1));
    }

    @Test
    public void testReadWithDataAvailable() throws IOException {
        byte[] result = new byte[2];
        int read = testee.read(result, 0, result.length);
        assertEquals(2, read);
        assertEquals((byte) 0x78, result[0]);
        assertEquals((byte) 0x49, result[1]);
    }

    @Test
    public void testReadAllBytes() throws IOException {
        byte[] readAllBytes = testee.readAllBytes();
        byte[] expected = new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x45, (byte) 0x4C, (byte) 0x4B, (byte) 0x06, (byte) 0x08};
        assertArrayEquals(expected, readAllBytes);
    }

    @Test(expected = IOException.class)
    public void testReadWithClosedInputStream() throws IOException {
        testee.close();
        testee.read();
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = testee.skip(5L);
        assertEquals(5, skipped);
    }

    @Test(expected = IOException.class)
    public void testSkipWithClosedInputStream() throws IOException {
        testee.close();
        testee.skip(1);
    }

    @Test
    public void testAvailable() throws IOException {
        assertEquals(8, testee.available());
    }

    @Test
    public void testClose() throws IOException {
        testee.close();
        assertFalse(testee.isOpen());
    }

    @Test
    public void testIsOpen() throws IOException {
        assertTrue(testee.isOpen());
    }

    @Test
    public void testChecksWithZlibHeader() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{0x78, 0x49, 0x50, 0x45, 0x4C, 0x4B, (byte) 0xD1, 0x06, 0x08, 0xFF, 0x00, 0x00}), new DeflateParameters());
        assertTrue(deflateCompressorInputStream.checksWithZlibHeader());
    }

    @Test
    public void testChecksWithZlibNoHeader() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{0x78, 0x49, 0x50, 0x45, 0x4C, 0x4B, 0x06, 0x08}), new DeflateParameters(false));
        assertFalse(deflateCompressorInputStream.checksWithZlibHeader());
    }

}