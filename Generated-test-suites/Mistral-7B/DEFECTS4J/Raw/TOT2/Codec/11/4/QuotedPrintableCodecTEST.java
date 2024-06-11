import org.apache.commons.codec.ByteArrayOutputStream;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * QuotedPrintableCodecTest.java
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    /**
     * Test for basic functionality of encode method
     */
    @Test
    public void testEncodeBasicFunctionality() throws EncoderException {
        String input = "Hello, World!";
        byte[] expectedOutput = new byte[]{"48", "65", "6C", "6C", "6F", "2C", "20", "57", "4C", "6F", "21"};
        byte[] output = codec.encode(input.getBytes());
        Assert.assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test for basic functionality of decode method
     */
    @Test
    public void testDecodeBasicFunctionality() throws DecoderException {
        String input = "48-65-6C-6C-6F-2C-20-57-4C-6F-21";
        String expectedOutput = "Hello, World!";
        String output = codec.decode(input.getBytes()).toString();
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test edge case: handling of CR and LF characters
     */
    @Test
    public void testEncodeEdgeCaseCRAndLF() throws EncoderException {
        String input = "Hello\rWorld!\n";
        byte[] expectedOutput = new byte[]{"48", "65", "6C", "6C", "6F", "2C", "0D", "0A", "57", "4C", "6F", "21"};
        byte[] output = codec.encode(input.getBytes());
        Assert.assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test edge case: handling of whitespace characters
     */
    @Test
    public void testEncodeEdgeCaseWhitespace() throws EncoderException {
        String input = "Hello World\t\n";
        byte[] expectedOutput = new byte[]{"48", "65", "6C", "6C", "6F", "20", "57", "6F", "72", "6C", "64", "20", "09", "0A"};
        byte[] output = codec.encode(input.getBytes());
        Assert.assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test error scenario: null input for encode method
     */
    @Test(expected = EncoderException.class)
    public void testEncodeErrorScenarioNullInput() throws EncoderException {
        codec.encode(null);
    }

    /**
     * Test error scenario: null input for decode method
     */
    @Test(expected = DecoderException.class)
    public void testDecodeErrorScenarioNullInput() throws DecoderException {
        codec.decode(null);
    }
}