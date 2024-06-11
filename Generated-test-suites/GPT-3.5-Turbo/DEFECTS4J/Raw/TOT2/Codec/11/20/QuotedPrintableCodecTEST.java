import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import java.io.UnsupportedEncodingException;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World!";
        String expected = "Hello World!";
        try {
            assertEquals(expected, codec.encode(input));
        } catch (UnsupportedEncodingException e) {
            fail("UnsupportedEncodingException occurred");
        }
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello=3DWorld=21";
        String expected = "Hello World!";
        try {
            assertEquals(expected, codec.decode(input));
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testEncodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode(null));
    }

    @Test
    public void testDecodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.decode(null));
    }

    @Test
    public void testDecodeInvalidInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Invalid=Input";
        try {
            codec.decode(input);
            fail("DecoderException not thrown for invalid input");
        } catch (Exception e) {
            assertTrue(e instanceof DecoderException);
        }
    }
}