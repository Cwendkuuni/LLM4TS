import static org.junit.Assert.*;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Test encoding null byte array
        assertNull(codec.encode(null));

        // Test encoding empty byte array
        assertArrayEquals(new byte[]{}, codec.encode(new byte[]{}));

        // Test encoding printable characters
        String text = "Hello World!";
        byte[] encodedText = codec.encode(text.getBytes());
        assertArrayEquals(text.getBytes(), encodedText);
    }

    @Test
    public void testDecode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Test decoding null byte array
        assertNull(codec.decode(null));

        // Test decoding empty byte array
        assertArrayEquals(new byte[]{}, codec.decode(new byte[]{}));

        // Test decoding printable characters
        String text = "Hello World!";
        byte[] encodedText = text.getBytes();
        byte[] decodedText = codec.decode(encodedText);
        assertArrayEquals(encodedText, decodedText);
    }

    @Test
    public void testEncodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Test encoding null string
        assertNull(codec.encode(null));

        // Test encoding empty string
        assertEquals("", codec.encode(""));

        // Test encoding printable string
        String text = "Hello World!";
        assertEquals(text, codec.encode(text));
    }

    @Test
    public void testDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Test decoding null string
        assertNull(codec.decode(null));

        // Test decoding empty string
        assertEquals("", codec.decode(""));

        // Test decoding printable string
        String text = "Hello World!";
        assertEquals(text, codec.decode(text));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidEncoding() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Test decoding invalid encoding
        String invalidEncoding = "Invalid=Encoding";
        codec.decode(invalidEncoding);
    }
}