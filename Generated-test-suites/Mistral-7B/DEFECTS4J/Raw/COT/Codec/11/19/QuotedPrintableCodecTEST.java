package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec QUOTED_PRINTABLE_CODEC = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeDefaultCharset() throws EncoderException, DecoderException, UnsupportedEncodingException {
        String input = "Test Quoted Printable Encoding";
        byte[] encodedBytes = QUOTED_PRINTABLE_CODEC.encode(input.getBytes(CharEncoding.US_ASCII));
        String decodedString = new String(QUOTED_PRINTABLE_CODEC.decode(encodedBytes), CharEncoding.US_ASCII);
        assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeDecodeUtf8Charset() throws EncoderException, DecoderException, UnsupportedEncodingException {
        String input = "Test Quoted Printable Encoding 안녕하세요";
        byte[] encodedBytes = QUOTED_PRINTABLE_CODEC.encode(input.getBytes(CharEncoding.UTF_8));
        String decodedString = new String(QUOTED_PRINTABLE_CODEC.decode(encodedBytes), CharEncoding.UTF_8);
        assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeBinaryData() throws EncoderException {
        byte[] input = {0x01, 0x02, 0x03, 0x04};
        byte[] encodedBytes = QUOTED_PRINTABLE_CODEC.encode(input);
        byte[] expectedEncodedBytes = new byte[]{0x01, 0x02, 0x3D, 0x31, 0x30, 0x3D, 0x31, 0x32, 0x3D, 0x31, 0x33, 0x3D, 0x31, 0x34};
        assertArrayEquals(expectedEncodedBytes, encodedBytes);
    }

    @Test
    public void testDecodeBinaryData() throws DecoderException, UnsupportedEncodingException {
        byte[] input = {0x01, 0x3D, 0x31, 0x30, 0x3D, 0x31, 0x32, 0x3D, 0x31, 0x33, 0x3D, 0x31, 0x34};
        byte[] decodedBytes = QUOTED_PRINTABLE_CODEC.decode(input);
        byte[] expectedDecodedBytes = {0x01, 0x02, 0x03, 0x04};
        assertArrayEquals(expectedDecodedBytes, decodedBytes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeNullInput() throws EncoderException {
        byte[] input = null;
        QUOTED_PRINTABLE_CODEC.encode(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeNullInput() throws DecoderException, UnsupportedEncodingException {
        byte[] input = null;
        QUOTED_PRINTABLE_CODEC.decode(input);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidQuotedPrintableData() throws DecoderException, UnsupportedEncodingException {
        byte[] input = {0x01, 0x3D, 0x31, 0x30, 0x3D, 0x31, 0x32, 0x3D, 0x31, 0x33, 0x3D, 0x31, 0x34, 0x05};
        QUOTED_PRINTABLE_CODEC.decode(input);
    }
}