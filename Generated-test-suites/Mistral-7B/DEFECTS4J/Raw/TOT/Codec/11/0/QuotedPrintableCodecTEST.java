package org.apache.commons.codec.net;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Random;

/**
 * Tests for QuotedPrintableCodec class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Test encoding and decoding of a simple string.
     */
    @Test
    public void testEncodeDecodeSimpleString() throws Exception {
        String original = "Hello World!";
        String encoded = new QuotedPrintableCodec().encode(original).toString();
        String decoded = new String(new QuotedPrintableCodec().decode(encoded.getBytes()), StandardCharsets.UTF_8);
        Assert.assertEquals(original, decoded);
    }

    /**
     * Test encoding of a string with special characters.
     */
    @Test
    public void testEncodeSpecialCharacters() throws Exception {
        String original = "Hello\nM\"öö world!";
        String encoded = new QuotedPrintableCodec().encode(original).toString();
        String decoded = new String(new QuotedPrintableCodec().decode(encoded.getBytes()), StandardCharsets.UTF_8);
        Assert.assertEquals(original, decoded);
    }

    /**
     * Test encoding of binary data.
     */
    @Test
    public void testEncodeBinaryData() throws Exception {
        byte[] original = {0x01, 0x02, 0x03, 0x04};
        byte[] encoded = new QuotedPrintableCodec().encode(original);
        byte[] decoded = new QuotedPrintableCodec().decode(encoded);
        Assert.assertArrayEquals(original, decoded);
    }

    /**
     * Test decoding of invalid quoted-printable data.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidData() throws Exception {
        String invalidData = "=?\n";
        new QuotedPrintableCodec().decode(invalidData.getBytes());
    }

    /**
     * Test decoding of data with missing whitespace.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeMissingWhitespace() throws Exception {
        String missingWhitespace = "=7D=7D=7D";
        new QuotedPrintableCodec().decode(missingWhitespace.getBytes());
    }

    /**
     * Test decoding of data with trailing whitespace.
     */
    @Test
    public void testDecodeTrailingWhitespace() throws Exception {
        String trailingWhitespace = "=7D=7D=7D=\r\n";
        byte[] decoded = new QuotedPrintableCodec().decode(trailingWhitespace.getBytes());
        Assert.assertArrayEquals(new byte[]{0x7D, 0x7D, 0x7D}, decoded);
    }

    /**
     * Test encoding and decoding of large data.
     */
    @Test
    public void testEncodeDecodeLargeData() throws IOException, DecoderException {
        Random random = new Random();
        byte[] original = new byte[1024 * 1024];
        random.nextBytes(original);
        byte[] encoded = new QuotedPrintableCodec().encode(original);
        byte[] decoded = new QuotedPrintableCodec().decode(encoded);
        Assert.assertArrayEquals(original, decoded);
    }

    /**
     * Test encoding of large data with custom printable characters.
     */
    @Test
    public void testEncodeLargeDataCustomPrintable() throws IOException, DecoderException {
        Random random = new Random();
        byte[] original = new byte[1024 * 1024];
        random.nextBytes(original);
        BitSet printable = new BitSet(256);
        printable.set(0x21);
        printable.set(0x23);
        printable.set(0x25);
        printable.set(0x27);
        printable.set(0x2A);
        printable.set(0x2B);
        printable.set(0x2D);
        printable.set(0x2F);
        printable.set(0x3B);
        printable.set(0x3D);
        QuotedPrintableCodec codec = new QuotedPrintableCodec(new StringEncoder() {
            @Override
            public String encode(byte[] bytes, String charset) throws EncoderException, UnsupportedEncodingException {
                byte[] encodedBytes = new QuotedPrintableCodec(new BitSet(256, printable)).encode(bytes);
                return StringUtils.newStringUsAscii(encodedBytes);
            }
        });
        byte[] encoded = codec.encode(original);
        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals(original, decoded);
    }
}