import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    private static final byte[] TEST_BYTE_ARRAY = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
    private static final String TEST_STRING = "abcdefg";
    private static final String TEST_CHARSET = "UTF-8";

    @Test
    public void testEncodeByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] encoded = codec.encode(TEST_BYTE_ARRAY);
        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        for (byte b : TEST_BYTE_ARRAY) {
            if (PRINTABLE_CHARS.get(b)) {
                expected.write(b);
            } else {
                    encodeQuotedPrintable(b, expected);
                }
            }
        expected.flush();
        byte[] expectedBytes = expected.toByteArray();
        Assert.assertArrayEquals(expectedBytes, encoded);
    }

    @Test
    public void testDecodeByteArray() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] encoded = encodeQuotedPrintable(PRINTABLE_CHARS, TEST_BYTE_ARRAY);
        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals(TEST_BYTE_ARRAY, decoded);
    }

    @Test
    public void testEncodeString() throws EncoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encoded = codec.encode(TEST_STRING);
        String expected = StringUtils.newStringUsAscii(encodeQuotedPrintable(PRINTABLE_CHARS, TEST_BYTE_ARRAY(TEST_STRING)));
        Assert.assertEquals(expected, encoded);
    }

    @Test
    public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encoded = encodeQuotedPrintable(PRINTABLE_CHARS, TEST_BYTE_ARRAY(TEST_STRING));
        String decoded = codec.decode(encoded, TEST_CHARSET);
        Assert.assertEquals(TEST_STRING, decoded);
    }

    @Test
    public void testEncodeByteArrayWithCharset() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(TEST_CHARSET);
        byte[] encoded = codec.encode(TEST_BYTE_ARRAY);
        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        for (byte b : TEST_BYTE_ARRAY) {
            if (PRINTABLE_CHARS.get(b)) {
                expected.write(b);
            } else {
                encodeQuotedPrintable(b, expected);
            }
        }
        expected.flush();
        byte[] expectedBytes = expected.toByteArray();
        Assert.assertArrayEquals(expectedBytes, encoded);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeObjectUnsupported() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object obj = new Object() {};
        codec.encode(obj);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDecodeObjectUnsupported() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object obj = new Object() {};
        codec.decode(obj);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(null);
    }

    private byte[] TEST_BYTE_ARRAY(String string) {
        try {
            return StringUtils.getBytesUsAscii(string);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}