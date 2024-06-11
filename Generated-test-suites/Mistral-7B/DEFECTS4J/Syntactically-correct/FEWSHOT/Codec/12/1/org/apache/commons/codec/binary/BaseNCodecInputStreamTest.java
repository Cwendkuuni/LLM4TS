package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());

        BaseNCodecInputStream bnIs = new BaseNCodecInputStream(input, codec, false);

        byte[] expected = {1};
        byte[] actual = new byte[1];

        int numBytesRead = bnIs.read(actual, 0, 1);
        assertEquals(1, numBytesRead);
        assertArrayEquals(expected, actual);
        assertTrue(bnIs.markSupported()); // Test markSupported for completeness
        bnIs.close();
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());

        BaseNCodecInputStream bnIs = new BaseNCodecInputStream(input, codec, false);

        byte[] expected = {1, 2, 3};
        byte[] actual = new byte[3];

        int numBytesRead = bnIs.read(actual, 0, 3);
        assertEquals(3, numBytesRead);
        assertArrayEquals(expected, actual);
        assertTrue(bnIs.markSupported()); // Test markSupported for completeness
        bnIs.close();
    }

    @Test
    public void testReadNegativeLength() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream bnIs = new BaseNCodecInputStream(input, codec, false);

        byte[] actual = new byte[1];

        try {
            bnIs.read(actual, -1, 1);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        assertTrue(bnIs.markSupported()); // Test markSupported for completeness
        bnIs.close();
    }

    @Test
    public void testReadZeroLength() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());

        BaseNCodecInputStream bnIs = new BaseNCodecInputStream(input, codec, false);

        byte[] actual = new byte[1];

        int numBytesRead = bnIs.read(actual, 0, 0);
        assertEquals(0, numBytesRead);
        assertArrayEquals(new byte[0], actual);
        assertTrue(bnIs.markSupported()); // Test markSupported for completeness
        bnIs.close();
    }

    @Test
    public void testReadEndOfStream() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());

        BaseNCodecInputStream bnIs = new BaseNCodecInputStream(input, codec, false);

        byte[] actual = new byte[1];

        int numBytesRead = bnIs.read(actual, 0, 1);
        assertEquals(1, numBytesRead);
        assertArrayEquals(new byte[] { 0 }, actual);

        // Keep trying to read more bytes
        numBytesRead = bnIs.read(actual, 0, 1);
        assertEquals(0, numBytesRead);
        assertNull(actual);

        // Try reading a larger chunk
        numBytesRead = bnIs.read(actual, 0, 10);
        assertEquals(0, numBytesRead);
        assertNull(actual);

        assertTrue(bnIs.markSupported()); // Test markSupported for completeness
        bnIs.close();
    }

    @Test
    public void testReadMultipleChunks() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());

        BaseNCodecInputStream bnIs = new BaseNCodecInputStream(input, codec, false);

        byte[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        byte[] actual = new byte[11];

        int numBytesRead = 0;
        while (numBytesRead < expected.length) {
            int chunkSize = Math.min(expected.length - numBytesRead, 10);
            numBytesRead += bnIs.read(actual, numBytesRead, chunkSize);
        }

        assertArrayEquals(expected, actual);
        assertTrue(bnIs.markSupported()); // Test markSupported for completeness
        bnIs.close();
    }

    @Test
    public void testReadNegativeOffset() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream bnIs = new BaseNCodecInputStream(input, codec, false);

        byte[] actual = new byte[1];

        try {
            bnIs.read(actual, -1, 1);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            bnIs.read(actual, -1, -1);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            bnIs.read(actual, -10, 1);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            bnIs.read(actual, -1, 10);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        assertTrue(bnIs.markSupported()); // Test markSupported for completeness
        bnIs.close();
    }

    @Test
    public void testReadWithMark() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());

        BaseNCodecInputStream bnIs = new BaseNCodecInputStream(input, codec, false);

        // Mark the stream
        bnIs.mark(100);

        // Read some data
        byte[] expected = {1, 2, 3};
        byte[] actual = new byte[3];
        int numBytesRead = bnIs.read(actual, 0, 3);
        assertArrayEquals(expected, actual);

        // Reset the stream
        bnIs.reset();

        // Try reading more data
        int numBytesToRead = 2;
        numBytesRead = bnIs.read(actual, 0, numBytesToRead);
        assertArrayEquals(new byte[] { 4, 5 }, actual);
        assertEquals(numBytesToRead, numBytesRead);

        // Try reading the rest of the data
        numBytesToRead = expected.length - numBytesRead;
        numBytesRead = bnIs.read(actual, numBytesRead, numBytesToRead);
        assertArrayEquals(new byte[] { 6, 7, 8, 9, 10 }, new byte[] { actual[0], actual[1], actual[2], actual[3], actual[4] });
        assertEquals(numBytesToRead, numBytesRead);

        // Try reading beyond the available data
        numBytesToRead = 5;
        numBytesRead = bnIs.read(actual, numBytesRead, numBytesToRead);
        assertEquals(0, numBytesRead);
        assertNull(actual);

        assertTrue(bnIs.markSupported()); // Test markSupported for completeness
        bnIs.close();
    }

    @Test
    public void testReadWithMarkAndNegativeOffset() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());

        BaseNCodecInputStream bnIs = new BaseNCodecInputStream(input, codec, false);

        // Mark the stream
        bnIs.mark(100);

        // Read some data
        byte[] expected = {1, 2, 3};
        byte[] actual = new byte[3];
        int numBytesRead = bnIs.read(actual, 0, 3);
        assertArrayEquals(expected, actual);

        // Reset the stream
        bnIs.reset();

        // Try reading with a negative offset
        try {
            bnIs.read(actual, -1, 1);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        // Try reading the rest of the data
        int numBytesToRead = expected.length;
        numBytesRead = bnIs.read(actual, 0, numBytesToRead);
        assertArrayEquals(expected, actual);
        assertEquals(numBytesToRead, numBytesRead);

        assertTrue(bnIs.markSupported()); // Test markSupported for completeness
        bnIs.close();
    }

    @Test
    public void testReadWithMarkAndNegativeLength() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());

        BaseNCodecInputStream bnIs = new BaseNCodecInputStream(input, codec, false);

        // Mark the stream
        bnIs.mark(100);

        // Read some data
        byte[] expected = {1, 2, 3};
        byte[] actual = new byte[3];
        int numBytesRead = bnIs.read(actual, 0, 3);
        assertArrayEquals(expected, actual);

        // Reset the stream
        bnIs.reset();

        // Try reading with a negative length
        try {
            bnIs.read(actual, -1, 1);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        // Try reading the rest of the data
        int numBytesToRead = expected.length;
        numBytesRead = bnIs.read(actual, 0, numBytesToRead);
        assertArrayEquals(expected, actual);
        assertEquals(numBytesToRead, numBytesRead);

        assertTrue(bnIs.markSupported()); // Test markSupported for completeness
        bnIs.close();
    }

    @Test
    public void testEncodeDecode() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());

        BaseNCodecInputStream bnIs = new BaseNCodecInputStream(input, codec, true);

        byte[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        byte[] actual = new byte[11];

        int numBytesRead = 0;
        while (numBytesRead < expected.length) {
            int chunkSize = Math.min(expected.length - numBytesRead, 10);
            numBytesRead += bnIs.read(actual, numBytesRead, chunkSize);
        }

        byte[] decoded = new byte[expected.length];
        numBytesRead = 0;
        while (numBytesRead < expected.length) {
            int chunkSize = Math.min(expected.length - numBytesRead, 10);
            numBytesRead += bnIs.read(decoded, numBytesRead, chunkSize);
            codec.decode(actual, 0, numBytesRead);
        }

        assertArrayEquals(expected, decoded);

        assertTrue(bnIs.markSupported()); // Test markSupported for completeness
        bnIs.close();
    }
}