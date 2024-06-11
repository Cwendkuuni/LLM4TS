import org.apache.commons.codec.DecoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    private QuotedPrintableCodec codec = new QuotedPrintableCodec();
  
    // Test encode(byte[]) method
    @Test
    public void testEncodeByteArray() {
        byte[] input = {97, 98, 99};
        assertEquals("=61=62=63", new String(codec.encode(input)));
    }

    // Test decode(byte[]) method
    @Test
    public void testDecodeByteArray() throws DecoderException {
        byte[] input = {61, 54, 65};
        assertArrayEquals(new byte[] {54}, codec.decode(input));
    }

    // Test encode(String) method
    @Test
    public void testEncodeString() throws DecoderException {
        String input = "Hello world!";
        assertEquals("Hello world!", codec.encode(input));
    }

    // Test decode(String) method
    @Test
    public void testDecodeString() throws DecoderException {
        String input = "Hello world!";
        assertEquals("Hello world!", codec.decode(input));
    }

}