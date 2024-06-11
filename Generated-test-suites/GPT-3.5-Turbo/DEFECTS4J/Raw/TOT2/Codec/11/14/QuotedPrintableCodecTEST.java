import static org.junit.Assert.*;
import org.junit.Test;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    @Test
    public void testDefaultConstructor() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNotNull(codec);
    }

    @Test
    public void testCustomCharsetConstructor() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        assertNotNull(codec);
    }

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World!";
        String expectedOutput = "Hello=20World!";
        try {
            String encoded = codec.encode(input);
            assertEquals(expectedOutput, encoded);
        } catch (EncoderException e) {
            fail("EncoderException thrown");
        }
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello=20World!";
        String expectedOutput = "Hello World!";
        try {
            String decoded = codec.decode(input);
            assertEquals(expectedOutput, decoded);
        } catch (DecoderException e) {
            fail("DecoderException thrown");
        }
    }

    @Test
    public void testEncodedBytesNotNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Hello World!".getBytes();
        byte[] encoded = codec.encode(input);
        assertNotNull(encoded);
    }

    @Test
    public void testDecodedBytesNotNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Hello=20World!".getBytes();
        try {
            byte[] decoded = codec.decode(input);
            assertNotNull(decoded);
        } catch (DecoderException e) {
            fail("DecoderException thrown");
        }
    }

    @Test(expected = DecoderException.class)
    public void testInvalidQuotedPrintableInput() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Invalid".getBytes();
        codec.decode(input);
    }

    @Test
    public void testCustomEncodeWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World!";
        String expectedOutput = "Hello=20World!";
        try {
            String encoded = codec.encode(input, "UTF-8");
            assertEquals(expectedOutput, encoded);
        } catch (EncoderException | UnsupportedEncodingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCustomDecodeWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello=20World!";
        String expectedOutput = "Hello World!";
        try {
            String decoded = codec.decode(input, "UTF-8");
            assertEquals(expectedOutput, decoded);
        } catch (DecoderException | UnsupportedEncodingException e) {
            fail("Exception thrown");
        }
    }
}