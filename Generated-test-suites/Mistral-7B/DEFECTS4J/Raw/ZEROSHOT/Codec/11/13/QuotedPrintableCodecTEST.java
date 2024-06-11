import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeBytes() throws Exception {
        byte[] input = {0x61, 0x62, 0x63, 0x0D, 0x0A, 0x64, 0x65, 0x66};
        byte[] output = CODEC.encode(input);
        byte[] decoded = CODEC.decode(output);

        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        String input = "abc\r\ndefghij";
        String output = CODEC.encode(input, StandardCharsets.UTF_8);
        String decoded = CODEC.decode(output, StandardCharsets.UTF_8);

        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeString() throws Exception {
        String input = "abc\r\ndefghij";
        String encoded = CODEC.encode(input);

        Assert.assertNotEquals(input, encoded);
        Assert.assertEquals(encoded.length(), input.length() + 4);

        BitSet printableChars = new BitSet(256);
        byte[] decoded = CODEC.decodeQuotedPrintable(encoded.getBytes());

        Assert.assertArrayEquals(input.getBytes(StandardCharsets.UTF_8), decoded);
    }

    @Test
    public void testDecodeInvalidString() throws Exception {
        String invalid = "=?def?=abc";
        try {
            CODEC.decode(invalid.getBytes(), StandardCharsets.UTF_8);
            Assert.fail();
        } catch (DecoderException e) {
            // expected
        }
    }

    @Test
    public void testEncodeInvalidByte() throws Exception {
        byte[] input = {0x61, 0x62, 0x63, (byte) 0x80, 0x0D, 0x0A, 0x64, 0x65, 0x66};
        byte[] output = CODEC.encode(input);

        Assert.assertArrayEquals(input, output);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNull() throws Exception {
        CODEC.encode(null);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeNull() throws Exception {
        CODEC.decode(null, StandardCharsets.UTF_8);
    }
}