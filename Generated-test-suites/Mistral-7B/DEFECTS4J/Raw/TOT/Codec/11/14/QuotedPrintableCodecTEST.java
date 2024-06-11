package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for QuotedPrintableCodec
 */
public class QuotedPrintableCodecTest {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final QuotedPrintableCodec QUOTED_PRINTABLE_CODEC = new QuotedPrintableCodec();

    /**
     * Test encoding and decoding of bytes using the default charset
     */
    @Test
    public void testEncodeDecodeBytes() throws EncoderException, DecoderException {
        byte[] source = {0x00, (byte) 0x7F, 0x1A, 0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F, 0x40};
        byte[] encoded = QUOTED_PRINTABLE_CODEC.encode(source);
        byte[] decoded = QUOTED_PRINTABLE_CODEC.decode(encoded);
        Assert.assertArrayEquals(source, decoded);
    }

    /**
     * Test encoding and decoding of a String using the default charset
     */
    @Test
    public void testEncodeDecodeString() throws EncoderException, DecoderException, UnsupportedEncodingException {
        String source = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/=";
        String encoded = QUOTED_PRINTABLE_CODEC.encode(source.getBytes(DEFAULT_CHARSET));
        String decoded = new String(QUOTED_PRINTABLE_CODEC.decode(encoded.getBytes(DEFAULT_CHARSET)), DEFAULT_CHARSET);
        Assert.assertEquals(source, decoded);
    }

    /**
     * Test encoding and decoding of bytes with CR and LF characters
     */
    @Test
    public void testEncodeDecodeCRLF() throws EncoderException, DecoderException {
        byte[] source = {0x0D, 0x0A};
        byte[] encoded = QUOTED_PRINTABLE_CODEC.encode(source);
        byte[] decoded = QUOTED_PRINTABLE_CODEC.decode(encoded);
        Assert.assertArrayEquals(source, decoded);
    }

    /**
     * Test encoding and decoding of a String with CR and LF characters
     */
    @Test
    public void testEncodeDecodeCRLFString() throws EncoderException, DecoderException, UnsupportedEncodingException {
        String source = "\r\n";
        String encoded = QUOTED_PRINTABLE_CODEC.encode(source.getBytes(DEFAULT_CHARSET));
        String decoded = new String(QUOTED_PRINTABLE_CODEC.decode(encoded.getBytes(DEFAULT_CHARSET)), DEFAULT_CHARSET);
        Assert.assertEquals(source, decoded);
    }

    /**
     * Test decoding of a quoted-printable string with an invalid byte sequence
     */
    @Test(expected = DecoderException.class)
    public void testInvalidDecode() throws DecoderException, UnsupportedEncodingException {
        String invalid = "=?=?q?=?=";
        QUOTED_PRINTABLE_CODEC.decode(invalid.getBytes(DEFAULT_CHARSET));
    }

    /**
     * Test encoding and decoding of bytes with whitespace characters
     */
    @Test
    public void testEncodeDecodeWhitespace() throws EncoderException, DecoderException {
        byte[] source = {SPACE, TAB, SPACE};
        byte[] encoded = QUOTED_PRINTABLE_CODEC.encode(source);
        byte[] decoded = QUOTED_PRINTABLE_CODEC.decode(encoded);
        Assert.assertArrayEquals(source, decoded);
    }

    /**
     * Test encoding and decoding of a String with whitespace characters
     */
    @Test
    public void testEncodeDecodeWhitespaceString() throws EncoderException, DecoderException, UnsupportedEncodingException {
        String source = " \t ";
        String encoded = QUOTED_PRINTABLE_CODEC.encode(source.getBytes(DEFAULT_CHARSET));
        String decoded = new String(QUOTED_PRINTABLE_CODEC.decode(encoded.getBytes(DEFAULT_CHARSET)), DEFAULT_CHARSET);
        Assert.assertEquals(source, decoded);
    }
}