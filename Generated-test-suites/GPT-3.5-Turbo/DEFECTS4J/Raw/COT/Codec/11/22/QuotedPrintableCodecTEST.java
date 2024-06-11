
import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Test".getBytes();
        byte[] expected = "=54=65=73=74".getBytes();
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDecode() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "=54=65=73=74".getBytes();
        byte[] expected = "Test".getBytes();
        byte[] result = codec.decode(input);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testEncodeString() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Test";
        String expected = "=54=65=73=74";
        String result = codec.encode(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDecodeString() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "=54=65=73=74";
        String expected = "Test";
        String result = codec.decode(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testEncodeWithCharset() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-1");
        String input = "Test";
        String expected = "=54=65=73=74";
        String result = codec.encode(input, "ISO-8859-1");
        assertEquals(expected, result);
    }
    
    @Test
    public void testDecodeWithCharset() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-1");
        String input = "=54=65=73=74";
        String expected = "Test";
        String result = codec.decode(input, "ISO-8859-1");
        assertEquals(expected, result);
    }
}
