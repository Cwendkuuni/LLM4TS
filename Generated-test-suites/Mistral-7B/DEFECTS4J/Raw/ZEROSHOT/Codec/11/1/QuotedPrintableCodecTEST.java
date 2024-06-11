import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeBytes() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        byte[] output = codec.encode(input);
        byte[] decoded = codec.decode(output);

        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String encoded = codec.encode(input, StandardCharsets.UTF_8);
        String decoded = codec.decode(encoded, StandardCharsets.UTF_8);

        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeBitSet() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        BitSet printable = new BitSet(256);

        byte[] input = codec.encode(printable.toByteArray(), printable);
        BitSet decoded = new BitSet(256);
        decoded.set(codec.decode(input));

        Assert.assertArrayEquals(printable.toByteArray(), decoded.toByteArray());
    }

    @Test
    public void testEncodeWhitespace() throws IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {'\t', ' '};
        byte[] output = codec.encode(input);

        byte expectedFirst = (byte) 0x3D;
        byte expectedSecond = 0x20; // Space character is printable, no need to encode

        Assert.assertArrayEquals(new byte[]{expectedFirst, ' ', expectedFirst, 0x3D}, output);
    }

    @Test
    public void testEncodeNewline() throws IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x0A};
        byte[] output = codec.encode(input);

        byte expectedFirst = 0x0D;
        byte expectedSecond = 0x0A;

        Assert.assertArrayEquals(new byte[]{expectedFirst, expectedSecond, '='}, output);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalid() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x3D, 'A', 'B', '=', 0x3D, 'C'};
        byte[] decoded = codec.decode(input);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidTrailingEquals() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x0D, 0x0A, 0x3D, 0x3D};
        byte[] decoded = codec.decode(input);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeMissingCR() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {'=', 'L', 'F'};
        byte[] decoded = codec.decode(input);
    }

    @Test
    public void testEncodeDecodeInvalidBytes() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, (byte) 0x80, 0x03, 0x04, 0x05};
        byte[] output = codec.encode(input);
        byte[] decoded = codec.decode(output);

        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithInvalidChars() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abc\u0080def";
        String encoded = codec.encode(input, StandardCharsets.UTF_8);
        String decoded = codec.decode(encoded, StandardCharsets.UTF_8);

        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeInvalidString() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "\u0000";
        String encoded = codec.encode(input, StandardCharsets.UTF_8);
        String decoded = codec.decode(encoded, StandardCharsets.UTF_8);

        Assert.assertEquals(input, decoded);
    }
}