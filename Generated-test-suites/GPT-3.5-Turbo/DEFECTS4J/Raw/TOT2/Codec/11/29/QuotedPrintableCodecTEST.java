import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Hello, world!".getBytes();
        byte[] expected = "Hello, world!".getBytes();

        byte[] result = codec.encode(input);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Hello, world!".getBytes();
        byte[] encoded = codec.encode(input);
        byte[] expected = "Hello, world!".getBytes();

        byte[] result = codec.decode(encoded);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testEncodeWithNullInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode(null));
    }

    @Test
    public void testDecodeWithNullInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.decode(null));
    }

    @Test
    public void testEncodeStringWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello, world!";
        String charset = "UTF-8";
        String expected = "Hello, world!";

        String result = codec.encode(input, charset);

        assertEquals(expected, result);
    }

    @Test
    public void testDecodeStringWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello, world!";
        String charset = "UTF-8";
        String encoded = codec.encode(input, charset);
        String expected = "Hello, world!";

        String result = codec.decode(encoded, charset);

        assertEquals(expected, result);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidQuotedPrintable() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] invalidInput = "Invalid=Quoted=Printable".getBytes();

        codec.decode(invalidInput);
    }
}