package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeByteArray() throws DecoderException, EncoderException {
        byte[] input = new byte[] { 0x61, 0x62, 0x63, 0x64, 0x65, 0x66 };
        byte[] expectedOutput = new byte[] { 0x61, 0x62, 0x3D, 0x31, 0x36, 0x31, 0x3D, 0x31, 0x36, 0x32, 0x3D, 0x31, 0x36, 0x33, 0x3D, 0x31, 0x36, 0x34, 0x3D, 0x31, 0x36, 0x35, 0x3D, 0x31, 0x36, 0x36 };

        byte[] output = CODEC.encode(input);
        assertArrayEquals(expectedOutput, output);

        byte[] decodedOutput = CODEC.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeDecodeString() throws DecoderException, EncoderException, UnsupportedEncodingException {
        String input = "abcdef";
        String expectedOutput = "61623D3136313D3136323D3136333D3136343D3136353D313636";
        String output = CODEC.encode(input, CharEncoding.US_ASCII);

        assertEquals(expectedOutput, output);

        String decodedOutput = CODEC.decode(output, CharEncoding.US_ASCII);
        assertEquals(input, decodedOutput);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testEncodeStringWithUnsupportedCharset() throws DecoderException, EncoderException {
        String input = "abcdef";
        CODEC.encode(input, "unknown-charset");
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeStringWithUnsupportedCharset() throws DecoderException {
        String encodedInput = "61623D3136313D3136323D3136333D3136343D3136353D313636";
        CODEC.decode(encodedInput, "unknown-charset");
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidQuotedPrintableData() throws DecoderException {
        String invalidData = "6162=633D3136313D3136323D3136333D3136343D3136353D313636"; // missing second digit in a hex pair

        CODEC.decode(invalidData.getBytes(), CharEncoding.US_ASCII);
    }
}