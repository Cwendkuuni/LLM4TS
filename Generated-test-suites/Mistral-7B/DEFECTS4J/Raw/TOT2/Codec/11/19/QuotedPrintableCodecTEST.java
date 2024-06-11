import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * QuotedPrintableCodecTest.java - Test class for QuotedPrintableCodec.
 */
public class QuotedPrintableCodecTest {

    /**
     * Test encoding of bytes using default charset.
     */
    @Test
    public void testEncodeBytes() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {0x61, 0x62, 0x63};
        byte[] expected = {(byte) 0x61, (byte) 0x62, (byte) 0x63};
        byte[] output = codec.encode(input);
        assertEquals(expected.length, output.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], output[i]);
        }
    }

    /**
     * Test encoding of bytes using custom charset.
     */
    @Test
    public void testEncodeBytesCustomCharset() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "abc";
        byte[] expected = StringUtils.getBytesUsAscii(input);
        byte[] output = codec.encode(input.getBytes("UTF-8"));
        assertEquals(expected.length, output.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], output[i]);
        }
    }

    /**
     * Test encoding of a string using default charset.
     */
    @Test
    public void testEncodeString() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "abc";
        String expected = StringUtils.newStringUsAscii(codec.encode(input.getBytes()));
        String output = codec.encode(input);
        assertEquals(expected, output);
    }

    /**
     * Test encoding of a string using custom charset.
     */
    @Test
    public void testEncodeStringCustomCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "abc";
        String expected = StringUtils.newStringUsAscii(codec.encode(input.getBytes("UTF-8")));
        String output = codec.encode(input);
        assertEquals(expected, output);
    }

    /**
     * Test decoding a string using default charset.
     */
    @Test
    public void testDecodeString() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "%61%62%63";
        String expected = "abc";
        String output = codec.decode(input);
        assertEquals(expected, output);
    }

    /**
     * Test decoding a string using custom charset.
     */
    @Test
    public void testDecodeStringCustomCharset() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "%61%62%63";
        String expected = "abc";
        String output = new String(codec.decode(StringUtils.getBytesUsAscii(input)), "UTF-8");
        assertEquals(expected, output);
    }

    /**
     * Test encoding an array of bytes with unsupported character.
     */
    @Test(expected = DecoderException.class)
    public void testEncodeBytesUnsupportedChar() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {(byte) 0x7f};
        codec.encode(input);
    }

    /**
     * Test decoding a string with missing data.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeStringMissingData() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "%61%62";
        codec.decode(StringUtils.getBytesUsAscii(input));
    }

    /**
     * Test decoding a string with incorrect data format.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeStringIncorrectFormat() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "%61%62%63%61";
        codec.decode(StringUtils.getBytesUsAscii(input));
    }
}