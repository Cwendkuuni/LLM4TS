package org.apache.commons.codec.net;

import static org.junit.Assert.*;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeBytes() throws Exception {
        byte[] input = new byte[]{(byte) 0x01, (byte) 0x2a, (byte) 0x33, (byte) 0x44, (byte) 0x55, (byte) 0x66};
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] output = codec.encode(input);
        byte[] decoded = codec.decode(output);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        String input = "foo\r\nbar\r\nbaz";
        String charset = "US-ASCII";
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String output = codec.encode(input, charset);
        String decoded = codec.decode(output, charset);
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeObject() throws Exception {
        String input = "foo\r\nbar\r\nbaz";
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object encoded = codec.encode(input);
        Object decoded = codec.decode(encoded);
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringDefaultCharset() throws Exception {
        String input = "foo\r\nbar\r\nbaz";
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String output = codec.encode(input);
        String decoded = codec.decode(output);
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeBytesDefaultCharset() throws Exception {
        byte[] input = new byte[]{(byte) 0x01, (byte) 0x2a, (byte) 0x33, (byte) 0x44, (byte) 0x55, (byte) 0x66};
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] output = codec.encode(input);
        byte[] decoded = codec.decode(output);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringUTF8() throws Exception {
        String input = "foo\r\nbar\r\nbaz";
        String charset = "UTF-8";
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String output = codec.encode(input, charset);
        String decoded = codec.decode(output, charset);
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringInvalidCharset() throws Exception {
        String input = "foo\r\nbar\r\nbaz";
        String charset = "ISO-8859-1";
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        try {
            String output = codec.encode(input, charset);
            fail("Expected UnsupportedEncodingException");
        } catch (UnsupportedEncodingException e) {
            // expected
        }
        try {
            String decoded = codec.decode(output, charset);
            fail("Expected DecoderException");
        } catch (DecoderException e) {
            // expected
        }
    }
}