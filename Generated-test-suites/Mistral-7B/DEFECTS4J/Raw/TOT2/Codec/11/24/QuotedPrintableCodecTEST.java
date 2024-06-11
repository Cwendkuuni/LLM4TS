import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.CharEncoding;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * QuotedPrintableCodecTest class for testing QuotedPrintableCodec methods.
 */
public class QuotedPrintableCodecTest {

    private final QuotedPrintableCodec codec = new QuotedPrintableCodec(CharEncoding.UTF_8);

    /**
     * Test encode method with printable characters.
     */
    @Test
    public void testEncodePrintableCharacters() {
        byte[] expected = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
        byte[] actual = codec.encode("Hello World!".getBytes(CharEncoding.UTF_8));
        assertArrayEquals(expected, actual);
    }

    /**
     * Test encode method with non-printable characters.
     */
    @Test
    public void testEncodeNonPrintableCharacters() {
        byte[] expected = new byte[]{'H', 'e', 'l', 'l', 'o', '=31', '=31', '=31', '=64', '=32', '=62', '=6C', '=6F', '=63', '=68', '=65', '=64', '!'};
        byte[] actual = codec.encode("Hello!world!\u007F".getBytes(CharEncoding.UTF_8));
        assertArrayEquals(expected, actual);
    }

    /**
     * Test decode method with printable characters.
     */
    @Test
    public void testDecodePrintableCharacters() throws UnsupportedEncodingException, DecoderException {
        byte[] encoded = new byte[]{'H', 'e', 'l', 'l', 'o', '=20', 'W', 'o', 'r', 'l', 'd', '=21'};
        String actual = new String(codec.decode(encoded), CharEncoding.UTF_8);
        assertEquals("Hello World!", actual);
    }

    /**
     * Test decode method with non-printable characters.
     */
    @Test
    public void testDecodeNonPrintableCharacters() throws UnsupportedEncodingException, DecoderException {
        byte[] encoded = new byte[]{'H', 'e', 'l', 'l', 'o', '=31', '=31', '=31', '=64', '=32', '=62', '=6C', '=6F', '=63', '=68', '=65', '=64', '!'};
        String actual = new String(codec.decode(encoded), CharEncoding.UTF_8);
        assertEquals("Hello!world!\u007F", actual);
    }

    /**
     * Test decode method with invalid encoded characters.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidEncodedCharacters() throws UnsupportedEncodingException {
        byte[] encoded = new byte[]{'H', 'e', 'l', 'l', 'o', '=31', '=31', '=31', '=64', '=32', '=62', '=6C', '=6F', '=63', '=68', '=65', '=64', '=7F'}; // Invalid character
        codec.decode(encoded);
    }

    /**
     * Test decode method with empty encoded string.
     */
    @Test
    public void testDecodeEmptyEncodedString() throws UnsupportedEncodingException, DecoderException {
        byte[] encoded = new byte[0];
        String actual = new String(codec.decode(encoded), CharEncoding.UTF_8);
        assertEquals("", actual);
    }

    /**
     * Test encode and decode methods round trip.
     */
    @Test
    public void testEncodeDecodeRoundTrip() throws UnsupportedEncodingException, DecoderException {
        String input = "Hello!world!\u007F";
        byte[] encoded = codec.encode(input.getBytes(CharEncoding.UTF_8));
        String decoded = new String(codec.decode(encoded), CharEncoding.UTF_8);
        assertEquals(input, decoded);
    }
}