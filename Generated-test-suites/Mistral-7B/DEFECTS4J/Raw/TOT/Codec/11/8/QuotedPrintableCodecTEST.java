package org.apache.commons.codec.net;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

/**
 * Test for QuotedPrintableCodec class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Test encoding and decoding of printable characters.
     */
    @Test
    public void testPrintable() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // encode
        byte[] encodedBytes = codec.encode("Hello World!".getBytes(StandardCharsets.UTF_8));
        String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);
        Assert.assertEquals("140D68656C6C6F20576F726C64210D", encodedString);

        // decode
        byte[] decodedBytes = codec.decode(encodedBytes);
        Assert.assertArrayEquals("Hello World!".getBytes(StandardCharsets.UTF_8), decodedBytes);
    }

    /**
     * Test encoding and decoding of non-printable characters.
     */
    @Test
    public void testNonPrintable() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // encode
        byte[] input = new byte[]{(byte) 0x80, (byte) 0x81, (byte) 0x82};
        byte[] encodedBytes = codec.encode(input);
        String encodedString = Hex.encodeHexString(encodedBytes);
        Assert.assertEquals("3D30803D3138313D313832", encodedString);

        // decode
        byte[] decodedBytes = codec.decode(Hex.decodeHex(encodedString.toCharArray()));
        Assert.assertArrayEquals(input, decodedBytes);
    }

    /**
     * Test encoding and decoding of whitespace characters.
     */
    @Test
    public void testWhitespace() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // encode
        byte[] input = new byte[]{(byte) ' ', (byte) '\t'};
        byte[] encodedBytes = codec.encode(input);
        String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);
        Assert.assertEquals("3D20203D09", encodedString);

        // decode
        byte[] decodedBytes = codec.decode(Hex.decodeHex(encodedString.toCharArray()));
        Assert.assertArrayEquals(input, decodedBytes);
    }

    /**
     * Test handling of empty input.
     */
    @Test
    public void testEmpty() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // encode
        byte[] input = new byte[0];
        byte[] encodedBytes = codec.encode(input);
        Assert.assertEquals(new byte[0][], encodedBytes);

        // decode
        byte[] decodedBytes = codec.decode(new byte[0]);
        Assert.assertEquals(new byte[0][], decodedBytes);
    }

    /**
     * Test handling of invalid input.
     */
    @Test(expected = DecoderException.class)
    public void testInvalid() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // decode
        codec.decode(new byte[]{0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D});
    }

    /**
     * Test handling of invalid encoding.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testInvalidEncoding() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // decode
        codec.decode("Invalid encoding".getBytes("ISO-8859-1"));
    }
}