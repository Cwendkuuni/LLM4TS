import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World!";
        String expected = "Hello World!";
        String actual = codec.encode(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World!";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "Hello World!";
        String expected = "Hello World!";
        String actual = codec.encode(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testDecodeWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "Hello World!";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeWithNullInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = null;
        String actual = codec.encode(input);
        assertEquals(null, actual);
    }

    @Test
    public void testDecodeWithNullInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = null;
        String actual = codec.decode(input);
        assertEquals(null, actual);
    }

    @Test
    public void testEncodeObject() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World!";
        Object encodedObject = codec.encode(input);
        assertNotNull(encodedObject);
    }

    @Test
    public void testDecodeObject() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World!";
        String encoded = codec.encode(input);
        Object decodedObject = codec.decode(encoded);
        assertNotNull(decodedObject);
    }

    @Test
    public void testEncodeDecodeEdgeCase() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "This is a test\n with line break";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        assertEquals(input, decoded);
    }

    @Test
    public void testDecodeInvalidQuotedPrintable() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Invalid enc=oding";
        try {
            codec.decode(input);
        } catch (DecoderException e) {
            // Expected exception
            return;
        }
        fail("DecoderException not thrown");
    }
}