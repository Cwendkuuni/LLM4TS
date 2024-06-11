import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] encoded = codec.encode("Hello World");
        assertArrayEquals("Expected encoded bytes do not match", "Hello World".getBytes(), encoded);
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String decoded = codec.decode("Hello=20World");
        assertEquals("Expected decoded string does not match", "Hello World", decoded);
    }

    @Test
    public void testEncodeWithNullBytes() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode(null));
    }

    @Test
    public void testDecodeWithNullBytes() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.decode(null));
    }

    @Test
    public void testEncodeWithUnsupportedCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        try {
            codec.encode("Hello World", "INVALID_CHARSET");
            fail("Expected UnsupportedEncodingException was not thrown");
        } catch (UnsupportedEncodingException e) {
            // Expected exception
        }
    }

    @Test
    public void testDecodeWithUnsupportedCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        try {
            codec.decode("Hello=20World", "INVALID_CHARSET");
            fail("Expected UnsupportedEncodingException was not thrown");
        } catch (UnsupportedEncodingException | DecoderException e) {
            // Expected exception
        }
    }
}