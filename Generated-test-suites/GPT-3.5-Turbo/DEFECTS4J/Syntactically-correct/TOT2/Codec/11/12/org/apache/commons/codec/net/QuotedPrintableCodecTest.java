import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    @Test
    public void test_encodeQuotedPrintable_withPrintableChars() {
        byte[] bytes = {65, 66, 67, 68}; // ABCD
        BitSet printableChars = new BitSet(256);
        for (int i = 65; i <= 90; i++) {
            printableChars.set(i);
        }
        byte[] expected = {65, 66, 67, 68};
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(printableChars, bytes);
        assertArrayEquals(expected, result);
    }

    @Test
    public void test_encodeQuotedPrintable_withNonPrintableChars() {
        byte[] bytes = {127, 128, -1}; // Non-printable characters
        byte[] expected = {61, 37, 38, 61, 31, 46};
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, bytes);
        assertArrayEquals(expected, result);
    }

    @Test
    public void test_encodeQuotedPrintable_withSpaceAndTab() {
        byte[] bytes = {32, 9, 32}; // Space, Tab, Space
        byte[] expected = {32, 61, 39, 9, 32};
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, bytes);
        assertArrayEquals(expected, result);
    }

    @Test
    public void test_encodeQuotedPrintable_withSoftLineBreak() {
        byte[] bytes = new byte[8000]; // Create byte array with length 8000
        for (int i = 0; i < 8000; i += 40) {
            bytes[i] = 13; // Carriage Return
            bytes[i + 1] = 10; // Line Feed
        }
        byte[] expected = new byte[8000 * 3];
        int index = 0;
        for (int i = 0; i < 8000; i += 40) {
            expected[index++] = 13; // Carriage Return
            expected[index++] = 10; // Line Feed
            expected[index++] = 61; // Soft Line Break
        }
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, bytes);
        assertArrayEquals(expected, result);
    }

    @Test
    public void test_decodeQuotedPrintable_withValidInput() throws DecoderException {
        byte[] bytes = {65, 61, 66, 61, 67, 61, 68}; // A=B=C=D
        byte[] expected = {65, 66, 67, 68}; // ABCD
        byte[] result = QuotedPrintableCodec.decodeQuotedPrintable(bytes);
        assertArrayEquals(expected, result);
    }

    @Test
    public void test_decodeQuotedPrintable_withInvalidInput() {
        byte[] bytes = {65, 66, 130}; // Invalid character 130
        assertThrows(DecoderException.class, () -> QuotedPrintableCodec.decodeQuotedPrintable(bytes));
    }

    @Test
    public void test_encode_withNullBytes() {
        byte[] bytes = null;
        byte[] result = new QuotedPrintableCodec().encode(bytes);
        assertNull(result);
    }

    @Test
    public void test_decode_withNullBytes() throws DecoderException {
        byte[] bytes = null;
        byte[] result = new QuotedPrintableCodec().decode(bytes);
        assertNull(result);
    }

    @Test
    public void test_encode_withStringInput() throws EncoderException {
        String input = "Hello World!";
        String expected = "Hello World!";
        String result = new QuotedPrintableCodec().encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void test_decode_withStringInput() throws DecoderException {
        String input = "Hello World!";
        String expected = "Hello World!";
        String result = new QuotedPrintableCodec().decode(input);
        assertEquals(expected, result);
    }
}