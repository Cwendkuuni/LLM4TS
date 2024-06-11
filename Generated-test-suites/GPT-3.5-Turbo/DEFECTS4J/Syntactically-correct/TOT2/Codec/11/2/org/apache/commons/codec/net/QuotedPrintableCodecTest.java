import static org.junit.Assert.*;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Hello World!".getBytes();
        byte[] expected = "Hello World!".getBytes();
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Hello=20World=21".getBytes();
        byte[] expected = "Hello World!".getBytes();
        byte[] result = codec.decode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testEncodeEmpty() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = new byte[0];
        byte[] expected = new byte[0];
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDecodeEmpty() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = new byte[0];
        byte[] expected = new byte[0];
        byte[] result = codec.decode(input);
        assertArrayEquals(expected, result);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Invalid=Input".getBytes();
        codec.decode(input);
    }

    @Test
    public void testEncodeString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World!";
        String expected = "Hello World!";
        String result = codec.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDecodeString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello=20World=21";
        String expected = "Hello World!";
        String result = codec.decode(input);
        assertEquals(expected, result);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeStringInvalidInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Invalid=Input";
        codec.decode(input);
    }
}