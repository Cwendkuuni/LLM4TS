package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test for reading a single byte.
     */
    @org.junit.Test
    public void testReadByte() throws IOException {
        final byte[] input = { 0x41, 0x00, 0x42, 0x00, 0x43, 0x01, EOF };
        final InputStream inputStream = new ByteArrayInputStream(input);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test reading multiple bytes, the first byte should be 'A'
        final int read = testee.read();
        assertEquals('A', read);

        // Test EOF
        final int readEOF = testee.read();
        assertEquals(EOF, read);
    }

    /**
     * Test for reading multiple bytes.
     */
    @org.junit.Test
    public void testReadMultipleBytes() throws IOException {
        final byte[] input = { 0x41, 0x00, 0x42, 0x00, 0x43, 0x01, EOF };
        final InputStream inputStream = new ByteArrayInputStream(input);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test reading multiple bytes, the first two bytes should be 'A' and 'B'
        final byte[] expected = { 'A', 'B' };
        final byte[] result = new byte[2];
        final int read = testee.read(result, 0, 2);
        assertEquals(expected.length, read);
        assertEquals(expected, result);

        // Test EOF
        final byte[] resultEOF = new byte[1];
        final int readEOF = testee.read(resultEOF, 0, 1);
        assertEquals(EOF, readEOF);
    }

    /**
     * Test for decoding invalid input.
     */
    @org.junit.Test
    public void testDecodeInvalidInput() throws IOException, EncoderException, DecoderException {
        final byte[] input = { 0x41, 0x00, 0x42, 0x00, 0x43, 0x01, 0x01 }; // Invalid byte
        final InputStream inputStream = new ByteArrayInputStream(input);
        final BaseNCodec baseNCodec = mock(BaseNCodec.class);
        when(baseNCodec.decode(inputStream)).thenThrow(new DecoderException("Unexpected end of stream"));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test decoding invalid input
        assertThrows(DecoderException.class, () -> testee.read());
    }

    /**
     * Test for encoding invalid input.
     */
    @org.junit.Test
    public void testEncodeInvalidInput() throws IOException {
        final byte[] input = { 0x41, 0x00, 0x42, 0x00, 0x43, 0x01, EOF };
        final InputStream inputStream = new ByteArrayInputStream(input);
        final BaseNCodec baseNCodec = mock(BaseNCodec.class);
        when(baseNCodec.encode(inputStream)).thenThrow(new IOException("Unexpected end of stream"));
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // Test encoding invalid input
        assertThrows(IOException.class, () -> testee.read());
    }

    /**
     * Test for mark and reset.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        final byte[] input = { 0x41, 0x00, 0x42, 0x00, 0x43, 0x01, EOF };
        final InputStream inputStream = new ByteArrayInputStream(input);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test mark and reset
        testee.mark(0);
        testee.reset();
    }

    /**
     * Test for skipping bytes.
     */
    @org.junit.Test
    public void testSkip() throws IOException {
        final byte[] input = { 0x41, 0x00, 0x42, 0x00, 0x43, 0x01, EOF };
        final InputStream inputStream = new ByteArrayInputStream(input);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test skipping bytes
        final long expectedSkipped = 2;
        final long resultSkipped = testee.skip(expectedSkipped);
        assertEquals(expectedSkipped, resultSkipped);
    }
}