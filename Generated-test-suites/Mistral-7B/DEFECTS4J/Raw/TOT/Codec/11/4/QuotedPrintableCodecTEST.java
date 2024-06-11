package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private QuotedPrintableCodec codec;

    @Before
    public void setUp() {
        codec = new QuotedPrintableCodec(DEFAULT_CHARSET);
    }

    /**
     * Test encoding and decoding bytes.
     */
    @Test
    public void testEncodeDecodeBytes() throws UnsupportedEncodingException, DecoderException {
        byte[] input = new byte[]{'A', 'B', 'C'};
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);

        assertNotNull(encoded);
        assertNotNull(decoded);
        assertEquals(input, decoded);
    }

    /**
     * Test encoding and decoding strings using default charset.
     */
    @Test
    public void testEncodeDecodeStrings() throws UnsupportedEncodingException, DecoderException {
        String input = "ABC";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded, DEFAULT_CHARSET);

        assertEquals(input, decoded);
    }

    /**
     * Test encoding and decoding strings with custom charset.
     */
    @Test
    public void testEncodeDecodeStringsCustomCharset() throws UnsupportedEncodingException, DecoderException {
        String input = "ABC";
        String encoded = codec.encode(input, "ISO-8859-1");
        String decoded = codec.decode(encoded, "ISO-8859-1");

        assertEquals(input, decoded);
    }

    /**
     * Test encoding of binary data with some edge cases and error scenarios.
     */
    @Test
    public void testEncodeBinaryData() throws UnsupportedEncodingException, DecoderException {
        // Test empty data
        byte[] emptyInput = {};
        byte[] encodedEmpty = codec.encode(emptyInput);
        byte[] decodedEmpty = codec.decode(encodedEmpty);
        assertEquals(emptyInput, decodedEmpty);

        // Test only whitespace characters
        byte[] whitespaceInput = {32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32};
        byte[] encodedWhitespace = codec.encode(whitespaceInput);
        byte[] decodedWhitespace = codec.decode(encodedWhitespace);
        assertEquals(whitespaceInput, decodedWhitespace);

        // Test CR and LF characters
        byte[] crLfInput = {13, 10};
        byte[] encodedCrLf = codec.encode(crLfInput);
        byte[] decodedCrLf = codec.decode(encodedCrLf);
        assertEquals(crLfInput, decodedCrLf);

        // Test invalid byte sequence (CR followed by LF)
        byte[] crLfInvalidInput = {13, 10, 13, 10};
        try {
            codec.encode(crLfInvalidInput);
            fail("Expected EncoderException to be thrown.");
        } catch (EncoderException e) {
            // Expected
        }
    }

    /**
     * Test decoding of quoted-printable data with some edge cases and error scenarios.
     */
    @Test
    public void testDecodeQuotedPrintableData() throws UnsupportedEncodingException, DecoderException {
        // Test empty data
        byte[] emptyEncoded = {};
        try {
            codec.decode(emptyEncoded);
            fail("Expected DecoderException to be thrown.");
        } catch (DecoderException e) {
            // Expected
        }

        // Test all whitespace characters
        byte[] whitespaceEncoded = {61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61};
        String decodedWhitespace = codec.decode(whitespaceEncoded, DEFAULT_CHARSET);
        assertEquals("", decodedWhitespace);

        // Test CR and LF characters
        byte[] crLfEncoded = {61, 62, 13, 62, 10};
        String decodedCrLf = codec.decode(crLfEncoded, DEFAULT_CHARSET);
        assertEquals("\r\n", decodedCrLf);

        // Test invalid byte sequence ('=' followed by invalid digit)
        byte[] invalidSequenceEncoded = {61, 62, 63, 63};
        try {
            codec.decode(invalidSequenceEncoded, DEFAULT_CHARSET);
            fail("Expected DecoderException to be thrown.");
        } catch (DecoderException e) {
            // Expected
        }
    }
}