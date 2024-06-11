package org.apache.commons.codec.net;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

/**
 * Test cases for QuotedPrintableCodec class.
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec QUOTED_PRINTABLE_CODEC = new QuotedPrintableCodec();

    /**
     * Test encoding and decoding of bytes using the default charset.
     */
    @Test
    public void testEncodeDecodeBytes() throws DecoderException, EncoderException, IOException {
        byte[] bytes = {0x61, 0x62, 0x63, 0x0D, 0x0A};
        byte[] encodedBytes = QUOTED_PRINTABLE_CODEC.encode(bytes);
        byte[] decodedBytes = QUOTED_PRINTABLE_CODEC.decode(encodedBytes);

        Assert.assertArrayEquals(bytes, decodedBytes);
    }

    /**
     * Test encoding and decoding of a string using the default charset.
     */
    @Test
    public void testEncodeDecodeString() throws DecoderException, EncoderException {
        String input = "abc\r\n";
        String encoded = QUOTED_PRINTABLE_CODEC.encode(input);
        String decoded = QUOTED_PRINTABLE_CODEC.decode(encoded, StandardCharsets.UTF_8);

        Assert.assertEquals(input, decoded);
    }

    /**
     * Test encoding and decoding of a string using a custom charset.
     */
    @Test
    public void testEncodeDecodeStringCustomCharset() throws DecoderException, EncoderException, UnsupportedEncodingException {
        String input = "abc\r\n";
        String charset = "ISO-8859-1";
        String encoded = QUOTED_PRINTABLE_CODEC.encode(input, CharEncoding.getCharset(charset)).getName();
        String decoded = QUOTED_PRINTABLE_CODEC.decode(encoded, CharEncoding.getCharset(charset)).getName();

        Assert.assertEquals(input, decoded);
    }

    /**
     * Test encoding a byte array with printable characters.
     */
    @Test
    public void testEncodePrintableBytes() throws EncoderException {
        byte[] bytes = {0x41, 0x42, 0x43, 0x20, 0x65, 0x78, 0x65, 0x20, 0x61, 0x6d, 0x65, 0x0D, 0x0A};
        byte[] encodedBytes = QUOTED_PRINTABLE_CODEC.encode(bytes);

        String expected = "ABC  Hello world.\r\n";
        String result = new String(encodedBytes, StandardCharsets.UTF_8);

        Assert.assertEquals(expected, result);
    }

    /**
     * Test decoding a byte array with encoded printable characters.
     */
    @Test
    public void testDecodePrintableBytes() throws DecoderException, IOException {
        byte[] encodedBytes = {0x50, 0x41, 0x52, 0x2D, 0x3D, 0x3D, 0x61, 0x63, 0x63, 0x65, 0x73, 0x73, 0x2D, 0x3D, 0x3D,
                0x20, 0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x2D, 0x3D, 0x3D, 0x0D, 0x0A};
        byte[] decodedBytes = QUOTED_PRINTABLE_CODEC.decode(encodedBytes);

        byte[] expectedBytes = {0x41, 0x42, 0x43, 0x20, 0x65, 0x78, 0x65, 0x20, 0x61, 0x6d, 0x65};
        Assert.assertArrayEquals(expectedBytes, decodedBytes);
    }

    /**
     * Test decoding a byte array with non-printable characters.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeNonPrintableBytes() throws DecoderException, IOException {
        byte[] encodedBytes = {0x41, 0x42, 0x43, 0x2D, 0x3D, 0x3D, 0x5C, 0x61, 0x62, 0x63, 0x2D, 0x3D, 0x3D, 0x0D, 0x0A};
        QUOTED_PRINTABLE_CODEC.decode(encodedBytes);
    }

    /**
     * Test encoding a byte array with non-printable characters.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeNonPrintableBytes() throws EncoderException, IOException {
        byte[] bytes = {0x41, 0x42, 0x43, 0x5C, 0x61, 0x62, 0x63};
        QUOTED_PRINTABLE_CODEC.encode(bytes);
    }

    /**
     * Test encoding a byte array with whitespace at the end.
     */
    @Test
    public void testEncodeWhitespaceBytes() throws EncoderException {
        byte[] bytes = {0x41, 0x42, 0x43, 0x20, 0x0D, 0x0A};
        byte[] encodedBytes = QUOTED_PRINTABLE_CODEC.encode(bytes);

        String expected = "ABC \r\n";
        String result = new String(encodedBytes, StandardCharsets.UTF_8);

        Assert.assertEquals(expected, result);
    }

    /**
     * Test decoding a byte array with whitespace at the end.
     */
    @Test
    public void testDecodeWhitespaceBytes() throws DecoderException, IOException {
        byte[] encodedBytes = {0x41, 0x42, 0x43, 0x2D, 0x3D, 0x3D, 0x20, 0x0D, 0x0A, 0x20};
        byte[] decodedBytes = QUOTED_PRINTABLE_CODEC.decode(encodedBytes);

        byte[] expectedBytes = {0x41, 0x42, 0x43};
        Assert.assertArrayEquals(expectedBytes, decodedBytes);
    }

    /**
     * Test decoding a byte array with a CR at the end.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeCrBytes() throws DecoderException, IOException {
        byte[] encodedBytes = {0x41, 0x42, 0x43, 0x2D, 0x3D, 0x3D, 0x0D};
        QUOTED_PRINTABLE_CODEC.decode(encodedBytes);
    }

    /**
     * Test decoding a byte array with a LF at the end.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeLfBytes() throws DecoderException, IOException {
        byte[] encodedBytes = {0x41, 0x42, 0x43, 0x2D, 0x3D, 0x3D, 0x0A};
        QUOTED_PRINTABLE_CODEC.decode(encodedBytes);
    }

    /**
     * Test decoding a byte array with both CR and LF at the end.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeCrLfBytes() throws DecoderException, IOException {
        byte[] encodedBytes = {0x41, 0x42, 0x43, 0x2D, 0x3D, 0x3D, 0x0D, 0x0A};
        QUOTED_PRINTABLE_CODEC.decode(encodedBytes);
    }
}