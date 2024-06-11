import static org.junit.Assert.*;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeQuotedPrintable() {
        byte[] input = "Hello World".getBytes();
        byte[] expected = "Hello World".getBytes(); // Since the input is printable characters, the output should be the same
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(input);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDecodeQuotedPrintable() throws DecoderException {
        byte[] input = "Hello=20World".getBytes(); // Encoded whitespace character
        byte[] expected = "Hello World".getBytes();
        byte[] result = QuotedPrintableCodec.decodeQuotedPrintable(input);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void testEncodeString() throws EncoderException {
        String input = "Hello World";
        String expected = "Hello World"; // Since the input is printable characters, the output should be the same
        String result = QuotedPrintableCodec.encode(input);
        
        assertEquals(expected, result);
    }

    @Test
    public void testDecodeString() throws DecoderException {
        String input = "Hello=20World"; // Encoded whitespace character
        String expected = "Hello World";
        String result = QuotedPrintableCodec.decode(input);
        
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeWithCustomCharset() throws UnsupportedEncodingException {
        String input = "Hello World";
        String charset = "UTF-16";
        String expected = "Hello World";
        String result = QuotedPrintableCodec.encode(input, charset);
        
        assertEquals(expected, result);
    }

    @Test
    public void testDecodeWithCustomCharset() throws DecoderException, UnsupportedEncodingException {
        String input = "Hello=20World";
        String charset = "UTF-16";
        String expected = "Hello World";
        String result = QuotedPrintableCodec.decode(input, charset);
        
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        String input = "Hello World";
        byte[] expected = "Hello World".getBytes();
        Object result = QuotedPrintableCodec.encode(input);
        
        assertArrayEquals(expected, (byte[]) result);
    }

    @Test
    public void testDecodeObject() throws DecoderException {
        byte[] input = "Hello=20World".getBytes();
        String expected = "Hello World";
        Object result = QuotedPrintableCodec.decode(input);
        
        assertEquals(expected, result);
    }
}