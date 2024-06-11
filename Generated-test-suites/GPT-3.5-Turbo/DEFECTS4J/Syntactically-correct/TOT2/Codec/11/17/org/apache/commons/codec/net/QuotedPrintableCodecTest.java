import static org.junit.Assert.*;
import org.junit.Test;

public class QuotedPrintableCodecTest {
    
    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Test";
        String expected = "Test";
        assertEquals(expected, codec.encode(input));
    }
    
    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Test";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        assertEquals(input, decoded);
    }
    
    @Test
    public void testEncodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode(null));
    }
    
    @Test
    public void testDecodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.decode(null));
    }
    
    @Test
    public void testEncodeWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "Test";
        String expected = "Test";
        assertEquals(expected, codec.encode(input));
    }
    
    @Test
    public void testDecodeWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "Test";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        assertEquals(input, decoded);
    }
    
    @Test
    public void testEncodeObject() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object input = "Test";
        String expected = "Test";
        assertEquals(expected, codec.encode(input));
    }
    
    @Test
    public void testDecodeObject() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Test";
        String encoded = codec.encode(input);
        Object decoded = codec.decode(encoded);
        assertEquals(input, decoded);
    }
}