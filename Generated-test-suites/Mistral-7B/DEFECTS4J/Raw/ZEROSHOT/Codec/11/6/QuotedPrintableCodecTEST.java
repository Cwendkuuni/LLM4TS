import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

/**
 * QuotedPrintableCodecTest.java
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    /**
     * Test encoding and decoding of ASCII printable characters.
     */
    @Test
    public void testEncodeAndDecodePrintable() throws EncoderException, DecoderException, IOException {
        byte[] encoded = CODEC.encode("Hello World!".getBytes());
        String decoded = CODEC.decode(encoded).toString();
        Assert.assertEquals("Hello World!", decoded);
    }

    /**
     * Test encoding and decoding of non-ASCII printable characters.
     */
    @Test
    public void testEncodeAndDecodeNonPrintable() throws EncoderException, DecoderException, IOException {
        byte[] encoded = CODEC.encode("\u00A1\u0080\u00FF".getBytes());
        String decoded = CODEC.decode(encoded).toString();
        Assert.assertEquals("\u00A1\u0080\u00FF", decoded);
    }

    /**
     * Test encoding and decoding of whitespace characters.
     */
    @Test
    public void testEncodeAndDecodeWhitespace() throws EncoderException, DecoderException, IOException {
        byte[] encoded = CODEC.encode(" \t\r\n".getBytes());
        String decoded = CODEC.decode(encoded).toString();
        Assert.assertEquals(" \t\r\n", decoded);
    }

    /**
     * Test encoding a byte array containing only printable characters.
     */
    @Test
    public void testEncodePrintableByteArray() throws EncoderException, IOException {
        byte[] input = "Hello World!".getBytes();
        byte[] output = CODEC.encode(input);
        byte[] decoded = CODEC.decode(output);
        Assert.assertArrayEquals(input, decoded);
    }

    /**
     * Test encoding a byte array containing non-printable characters.
     */
    @Test
    public void testEncodeNonPrintableByteArray() throws EncoderException, IOException {
        byte[] input = "\u00A1\u0080\u00FF".getBytes();
        byte[] output = CODEC.encode(input);
        byte[] decoded = CODEC.decode(output);
        Assert.assertArrayEquals(input, decoded);
    }

    /**
     * Test decoding a byte array containing printable characters.
     */
    @Test
    public void testDecodePrintableByteArray() throws DecoderException, IOException {
        byte[] input = CODEC.encode("Hello World!".getBytes());
        byte[] output = CODEC.decode(input);
        Assert.assertArrayEquals("Hello World!".getBytes(), output);
    }

    /**
     * Test decoding a byte array containing non-printable characters.
     */
    @Test
    public void testDecodeNonPrintableByteArray() throws DecoderException, IOException {
        byte[] input = CODEC.encode("\u00A1\u0080\u00FF".getBytes());
        byte[] output = CODEC.decode(input);
        Assert.assertArrayEquals("\u00A1\u0080\u00FF".getBytes(), output);
    }

    /**
     * Test decoding a byte array containing mixed printable and non-printable characters.
     */
    @Test
    public void testDecodeMixedByteArray() throws DecoderException, IOException {
        byte[] input = CODEC.encode("Hello World!\u00A1\u0080\u00FF".getBytes());
        byte[] output = CODEC.decode(input);
        Assert.assertArrayEquals("Hello World!\u00A1\u0080\u00FF".getBytes(), output);
    }
}