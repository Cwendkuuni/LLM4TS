import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/**
 * Test class for QuotedPrintableCodec.
 */
public class QuotedPrintableCodecTest {

    private static final byte TEST_BYTE = 0x61; // 'a'
    private static final String TEST_STRING = "test string";
    private static final String TEST_CHARSET = "UTF-8";
    private static final BitSet PRINTABLE_CHARS = QuotedPrintableCodec.PRINTABLE_CHARS;

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        QuotedPrintableCodec codecWithCharset = new QuotedPrintableCodec(TEST_CHARSET);
    }

    /**
     * Test encode method with byte and ByteArrayOutputStream.
     */
    @Test
    public void testEncodeByte() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        codec.encode(TEST_BYTE, outputStream);
        byte[] encodedBytes = outputStream.toByteArray();
        Assert.assertEquals(3, encodedBytes.length);

        String hexExpected = Hex.encodeHexString(new byte[] {ESCAPE_CHAR, '6', '1'});
        Assert.assertArrayEquals(hexExpected.getBytes(), encodedBytes);
    }

    /**
     * Test encode method with byte array and ByteArrayOutputStream.
     */
    @Test
    public void testEncodeByteArray() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        codec.encode(new byte[] {TEST_BYTE}, outputStream);
        byte[] encodedBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(new byte[] {ESCAPE_CHAR, '6', '1'}, encodedBytes);
    }

    /**
     * Test decode method with byte and ByteArrayInputStream.
     */
    @Test
    public void testDecodeByte() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Hex.decodeHex(new byte[] {ESCAPE_CHAR, '6', '1'}));

        byte decodedByte = codec.decode(inputStream)[0];
        Assert.assertEquals(TEST_BYTE, decodedByte);
    }

    /**
     * Test decode method with byte array and ByteArrayInputStream.
     */
    @Test
    public void testDecodeByteArray() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Hex.decodeHex(new byte[] {ESCAPE_CHAR, '6', '1'}));

        byte[] decodedBytes = codec.decode(inputStream);
        Assert.assertArrayEquals(new byte[] {TEST_BYTE}, decodedBytes);
    }

    /**
     * Test encode method with String and StringEncoder.
     */
    @Test
    public void testEncodeString() throws EncoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        StringEncoder encoder = codec.getEncoder();

        String encodedString = encoder.encode(TEST_STRING);
        String hexExpected = Hex.encodeHexString(encodeQuotedPrintable(PRINTABLE_CHARS, TEST_STRING.getBytes()));
        Assert.assertEquals(hexExpected, encodedString);
    }

    /**
     * Test decode method with String and StringDecoder.
     */
    @Test
    public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        StringDecoder decoder = codec.getDecoder();

        String decodedString = decoder.decodeToString(encodeQuotedPrintable(PRINTABLE_CHARS, TEST_STRING.getBytes()));
        Assert.assertEquals(TEST_STRING, decodedString);
    }

    /**
     * Test encode method with byte array and default charset.
     */
    @Test
    public void testEncodeByteArrayDefaultCharset() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encodedBytes = codec.encode(TEST_STRING.getBytes(TEST_CHARSET));
        String hexExpected = Hex.encodeHexString(encodeQuotedPrintable(PRINTABLE_CHARS, TEST_STRING.getBytes()));
        Assert.assertArrayEquals(hexExpected.getBytes(), encodedBytes);
    }

    /**
     * Test decode method with String and default charset.
     */
    @Test
    public void testDecodeStringDefaultCharset() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encodedString = StringUtils.newStringUsAscii(encodeQuotedPrintable(PRINTABLE_CHARS, TEST_STRING.getBytes()));
        String decodedString = codec.decodeToString(encodedString.getBytes(TEST_CHARSET));
        Assert.assertEquals(TEST_STRING, decodedString);
    }

    /**
     * Test encode method with byte array and custom charset.
     */
    @Test
    public void testEncodeByteArrayCustomCharset() throws EncoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(TEST_CHARSET);

        byte[] encodedBytes = codec.encode(TEST_STRING.getBytes(TEST_CHARSET));
        String hexExpected = Hex.encodeHexString(encodeQuotedPrintable(PRINTABLE_CHARS, TEST_STRING.getBytes()));
        Assert.assertArrayEquals(hexExpected.getBytes(), encodedBytes);
    }

    /**
     * Test decode method with String and custom charset.
     */
    @Test
    public void testDecodeStringCustomCharset() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(TEST_CHARSET);

        String encodedString = StringUtils.newStringUsAscii(encodeQuotedPrintable(PRINTABLE_CHARS, TEST_STRING.getBytes()));
        String decodedString = codec.decodeToString(encodedString.getBytes(TEST_CHARSET));
        Assert.assertEquals(TEST_STRING, decodedString);
    }

    /**
     * Test encode method with byte array and null charset.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testEncodeByteArrayNullCharset() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        codec.encode(TEST_STRING.getBytes(null));
    }

    /**
     * Test decode method with String and null charset.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeStringNullCharset() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        codec.decodeToString(null);
    }

    /**
     * Test escape byte with CR.
     */
    @Test
    public void testEncodeByteWithCR() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        codec.encode((byte) 13, outputStream);
        byte[] encodedBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(new byte[] {ESCAPE_CHAR, 'D'}, encodedBytes);
    }

    /**
     * Test escape byte with LF.
     */
    @Test
    public void testEncodeByteWithLF() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        codec.encode((byte) 10, outputStream);
        byte[] encodedBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(new byte[] {ESCAPE_CHAR, 'C'}, encodedBytes);
    }

    /**
     * Test decode method with CR byte.
     */
    @Test
    public void testDecodeByteWithCR() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {ESCAPE_CHAR, 'D'});

        byte decodedByte = codec.decode(inputStream)[0];
        Assert.assertEquals((byte) 13, decodedByte);
    }

    /**
     * Test decode method with LF byte.
     */
    @Test
    public void testDecodeByteWithLF() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {ESCAPE_CHAR, 'C'});

        byte decodedByte = codec.decode(inputStream)[0];
        Assert.assertEquals((byte) 10, decodedByte);
    }
}