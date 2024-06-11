package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

/**
 * Tests for the QuotedPrintableCodec class.
 */
public class QuotedPrintableCodecTest {

    // Test fixture
    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    /**
     * Test for the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        assertNotNull(CODEC);
    }

    /**
     * Test for the constructor that accepts a charset.
     */
    @Test
    public void testCharsetConstructor() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(CharEncoding.UTF_8);
        assertEquals(CharEncoding.UTF_8, codec.getDefaultCharset());
    }

    /**
     * Test the basic functionality of the encode method with bytes.
     */
    @Test
    public void testEncodeBytes() {
        byte[] input = {0x61, 0x62, 0x63};
        byte[] expectedOutput = {0x61, 0x62, 0x63};
        byte[] output = CODEC.encode(input);
        assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test the basic functionality of the encode method with a string.
     */
    @Test
    public void testEncodeString() throws UnsupportedEncodingException {
        String input = "abc";
        String expectedOutput = StringUtils.newStringUsAscii(CODEC.encode(input.getBytes("US-ASCII")));
        String output = CODEC.encode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test the basic functionality of the decode method with bytes.
     */
    @Test
    public void testDecodeBytes() throws DecoderException {
        byte[] input = {0x61, 0x62, 0x3D, 0x63, 0x3D, 0x63};
        byte[] expectedOutput = {0x61, 0x62, 0x63};
        byte[] output = CODEC.decode(input);
        assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test the basic functionality of the decode method with a string.
     */
    @Test
    public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
        String input = "abc=%63=%63";
        String expectedOutput = "abcccc";
        String output = CODEC.decode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test that decoding an empty string returns an empty string.
     */
    @Test
    public void testDecodeEmptyString() {
        String input = "";
        String output = CODEC.decode(input);
        assertEquals(input, output);
    }

    /**
     * Test that encoding an empty string returns an empty string.
     */
    @Test
    public void testEncodeEmptyString() {
        String input = "";
        String output = CODEC.encode(input);
        assertEquals(input, output);
    }

    /**
     * Test that encoding a null string throws an EncoderException.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeNullString() {
        CODEC.encode(null);
    }

    /**
     * Test that decoding a null byte array throws a DecoderException.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeNullBytes() {
        CODEC.decode(null);
    }

    /**
     * Test that decoding an invalid byte array throws a DecoderException.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidBytes() {
        byte[] input = {0x61, 0x62, 0x3D, 0x65, 0x3D, 0x68};
        assertThrows(DecoderException.class, () -> CODEC.decode(input));
    }

    /**
     * Test that decoding a string with an unsupported charset throws an UnsupportedEncodingException.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeUnsupportedCharset() {
        String input = "abc=%63=%63";
        assertThrows(UnsupportedEncodingException.class, () -> CODEC.decode(input, "ISO-8859-1"));
    }

    /**
     * Test that encoding a null byte array throws a DecoderException.
     */
    @Test(expected = DecoderException.class)
    public void testEncodeNullBytes() {
        CODEC.encode(null);
    }

    /**
     * Test that encoding a string with a null charset throws an IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEncodeNullCharset() {
        assertThrows(IllegalArgumentException.class, () -> CODEC.encode(new String(""), null));
    }

    /**
     * Test that decoding a null string throws a DecoderException.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeNullString() {
        CODEC.decode(null);
    }

    /**
     * Test that decoding an empty string returns an empty string.
     */
    @Test
    public void testDecodeEmptyString() {
        String input = "";
        String output = CODEC.decode(input);
        assertEquals(input, output);
    }

    /**
     * Test that encoding an empty string returns an empty string.
     */
    @Test
    public void testEncodeEmptyString() {
        String input = "";
        String output = CODEC.encode(input);
        assertEquals(input, output);
    }

    /**
     * Test that encoding a string with whitespace at the end results in the whitespace being encoded.
     */
    @Test
    public void testEncodeStringWithWhitespace() throws UnsupportedEncodingException {
        String input = "abc\u0020";
        String expectedOutput = "abc=%20";
        String output = CODEC.encode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test that decoding a string with whitespace at the end returns the decoded string with the whitespace decoded.
     */
    @Test
    public void testDecodeStringWithWhitespace() throws DecoderException, UnsupportedEncodingException {
        String input = "abc=%20";
        String expectedOutput = "abc ";
        String output = CODEC.decode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test that decoding a string with a CRLF line break at the end returns the decoded string with the line break decoded.
     */
    @Test
    public void testDecodeStringWithCRLF() throws DecoderException, UnsupportedEncodingException {
        String input = "abc=%0D=%0A";
        String expectedOutput = "abc\n";
        String output = CODEC.decode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test that decoding a string with a CR line break at the end returns the decoded string with the line break decoded.
     */
    @Test
    public void testDecodeStringWithCR() throws DecoderException, UnsupportedEncodingException {
        String input = "abc=%0D";
        String expectedOutput = "abc\r";
        String output = CODEC.decode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test that decoding a string with a LF line break at the end returns the decoded string with the line break decoded.
     */
    @Test
    public void testDecodeStringWithLF() throws DecoderException, UnsupportedEncodingException {
        String input = "abc=%0A";
        String expectedOutput = "abc\n";
        String output = CODEC.decode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test that encoding a string with a CR line break at the end results in the line break being encoded.
     */
    @Test
    public void testEncodeStringWithCR() throws UnsupportedEncodingException {
        String input = "abc\r";
        String expectedOutput = "abc=%0D";
        String output = CODEC.encode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test that encoding a string with a LF line break at the end results in the line break being encoded.
     */
    @Test
    public void testEncodeStringWithLF() throws UnsupportedEncodingException {
        String input = "abc\n";
        String expectedOutput = "abc=%0A";
        String output = CODEC.encode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test that encoding a string with a CRLF line break at the end results in the line break being encoded.
     */
    @Test
    public void testEncodeStringWithCRLF() throws UnsupportedEncodingException {
        String input = "abc\r\n";
        String expectedOutput = "abc=%0D=%0A";
        String output = CODEC.encode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test that encoding a string with whitespace in it results in the whitespace being encoded.
     */
    @Test
    public void testEncodeStringWithWhitespace() throws UnsupportedEncodingException {
        String input = "abc def\u0020";
        String expectedOutput = "abc def=%20";
        String output = CODEC.encode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test that encoding a string with an ASCII character results in the character being encoded as itself.
     */
    @Test
    public void testEncodeStringWithASCII() throws UnsupportedEncodingException {
        String input = "abc";
        String expectedOutput = "abc";
        String output = CODEC.encode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test that encoding a string with a non-ASCII character results in the character being encoded.
     */
    @Test
    public void testEncodeStringWithNonASCII() throws UnsupportedEncodingException {
        String input = "abc é";
        String expectedOutput = "abc %C3%A9";
        String output = CODEC.encode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test that encoding a string with a printable character results in the character being encoded as itself.
     */
    @Test
    public void testEncodeStringWithPrintable() throws UnsupportedEncodingException {
        String input = "abc! ";
        String expectedOutput = "abc! ";
        String output = CODEC.encode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test that encoding a string with a non-printable character results in the character being encoded.
     */
    @Test
    public void testEncodeStringWithNonPrintable() throws UnsupportedEncodingException {
        String input = "abc#";
        String expectedOutput = "abc=%23";
        String output = CODEC.encode(input);
        assertEquals(expectedOutput, output);
    }
}