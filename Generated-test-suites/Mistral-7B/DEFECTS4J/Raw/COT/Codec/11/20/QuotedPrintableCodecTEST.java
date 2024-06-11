package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.string.StringDecoder;
import org.apache.commons.codec.string.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

/**
 * Test class for QuotedPrintableCodec
 */
public class QuotedPrintableCodecTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Assert.assertNotNull(codec);
    }

    /**
     * Test encode method with default charset
     */
    @Test
    public void testEncodeDefaultCharset() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] bytes = "Hello World!".getBytes(StandardCharsets.UTF_8);
        byte[] encodedBytes = codec.encode(bytes);
        String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);
        Assert.assertEquals("=3Chelloworld%21%3D", encodedString);
    }

    /**
     * Test encode method with custom charset
     */
    @Test
    public void testEncodeCustomCharset() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-1");
        byte[] bytes = "Hello World!".getBytes(StandardCharsets.UTF_8);
        byte[] encodedBytes = codec.encode(bytes);
        String encodedString = new String(encodedBytes, "ISO-8859-1");
        Assert.assertEquals("=3Chelloworld%21%3D", encodedString);
    }

    /**
     * Test decode method with default charset
     */
    @Test
    public void testDecodeDefaultCharset() throws DecoderException, UnsupportedEncodingException {
        String encodedString = "=3Chelloworld%21%3D";
        byte[] encodedBytes = StringUtils.getBytesUsAscii(encodedString);
        byte[] decodedBytes = new QuotedPrintableCodec().decode(encodedBytes);
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        Assert.assertEquals("Hello World!", decodedString);
    }

    /**
     * Test decode method with custom charset
     */
    @Test
    public void testDecodeCustomCharset() throws DecoderException, UnsupportedEncodingException {
        String encodedString = "=3Chelloworld%21%3D";
        byte[] encodedBytes = StringUtils.getBytesUsAscii(encodedString);
        QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-1");
        byte[] decodedBytes = codec.decode(encodedBytes);
        String decodedString = new String(decodedBytes, "ISO-8859-1");
        Assert.assertEquals("Hello World!", decodedString);
    }

    /**
     * Test encode method edge cases
     */
    @Test
    public void testEncodeEdgeCases() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] bytes = new byte[256];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) i;
            byte[] encodedBytes = codec.encode(new byte[]{bytes[i]});
            String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);

            if (PRINTABLE_CHARS.get(bytes[i])) {
                Assert.assertEquals(String.valueOf((char) bytes[i]), encodedString);
            } else {
                Assert.assertTrue(encodedString.startsWith("="));
                Assert.assertTrue(encodedString.length() > 2);
            }
        }
    }

    /**
     * Test decode method edge cases
     */
    @Test
    public void testDecodeEdgeCases() throws DecoderException, UnsupportedEncodingException {
        String encodedString = "";

        for (int i = 0; i < 256; i++) {
            byte b = (byte) i;
            if (PRINTABLE_CHARS.get(b)) {
                encodedString += String.valueOf((char) b);
            } else {
                encodedString += "=" + String.format("%02X", (int) b);
            }
        }

        byte[] encodedBytes = StringUtils.getBytesUsAscii(encodedString);
        byte[] decodedBytes = new QuotedPrintableCodec().decode(encodedBytes);
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        Assert.assertEquals(new String(encodedBytes, StandardCharsets.UTF_8), decodedString);
    }

    /**
     * Test decode method with invalid input
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() throws DecoderException, UnsupportedEncodingException {
        String encodedString = "=3C%=3D";
        byte[] encodedBytes = StringUtils.getBytesUsAscii(encodedString);
        new QuotedPrintableCodec().decode(encodedBytes);
    }
}