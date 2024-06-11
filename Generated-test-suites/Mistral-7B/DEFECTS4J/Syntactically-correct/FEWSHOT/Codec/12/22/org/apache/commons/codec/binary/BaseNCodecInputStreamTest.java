package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
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
    public void testRead() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream input = new ByteArrayInputStream(new byte[] { 'a' });
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        assertEquals(0, inputStream.read());
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testReadWithAvailableData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream input = new ByteArrayInputStream(new byte[] { 'a' });
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        final byte[] buffer = new byte[1];
        assertEquals(1, inputStream.read(buffer, 0, 1));
        assertEquals('a', buffer[0]);
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testReadWithAvailableDataAndEncode() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream input = new ByteArrayInputStream(new byte[] { 'a' });
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        final byte[] buffer = new byte[4];
        assertEquals(4, inputStream.read(buffer, 0, 4));
        final byte[] expected = new byte[] { 'a', 'a', 'a', 'a' };
        assertArrayEquals(expected, buffer);
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testReadWithAvailableDataAndDecode() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream input = new ByteArrayInputStream(new byte[] { 'a', 'a' });
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        final byte[] buffer = new byte[2];
        assertEquals(2, inputStream.read(buffer, 0, 2));
        assertEquals('a', buffer[0]);
        assertEquals('a', buffer[1]);
        assertEquals(-1, inputStream.read());
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream input = new ByteArrayInputStream(new byte[] { 'a' });
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream input = new ByteArrayInputStream(new byte[] { 'a' });
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream input = new ByteArrayInputStream(new byte[] { 'a' });
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.read(new byte[1], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream input = new ByteArrayInputStream(new byte[] { 'a' });
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.read(new byte[1][]::new, 0, 1);
    }

    @Test
    public void testMarkSupported() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream input = new ByteArrayInputStream(new byte[] { 'a' });
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        final byte[] inputData = new byte[] { 'a', 'a' };
        final InputStream input = new ByteArrayInputStream(inputData);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        final long expectedBytesToSkip = 512;
        final long skippedBytes = inputStream.skip(expectedBytesToSkip);

        assertTrue(skippedBytes >= 0);
        assertTrue(skippedBytes <= expectedBytesToSkip);
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testAvailable() throws IOException {
        final byte[] inputData = new byte[] { 'a', 'a' };
        final InputStream input = new ByteArrayInputStream(inputData);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        assertEquals(2, inputStream.available());
        assertEquals(-1, inputStream.read());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeArgument() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream input = new ByteArrayInputStream(new byte[] { 'a' });
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.skip(-1);
    }

    @Test
    public void testClose() throws IOException {
        final byte[] inputData = new byte[] { 'a', 'a' };
        final InputStream input = new ByteArrayInputStream(inputData);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.close();
        final byte[] buffer = new byte[1];
        assertEquals(-1, inputStream.read(buffer, 0, buffer.length));

        // Test that mark and reset are not supported
        assertFalse(inputStream.markSupported());
        try {
            inputStream.mark(1);
            inputStream.reset();
            fail("expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testHasData() throws IOException {
        final byte[] inputData = new byte[] { 'a', 'a' };
        final InputStream input = new ByteArrayInputStream(inputData);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        assertTrue(inputStream.hasData());
        assertEquals(2, inputStream.read(new byte[2][], 0, 2).length);
        assertFalse(inputStream.hasData());
    }

    @Test
    public void testHasDataWhenNotReading() throws IOException {
        final byte[] inputData = new byte[] { 'a', 'a' };
        final InputStream input = new ByteArrayInputStream(inputData);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        assertFalse(inputStream.hasData());
    }

    @Test
    public void testHasDataWhenEncoded() throws IOException {
        final byte[] inputData = new byte[] { 'a', 'a' };
        final InputStream input = new ByteArrayInputStream(inputData);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        assertFalse(inputStream.hasData());
        final byte[] buffer = new byte[4];
        assertEquals(4, inputStream.read(buffer, 0, 4).length);
        assertTrue(inputStream.hasData());
        assertEquals(1, inputStream.read(new byte[1][], 0, 1).length);
        assertFalse(inputStream.hasData());
    }

    @Test(expected = NullPointerException.class)
    public void testEncodeDecodeWithNullInput() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream input = null;
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.read(new byte[1][], 0, 1);
    }
}