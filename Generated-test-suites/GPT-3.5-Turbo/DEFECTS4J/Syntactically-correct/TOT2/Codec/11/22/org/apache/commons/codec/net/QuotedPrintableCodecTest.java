import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    private QuotedPrintableCodec qpCodec;

    @Before
    public void setUp() {
        qpCodec = new QuotedPrintableCodec();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(qpCodec);
        assertEquals(CharEncoding.UTF_8, qpCodec.getDefaultCharset());
    }

    @Test
    public void testSpecificCharsetConstructor() {
        qpCodec = new QuotedPrintableCodec(CharEncoding.ISO_8859_1);
        assertNotNull(qpCodec);
        assertEquals(CharEncoding.ISO_8859_1, qpCodec.getDefaultCharset());
    }

    @Test
    public void testEncodeByteArray() {
        String testString = "Hello World!";
        byte[] encodedByteArray = qpCodec.encode(testString.getBytes());
        assertNotNull(encodedByteArray);
    }

    @Test
    public void testDecodeByteArray() throws DecoderException {
        String testString = "Hello World!";
        byte[] encodedByteArray = qpCodec.encode(testString.getBytes());
        byte[] decodedByteArray = qpCodec.decode(encodedByteArray);
        assertNotNull(decodedByteArray);
        assertArrayEquals(testString.getBytes(), decodedByteArray);
    }

    @Test
    public void testEncodeString() throws EncoderException {
        String testString = "Hello World!";
        String encodedString = qpCodec.encode(testString);
        assertNotNull(encodedString);
    }

    @Test
    public void testDecodeString() throws DecoderException {
        String testString = "Hello World!";
        String encodedString = qpCodec.encode(testString);
        String decodedString = qpCodec.decode(encodedString);
        assertNotNull(decodedString);
        assertEquals(testString, decodedString);
    }

    @Test
    public void testEncodeDecodeObject() throws EncoderException, DecoderException {
        String testString = "Hello World!";
        String encodedString = (String) qpCodec.encode((Object) testString);
        assertNotNull(encodedString);
        String decodedString = (String) qpCodec.decode(encodedString);
        assertNotNull(decodedString);
        assertEquals(testString, decodedString);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidQuotedPrintable() throws DecoderException {
        String invalidEncodedString = "Hello=invalid";
        qpCodec.decode(invalidEncodedString);
    }

    @Test
    public void testEncodeDecodeWithSpecificCharset() throws EncoderException, DecoderException, UnsupportedEncodingException {
        String testString = "Hello World!";
        String charset = CharEncoding.UTF_8;
        String encodedString = qpCodec.encode(testString, charset);
        assertNotNull(encodedString);
        String decodedString = qpCodec.decode(encodedString, charset);
        assertNotNull(decodedString);
        assertEquals(testString, decodedString);
    }

    @Test
    public void testEncodeDecodeWithUnsupportedCharset() {
        String testString = "Hello World!";
        String unsupportedCharset = "unsupportedCharset";
        EncoderException encoderException = null;
        try {
            qpCodec.encode(testString, unsupportedCharset);
        } catch (EncoderException e) {
            encoderException = e;
        }

        assertNotNull(encoderException);
        DecoderException decoderException = null;
        try {
            qpCodec.decode(testString, unsupportedCharset);
        } catch (DecoderException e) {
            decoderException = e;
        }

        assertNotNull(decoderException);
    }
}