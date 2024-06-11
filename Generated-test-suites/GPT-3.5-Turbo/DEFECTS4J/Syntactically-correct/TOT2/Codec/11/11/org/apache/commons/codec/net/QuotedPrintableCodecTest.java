import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeQuotedPrintable() {
        byte[] bytes = "Hello World!".getBytes();
        byte[] encoded = QuotedPrintableCodec.encodeQuotedPrintable(bytes);
        assertNotNull(encoded);
        assertEquals("Hello World!", new String(encoded));
    }

    @Test
    public void testDecodeQuotedPrintable() throws DecoderException {
        byte[] bytes = QuotedPrintableCodec.encodeQuotedPrintable(null);
        byte[] decoded = QuotedPrintableCodec.decodeQuotedPrintable(bytes);
        assertNull(decoded);
    }

    @Test
    public void testEncode() throws EncoderException {
        String text = "Hello World!";
        String encoded = QuotedPrintableCodec.encode(text);
        assertNotNull(encoded);
        assertEquals("Hello World!", encoded);
    }

    @Test
    public void testDecode() throws DecoderException {
        String text = "Hello World!";
        String encoded = QuotedPrintableCodec.encode(text);
        String decoded = QuotedPrintableCodec.decode(encoded);
        assertNotNull(decoded);
        assertEquals(text, decoded);
    }

    @Test
    public void testDefaultCharset() {
        String charset = QuotedPrintableCodec.getDefaultCharset();
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

}