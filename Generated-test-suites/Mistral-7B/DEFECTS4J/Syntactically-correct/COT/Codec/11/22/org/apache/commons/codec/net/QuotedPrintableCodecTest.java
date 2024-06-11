package org.apache.commons.codec.net;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for QuotedPrintableCodec class
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    @Test
    public void testEncodeAndDecodeByteArray() throws EncoderException, DecoderException {
        byte[] input = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        byte[] encoded = CODEC.encode(input);
        byte[] decoded = CODEC.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeAndDecodeString() throws EncoderException, DecoderException, UnsupportedEncodingException {
        String input = "Hello, World!";
        String encoded = CODEC.encode(input, CharEncoding.US_ASCII);
        String decoded = CODEC.decode(encoded, CharEncoding.US_ASCII);
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeAndDecodeByteArrayWithDefaultCharset() throws EncoderException, DecoderException {
        byte[] input = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        byte[] encoded = CODEC.encode(input);
        byte[] decoded = CODEC.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeAndDecodeStringWithDefaultCharset() throws EncoderException, DecoderException {
        String input = "Hello, World!";
        String encoded = CODEC.encode(input);
        String decoded = CODEC.decode(encoded);
        assertEquals(input, decoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidObject() {
        CODEC.encode(new Object());
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidObject() {
        CODEC.decode(new Object());
    }

    @Test
    public void testEncodeAndDecodeBitSet() throws EncoderException, DecoderException {
        BitSet input = new BitSet(256);
        input.set(0, 255);
        byte[] encoded = CODEC.encode(input.toByteArray());
        BitSet decoded = new BitSet(256);
        decoded.set(0, 255);
        decoded.decode(encoded);
        assertTrue(decoded.equals(input));
    }

    @Test
    public void testEncodeAndDecodeByteArrayWithEscapedCharacters() throws EncoderException, DecoderException {
        byte[] input = {'@', '=', '#', '%'};
        byte[] encoded = CODEC.encode(input);
        byte[] decoded = CODEC.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeAndDecodeStringWithEscapedCharacters() throws EncoderException, DecoderException, UnsupportedEncodingException {
        String input = "!=\u0023%";
        String encoded = CODEC.encode(input, CharEncoding.UTF_8);
        String decoded = CODEC.decode(encoded, CharEncoding.UTF_8);
        assertEquals(input, decoded);
    }
}