import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testDefaultConstructor() {
        assertNotNull(codec);
    }

    @Test
    public void testCustomCharsetConstructor() {
        QuotedPrintableCodec customCodec = new QuotedPrintableCodec("UTF-16");
        assertNotNull(customCodec);
    }

    @Test
    public void testEncodeByteArray() {
        byte[] input = {65, 66, 67};
        byte[] encoded = codec.encode(input);
        assertNotNull(encoded);
        assertArrayEquals(new byte[]{65, 66, 67}, encoded);
    }

    @Test
    public void testDecodeByteArray() {
        byte[] input = {65, 66, 67};
        byte[] decoded = codec.decode(input);
        assertNotNull(decoded);
        assertArrayEquals(new byte[]{65, 66, 67}, decoded);
    }

    @Test
    public void testEncodeString() {
        String input = "Hello World";
        String encoded = codec.encode(input);
        assertNotNull(encoded);
        assertEquals("Hello World", encoded);
    }

    @Test
    public void testDecodeString() {
        String input = "Hello World";
        String decoded = codec.decode(input);
        assertNotNull(decoded);
        assertEquals("Hello World", decoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidString() throws DecoderException {
        String input = "Invalid=String";
        codec.decode(input);
    }
}