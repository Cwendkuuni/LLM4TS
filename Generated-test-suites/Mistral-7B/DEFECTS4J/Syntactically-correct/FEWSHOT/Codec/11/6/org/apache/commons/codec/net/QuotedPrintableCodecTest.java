package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

public class QuotedPrintableCodecTest {
    private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeByteArray() throws EncoderException, DecoderException {
        byte[] input = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        byte[] output = codec.encode(input);
        byte[] decoded = codec.decode(output);

        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws EncoderException, DecoderException, UnsupportedEncodingException {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/";
        String encoded = codec.encode(input.getBytes(CharEncoding.US_ASCII));
        String decoded = new StringDecoder(codec).decodeBuffer(encoded);

        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeObject() throws EncoderException, DecoderException {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/";
        byte[] byteInput = input.getBytes(CharEncoding.US_ASCII);

        Object encoded = codec.encode(byteInput);
        Object decoded = codec.decode(encoded);

        assertEquals(byteInput, decoded);
    }

    @Test
    public void testEncodeNullInput() throws EncoderException {
        byte[] expected = null;

        try {
            codec.encode(null);
        } catch (NullPointerException e) {
            expected = null;
        }

        assertArrayEquals(expected, codec.encode(null));
    }

    @Test
    public void testDecodeNullInput() throws DecoderException {
        byte[] expected = null;

        try {
            codec.decode(null);
        } catch (NullPointerException e) {
            expected = null;
        }

        assertArrayEquals(expected, codec.decode(null));
    }
}