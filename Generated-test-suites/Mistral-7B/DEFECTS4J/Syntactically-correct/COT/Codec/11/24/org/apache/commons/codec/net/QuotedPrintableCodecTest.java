package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

/**
 * Test class for QuotedPrintableCodec.
 */
public class QuotedPrintableCodecTest {

    // Test instances
    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();
    private static final CharEncoding UTF_8 = CharEncoding.UTF_8;

    // Test data
    private static final byte[] TEST_DATA_QUOTED_PRINTABLE =
            Hex.decodeHex("D2C3DBA03131322333321416172616321534243415441425462554243");
    private static final byte[] TEST_DATA_ORIGINAL = Hex.decodeHex("31323334353637383930313233343536373839");

    /**
     * Test encode method with default charset.
     */
    @Test
    public void testEncodeDefaultCharset() throws EncoderException {
        byte[] encoded = CODEC.encode(TEST_DATA_ORIGINAL);
        assertArrayEquals(TEST_DATA_QUOTED_PRINTABLE, encoded);
    }

    /**
     * Test encode method with UTF-8 charset.
     */
    @Test
    public void testEncodeUTF8Charset() throws EncoderException, UnsupportedEncodingException {
        byte[] encoded = CODEC.encode(TEST_DATA_ORIGINAL, UTF_8.getName());
        assertArrayEquals(TEST_DATA_QUOTED_PRINTABLE, encoded);
    }

    /**
     * Test decode method with default charset.
     */
    @Test
    public void testDecodeDefaultCharset() throws DecoderException {
        byte[] decoded = CODEC.decode(TEST_DATA_QUOTED_PRINTABLE);
        assertArrayEquals(TEST_DATA_ORIGINAL, decoded);
    }

    /**
     * Test decode method with UTF-8 charset.
     */
    @Test
    public void testDecodeUTF8Charset() throws DecoderException, UnsupportedEncodingException {
        byte[] decoded = CODEC.decode(TEST_DATA_QUOTED_PRINTABLE, UTF_8);
        assertEquals(new String(TEST_DATA_ORIGINAL, UTF_8), new String(decoded, UTF_8));
    }

    /**
     * Test decode method with invalid data.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidData() throws DecoderException, UnsupportedEncodingException {
        byte[] decoded = CODEC.decode(new byte[]{}, UTF_8);
    }

    /**
     * Test encode method with invalid data.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeInvalidData() throws EncoderException, UnsupportedEncodingException {
        byte[] encoded = CODEC.encode(new byte[]{}, UTF_8);
    }
}