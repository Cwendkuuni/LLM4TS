import org.junit.Test;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    private QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeByte() {
        byte[] input = {65, 66, 67};
        byte[] expectedOutput = {65, 66, 67};
        byte[] encodedOutput = codec.encode(input);
        assertArrayEquals(expectedOutput, encodedOutput);
    }

    @Test
    public void testEncodeNullByte() {
        byte[] input = null;
        byte[] expectedOutput = null;
        byte[] encodedOutput = codec.encode(input);
        assertArrayEquals(expectedOutput, encodedOutput);
    }

    @Test
    public void testDecodeByte() throws DecoderException {
        byte[] input = {65, 66, 67};
        byte[] expectedOutput = {65, 66, 67};
        byte[] decodedOutput = codec.decode(input);
        assertArrayEquals(expectedOutput, decodedOutput);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidEncoding() throws DecoderException {
        byte[] input = {65, 61, 99};
        codec.decode(input);
    }

    @Test
    public void testEncodeString() throws EncoderException {
        String input = "ABC";
        String expectedOutput = "ABC";
        String encodedOutput = codec.encode(input);
        assertEquals(expectedOutput, encodedOutput);
    }

    @Test
    public void testEncodeNullString() throws EncoderException {
        String input = null;
        String expectedOutput = null;
        String encodedOutput = codec.encode(input);
        assertEquals(expectedOutput, encodedOutput);
    }

    @Test
    public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
        String input = "ABC";
        String expectedOutput = "ABC";
        String decodedOutput = codec.decode(input);
        assertEquals(expectedOutput, decodedOutput);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidEncodingString() throws DecoderException {
        String input = "ABC=";
        codec.decode(input);
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Object input = "ABC";
        Object expectedOutput = "ABC";
        Object encodedOutput = codec.encode(input);
        assertEquals(expectedOutput, encodedOutput);
    }

    @Test
    public void testDecodeObject() throws DecoderException, UnsupportedEncodingException {
        Object input = "ABC";
        Object expectedOutput = "ABC";
        Object decodedOutput = codec.decode(input);
        assertEquals(expectedOutput, decodedOutput);
    }
}