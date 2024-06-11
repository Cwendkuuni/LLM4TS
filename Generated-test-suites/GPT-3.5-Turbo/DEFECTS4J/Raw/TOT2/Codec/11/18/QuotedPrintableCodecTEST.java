import org.junit.Test;
import static org.junit.Assert.*;
import java.io.UnsupportedEncodingException;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeQuotedPrintable() {
        byte[] testBytes = {65, 66, 67, 32, 126};
        BitSet printableChars = new BitSet(256);
        printableChars.set(32, 127);

        byte[] expected = {65, 66, 67, 61, 32, 126};
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(printableChars, testBytes);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDecodeQuotedPrintable() {
        byte[] testBytes = {65, 66, 67, 61, 32, 126};

        byte[] expected = {65, 66, 67, 32, 126};
        byte[] result = QuotedPrintableCodec.decodeQuotedPrintable(testBytes);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testEncodeString() throws EncoderException {
        String testString = "ABC ~";
        String expected = "ABC=20~";

        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.encode(testString);

        assertEquals(expected, result);
    }

    @Test
    public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
        String testString = "ABC=20~";
        String expected = "ABC ~";

        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.decode(testString);

        assertEquals(expected, result);
    }
}