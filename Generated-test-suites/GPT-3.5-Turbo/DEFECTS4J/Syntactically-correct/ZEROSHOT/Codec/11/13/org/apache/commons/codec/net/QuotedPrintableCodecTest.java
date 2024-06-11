import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World!";
        String expected = "Hello=20World!";
        String result = codec.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello=20World!";
        String expected = "Hello World!";
        String result = codec.decode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "Hello World!";
        String expected = "Hello=20World!";
        String result = codec.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDecodeWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "Hello=20World!";
        String expected = "Hello World!";
        String result = codec.decode(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testEncodeNullInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.encode(null);
        assertNull(result);
    }
    
    @Test
    public void testDecodeNullInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.decode(null);
        assertNull(result);
    }
    
    @Test(expected = EncoderException.class)
    public void testEncodeUnsupportedCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("InvalidCharset");
        String input = "Hello World!";
        String result = codec.encode(input);
    }
    
    @Test(expected = DecoderException.class)
    public void testDecodeUnsupportedCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("InvalidCharset");
        String input = "Hello=20World!";
        String result = codec.decode(input);
    }
}