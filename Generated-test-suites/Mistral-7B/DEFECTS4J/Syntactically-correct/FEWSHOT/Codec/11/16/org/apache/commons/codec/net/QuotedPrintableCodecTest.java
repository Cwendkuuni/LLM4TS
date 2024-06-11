package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeByteArray() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        byte[] output = codec.encode(input);
        byte[] decodedOutput = codec.decode(output);

        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String output = codec.encode(input, CharEncoding.UTF_8.name());
        String decodedOutput = codec.decode(output, CharEncoding.UTF_8.name());

        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeDecodeObject() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Object encodedInput = codec.encode(input);
        Object decodedOutput = codec.decode(encodedInput);

        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeDecodeStringWithDefaultCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String output = codec.encode(input);
        String decodedOutput = codec.decode(output);

        assertEquals(input, decodedOutput);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testEncodeDecodeStringWithUnsupportedCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String output = codec.encode(input, "XYZ");
        // UnsupportedEncodingException is expected
    }

    @Test
    public void testEncodeStringWithUnicodeCharacter() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abcδefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String output = codec.encode(input, CharEncoding.UTF_8.name());
        String decodedOutput = codec.decode(output, CharEncoding.UTF_8.name());

        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeByteWithWhitespace() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte input = (byte) 0x20;
        byte[] output = codec.encode(new byte[]{input});
        byte decodedOutput = codec.decode(output)[0];

        assertEquals((byte) 0x20, decodedOutput);
    }

    @Test
    public void testEncodeByteWithCR() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte input = 0x0D;
        byte[] output = codec.encode(new byte[]{input});
        byte[] decodedOutput = codec.decode(output);

        assertEquals(1, decodedOutput.length);
        assertEquals(0, decodedOutput[0]);
    }

    @Test
    public void testEncodeByteWithLF() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte input = 0x0A;
        byte[] output = codec.encode(new byte[]{input});
        byte[] decodedOutput = codec.decode(output);

        assertEquals(1, decodedOutput.length);
        assertEquals(0, decodedOutput[0]);
    }

    @Test
    public void testEncodeByteWithCRAndLF() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte input = (byte) 0x0D;
        byte cr = input;
        input = (byte) 0x0A;
        byte lf = input;
        byte[] output = codec.encode(new byte[]{cr, lf});
        byte[] decodedOutput = codec.decode(output);

        assertEquals(2, decodedOutput.length);
        assertEquals(0, decodedOutput[0]);
        assertEquals(0, decodedOutput[1]);
    }
}