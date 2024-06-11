import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    private QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncode() throws EncoderException {
        String input = "Hello World";
        String expected = "Hello=20World";
        assertEquals(expected, codec.encode(input));
    }

    @Test
    public void testDecode() throws DecoderException {
        String input = "Hello=20World";
        String expected = "Hello World";
        assertEquals(expected, codec.decode(input));
    }

    @Test
    public void testEncodeWithNullInput() throws EncoderException {
        assertNull(codec.encode(null));
    }

    @Test
    public void testDecodeWithNullInput() throws DecoderException {
        assertNull(codec.decode(null));
    }

    @Test
    public void testEncodeByteArray() {
        byte[] input = {72, 101, 108, 108, 111};
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, codec.encode(input));
    }

    @Test
    public void testDecodeByteArray() throws DecoderException {
        byte[] input = {72, 101, 108, 108, 111};
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, codec.decode(input));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeByteArrayWithInvalidInput() throws DecoderException {
        byte[] input = {72, 101, 108, 108, 111, 61, 65}; // Hello=A
        codec.decode(input);
    }
}