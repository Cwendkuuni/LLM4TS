import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World!";
        String expected = "Hello World!";
        String actual = codec.encode(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World!";
        String expected = "Hello World!";
        String actual = codec.decode(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeWithCharset() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "Hello World!";
        String expected = "Hello World!";
        String actual = codec.encode(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testDecodeWithCharset() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "Hello World!";
        String expected = "Hello World!";
        String actual = codec.decode(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = null;
        String expected = null;
        String actual = codec.encode(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testDecodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = null;
        String expected = null;
        String actual = codec.decode(input);
        assertEquals(expected, actual);
    }
}