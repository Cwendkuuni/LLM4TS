import static org.junit.Assert.*;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testDefaultConstructor() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNotNull(codec);
    }

    @Test
    public void testParametrizedConstructor() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        assertNotNull(codec);
    }

    @Test
    public void testEncodeByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {65, 66, 67}; // ABC
        byte[] expected = "ABC".getBytes();
        assertArrayEquals(codec.encode(input), expected);
    }

    @Test
    public void testDecodeByteArray() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "ABC".getBytes();
        byte[] expected = {65, 66, 67}; // ABC
        assertArrayEquals(codec.decode(input), expected);
    }

    @Test
    public void testEncodeString() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World";
        String expected = "Hello World";
        assertEquals(codec.encode(input), expected);
    }

    @Test
    public void testDecodeString() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World";
        String expected = "Hello World";
        assertEquals(codec.decode(input), expected);
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object input = "Hello World";
        assertEquals(codec.encode(input), input);
    }

    @Test
    public void testDecodeObject() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object input = "Hello World";
        assertEquals(codec.decode(input), input);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidString() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode("Invalid String");
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidByteArray() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(new byte[]{127, 128});
    }

    @Test
    public void testDefaultCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertEquals(codec.getDefaultCharset(), "UTF-8");
    }
}