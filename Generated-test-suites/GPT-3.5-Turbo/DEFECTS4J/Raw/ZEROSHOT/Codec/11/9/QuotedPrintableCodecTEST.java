import org.junit.Test;

import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Test String".getBytes();
        byte[] expected = "Test String".getBytes();
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Test String".getBytes();
        byte[] encoded = codec.encode(input);
        byte[] expected = "Test String".getBytes();
        byte[] result = codec.decode(encoded);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testEncodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Test String";
        String expected = "Test String";
        String result = codec.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Test String";
        String encoded = codec.encode(input);
        String expected = "Test String";
        String result = codec.decode(encoded);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeObject() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object input = "Test String";
        Object expected = "Test String";
        Object result = codec.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDecodeObject() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object input = "Test String";
        Object encoded = codec.encode(input);
        Object expected = "Test String";
        Object result = codec.decode(encoded);
        assertEquals(expected, result);
    }

}