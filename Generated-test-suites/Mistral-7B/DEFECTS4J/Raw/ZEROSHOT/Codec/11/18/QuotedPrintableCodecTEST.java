import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/**
 * QuotedPrintableCodecTest class to test QuotedPrintableCodec methods.
 */
public class QuotedPrintableCodecTest {

    private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    /**
     * Test encode method with printable characters.
     */
    @Test
    public void testEncodePrintable() {
        byte[] expected = new byte[]{'A', 'B', 'C'};
        byte[] actual = codec.encode(expected);
        Assert.assertArrayEquals("encode method: printable characters", expected, actual);
    }

    /**
     * Test encode method with non-printable characters.
     */
    @Test
    public void testEncodeNonPrintable() {
        byte[] expected = new byte[]{'=31', '=32', '=33'};
        byte[] actual = codec.encode((byte) 0x11, (byte) 0x12, (byte) 0x13);
        Assert.assertArrayEquals("encode method: non-printable characters", expected, actual);
    }

    /**
     * Test encode method with whitespace character at the end of line.
     */
    @Test
    public void testEncodeWhitespace() {
        byte[] expected = new byte[]{'A', 'B', 'C', '=0D', '=0A', ' ', '='};
        byte[] actual = codec.encode('A', 'B', 'C', '\r', '\n', ' ');
        Assert.assertArrayEquals("encode method: whitespace character at the end of line", expected, actual);
    }

    /**
     * Test encode method with CR character.
     */
    @Test
    public void testEncodeCR() {
        byte[] expected = new byte[]{'A', 'B', 'C', '=0D'};
        byte[] actual = codec.encode('A', 'B', 'C', (byte) 0x0D);
        Assert.assertArrayEquals("encode method: CR character", expected, actual);
    }

    /**
     * Test decode method with printable characters.
     */
    @Test
    public void testDecodePrintable() {
        byte[] encoded = new byte[]{'A', 'B', 'C', '=31', '=32', '=33'};
        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals("decode method: printable characters", new byte[]{'A', 'B', 'C'}, decoded);
    }

    /**
     * Test decode method with non-printable characters.
     */
    @Test
    public void testDecodeNonPrintable() {
        byte[] encoded = new byte[]{'A', 'B', 'C', '=31', '=32', '=33'};
        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals("decode method: non-printable characters", new byte[]{(byte) 0x11, (byte) 0x12, (byte) 0x13}, decoded);
    }

    /**
     * Test decode method with whitespace character at the end of line.
     */
    @Test
    public void testDecodeWhitespace() {
        byte[] encoded = new byte[]{'A', 'B', 'C', '=0D', '=0A', ' ', '='};
        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals("decode method: whitespace character at the end of line", new byte[]{'A', 'B', 'C'}, decoded);
    }

    /**
     * Test decode method with CR character.
     */
    @Test
    public void testDecodeCR() {
        byte[] encoded = new byte[]{'A', 'B', 'C', '=0D'};
        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals("decode method: CR character", new byte[]{'A', 'B', 'C'}, decoded);
    }

    /**
     * Test encode and decode methods with empty byte array.
     */
    @Test
    public void testEncodeDecodeEmpty() {
        byte[] encoded = codec.encode((byte[]) null);
        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals("encode and decode methods: empty byte array", null, decoded);
    }

    /**
     * Test encode method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testEncodeNull() {
        codec.encode(null);
    }

    /**
     * Test decode method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testDecodeNull() {
        codec.decode(null);
    }
}