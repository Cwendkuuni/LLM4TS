import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {
    
    QuotedPrintableCodec codec = new QuotedPrintableCodec();
    
    @Test
    public void testEncode() {
        String input = "hello world";
        byte[] expected = "hello world".getBytes();
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDecode() {
        byte[] input = "hello world".getBytes();
        String expected = "hello world";
        String result = codec.decode(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testEncodeBlankString() {
        String input = "";
        byte[] expected = "".getBytes();
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDecodeBlankString() {
        byte[] input = "".getBytes();
        String expected = "";
        String result = codec.decode(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testEncodeNull() {
        byte[] result = codec.encode(null);
        assertNull(result);
    }
    
    @Test
    public void testDecodeNull() {
        String result = codec.decode(null);
        assertNull(result);
    }
}