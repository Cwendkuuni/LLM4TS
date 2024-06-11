import static org.junit.Assert.*;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Hello World".getBytes();
        byte[] expected = "Hello World".getBytes();
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Hello World".getBytes();
        byte[] encoded = codec.encode(input);
        byte[] expected = "Hello World".getBytes();
        byte[] result = codec.decode(encoded);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testEncodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] result = codec.encode(null);
        assertNull(result);
    }

    @Test
    public void testDecodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] result = codec.decode(null);
        assertNull(result);
    }

    @Test
    public void testEncodeString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World";
        String expected = "Hello World";
        String result = codec.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDecodeString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World";
        String encoded = codec.encode(input);
        String expected = "Hello World";
        String result = codec.decode(encoded);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeObject() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object input = "Hello World";
        String expected = "Hello World";
        Object result = codec.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDecodeObject() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object input = "Hello World";
        String encoded = codec.encode(input);
        String expected = "Hello World";
        Object result = codec.decode(encoded);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeUnsupportedCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        
        assertThrows(UnsupportedEncodingException.class, () -> {
            codec.encode("Hello World", "unsupportedCharset");
        });
    }

    @Test
    public void testDecodeUnsupportedCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encoded = codec.encode("Hello World");
        
        assertThrows(UnsupportedEncodingException.class, () -> {
            codec.decode(encoded, "unsupportedCharset");
        });
    }
}