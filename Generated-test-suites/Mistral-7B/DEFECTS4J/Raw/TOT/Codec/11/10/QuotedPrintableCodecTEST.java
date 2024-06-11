package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;

import org.junit.Test;

/**
 * Tests for QuotedPrintableCodec
 */
public class QuotedPrintableCodecTest {

    /**
     * Test encoding and decoding of a simple String using the default charset.
     */
    @Test
    public void testSimpleString() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String plainText = "Hello World";
        byte[] bytes = codec.encode(plainText);
        String encodedText = new String(bytes, CharEncoding.UTF_8);
        assertEquals("Hello%20World", encodedText);

        byte[] decodedBytes = codec.decode(bytes);
        String decodedText = new String(decodedBytes, CharEncoding.UTF_8);
        assertEquals(plainText, decodedText);
    }

    /**
     * Test encoding and decoding of a simple byte array using the default charset.
     */
    @Test
    public void testSimpleByteArray() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = "Hello World".getBytes();
        byte[] encodedBytes = codec.encode(bytes);
        String encodedText = new String(encodedBytes, CharEncoding.UTF_8);
        assertEquals("Hello%20World", encodedText);

        byte[] decodedBytes = codec.decode(encodedBytes);
        String decodedText = new String(decodedBytes, CharEncoding.UTF_8);
        assertEquals("Hello World", decodedText);
    }

    /**
     * Test encoding and decoding of a String with no printable characters using the default charset.
     */
    @Test
    public void testNoPrintableString() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String plainText = "\u0000\u0001\u00FF";
        byte[] bytes = codec.encode(plainText);
        String encodedText = new String(bytes, CharEncoding.UTF_8);
        assertEquals("\u0000=\u0000%01=\u0001%FF=\u00FF", encodedText);

        byte[] decodedBytes = codec.decode(bytes);
        String decodedText = new String(decodedBytes, CharEncoding.UTF_8);
        assertEquals(plainText, decodedText);
    }

    /**
     * Test encoding and decoding of a byte array with no printable characters.
     */
    @Test
    public void testNoPrintableByteArray() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = {0, 1, 255};
        byte[] encodedBytes = codec.encode(bytes);
        String encodedText = new String(encodedBytes, CharEncoding.UTF_8);
        assertEquals("0=\u0000%01=\u0001%FF=\u00FF", encodedText);

        byte[] decodedBytes = codec.decode(encodedBytes);
        String decodedText = new String(decodedBytes, CharEncoding.UTF_8);
        assertArrayEquals(bytes, decodedBytes);
    }

    /**
     * Test encoding and decoding of a String with a CR LF sequence using the default charset.
     */
    @Test
    public void testCRLFString() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String plainText = "Test\r\n";
        byte[] bytes = codec.encode(plainText);
        String encodedText = new String(bytes, CharEncoding.UTF_8);
        assertEquals("Test%0D%0A", encodedText);

        byte[] decodedBytes = codec.decode(bytes);
        String decodedText = new String(decodedBytes, CharEncoding.UTF_8);
        assertEquals(plainText, decodedText);
    }

    /**
     * Test encoding and decoding of a byte array with a CR LF sequence.
     */
    @Test
    public void testCRLFByteArray() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = {"Test".getBytes(), 13, 10};
        byte[] encodedBytes = codec.encode(bytes);
        String encodedText = new String(encodedBytes, CharEncoding.UTF_8);
        assertEquals("Test%0D%0A", encodedText);

        byte[] decodedBytes = codec.decode(encodedBytes);
        String decodedText = new String(decodedBytes, CharEncoding.UTF_8);
        byte[] expectedDecodedBytes = {"Test".getBytes(), 13, 10};
        assertArrayEquals(expectedDecodedBytes, decodedBytes);
    }

    /**
     * Test decoding a String with an invalid byte sequence.
     */
    @Test(expected = DecoderException.class)
    public void testInvalidString() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encodedText = "Test%0D%0ABad";
        byte[] decodedBytes = codec.decode(StringUtils.getBytesUsAscii(encodedText));
    }

    /**
     * Test decoding a byte array with an invalid byte sequence.
     */
    @Test(expected = DecoderException.class)
    public void testInvalidByteArray() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encodedBytes = {"Test".getBytes(), 13, 12};
        byte[] decodedBytes = codec.decode(encodedBytes);
    }
}