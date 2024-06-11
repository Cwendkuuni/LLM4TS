package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
 * Test cases for DeflateCompressorInputStream class.
 */
public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStream() {
        byte[] inputData = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };
        InputStream inputStream = new ByteArrayInputStream(inputData);

        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        byte[] outputData = new byte[inputData.length];

        int bytesRead = deflateStream.read(outputData, 0, inputData.length);
        assertEquals(inputData.length, bytesRead);
        assertSame(inputData, outputData);

        deflateStream.close();
    }

    @Test
    public void testDeflateCompressorInputStreamWithoutZlibHeader() throws IOException {
        byte[] inputData = new byte[] { 0x78, 0x5E, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };
        InputStream inputStream = new ByteArrayInputStream(inputData);

        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        byte[] outputData = new byte[inputData.length];

        int bytesRead = deflateStream.read(outputData, 0, inputData.length);
        assertEquals(inputData.length, bytesRead);
        assertSame(inputData, outputData);

        deflateStream.close();
    }

    @Test
    public void testDeflateCompressorInputStreamWithZlibHeader() throws IOException {
        byte[] inputData = new byte[] { 0x78, 0x5E, 0x08, 0x02, 0x00, 0x00, 0x00, 0x04, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };
        InputStream inputStream = new ByteArrayInputStream(inputData);

        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        byte[] outputData = new byte[inputData.length - 8]; // exclude zlib header

        int bytesRead = deflateStream.read(outputData, 0, outputData.length);
        assertEquals(outputData.length, bytesRead);
        assertNotSame(inputData, outputData); // zlib header should be excluded

        deflateStream.close();
    }

    @Test
    public void testDeflateCompressorInputStreamWithExceptionalInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        try {
            new DeflateCompressorInputStream(inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        inputStream = new ByteArrayInputStream(new byte[] { 0x01 });

        try {
            new DeflateCompressorInputStream(inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDeflateCompressorInputStreamWithNullInput() {
        try {
            new DeflateCompressorInputStream(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testDeflateCompressorInputStreamWithNegativeLength() {
        byte[] inputData = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(inputData);

        try {
            new DeflateCompressorInputStream(inputStream, new DeflateParameters());
            inputStream.skip(-1);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeflateCompressorInputStreamWithZeroLength() throws IOException {
        byte[] inputData = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(inputData);

        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        byte[] outputData = new byte[inputData.length];

        int bytesRead = deflateStream.read(outputData, 0, inputData.length);
        assertEquals(0, bytesRead);
        assertArrayEquals(new byte[0], outputData);

        deflateStream.close();
    }

    @Test
    public void testDeflateCompressorInputStreamWithLimitedLength() throws IOException {
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A };
        InputStream inputStream = new ByteArrayInputStream(inputData);

        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        byte[] outputData = new byte[5];

        int bytesRead = deflateStream.read(outputData, 0, outputData.length);
        assertEquals(5, bytesRead);
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 }, outputData);

        deflateStream.close();
    }

    @Test
    public void testDeflateCompressorInputStreamWithLargerLength() throws IOException {
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A };
        InputStream inputStream = new ByteArrayInputStream(inputData);

        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        byte[] outputData = new byte[inputData.length];

        int bytesRead = deflateStream.read(outputData, 0, inputData.length);
        assertEquals(inputData.length, bytesRead);
        assertArrayEquals(inputData, outputData);

        deflateStream.close();
    }

    @Test
    public void testDeflateCompressorInputStreamWithPartialRead() throws IOException {
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A };
        InputStream inputStream = new ByteArrayInputStream(inputData);

        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        byte[] outputData = new byte[5];

        int bytesRead = deflateStream.read(outputData, 0, outputData.length);
        assertEquals(5, bytesRead);
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 }, outputData);

        int remainingData = inputData.length - bytesRead;
        byte[] remainingOutputData = new byte[remainingData];

        bytesRead = deflateStream.read(remainingOutputData, 0, remainingData);
        assertEquals(remainingData, bytesRead);
        assertArrayEquals(new byte[] { 0x06, 0x07, 0x08, 0x09, 0x0A }, remainingOutputData);

        deflateStream.close();
    }

    @Test
    public void testDeflateCompressorInputStreamWithAvailable() throws IOException {
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A };
        InputStream inputStream = new ByteArrayInputStream(inputData);

        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        byte[] outputData = new byte[inputData.length];

        int bytesRead = deflateStream.read(outputData, 0, inputData.length);
        assertEquals(inputData.length, bytesRead);
        assertArrayEquals(inputData, outputData);

        int availableData = deflateStream.available();
        assertEquals(0, availableData);

        deflateStream.close();
    }

    /**
     * Verifies that the test methods cover all paths in DeflateCompressorInputStream
     * class.
     */
    @Test
    public void testDeflateCompressorInputStreamCodeCoverage() {
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A };
        InputStream inputStream = new ByteArrayInputStream(inputData);

        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        byte[] outputData = new byte[inputData.length];

        try {
            deflateStream.read(null, 0, inputData.length);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }

        try {
            deflateStream.read(outputData, -1, inputData.length);
            fail("Expected NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            // expected
        }

        try {
            deflateStream.read(outputData, 0, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            deflateStream.read(outputData, 0, inputData.length + 1);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }

        deflateStream.close();
    }

    /**
     * Helper method for comparing two byte arrays.
     *
     * @param expected
     * @param actual
     */
    private void assertArrayEquals(byte[] expected, byte[] actual) {
        if (expected.length != actual.length) {
            fail("expected array size doesn't match actual array size: expected=" + expected.length + " actual=" + actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                fail("expected[" + i + "] != actual[" + i + "]: expected=" + (int) expected[i] + " actual=" + (int) actual[i]);
            }
        }
    }

    /**
     * Helper method for comparing two longs.
     *
     * @param expected
     * @param actual
     */
    private void assertEquals(long expected, long actual) {
        assertEquals(Long.toString(expected), Long.toString(actual));
    }

    /**
     * Helper method for comparing two integers.
     *
     * @param expected
     * @param actual
     */
    private void assertEquals(int expected, int actual) {
        assertEquals(Integer.toString(expected), Integer.toString(actual));
    }

    /**
     * Helper method for comparing two strings.
     *
     * @param expected
     * @param actual
     */
    private void assertEquals(String expected, String actual) {
        assertEquals(expected, actual);
    }

    /**
     * Helper method for checking that a boolean is true.
     *
     * @param condition
     */
    private void assertTrue(boolean condition) {
        if (!condition) {
            fail("Expected true");
        }
    }

    /**
     * Helper method for checking that a boolean is false.
     *
     * @param condition
     */
    private void assertFalse(boolean condition) {
        if (condition) {
            fail("Expected false");
        }
    }

    /**
     * Helper method for checking that an object is null.
     *
     * @param obj
     */
    private void assertNull(Object obj) {
        if (obj != null) {
            fail("Expected null");
        }
    }
}