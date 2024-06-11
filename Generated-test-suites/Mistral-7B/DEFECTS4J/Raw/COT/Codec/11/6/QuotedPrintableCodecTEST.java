package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

/**
 * Test class for QuotedPrintableCodec
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    /**
     * Test constructor.
     */
    @Test
    public void testQuotedPrintableCodecConstructor() {
        assertEquals("UTF-8", CODEC.getDefaultCharset());
    }

    /**
     * Test encode method with byte array.
     */
    @Test
    public void testEncodeByteArray() {
        byte[] bytes = {0x68, 0x65, 0x6C, 0x6C, 0x6F};
        byte[] encoded = CODEC.encode(bytes);
        byte[] expected = {0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x3D, 0x63, 0x61, 0x6D, 0x65, 0x3D, 0x63, 0x64, 0x6F};
        assertArrayEquals(expected, encoded);
    }

    /**
     * Test encode method with String.
     */
    @Test
    public void testEncodeString() {
        String string = "hello";
        String encoded = CODEC.encode(string);
        String expected = "hello=" + StringUtils.newStringFromBytes(CODEC.encode(string.getBytes(CharEncoding.UTF_8)));
        assertEquals(expected, encoded);
    }

    /**
     * Test decode method with byte array.
     */
    @Test
    public void testDecodeByteArray() throws DecoderException {
        byte[] encoded = {0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x3D, 0x63, 0x61, 0x6D, 0x65, 0x3D, 0x63, 0x64, 0x6F};
        byte[] decoded = CODEC.decode(encoded);
        byte[] expected = {0x68, 0x65, 0x6C, 0x6C, 0x6F};
        assertArrayEquals(expected, decoded);
    }

    /**
     * Test decode method with String.
     */
    @Test
    public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
        String encoded = "hello=68656C6C6F";
        String decoded = CODEC.decode(encoded);
        assertEquals(new String(CODEC.decode(StringUtils.getBytesUsAscii(encoded.substring(0, encoded.lastIndexOf("=")))),
                CharEncoding.UTF_8), decoded);
    }

    /**
     * Test encode method with Object (byte array).
     */
    @Test
    public void testEncodeObjectByteArray() throws EncoderException {
        byte[] bytes = {0x68, 0x65, 0x6C, 0x6C, 0x6F};
        Object encoded = CODEC.encode(bytes);
        assertEquals(new String(CODEC.encode(bytes)), encoded.toString());
    }

    /**
     * Test encode method with Object (String).
     */
    @Test
    public void testEncodeObjectString() throws EncoderException, UnsupportedEncodingException {
        String string = "hello";
        Object encoded = CODEC.encode(string);
        assertEquals(CODEC.encode(string.getBytes(CharEncoding.UTF_8)), encoded);
    }

    /**
     * Test decode method with Object (byte array).
     */
    @Test
    public void testDecodeObjectByteArray() throws DecoderException {
        byte[] encoded = {0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x3D, 0x63, 0x61, 0x6D, 0x65, 0x3D, 0x63, 0x64, 0x6F};
        Object decoded = CODEC.decode(encoded);
        assertEquals(new String(CODEC.decode(encoded)), decoded.toString());
    }

    /**
     * Test decode method with Object (String).
     */
    @Test(expected = DecoderException.class)
    public void testDecodeObjectStringInvalid() {
        String encoded = "hello=";
        CODEC.decode(encoded);
    }

    /**
     * Test decode method with Object (String, charset) valid.
     */
    @Test
    public void testDecodeObjectStringValid() throws DecoderException, UnsupportedEncodingException {
        String encoded = "hello=68656C6C6F";
        String decoded = CODEC.decode(encoded, CharEncoding.UTF_8);
        assertEquals(new String(CODEC.decode(StringUtils.getBytesUsAscii(encoded.substring(0, encoded.lastIndexOf("="))))), decoded);
    }

    /**
     * Test decode method with Object (String, charset) invalid.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeObjectStringInvalidCharset() {
        String encoded = "hello=68656C6C6F";
        CODEC.decode(encoded, "ISO-8859-1");
    }

    /**
     * Test decode method with Object (BinaryDecoder).
     */
    @Test
    public void testDecodeObjectBinaryDecoder() throws DecoderException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        CODEC.encode(new byte[]{"h", "e", "l", "l", "o"}, buffer);
        ByteArrayInputStream input = new ByteArrayInputStream(buffer.toByteArray());
        BinaryDecoder decoder = new StringDecoder();
        Object decoded = CODEC.decode(decoder, input);
        assertEquals(new String(CODEC.decode(buffer.toByteArray())), decoded.toString());
    }

    /**
     * Test decode method with Object (StringDecoder).
     */
    @Test
    public void testDecodeObjectStringDecoder() throws DecoderException, UnsupportedEncodingException {
        String encoded = StringUtils.newStringFromBytes(CODEC.encode(new byte[]{"h", "e", "l", "l", "o"}));
        StringDecoder decoder = new StringDecoder(CharEncoding.UTF_8);
        Object decoded = CODEC.decode(decoder, encoded);
        assertEquals(new String(CODEC.decode(CODEC.encode(new byte[]{"h", "e", "l", "l", "o"}))), decoded.toString());
    }

    /**
     * Test decode method with Object (StringDecoder, charset).
     */
    @Test
    public void testDecodeObjectStringDecoderCharset() throws DecoderException, UnsupportedEncodingException {
        String encoded = StringUtils.newStringFromBytes(CODEC.encode(new byte[]{"h", "e", "l", "l", "o"}));
        StringDecoder decoder = new StringDecoder();
        Object decoded = CODEC.decode(decoder, encoded, CharEncoding.UTF_8);
        assertEquals(new String(CODEC.decode(CODEC.encode(new byte[]{"h", "e", "l", "l", "o"}))), decoded.toString());
    }

    /**
     * Test decode method with Object (null).
     */
    @Test
    public void testDecodeObjectNull() {
        assertNull(CODEC.decode(null));
    }

    /**
     * Test encode method with Object (null).
     */
    @Test
    public void testEncodeObjectNull() {
        assertNull(CODEC.encode(null));
    }
}