import static org.junit.Assert.*;
import org.junit.Test;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        try {
            byte[] encoded = codec.encode("Hello World");
            assertArrayEquals("Hello World".getBytes(), encoded);
        } catch (EncoderException e) {
            fail("Encoding failed");
        }
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        try {
            String decoded = codec.decode("Hello=20World");
            assertEquals("Hello World", decoded);
        } catch (DecoderException e) {
            fail("Decoding failed");
        }
    }

    @Test
    public void testEncodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        try {
            byte[] encoded = codec.encode(null);
            assertNull(encoded);
        } catch (EncoderException e) {
            fail("Encoding failed");
        }
    }

    @Test
    public void testDecodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        try {
            String decoded = codec.decode(null);
            assertNull(decoded);
        } catch (DecoderException e) {
            fail("Decoding failed");
        }
    }

    @Test
    public void testDecodeInvalid() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        try {
            String decoded = codec.decode("Invalid=3");
            fail("Decoding should have failed but did not");
        } catch (DecoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testDecodeUnsupportedCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        try {
            String decoded = codec.decode("Hello=20World", "InvalidCharset");
            fail("Decoding with unsupported charset should have failed but did not");
        } catch (UnsupportedEncodingException | DecoderException e) {
            // Expected exceptions
        }
    }
}