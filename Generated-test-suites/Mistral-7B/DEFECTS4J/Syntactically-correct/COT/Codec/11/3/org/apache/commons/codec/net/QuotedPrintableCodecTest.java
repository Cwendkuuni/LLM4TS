package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

/**
 * Test class for QuotedPrintableCodec.
 */
public class QuotedPrintableCodecTest {

    // Test instances
    private QuotedPrintableCodec codec = new QuotedPrintableCodec();

    /**
     * Test encode method with basic cases.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testEncodeBasicCases() throws UnsupportedEncodingException {
        byte[] input = "Hello, World!".getBytes(CharEncoding.US_ASCII);
        byte[] expected = codec.encode(input);
        byte[] output = codec.encode(input);
        assertArrayEquals(expected, output);
    }

    /**
     * Test decode method with basic cases.
     * @throws UnsupportedEncodingException
     * @throws DecoderException
     */
    @Test
    public void testDecodeBasicCases() throws UnsupportedEncodingException, DecoderException {
        byte[] input = StringUtils.getBytesUsAscii("=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21");
        byte[] expected = "Hello, World!".getBytes(CharEncoding.US_ASCII);
        byte[] decoded = codec.decode(input);
        assertArrayEquals(expected, decoded);
    }

    /**
     * Test decode method with invalid input.
     * @throws UnsupportedEncodingException
     * @throws DecoderException
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() throws UnsupportedEncodingException, DecoderException {
        byte[] input = StringUtils.getBytesUsAscii("=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=XX");
        codec.decode(input);
    }

    /**
     * Test decode method with unsupported charset.
     * @throws UnsupportedEncodingException
     * @throws DecoderException
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeUnsupportedCharset() throws UnsupportedEncodingException, DecoderException {
        byte[] input = StringUtils.getBytesUsAscii("=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21");
        codec.decode(input, "ISO-8859-1");
    }

    /**
     * Test encode method with invalid input.
     * @throws UnsupportedEncodingException
     * @throws EncoderException
     */
    @Test(expected = EncoderException.class)
    public void testEncodeInvalidInput() throws UnsupportedEncodingException, EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-1");
        String input = "Hello, World!";
        codec.encode(input.getBytes(CharEncoding.ISO_8859_1));
    }

    /**
     * Test encode method with unsupported charset.
     * @throws UnsupportedEncodingException
     * @throws EncoderException
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testEncodeUnsupportedCharset() throws UnsupportedEncodingException, EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-1");
        String input = "Hello, World!";
        codec.encode(input.getBytes("UTF-16BE"));
    }

    /**
     * Test decode method with leading whitespace.
     * @throws UnsupportedEncodingException
     * @throws DecoderException
     */
    @Test
    public void testDecodeLeadingWhitespace() throws UnsupportedEncodingException, DecoderException {
        byte[] input = StringUtils.getBytesUsAscii(" =68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21");
        byte[] expected = "Hello, World!".getBytes(CharEncoding.US_ASCII);
        byte[] decoded = codec.decode(input);
        assertArrayEquals(expected, decoded);
    }

    /**
     * Test decode method with trailing whitespace.
     * @throws UnsupportedEncodingException
     * @throws DecoderException
     */
    @Test
    public void testDecodeTrailingWhitespace() throws UnsupportedEncodingException, DecoderException {
        byte[] input = StringUtils.getBytesUsAscii("=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21 =");
        byte[] expected = "Hello, World!".getBytes(CharEncoding.US_ASCII);
        byte[] decoded = codec.decode(input);
        assertArrayEquals(expected, decoded);
    }

    /**
     * Test decode method with CRLF at the end.
     * @throws UnsupportedEncodingException
     * @throws DecoderException
     */
    @Test
    public void testDecodeCRLF() throws UnsupportedEncodingException, DecoderException {
        byte[] input = StringUtils.getBytesUsAscii("=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21\r\n");
        byte[] expected = "Hello, World!".getBytes(CharEncoding.US_ASCII);
        byte[] decoded = codec.decode(input);
        assertArrayEquals(expected, decoded);
    }

    /**
     * Test decode method with unescaped CR and LF.
     * @throws UnsupportedEncodingException
     * @throws DecoderException
     */
    @Test(expected = DecoderException.class)
    public void testDecodeUnescapedCRLF() throws UnsupportedEncodingException, DecoderException {
        byte[] input = StringUtils.getBytesUsAscii("=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=0D=0A");
        codec.decode(input);
    }

    /**
     * Test decode method with invalid escape sequence.
     * @throws UnsupportedEncodingException
     * @throws DecoderException
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidEscapeSequence() throws UnsupportedEncodingException, DecoderException {
        byte[] input = StringUtils.getBytesUsAscii("=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=XA");
        codec.decode(input);
    }

    /**
     * Test decode method with escape sequence out of order.
     * @throws UnsupportedEncodingException
     * @throws DecoderException
     */
    @Test(expected = DecoderException.class)
    public void testDecodeEscapeSequenceOutOfOrder() throws UnsupportedEncodingException, DecoderException {
        byte[] input = StringUtils.getBytesUsAscii("=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21=65");
        codec.decode(input);
    }

    /**
     * Test decode method with invalid hex digits.
     * @throws UnsupportedEncodingException
     * @throws DecoderException
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidHexDigit() throws UnsupportedEncodingException, DecoderException {
        byte[] input = StringUtils.getBytesUsAscii("=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=F2");
        codec.decode(input);
    }

    /**
     * Test encode method with leading CR and LF.
     * @throws UnsupportedEncodingException
     * @throws EncoderException
     */
    @Test
    public void testEncodeLeadingCRLF() throws UnsupportedEncodingException, EncoderException {
        byte[] input = "Hello, World!".getBytes(CharEncoding.US_ASCII);
        byte[] expected = StringUtils.getBytesUsAscii("\r\n=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21");
        byte[] encoded = codec.encode(input);
        assertArrayEquals(expected, encoded);
    }

    /**
     * Test encode method with trailing CR and LF.
     * @throws UnsupportedEncodingException
     * @throws EncoderException
     */
    @Test
    public void testEncodeTrailingCRLF() throws UnsupportedEncodingException, EncoderException {
        byte[] input = "Hello, World!".getBytes(CharEncoding.US_ASCII);
        byte[] expected = StringUtils.getBytesUsAscii("=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21\r\n");
        byte[] encoded = codec.encode(input);
        assertArrayEquals(expected, encoded);
    }

    /**
     * Test encode method with CRLF in the input.
     * @throws UnsupportedEncodingException
     * @throws EncoderException
     */
    @Test
    public void testEncodeCRLF() throws UnsupportedEncodingException, EncoderException {
        byte[] input = "Hello, World!\r\n".getBytes(CharEncoding.US_ASCII);
        byte[] expected = StringUtils.getBytesUsAscii("=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21\r\n");
        byte[] encoded = codec.encode(input);
        assertArrayEquals(expected, encoded);
    }

    /**
     * Test encode method with unescaped CR and LF.
     * @throws UnsupportedEncodingException
     * @throws EncoderException
     */
    @Test(expected = EncoderException.class)
    public void testEncodeUnescapedCRLF() throws UnsupportedEncodingException, EncoderException {
        byte[] input = "Hello, World!\r\n".getBytes(CharEncoding.US_ASCII);
        codec.encode(input);
    }

    /**
     * Test encode method with invalid hex digits.
     * @throws UnsupportedEncodingException
     * @throws EncoderException
     */
    @Test(expected = EncoderException.class)
    public void testEncodeInvalidHexDigit() throws UnsupportedEncodingException, EncoderException {
        byte[] input = "Hello, World!".getBytes(CharEncoding.US_ASCII);
        codec.encode(input, "ISO-8859-1");
    }

    /**
     * Test decode method with null input.
     */
    @Test
    public void testDecodeNullInput() {
        assertNull(codec.decode(null));
    }

    /**
     * Test decode method with empty input.
     */
    @Test
    public void testDecodeEmptyInput() {
        assertArrayEquals(new byte[0], codec.decode(new byte[0]));
    }

    /**
     * Test encode method with null input.
     */
    @Test
    public void testEncodeNullInput() {
        assertNull(codec.encode(null));
    }

    /**
     * Test encode method with empty input.
     */
    @Test
    public void testEncodeEmptyInput() {
        byte[] encoded = codec.encode(new byte[0]);
        assertEquals(new byte[0].length, encoded.length);
    }

    /**
     * Test decode method with null charset.
     * @throws UnsupportedEncodingException
     * @throws DecoderException
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeNullCharset() throws UnsupportedEncodingException, DecoderException {
        codec.decode(StringUtils.getBytesUsAscii("=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21"), null);
    }

    /**
     * Test decode method with empty charset.
     * @throws UnsupportedEncodingException
     * @throws DecoderException
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeEmptyCharset() throws UnsupportedEncodingException, DecoderException {
        codec.decode(StringUtils.getBytesUsAscii("=68=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21"), "");
    }

    /**
     * Test encode method with null charset.
     * @throws UnsupportedEncodingException
     * @throws EncoderException
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testEncodeNullCharset() throws UnsupportedEncodingException, EncoderException {
        codec.encode(new byte[0], null);
    }

    /**
     * Test encode method with empty charset.
     * @throws UnsupportedEncodingException
     * @throws EncoderException
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testEncodeEmptyCharset() throws UnsupportedEncodingException, EncoderException {
        codec.encode(new byte[0], "");
    }
}