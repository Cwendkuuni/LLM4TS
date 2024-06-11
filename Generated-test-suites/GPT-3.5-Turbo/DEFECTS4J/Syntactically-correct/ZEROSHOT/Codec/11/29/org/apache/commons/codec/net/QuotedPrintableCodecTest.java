import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    private QuotedPrintableCodec quotedPrintableCodec;

    @Before
    public void setUp() {
        quotedPrintableCodec = new QuotedPrintableCodec();
    }

    @Test
    public void testEncode() {
        byte[] bytes = new byte[]{97, 98, 99, 100};
        byte[] expected = new byte[]{97, 98, 99, 100};
        byte[] result = quotedPrintableCodec.encode(bytes);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDecode() throws DecoderException {
        byte[] bytes = new byte[]{97, 98, 99, 100};
        byte[] encodedBytes = quotedPrintableCodec.encode(bytes);
        byte[] expected = new byte[]{97, 98, 99, 100};
        byte[] result = quotedPrintableCodec.decode(encodedBytes);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testEncodeString() throws EncoderException {
        String input = "abcd";
        String expected = "abcd";
        String result = quotedPrintableCodec.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDecodeString() throws DecoderException {
        String input = "abcd";
        String encodedString = quotedPrintableCodec.encode(input);
        String expected = "abcd";
        String result = quotedPrintableCodec.decode(encodedString);
        assertEquals(expected, result);
    }

    @Test
    public void testDecodeStringWithCharset() throws DecoderException, UnsupportedEncodingException {
        String input = "abcd";
        String charset = "UTF-8";
        String encodedString = quotedPrintableCodec.encode(input, charset);
        String expected = "abcd";
        String result = quotedPrintableCodec.decode(encodedString, charset);
        assertEquals(expected, result);
    }
}