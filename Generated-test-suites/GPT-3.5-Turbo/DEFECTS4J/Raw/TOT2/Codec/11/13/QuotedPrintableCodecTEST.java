import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeQuotedPrintable() {
        byte[] testBytes = {97, 98, 99}; // Test data "abc"
        byte[] expectedBytes = {97, 98, 99}; // Expected encoded bytes
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, testBytes);
        assertArrayEquals(expectedBytes, result);
    }

    @Test
    public void testDecodeQuotedPrintable() throws DecoderException {
        byte[] testBytes = {97, 98, 99}; // Test data "abc"
        byte[] encodedBytes = QuotedPrintableCodec.encodeQuotedPrintable(null, testBytes);
        byte[] result = QuotedPrintableCodec.decodeQuotedPrintable(encodedBytes);
        assertArrayEquals(testBytes, result);
    }

    @Test
    public void testEncodeString() throws EncoderException {
        String testString = "Hello, World!";
        String expectedEncodedString = "=48=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21";
        String result = new QuotedPrintableCodec().encode(testString);
        assertEquals(expectedEncodedString, result);
    }

    @Test
    public void testDecodeString() throws DecoderException {
        String expectedDecodedString = "Hello, World!";
        String encodedString = "=48=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21";
        String result = new QuotedPrintableCodec().decode(encodedString);
        assertEquals(expectedDecodedString, result);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeStringWithInvalidEncoding() throws DecoderException {
        String invalidEncodedString = "=48=65=6C=6C=6F=2C=20=57=6F=72=6C=64"; // Missing last character
        new QuotedPrintableCodec().decode(invalidEncodedString);
    }
}