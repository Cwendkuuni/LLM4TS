import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeQuotedPrintable() {
        byte[] input = "Hello World".getBytes();
        byte[] expected = "Hello World".getBytes();
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDecodeQuotedPrintable() throws DecoderException {
        byte[] input = "Hello World".getBytes();
        byte[] encoded = QuotedPrintableCodec.encodeQuotedPrintable(null, input);
        byte[] result = QuotedPrintableCodec.decodeQuotedPrintable(encoded);
        assertArrayEquals(input, result);
    }

    @Test
    public void testEncode() throws EncoderException {
        String input = "Hello World";
        String expected = "Hello World";
        String result = new QuotedPrintableCodec().encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDecode() throws DecoderException {
        String input = "Hello World";
        String encoded = new QuotedPrintableCodec().encode(input);
        String result = new QuotedPrintableCodec().decode(encoded);
        assertEquals(input, result);
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        String input = "Hello World";
        byte[] inputBytes = input.getBytes();
        
        byte[] expected = QuotedPrintableCodec.encodeQuotedPrintable(null, inputBytes);
        byte[] resultBytes = (byte[]) new QuotedPrintableCodec().encode(inputBytes);
        assertArrayEquals(expected, resultBytes);
        
        String resultString = (String) new QuotedPrintableCodec().encode(input);
        assertEquals(input, resultString);
    }

    @Test
    public void testDecodeObject() throws DecoderException {
        String input = "Hello World";
        byte[] inputBytes = input.getBytes();
        
        byte[] encoded = QuotedPrintableCodec.encodeQuotedPrintable(null, inputBytes);
        
        byte[] resultBytes = (byte[]) new QuotedPrintableCodec().decode(inputBytes);
        assertArrayEquals(inputBytes, resultBytes);
        
        String resultString = (String) new QuotedPrintableCodec().decode(new String(encoded));
        assertEquals(input, resultString);
    }
}