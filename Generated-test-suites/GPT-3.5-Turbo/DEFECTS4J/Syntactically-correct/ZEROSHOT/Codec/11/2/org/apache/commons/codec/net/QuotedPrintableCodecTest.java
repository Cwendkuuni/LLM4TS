import static org.junit.Assert.*;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        
        byte[] bytes = "Hello World".getBytes();
        byte[] expected = "=48=65=6C=6C=6F=20=57=6F=72=6C=64".getBytes();
        byte[] encoded = codec.encode(bytes);
        
        assertArrayEquals(expected, encoded);
    }
    
    @Test
    public void testDecode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        
        byte[] encoded = "=48=65=6C=6C=6F=20=57=6F=72=6C=64".getBytes();
        byte[] expected = "Hello World".getBytes();
        byte[] decoded = codec.decode(encoded);
        
        assertArrayEquals(expected, decoded);
    }
    
    @Test
    public void testEncodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        
        String str = "Hello World";
        String expected = "=48=65=6C=6C=6F=20=57=6F=72=6C=64";
        String encoded = codec.encode(str);
        
        assertEquals(expected, encoded);
    }
    
    @Test
    public void testDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        
        String encoded = "=48=65=6C=6C=6F=20=57=6F=72=6C=64";
        String expected = "Hello World";
        String decoded = codec.decode(encoded);
        
        assertEquals(expected, decoded);
    }
}