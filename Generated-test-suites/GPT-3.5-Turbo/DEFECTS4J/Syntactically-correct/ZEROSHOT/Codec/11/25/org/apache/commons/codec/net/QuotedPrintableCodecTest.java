import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {
    
    private QuotedPrintableCodec codec = new QuotedPrintableCodec();
    
    @Test
    public void testEncode() throws Exception {
        byte[] input = "Hello World!".getBytes("UTF-8");
        byte[] expected = {72,101,108,108,111,32,87,111,114,108,100,33};
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDecode() throws Exception {
        byte[] input = {72,101,108,108,111,32,87,111,114,108,100,33};
        byte[] expected = "Hello World!".getBytes("UTF-8");
        byte[] result = codec.decode(input);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testEncodeString() throws Exception {
        String input = "Hello World!";
        String expected = "Hello World!";
        String result = codec.encode(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDecodeString() throws Exception {
        String input = "Hello World!";
        String expected = "Hello World!";
        String result = codec.decode(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testEncodeStringWithCharset() throws Exception {
        String input = "Hello World!";
        String charset = "UTF-8";
        String expected = "Hello World!";
        String result = codec.encode(input, charset);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDecodeStringWithCharset() throws Exception {
        String input = "Hello World!";
        String charset = "UTF-8";
        String expected = "Hello World!";
        String result = codec.decode(input, charset);
        assertEquals(expected, result);
    }
}