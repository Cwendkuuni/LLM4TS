
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello world!";
        String expected = "Hello world!";
        String encoded = codec.encode(input);
        assertEquals(expected, encoded);
    }

    @Test
    public void testDecode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello world=21";
        String expected = "Hello world!";
        String decoded = codec.decode(input);
        assertEquals(expected, decoded);
    }

    @Test
    public void testEncodeWithCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "A simple string";
        String expected = "A simple string";
        String encoded = codec.encode(input, "UTF-8");
        assertEquals(expected, encoded);
    }

    @Test
    public void testDecodeWithCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "A=20simple=20string";
        String expected = "A simple string";
        String decoded = codec.decode(input, "UTF-8");
        assertEquals(expected, decoded);
    }
}
