package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * Test class for QuotedPrintableCodec.
 */
public class QuotedPrintableCodecTest {

    // Testing instance of QuotedPrintableCodec
    private QuotedPrintableCodec codec = new QuotedPrintableCodec();

    /**
     * Test method for {@link QuotedPrintableCodec#encode(byte[], byte[])}.
     */
    @Test
    public void testEncode() throws UnsupportedEncodingException, DecoderException {
        // Test encoding with default charset
        byte[] input = "Mégane Renault Sport 225".getBytes(CharEncoding.UTF_8);
        byte[] expectedOutput = new byte[]{(byte) 0x2d, (byte) 0x31, (byte) 0x33, (byte) 0x31, (byte) 0x20, (byte) 0x52, (byte) 0x65, (byte) 0x65, (byte) 0x6e, (byte) 0x61, (byte) 0x6d, (byte) 0x65, (byte) 0x20, (byte) 0x52, (byte) 0x61, (byte) 0x6e, (byte) 0x69, (byte) 0x6e, (byte) 0x64, (byte) 0x20, (byte) 0x32, (byte) 0x32, (byte) 0x35, (byte) 0x0D, 0x0A};
        byte[] output = codec.encode(input);
        assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test method for {@link QuotedPrintableCodec#decode(byte[], byte[])}.
     */
    @Test
    public void testDecode() throws UnsupportedEncodingException, DecoderException {
        byte[] input = new byte[]{(byte) 0x2d, (byte) 0x31, (byte) 0x33, (byte) 0x31, (byte) 0x20, (byte) 0x52, (byte) 0x65, (byte) 0x65, (byte) 0x6e, (byte) 0x61, (byte) 0x6d, (byte) 0x65, (byte) 0x20, (byte) 0x52, (byte) 0x61, (byte) 0x6e, (byte) 0x69, (byte) 0x6e, (byte) 0x64, (byte) 0x20, (byte) 0x32, (byte) 0x32, (byte) 0x35, (byte) 0x0D, 0x0A};
        String expectedOutput = "Mégane Renault Sport 225";
        String output = new String(codec.decode(input), CharEncoding.UTF_8);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test method for {@link QuotedPrintableCodec#encode(String, String)}.
     */
    @Test
    public void testEncodeString() throws UnsupportedEncodingException, DecoderException {
        String input = "Mégane Renault Sport 225";
        String expectedOutput = "=\r\n2d 31 33 1 52 65 65 6e 61 6d 65 2 52 61 6e 69 6e 64 2 32 32 35 \r\n";
        String output = codec.encode(input, CharEncoding.UTF_8);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test method for {@link QuotedPrintableCodec#decode(String, String)}.
     */
    @Test
    public void testDecodeString() throws UnsupportedEncodingException, DecoderException {
        String input = "=\r\n2d 31 33 1 52 65 65 6e 61 6d 65 2 52 61 6e 69 6e 64 2 32 32 35 \r\n";
        String expectedOutput = "Mégane Renault Sport 225";
        String output = codec.decode(input, CharEncoding.UTF_8);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test method for {@link QuotedPrintableCodec#decode(byte[], byte[])}.
     */
    @Test
    public void testDecodeByteArray() throws UnsupportedEncodingException, DecoderException {
        byte[] input = new byte[]{(byte) 0x2d, (byte) 0x31, (byte) 0x33, (byte) 0x31, (byte) 0x20, (byte) 0x52, (byte) 0x65, (byte) 0x65, (byte) 0x6e, (byte) 0x61, (byte) 0x6d, (byte) 0x65, (byte) 0x20, (byte) 0x52, (byte) 0x61, (byte) 0x6e, (byte) 0x69, (byte) 0x6e, (byte) 0x64, (byte) 0x20, (byte) 0x32, (byte) 0x32, (byte) 0x35, (byte) 0x0D, 0x0A};
        String expectedOutput = "Mégane Renault Sport 225";
        String output = new String(codec.decode(input), CharEncoding.UTF_8);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test method for {@link QuotedPrintableCodec#encode(Object, String)}.
     */
    @Test
    public void testEncodeObject() throws UnsupportedEncodingException, DecoderException {
        String input = "Mégane Renault Sport 225";
        String expectedOutput = "=\r\n2d 31 33 1 52 65 65 6e 61 6d 65 2 52 61 6e 69 6e 64 2 32 32 35 \r\n";
        String output = codec.encode(input).toString();
        assertEquals(expectedOutput, output);
    }

    /**
     * Test method for {@link QuotedPrintableCodec#decode(Object, String)}.
     */
    @Test
    public void testDecodeObject() throws UnsupportedEncodingException, DecoderException {
        String input = "=\r\n2d 31 33 1 52 65 65 6e 61 6d 65 2 52 61 6e 69 6e 64 2 32 32 35 \r\n";
        String expectedOutput = "Mégane Renault Sport 225";
        String output = (String) codec.decode(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test method for encoding invalid UTF-8 characters.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testEncodeInvalidUTF8() throws UnsupportedEncodingException, DecoderException {
        String input = "Mégane Renault Sport ♫ 225";
        codec.encode(input, CharEncoding.UTF_8);
    }

    /**
     * Test method for decoding invalid quoted-printable strings.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidQuotedPrintable() throws UnsupportedEncodingException, DecoderException {
        String input = "=\r\n2d 31 33 1 52 65 65 6e 61 6d 65 2 52 61 6e 69 6e 64 2 32 32 35 2 32 32";
        codec.decode(input.getBytes(CharEncoding.UTF_8), CharEncoding.UTF_8);
    }

    /**
     * Test method for decoding an empty string.
     */
    @Test
    public void testDecodeEmptyString() throws UnsupportedEncodingException, DecoderException {
        String input = "";
        String output = codec.decode(input, CharEncoding.UTF_8);
        assertEquals(input, output);
    }

    /**
     * Test method for decoding a null string.
     */
    @Test(expected = NullPointerException.class)
    public void testDecodeNullString() throws UnsupportedEncodingException, DecoderException {
        codec.decode(null, CharEncoding.UTF_8);
    }

    /**
     * Test method for encoding a null byte array.
     */
    @Test(expected = NullPointerException.class)
    public void testEncodeNullByteArray() throws UnsupportedEncodingException, DecoderException {
        byte[] input = null;
        codec.encode(input);
    }
}