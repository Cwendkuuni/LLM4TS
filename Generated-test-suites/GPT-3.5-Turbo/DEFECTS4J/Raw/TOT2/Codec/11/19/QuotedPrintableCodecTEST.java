import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    private QuotedPrintableCodec codec;

    @Before
    public void setup() {
        codec = new QuotedPrintableCodec();
    }

    @Test
    public void testEncode() {
        byte[] input = "Hello World".getBytes();
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDecode() throws DecoderException {
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        String expected = "Hello World";
        String result = codec.decode(input);
        assertEquals(expected, result);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() throws DecoderException {
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114}; // Incomplete encoded data
        codec.decode(input);
    }

    @Test
    public void testEncodeString() throws EncoderException {
        String input = "Hello World";
        String expected = "Hello World";
        String result = codec.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDecodeString() throws DecoderException {
        String input = "Hello World";
        String expected = "Hello World";
        String result = codec.decode(input);
        assertEquals(expected, result);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidString() throws DecoderException {
        String input = "Invalid=Input"; // Invalid quoted-printable encoding
        codec.decode(input);
    }
}