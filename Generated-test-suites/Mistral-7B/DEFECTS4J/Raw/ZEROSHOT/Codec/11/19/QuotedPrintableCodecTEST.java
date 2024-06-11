import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeBinary() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x00, 0x07, 0x1a, 0x23, 0x36, 0x45, 0x5e, 0x6f, 0x7a, 0x82, 0x91, 0x10, 0x11, 0x12, 0x13};
        byte[] output = codec.encode(input);
        byte[] decoded = codec.decode(output);

        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ\u0007\u0008";
        String output = codec.encode(input, StandardCharsets.UTF_8);
        String decoded = codec.decode(output, StandardCharsets.UTF_8);

        Assert.assertEquals(input, decoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeStringUnsupportedCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode("abc\u0300", "ISO-8859-1");
    }

    @Test(expected = DecoderException.class)
    public void testDecodeStringUnsupportedCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode("ab%C3%B1c", "ISO-8859-1");
    }

    @Test
    public void testEncodeDecodeSpecialChars() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "!\r\n\t \t\f\b\b\b\b\b\b\b\b\b\b\b\bCR\rLF\n";
        String output = codec.encode(input, StandardCharsets.UTF_8);
        String decoded = codec.decode(output, StandardCharsets.UTF_8);

        Assert.assertEquals(input, decoded);
    }
}