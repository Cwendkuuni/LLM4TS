import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeByteArray_Basic() throws Exception {
        byte[] input = new byte[]{'A', 'B', 'C'};
        byte[] expectedOutput = Hex.decodeHex("414243");
        byte[] output = CODEC.encode(input);
        byte[] decodedOutput = CODEC.decode(output);

        Assert.assertArrayEquals(expectedOutput, decodedOutput);
        Assert.assertArrayEquals(output, CODEC.encode(decodedOutput));
    }

    @Test
    public void testEncodeDecodeByteArray_PrintableCharacters() throws Exception {
        byte[] input = new byte[]{'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] expectedOutput = Hex.decodeHex("414243444546");
        byte[] output = CODEC.encode(input);

        byte[] decodedOutput = CODEC.decode(output);
        Assert.assertArrayEquals(expectedOutput, decodedOutput);
        Assert.assertArrayEquals(output, CODEC.encode(decodedOutput));
    }

    @Test
    public void testEncodeDecodeByteArray_UnprintableCharacters() throws Exception {
        byte[] input = new byte[]{'A', 'B', '\u007F', 'C', 'D', 'E', 'F'};
        byte[] expectedOutput = Hex.decodeHex("41427f43444546");
        byte[] output = CODEC.encode(input);

        byte[] decodedOutput = CODEC.decode(output);
        Assert.assertArrayEquals(expectedOutput, decodedOutput);
        Assert.assertArrayEquals(output, CODEC.encode(decodedOutput));
    }

    @Test
    public void testEncodeDecodeByteArray_Whitespace() throws Exception {
        byte[] input = new byte[]{'\u0009', '\u0020', 'A', 'B', '\u0009', '\u0020', 'C'};
        byte[] expectedOutput = Hex.decodeHex("09204142092043");
        byte[] output = CODEC.encode(input);

        byte[] decodedOutput = CODEC.decode(output);
        Assert.assertArrayEquals(expectedOutput, decodedOutput);
        Assert.assertArrayEquals(output, CODEC.encode(decodedOutput));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeByteArray_InvalidEscapeSequence() throws Exception {
        byte[] input = new byte[]{'A', 'B', 'C', '=', 'X'}; // Invalid escape sequence 'X' instead of valid '4x' or '2x'

        CODEC.decode(input);
    }

    @Test
    public void testEncodeDecodeByteArray_MaxLength() throws Exception {
        byte[] input = new byte[65536];
        byte[] output = CODEC.encode(input);
        byte[] decodedOutput = CODEC.decode(output);

        Assert.assertArrayEquals(input, decodedOutput);
        Assert.assertArrayEquals(output, CODEC.encode(decodedOutput));
    }

    @Test
    public void testEncodeDecodeString_Basic() throws Exception {
        String input = "ABC";
        String expectedOutput = "=41=42=43";
        String output = CODEC.encode(input);

        String decodedOutput = CODEC.decode(output);
        Assert.assertEquals(input, decodedOutput);
        Assert.assertEquals(output, CODEC.encode(decodedOutput));
    }

    @Test
    public void testEncodeDecodeString_PrintableCharacters() throws Exception {
        String input = "ABCDEF";
        String expectedOutput = "=41=42=43=44=45=46";
        String output = CODEC.encode(input);

        String decodedOutput = CODEC.decode(output);
        Assert.assertEquals(input, decodedOutput);
        Assert.assertEquals(output, CODEC.encode(decodedOutput));
    }

    @Test
    public void testEncodeDecodeString_UnprintableCharacters() throws Exception {
        String input = "ABC\u007FDEF";
        String expectedOutput = "=41=42=7f=44=45=46";
        String output = CODEC.encode(input);

        String decodedOutput = CODEC.decode(output);
        Assert.assertEquals(input, decodedOutput);
        Assert.assertEquals(output, CODEC.encode(decodedOutput));
    }

    @Test
    public void testEncodeDecodeString_Whitespace() throws Exception {
        String input = "\t \tABC\t \tDEF";
        String expectedOutput = "0920092041092042092043092044092045092046";
        String output = CODEC.encode(input);

        String decodedOutput = CODEC.decode(output);
        Assert.assertEquals(input, decodedOutput);
        Assert.assertEquals(output, CODEC.encode(decodedOutput));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeString_InvalidInput() throws Exception {
        CODEC.encode(null);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeByteArray_InvalidInput() throws Exception {
        byte[] invalidInput = new byte[]{0x01, 0x02};

        CODEC.decode(invalidInput);
    }
}