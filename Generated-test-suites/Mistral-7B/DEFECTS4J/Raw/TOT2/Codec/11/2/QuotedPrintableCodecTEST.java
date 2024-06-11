package org.apache.commons.codec.net;

import static org.junit.Assert.*;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.net.QuotedPrintableCodec;

import org.junit.Test;

/**
 * Tests for QuotedPrintableCodec class
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    /**
     * Test encoding and decoding of plain ASCII text
     */
    @Test
    public void testPlainText() throws Exception {
        byte[] input = "Hello World!".getBytes();
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, "US-ASCII");
        assertEquals("144E6F6B656E637265746564", Hex.encodeHexString(encoded));
        byte[] decoded = codec.decode(encoded);
        assertEquals("Hello World!", new String(decoded, "US-ASCII"));
    }

    /**
     * Test encoding and decoding of text with whitespace characters
     */
    @Test
    public void testWhitespace() throws Exception {
        byte[] input = "Hello\tWorld!".getBytes();
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, "US-ASCII");
        assertEquals("144E6F6B656E20576F726C64", Hex.encodeHexString(encoded));
        byte[] decoded = codec.decode(encoded);
        assertEquals("Hello\tWorld!", new String(decoded, "US-ASCII"));
    }

    /**
     * Test encoding and decoding of text with CRLF and CR characters
     */
    @Test
    public void testCrlfAndCr() throws Exception {
        byte[] input = "\r\nHello\rWorld!".getBytes();
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, "US-ASCII");
        assertEquals("1E144D1368656C6C6F20486F726C64", Hex.encodeHexString(encoded));
        byte[] decoded = codec.decode(encoded);
        assertEquals("\r\nHello\rWorld!", new String(decoded, "US-ASCII"));
    }

    /**
     * Test encoding and decoding of text with non-printable characters
     */
    @Test
    public void testNonPrintable() throws Exception {
        byte[] input = "Hello\u0001World!".getBytes();
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, "US-ASCII");
        assertEquals("144E6F6B656E63726574656461", Hex.encodeHexString(encoded));
        byte[] decoded = codec.decode(encoded);
        assertEquals("Hello\u0001World!", new String(decoded, "US-ASCII"));
    }

    /**
     * Test encoding and decoding of binary data with printable characters
     */
    @Test
    public void testBinaryWithPrintable() throws Exception {
        byte[] input = "Hello\u0007World!".getBytes();
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, "US-ASCII");
        assertEquals("144E6F6B656E63726574656467", Hex.encodeHexString(encoded));
        byte[] decoded = codec.decode(encoded);
        assertEquals("Hello\u0007World!", new String(decoded, "US-ASCII"));
    }

    /**
     * Test encoding and decoding of binary data with non-printable characters
     */
    @Test
    public void testBinaryWithNonPrintable() throws Exception {
        byte[] input = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, "US-ASCII");
        assertEquals("41014202430361", Hex.encodeHexString(encoded));
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03, 0x04 }, decoded);
    }

    /**
     * Test decoding of quoted-printable message with missing data
     */
    @Test(expected = DecoderException.class)
    public void testMissingData() throws Exception {
        byte[] input = "14=4E6F6B65".getBytes();
        codec.decode(input);
    }

    /**
     * Test decoding of quoted-printable message with invalid data
     */
    @Test(expected = DecoderException.class)
    public void testInvalidData() throws Exception {
        byte[] input = "14=4E6F6B65X".getBytes();
        codec.decode(input);
    }
}