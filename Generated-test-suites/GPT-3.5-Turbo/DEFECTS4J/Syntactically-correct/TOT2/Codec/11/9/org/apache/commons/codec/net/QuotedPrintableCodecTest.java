import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeByte() {
        byte[] bytes = {65, 66, 67}; // ABC
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] encodedBytes = codec.encode(bytes);
        byte[] expectedBytes = {65, 66, 67};
        assertArrayEquals(expectedBytes, encodedBytes);
    }

    @Test
    public void testDecodeByte() {
        byte[] bytes = {65, 66, 67}; // ABC
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] decodedBytes = codec.decode(bytes);
        byte[] expectedBytes = {65, 66, 67};
        assertArrayEquals(expectedBytes, decodedBytes);
    }

    @Test
    public void testEncodeString() {
        String inputString = "Hello, World!";
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encodedString = codec.encode(inputString);
        assertEquals("Hello, World!", encodedString);
    }

    @Test
    public void testDecodeString() {
        String inputString = "Hello, World!";
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encodedString = codec.encode(inputString);
        String decodedString = codec.decode(encodedString);
        assertEquals("Hello, World!", decodedString);
    }

    @Test
    public void testDecodeStringWithCharset() {
        String inputString = "Hello, World!";
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encodedString = codec.encode(inputString);
        String decodedString = codec.decode(encodedString, "UTF-8");
        assertEquals("Hello, World!", decodedString);
    }

    @Test
    public void testEncodeObject() {
        String inputString = "Hello, World!";
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object encodedObject = codec.encode(inputString);
        assertTrue(encodedObject instanceof String);
        assertEquals("Hello, World!", encodedObject);
    }

    @Test
    public void testDecodeObject() {
        String inputString = "Hello, World!";
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encodedString = codec.encode(inputString);
        Object decodedObject = codec.decode(encodedString);
        assertTrue(decodedObject instanceof String);
        assertEquals("Hello, World!", decodedObject);
    }

    @Test
    public void testDecodeInvalidString() {
        String inputString = "Hello=World!"; // Invalid quoted printable encoding
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertThrows(DecoderException.class, () -> {
            codec.decode(inputString);
        });
    }
}