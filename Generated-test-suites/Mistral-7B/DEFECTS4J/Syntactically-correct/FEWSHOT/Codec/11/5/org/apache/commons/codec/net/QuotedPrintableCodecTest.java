package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeBytes() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "Hello World!".getBytes();
        byte[] output = codec.encode(input);
        byte[] decoded = codec.decode(output);

        assertEquals(input.length, output.length);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "Hello World!";
        String output = codec.encode(input, CharEncoding.UTF_8);
        String decoded = codec.decode(output, CharEncoding.UTF_8);

        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeObject() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "Hello World!";
        Object encoded = codec.encode(input);
        Object decoded = codec.decode(encoded);

        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeByteArraysWithDifferentCharsets() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String charset1 = "UTF-8";
        String charset2 = "ISO-8859-1";

        byte[] input1 = "Hello World!".getBytes(CharEncoding.getCharset(charset1));
        byte[] output = codec.encode(input1);
        byte[] decoded1 = codec.decode(output);

        byte[] input2 = StringUtils.getBytesUsAscii("Hello World!");
        byte[] decoded2 = codec.decode(output, charset2);

        assertEquals(input1.length, output.length);
        assertArrayEquals(input1, decoded1);
        assertEquals(input2.length, decoded2.length);
        assertArrayEquals(input2, decoded2);
    }

    @Test
    public void testEncodeDecodeStringWithDifferentCharsets() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String charset1 = "UTF-8";
        String charset2 = "ISO-8859-1";

        String input1 = "Hello World!";
        String output = codec.encode(input1, CharEncoding.getCharset(charset1));
        String decoded1 = codec.decode(output, CharEncoding.getCharset(charset1));
        String decoded2 = codec.decode(output, charset2);

        assertEquals(input1, decoded1);
        assertEquals(input1, decoded2);
    }

    @Test
    public void testEncodeDecodeObjectWithDifferentCharsets() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String charset1 = "UTF-8";
        String charset2 = "ISO-8859-1";

        String input = "Hello World!";
        Object encoded = codec.encode(input);
        Object decoded1 = codec.decode(encoded);
        Object decoded2 = codec.decode(encoded, charset2);

        assertEquals(input, decoded1);
        assertEquals(input, decoded2);
    }

    @Test
    public void testEncodeWhitespace() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "Hello\tWorld!".getBytes();
        byte[] output = codec.encode(input);
        byte[] decoded = codec.decode(output);

        assertEquals(input.length, output.length);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeCRLF() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "\r\n".getBytes();
        byte[] output = codec.encode(input);
        byte[] decoded = codec.decode(output);

        assertEquals(input.length, output.length);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testDecodeInvalid() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "invalid".getBytes();
        byte[] output = codec.encode(input);

        try {
            codec.decode(output);
            fail("DecoderException expected");
        } catch (DecoderException e) {
            // expected
        }
    }

    @Test
    public void testDecodeNullInput() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        try {
            codec.decode(null);
            fail("DecoderException expected");
        } catch (DecoderException e) {
            // expected
        }
    }
}