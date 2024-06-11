import static org.junit.Assert.*;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "This is a test string.";
        String expected = "This=20is=20a=20test=20string.";
        assertEquals(expected, codec.encode(input));

        // Edge case: Empty string
        input = "";
        expected = "";
        assertEquals(expected, codec.encode(input));

        // Error scenario: Unsupported character in input
        input = "Unsupported character: €";
        assertThrows(EncoderException.class, () -> codec.encode(input));
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "This=20is=20a=20test=20string.";
        String expected = "This is a test string.";
        assertEquals(expected, codec.decode(input));

        // Edge case: Empty string
        input = "";
        expected = "";
        assertEquals(expected, codec.decode(input));

        // Error scenario: Invalid escape sequence
        input = "Invalid escape sequence: =-1";
        assertThrows(DecoderException.class, () -> codec.decode(input));
    }
}