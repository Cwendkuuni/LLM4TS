package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.encoder.Hex;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class QuotedPrintableCodecTest {

    private QuotedPrintableCodec codec;

    @Test
    public void testDefaultConstructor() {
        codec = new QuotedPrintableCodec();
    }

    @Test
    public void testQuotedPrintableCodecWithUtf8Charset() {
        codec = new QuotedPrintableCodec(StandardCharsets.UTF_8.name());
    }

    @Test
    public void testEncodeDecodeBytes() throws DecoderException {
        byte[] input = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        byte[] encodedBytes = codec.encode(input);
        byte[] decodedBytes = codec.decode(encodedBytes);
        assertArrayEquals(input, decodedBytes);
    }

    @Test
    public void testEncodeDecodeString() throws DecoderException, UnsupportedEncodingException {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String encodedString = codec.encode(input.getBytes(StandardCharsets.UTF_8));
        String decodedString = codec.decode(encodedString, StandardCharsets.UTF_8.name());
        assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeDecodeByteArrayStringWithUnsupportedCharset() throws DecoderException, UnsupportedEncodingException {
        byte[] input = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        String encodedString = codec.encode(input).toString();
        byte[] decodedBytes = codec.decode(new Hex().decodeHex(encodedString.toCharArray()), "invalidCharset");
        assertNull(decodedBytes);
    }
}