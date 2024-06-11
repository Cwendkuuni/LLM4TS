import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {
    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    @Test
    public void testEncodeByte() throws Exception {
        byte input = 65;
        byte[] output = CODEC.encode(input);
        assertEquals("Expected single byte to be encoded as single byte", input, output[0]);
    }

    @Test
    public void testEncodeByteWithEncoding() throws Exception {
        byte input = 65;
        String charset = "UTF-8";
        byte[] output = CODEC.encode(input, charset);
        assertEquals("Expected single byte to be encoded as single byte", input, output[0]);
    }

    @Test
    public void testEncodeString() throws Exception {
        String input = "Hello, World!";
        String output = CODEC.encode(input);
        String expected = "=48=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21";
        assertEquals("Expected string to be encoded correctly", expected, output);
    }

    @Test
    public void testEncodeStringWithEncoding() throws Exception {
        String input = "Hello, World!";
        String charset = "UTF-8";
        String output = CODEC.encode(input, charset);
        String expected = "=48=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21";
        assertEquals("Expected string to be encoded correctly", expected, output);
    }

    @Test
    public void testDecodeByte() throws Exception {
        byte input = 65;
        byte[] encoded = {ESCAPE_CHAR, '4', '8'};
        byte output = CODEC.decode(encoded)[0];
        assertEquals("Expected single byte to be decoded correctly", input, output);
    }

    @Test
    public void testDecodeByteArray() throws Exception {
        byte[] input = {65, 66, 67};
        byte[] output = CODEC.decode(CODEC.encode(input));
        assertArrayEquals("Expected decoded byte array to match input", input, output);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeByteArrayWithInvalidByte() throws Exception {
        byte[] input = {65, ESCAPE_CHAR, '4', 'F'};
        CODEC.decode(input);
    }

    @Test
    public void testDecodeString() throws Exception {
        String input = "=48=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21";
        String output = CODEC.decode(input);
        assertEquals("Expected string to be decoded correctly", "Hello, World!", output);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeStringWithInvalidCharacter() throws Exception {
        String input = "=48=65=6C=6C=6F=2C=20=57=6F=72=6C=64=X";
        CODEC.decode(input);
    }

    @Test
    public void testEncodeDecodeByteArrayRoundtrip() throws Exception {
        byte[] input = {65, 66, 67};
        byte[] output = CODEC.encode(input);
        byte[] roundtrip = CODEC.decode(output);
        assertArrayEquals("Expected decoded byte array to match input", input, roundtrip);
    }

    @Test
    public void testEncodeDecodeStringRoundtrip() throws Exception {
        String input = "Hello, World!";
        String output = CODEC.encode(input);
        String roundtrip = CODEC.decode(output);
        assertEquals("Expected decoded string to match input", input, roundtrip);
    }
}