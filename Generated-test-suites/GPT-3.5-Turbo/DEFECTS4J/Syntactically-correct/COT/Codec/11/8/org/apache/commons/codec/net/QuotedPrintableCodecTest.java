import org.junit.Test;

public class QuotedPrintableCodecTest {

    private QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec();

    @Test
    public void testEncode() {
        byte[] input = "Hello World".getBytes();
        byte[] expectedOutput = "Hello World".getBytes();
        byte[] actualOutput = quotedPrintableCodec.encode(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDecode() throws DecoderException {
        byte[] input = "Hello World".getBytes();
        byte[] expectedOutput = "Hello World".getBytes();
        byte[] encodedInput = quotedPrintableCodec.encode(input);
        byte[] actualOutput = quotedPrintableCodec.decode(encodedInput);
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEncodeString() throws EncoderException {
        String input = "Hello World";
        String expectedOutput = "Hello World";
        String actualOutput = quotedPrintableCodec.encode(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDecodeString() throws DecoderException {
        String input = "Hello World";
        String expectedOutput = "Hello World";
        String encodedInput = quotedPrintableCodec.encode(input);
        String actualOutput = quotedPrintableCodec.decode(encodedInput);
        assertEquals(expectedOutput, actualOutput);
    }

}