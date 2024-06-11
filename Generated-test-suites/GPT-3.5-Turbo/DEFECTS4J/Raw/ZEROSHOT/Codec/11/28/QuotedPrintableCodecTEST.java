import static org.junit.Assert.*;
import org.junit.Test;

public class QuotedPrintableCodecTest {
    
    private QuotedPrintableCodec codec = new QuotedPrintableCodec();
    
    @Test
    public void testEncode() {
        byte[] bytes = "Hello World".getBytes();
        byte[] encodedBytes = codec.encode(bytes);
        byte[] expectedBytes = "=48=65=6C=6C=6F=20=57=6F=72=6C=64".getBytes();
        assertArrayEquals(expectedBytes, encodedBytes);
    }
    
    @Test
    public void testDecode() {
        byte[] bytes = "=48=65=6C=6C=6F=20=57=6F=72=6C=64".getBytes();
        byte[] decodedBytes = codec.decode(bytes);
        byte[] expectedBytes = "Hello World".getBytes();
        assertArrayEquals(expectedBytes, decodedBytes);
    }
    
    @Test
    public void testEncodeWithCharset() throws UnsupportedEncodingException {
        String str = "Hello World";
        String encodedStr = codec.encode(str, "UTF-8");
        String expectedStr = "=48=65=6C=6C=6F=20=57=6F=72=6C=64";
        assertEquals(expectedStr, encodedStr);
    }
    
    @Test
    public void testDecodeWithCharset() throws UnsupportedEncodingException {
        String str = "=48=65=6C=6C=6F=20=57=6F=72=6C=64";
        String decodedStr = codec.decode(str, "UTF-8");
        String expectedStr = "Hello World";
        assertEquals(expectedStr, decodedStr);
    }
    
}