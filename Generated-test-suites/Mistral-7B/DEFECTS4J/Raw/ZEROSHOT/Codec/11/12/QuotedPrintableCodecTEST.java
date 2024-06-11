import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeByteArray() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x61, 0x62, 0x63};
        byte[] expectedOutput = {0x61, 0x62, 0x63, 0x61, 0x62, 0x63};
        byte[] output = codec.encode(input);

        Assert.assertArrayEquals(expectedOutput, output);

        byte[] decodedOutput = codec.decode(output);

        Assert.assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeDecodeString() throws EncoderException, DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abc";
        String expectedOutput = "abc\r\nABC\r\n";
        String output = codec.encode(input, StandardCharsets.UTF_8);

        Assert.assertEquals(expectedOutput, output);

        String decodedOutput = codec.decode(output, StandardCharsets.UTF_8);

        Assert.assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeByteArray() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x61, 0x62, 0x63};
        byte[] expectedOutput = {(byte) 0x61, (byte) 0x62, (byte) 0x63, (byte) 0x61, (byte) 0x62, (byte) 0x63};
        byte[] output = codec.encode(input);

        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testDecodeByteArray() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x61, 0x62, 0x63, 0x61, 0x62, 0x63};
        byte[] output = codec.decode(input);

        Assert.assertArrayEquals(new byte[]{0x61, 0x62, 0x63}, output);
    }

    @Test
    public void testEncodeString() throws EncoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abc";
        String expectedOutput = "abc\r\nABC\r\n";
        String output = codec.encode(input);

        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abc\r\nABC\r\n";
        String output = codec.decode(input, StandardCharsets.UTF_8);

        Assert.assertEquals("abc", output);
    }
}