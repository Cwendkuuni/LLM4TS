import org.apache.commons.codec.ByteArrayOutputStream;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/**
 * QuotedPrintableCodec test.
 */
public class QuotedPrintableCodecTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Assert.assertNotNull(codec);
    }

    /**
     * Test constructor with charset.
     */
    @Test
    public void testConstructorWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(CharEncoding.UTF_8.name());
        Assert.assertNotNull(codec);
    }

    /**
     * Test encode byte to quoted printable form.
     */
    @Test
    public void testEncodeByte() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte b = 0x61; // 'a'
        byte[] encoded = codec.encode(b);
        Assert.assertArrayEquals(new byte[]{'61'}, encoded);

        b = 0x7D; // '='
        encoded = codec.encode(b);
        Assert.assertArrayEquals(new byte[]{'3D'}, encoded);

        b = 0x7E; // '~'
        encoded = codec.encode(b);
        Assert.assertArrayEquals(new byte[]{ESCAPE_CHAR, '7E'}, encoded);
    }

    /**
     * Test decode quoted printable form to byte.
     */
    @Test
    public void testDecodeByte() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] encoded = new byte[]{ESCAPE_CHAR, '7E', 0x3D}; // "~="
        byte decoded = codec.decode(encoded);
        Assert.assertEquals((byte) '~', decoded);

        encoded = new byte[]{ESCAPE_CHAR, '61'}; // "a"
        decoded = codec.decode(encoded);
        Assert.assertEquals((byte) 'a', decoded);
    }

    /**
     * Test encode bytes to quoted printable form.
     */
    @Test
    public void testEncodeBytes() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = new byte[]{'a', 'b', 'c'};
        byte[] encoded = codec.encode(input);
        Assert.assertArrayEquals(new byte[]{ESCAPE_CHAR, '61', '62', ESCAPE_CHAR, '63'}, encoded);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int result = codec.encode(input, output);
        byte[] actualEncoded = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{ESCAPE_CHAR, '61', '62', ESCAPE_CHAR, '63'}, actualEncoded);
        Assert.assertEquals(input.length, result);
    }

    /**
     * Test decode quoted printable form to bytes.
     */
    @Test
    public void testDecodeBytes() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] encoded = new byte[]{ESCAPE_CHAR, '61', '62', ESCAPE_CHAR, '63'}; // "a=b=c"
        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals(new byte[]{'a', 'b', 'c'}, decoded);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int result = codec.decode(StringUtils.getBytesUsAscii("a=b=c"), output);
        byte[] actualDecoded = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'a', 'b', 'c'}, actualDecoded);
        Assert.assertEquals(3, result);
    }

    /**
     * Test encode string to quoted printable form.
     */
    @Test
    public void testEncodeString() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "abc";
        String encoded = codec.encode(input);
        Assert.assertEquals("616263", encoded);

        encoded = codec.encode(input, CharEncoding.US_ASCII);
        Assert.assertEquals("616263", encoded);
    }

    /**
     * Test decode quoted printable form to string.
     */
    @Test
    public void testDecodeString() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] encoded = new byte[]{'61', '62', '63'}; // "616263"
        String decoded = codec.decode(encoded);
        Assert.assertEquals("abc", decoded);

        String encodedString = "616263";
        decoded = codec.decode(encodedString);
        Assert.assertEquals("abc", decoded);
    }

    /**
     * Test encode null to quoted printable form.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode(null);
    }

    /**
     * Test decode null to string.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(null);
    }

    /**
     * Test decode bytes with invalid length.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeBytesInvalidLength() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] encoded = new byte[]{ESCAPE_CHAR, '61', '62'}; // "a="
        codec.decode(new byte[]{ESCAPE_CHAR, '61'}, 0, 1);
    }

    /**
     * Test encode string with invalid charset.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testEncodeStringInvalidCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "abc";
        codec.encode(input, "invalid");
    }

    /**
     * Test decode string with invalid charset.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeStringInvalidCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] encoded = new byte[]{'61', '62', '63'}; // "616263"
        codec.decode(encoded, "invalid");
    }
}