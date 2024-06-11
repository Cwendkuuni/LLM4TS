import static org.junit.Assert.*;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World";
        String expected = "Hello=20World";
        assertEquals(expected, codec.encode(input));
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello=20World";
        String expected = "Hello World";
        assertEquals(expected, codec.decode(input));
    }

    @Test
    public void testEncodeWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "Hello World";
        String expected = "Hello=20World";
        assertEquals(expected, codec.encode(input));
    }

    @Test
    public void testDecodeWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "Hello=20World";
        String expected = "Hello World";
        assertEquals(expected, codec.decode(input));
    }

    @Test
    public void testEncodeByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Hello World".getBytes();
        byte[] expected = "Hello=20World".getBytes();
        assertArrayEquals(expected, codec.encode(input));
    }

    @Test
    public void testDecodeByteArray() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Hello=20World".getBytes();
        byte[] expected = "Hello World".getBytes();
        assertArrayEquals(expected, codec.decode(input));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Invalid=3ZCharacter";
        codec.decode(input);
    }

}