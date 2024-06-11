package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test reading bytes from an input stream using BaseNCodecInputStream.
     *
     * @throws IOException
     *             Upon failure
     */
    @org.junit.Test
    public void testReadBytes() throws IOException {
        // Given: a BaseNCodecInputStream
        final BaseNCodec codec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKL1234567890".getBytes(StandardCharsets.UTF_8));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, codec, false);

        // When: reading bytes
        final byte[] buffer = new byte[10];
        int read = testee.read(buffer, 0, buffer.length);

        // Then: bytes should be read correctly
        assertEquals(10, read);
        assertEquals("ABCDEFGHIJ", new String(buffer, 0, read, StandardCharsets.UTF_8));
    }

    /**
     * Test reading bytes from an input stream using BaseNCodecInputStream with decoding.
     *
     * @throws IOException
     *             Upon failure
     */
    @org.junit.Test
    public void testReadBytesDecoded() throws IOException {
        // Given: a BaseNCodecInputStream
        final BaseNCodec codec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKL1234567890_===".getBytes(StandardCharsets.UTF_8));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, codec, true);

        // When: reading bytes
        final byte[] buffer = new byte[10];
        int read = testee.read(buffer, 0, buffer.length);

        // Then: bytes should be decoded correctly
        assertEquals(10, read);
        assertEquals("ABCDEFGHIJKL123456", new String(buffer, 0, read, StandardCharsets.UTF_8));
    }

    /**
     * Test markSupported method.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() {
        // Given: a BaseNCodecInputStream
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKL1234567890".getBytes(StandardCharsets.UTF_8));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        // When: calling markSupported method
        testee.markSupported();
    }

    /**
     * Test mark and reset methods.
     *
     * @throws IOException
     *             Upon failure
     */
    @org.junit.Test
    public void testMarkAndReset() throws IOException {
        // Given: a BaseNCodecInputStream
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKL1234567890".getBytes(StandardCharsets.UTF_8));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        // When: marking position
        testee.mark(10);

        // Then: current position should not have been changed
        assertEquals(0, testee.getIndex());

        // When: reading some bytes
        final byte[] buffer = new byte[5];
        testee.read(buffer, 0, buffer.length);

        // When: resetting position
        testee.reset();

        // Then: position should be reset to mark
        assertEquals(0, testee.getIndex());

        // When: reading some bytes again
        testee.read(buffer, 0, buffer.length);

        // Then: bytes should be read correctly again
        assertEquals(5, testee.getIndex());
        assertEquals("EFGHI", new String(buffer, 0, testee.getIndex(), StandardCharsets.UTF_8));
    }

    /**
     * Test reading bytes when EOF has been reached.
     *
     * @throws IOException
     *             Upon failure
     */
    @org.junit.Test
    public void testReadBytesWhenEof() throws IOException {
        // Given: a BaseNCodecInputStream with EOF reached
        final BaseNCodec codec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKL1234567890".getBytes(StandardCharsets.UTF_8));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, codec, false);
        testee.readAllBytes();

        // When: reading bytes
        final byte[] buffer = new byte[1];
        int read = testee.read(buffer, 0, buffer.length);

        // Then: EOF should have been reached
        assertEquals(BaseNCodecInputStream.EOF, read);
    }

    /**
     * Test IOException when reading bytes with a null buffer.
     */
    @org.junit.Test
    public void testIOExceptionWhenReadingWithNullBuffer() {
        // Given: a BaseNCodecInputStream
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKL1234567890".getBytes(StandardCharsets.UTF_8));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        // When: reading bytes with a null buffer
        assertThrows(NullPointerException.class, () -> testee.read(null, 0, 1));
    }

    /**
     * Test IOException when reading bytes with negative offset.
     */
    @org.junit.Test
    public void testIOExceptionWhenReadingWithNegativeOffset() {
        // Given: a BaseNCodecInputStream
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKL1234567890".getBytes(StandardCharsets.UTF_8));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        // When: reading bytes with a negative offset
        assertThrows(IndexOutOfBoundsException.class, () -> testee.read(-1, 0, 1));
    }

    /**
     * Test IOException when reading bytes with a negative length.
     */
    @org.junit.Test
    public void testIOExceptionWhenReadingWithNegativeLength() {
        // Given: a BaseNCodecInputStream
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKL1234567890".getBytes(StandardCharsets.UTF_8));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        // When: reading bytes with a negative length
        assertThrows(IndexOutOfBoundsException.class, () -> testee.read(new byte[1], 0, -1));
    }

    /**
     * Test IOException when reading bytes with a buffer size larger than the available data.
     */
    @org.junit.Test
    public void testIOExceptionWhenReadingWithBufferSizeLargerThanAvailableData() {
        // Given: a BaseNCodecInputStream
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKL1234567890".getBytes(StandardCharsets.UTF_8));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        // When: reading bytes with a buffer size larger than the available data
        assertThrows(IOException.class, () -> testee.read(new byte[11], 0, 11));
    }

    /**
     * Test IOException when reading bytes with a negative skip length.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIOExceptionWhenSkippingWithNegativeLength() {
        // Given: a BaseNCodecInputStream
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKL1234567890".getBytes(StandardCharsets.UTF_8));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        // When: skipping bytes with a negative length
        testee.skip(-1);
    }

    /**
     * Test skipping bytes in chunks of 512 bytes.
     *
     * @throws IOException
     *             Upon failure
     */
    @org.junit.Test
    public void testSkipBytes() throws IOException {
        // Given: a BaseNCodecInputStream
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKL1234567890".getBytes(StandardCharsets.UTF_8));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        // When: skipping bytes
        final int skip = 512;
        int skipped = 0;
        while (skipped < 5 * 512) {
            int skippedBytes = testee.skip(skip);
            skipped += skippedBytes;
        }

        // Then: all bytes should have been skipped
        assertEquals(2560, skipped);

        // When: reading remaining bytes
        final byte[] buffer = new byte[1];
        int read = 0;
        while ((read = testee.read(buffer, 0, buffer.length)) != BaseNCodecInputStream.EOF) {
            // no-op
        }

        // Then: no bytes should be left
        assertEquals(0, testee.available());
    }

    /**
     * Test available method.
     *
     * @throws IOException
     *             Upon failure
     */
    @org.junit.Test
    public void testAvailable() throws IOException {
        // Given: a BaseNCodecInputStream
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKL1234567890".getBytes(StandardCharsets.UTF_8));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        // When: checking available bytes
        assertEquals(10, testee.available());

        // When: reading bytes
        final byte[] buffer = new byte[5];
        testee.read(buffer, 0, buffer.length);

        // Then: available bytes should have been reduced
        assertEquals(5, testee.available());

        // When: reading remaining bytes
        int read = 0;
        while ((read = testee.read(buffer, 0, buffer.length)) != BaseNCodecInputStream.EOF) {
            // no-op
        }

        // Then: no bytes should be left
        assertEquals(0, testee.available());
    }

    /**
     * Test close method.
     *
     * @throws IOException
     *             Upon failure
     */
    @org.junit.Test
    public void testClose() throws IOException {
        // Given: a BaseNCodecInputStream
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKL1234567890".getBytes(StandardCharsets.UTF_8));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        // When: closing the stream
        testee.close();

        // Then: underlying input stream should be closed
        final InputStream closedInputStream = testee.getInputStream();
        assertFalse(closedInputStream.isOpen());
    }
}