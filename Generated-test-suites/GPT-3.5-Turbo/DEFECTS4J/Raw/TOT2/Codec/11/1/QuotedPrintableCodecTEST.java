import static org.junit.Assert.*;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Typical use case
        String input = "Hello World!";
        String encodedOutput = codec.encode(input);
        assertEquals("Encoded output does not match expected value", "Hello World!", encodedOutput);

        // Edge case - Empty string
        String emptyInput = "";
        String emptyEncodedOutput = codec.encode(emptyInput);
        assertEquals("Encoded output does not match expected value for empty input", "", emptyEncodedOutput);
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Typical use case
        String input = "Hello=20World!";
        String decodedOutput = codec.decode(input);
        assertEquals("Decoded output does not match expected value", "Hello World!", decodedOutput);

        // Edge case - Invalid encoding
        try {
            String invalidInput = "Hello=!";
            codec.decode(invalidInput);
            fail("DecoderException should have been thrown for invalid encoding");
        } catch (DecoderException e) {
            // Exception expected
        }
    }

    @Test
    public void testEncodeCustomCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-16");

        // Typical use case
        String input = "Hello World!";
        String encodedOutput = codec.encode(input);
        assertEquals("Encoded output with custom charset does not match expected value", "Hello World!", encodedOutput);

        // Edge case - Unsupported charset
        try {
            codec.encode("Test");
            fail("UnsupportedEncodingException should have been thrown for invalid charset");
        } catch (UnsupportedEncodingException e) {
            // Exception expected
        }
    }

    @Test
    public void testDecodeCustomCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-16");

        // Typical use case
        String input = "Hello=20World!";
        String decodedOutput = codec.decode(input);
        assertEquals("Decoded output with custom charset does not match expected value", "Hello World!", decodedOutput);

        // Edge case - Unsupported charset
        try {
            codec.decode("Test");
            fail("UnsupportedEncodingException should have been thrown for invalid charset");
        } catch (UnsupportedEncodingException | DecoderException e) {
            // Exception expected
        }
    }

    @Test
    public void testDefaultCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Typical use case
        String defaultCharset = codec.getDefaultCharset();
        assertEquals("Default charset does not match expected value", "UTF-8", defaultCharset);
    }
}