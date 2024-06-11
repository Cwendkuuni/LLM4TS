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
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/**
 * QuotedPrintableCodecTest class file to test all methods in QuotedPrintableCodec class.
 */
public class QuotedPrintableCodecTest {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final ByteArrayOutputStream OUTPUT_STREAM = new ByteArrayOutputStream();
    private static final byte[] INPUT_DATA = new byte[]{'A', 'B', 'C', '\t', 'D', 'E', 'F', 0x80};
    private static final BitSet printableChars = new BitSet(256);

    /**
     * Set up printable chars collection.
     */
    static {
        // alpha characters
        for (int i = 33; i <= 60; i++) {
            printableChars.set(i);
        }
        for (int i = 62; i <= 126; i++) {
            printableChars.set(i);
        }
        printableChars.set((byte) '\t');
        printableChars.set((byte) 32);
    }

    /**
     * Test QuotedPrintableCodec constructor.
     */
    @Test
    public void testQuotedPrintableCodecConstructor() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Assert.assertNotNull(codec);
    }

    /**
     * Test QuotedPrintableCodec constructor with charset.
     */
    @Test
    public void testQuotedPrintableCodecConstructorWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(DEFAULT_CHARSET);
        Assert.assertNotNull(codec);
    }

    /**
     * Test encode method with bytes.
     */
    @Test
    public void testEncodeBytes() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] encodedBytes = codec.encode(INPUT_DATA);
        Assert.assertArrayEquals(encodedBytes, encodeQuotedPrintable(printableChars, INPUT_DATA));
    }

    /**
     * Test decode method with bytes.
     */
    @Test
    public void testDecodeBytes() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] decodedBytes = codec.decode(encodeQuotedPrintable(printableChars, INPUT_DATA));
        Assert.assertArrayEquals(INPUT_DATA, decodedBytes);
    }

    /**
     * Test encode method with String.
     */
    @Test
    public void testEncodeString() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encodedString = codec.encode("ABC\tDE FG");
        Assert.assertEquals(encode("ABC\tDE FG", printableChars), encodedString);
    }

    /**
     * Test decode method with String.
     */
    @Test
    public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String decodedString = codec.decode(encode("ABC\tDE FG", printableChars));
        Assert.assertEquals("ABC\tDE FG", decodedString);
    }

    /**
     * Test encode method with Object.
     */
    @Test
    public void testEncodeObject() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] inputDataBytes = "ABC\tDE FG".getBytes(DEFAULT_CHARSET);
        Object encodedObject = codec.encode(inputDataBytes);
        Assert.assertArrayEquals(codec.encode(inputDataBytes), encodedObject);
    }

    /**
     * Test decode method with Object.
     */
    @Test
    public void testDecodeObject() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] encodedDataBytes = encodeQuotedPrintable(printableChars, INPUT_DATA);
        Object decodedObject = codec.decode(encodedDataBytes);
        Assert.assertArrayEquals(INPUT_DATA, decodedObject);
    }

    /**
     * Test getDefaultCharset method.
     */
    @Test
    public void testGetDefaultCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Assert.assertEquals(DEFAULT_CHARSET, codec.getDefaultCharset());
    }

    /**
     * Helper method to encodeQuotedPrintable.
     */
    private static byte[] encodeQuotedPrintable(BitSet printable, byte[] bytes) {
        return QuotedPrintableCodec.encodeQuotedPrintable(printable, bytes);
    }

    /**
     * Helper method to encode.
     */
    private static String encode(String str, BitSet printableChars) throws UnsupportedEncodingException {
        return QuotedPrintableCodec.encode(str.getBytes(CharEncoding.UTF_8), printableChars).toString();
    }
}