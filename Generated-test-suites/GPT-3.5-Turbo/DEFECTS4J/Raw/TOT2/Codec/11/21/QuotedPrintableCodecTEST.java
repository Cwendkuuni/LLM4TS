import static org.junit.Assert.*;

import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testConstructorDefaultCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNotNull(codec);
        assertEquals("UTF-8", codec.getDefaultCharset());
    }

    @Test
    public void testConstructorCustomCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-1");
        assertNotNull(codec);
        assertEquals("ISO-8859-1", codec.getDefaultCharset());
    }

    @Test
    public void testEncodeAndDecode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeNullArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode(null));
    }

    @Test
    public void testDecodeNullArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.decode(null));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode("InvalidInput");
    }

    @Test
    public void testEncodeWithCustomCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-16");
        String input = "Hello World";
        String encoded = codec.encode(input, "UTF-16");
        assertEquals(input, codec.decode(encoded, "UTF-16"));
    }
}