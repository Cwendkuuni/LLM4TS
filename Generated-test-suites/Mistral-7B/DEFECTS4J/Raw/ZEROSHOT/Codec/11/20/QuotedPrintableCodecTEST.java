import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

/**
 * QuotedPrintableCodecTest class to test QuotedPrintableCodec methods.
 */
public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecode() throws DecoderException, EncoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Test encoding and decoding of basic characters
        byte[] input = "Hello World!".getBytes();
        byte[] encoded = codec.encode(input);
        String decoded = codec.decodeToString(encoded);
        Assert.assertEquals("Hello World!", decoded);

        // Test encoding and decoding of special characters
        input = "\r\n\t  ".getBytes();
        encoded = codec.encode(input);
        decoded = codec.decodeToString(encoded);
        Assert.assertEquals("\r\n\t  ", decoded);

        // Test encoding and decoding of escaped characters
        input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".getBytes();
        encoded = codec.encode(input);
        decoded = codec.decodeToString(encoded);
        Assert.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=", decoded);

        // Test encoding and decoding of binary data
        input = new byte[]{(byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
        encoded = codec.encode(input);
        byte[] decodedBytes = codec.decode(encoded);
        Assert.assertArrayEquals(new byte[]{(byte) 0xAB, (byte) 0xCD, (byte) 0xEF}, decodedBytes);

        // Test encoding and decoding of long input
        String longInput = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
        byte[] longEncoded = codec.encode(longInput.getBytes());
        String longDecoded = codec.decodeToString(longEncoded);
        Assert.assertEquals(longInput, longDecoded);

        // Test encoding and decoding of multiline input
        String multilineInput = "Line 1\r\nLine 2\r\nLine 3\r\n";
        byte[] multilineEncoded = codec.encode(multilineInput.getBytes());
        String multilineDecoded = codec.decodeToString(multilineEncoded);
        Assert.assertEquals(multilineInput, multilineDecoded);

        // Test encoding and decoding of encoded characters in input
        String encodedInput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".replace("=", "==");
        byte[] encodedBytes = Hex.decodeHex(encodedInput.substring(1));
        byte[] decodedBytes = codec.decode(encodedBytes);
        String decoded = codec.decodeToString(encodedBytes);
        Assert.assertEquals(encodedInput, "=" + new String(decodedBytes));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeFailure() throws IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(new byte[]{(byte) 0xFF});
    }

    @Test(expected = EncoderException.class)
    public void testEncodeFailure() throws IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode(new byte[]{(byte) 0xFF});
    }
}

class Utils {
    /**
     * Converts 2 hex digits to a byte.
     *
     * @param c1 the first hex digit
     * @param c2 the second hex digit
     * @return the byte value
     */
    public static int digit16(char c1, char c2) {
        int value = Character.isDigit(c1) ? Character.digit(c1) : Character.toUpperCase(c1) - 64;
        return value * 16 + Character.isDigit(c2) ? Character.digit(c2) : Character.toUpperCase(c2) - 64;
    }
}