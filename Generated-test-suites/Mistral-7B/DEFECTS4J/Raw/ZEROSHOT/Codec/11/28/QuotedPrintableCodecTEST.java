import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecode() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Hello, 世界!".getBytes();
        byte[] encoded = codec.encode(input);
        Assert.assertArrayEquals("Encoded incorrectly.", codec.decode(encoded), input);
    }

    @Test
    public void testEncodeDecodeString() throws EncoderException, DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello, 世界!";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        Assert.assertEquals("Decoded incorrectly.", input, decoded);
    }

    @Test
    public void testEncodeBinaryCharacters() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {0x01, 0xFF, 0x7F, (byte) 0x80};
        byte[] encoded = codec.encode(input);
        BitSet printable = QuotedPrintableCodec.PRINTABLE_CHARS;
        byte[] decoded = codec.decode(encoded);
        for (int i = 0; i < input.length; i++) {
            Assert.assertTrue("Decoding failed for byte " + i, printable.get(decoded[i]));
        }
        Assert.assertArrayEquals("Encoded incorrectly.", encoded, codec.encode(decoded));
    }

    @Test
    public void testEncodeWhitespace() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = "Hello\tWorld!".getBytes();
        byte[] encoded = codec.encode(input);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (byte b : encoded) {
            if (b == QuotedPrintableCodec.ESCAPE_CHAR) {
                output.write(QuotedPrintableCodec.ESCAPE_CHAR);
                output.write(0x20); // space
                output.write(0x20); // space
            } else {
                output.write(b);
            }
        }
        byte[] expected = output.toByteArray();
        Assert.assertArrayEquals("Encoded incorrectly.", expected, encoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalid() throws DecoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {0x61, 0x62, 0x63, (byte) 0x80, 0x65, 0x6E};
        codec.decode(input);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeUnsupportedCharset() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello, 世界!";
        codec.decode(input.getBytes("ASCII"));
    }
}