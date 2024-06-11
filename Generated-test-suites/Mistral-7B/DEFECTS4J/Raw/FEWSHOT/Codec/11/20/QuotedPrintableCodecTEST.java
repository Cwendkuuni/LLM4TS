package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.BitSet;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeBytes() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        byte[] output = codec.encode(input);
        byte[] decoded = codec.decode(output);

        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abcdéfghijklmñopqrstuvwxyzABCDÉFGHIJKLMÑOPQRSTUVWXYZ0123456789";
        String encoded = codec.encode(input, "UTF-8");
        String decoded = codec.decode(encoded, "UTF-8");

        assertEquals(input, decoded);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeWithUnsupportedCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encoded = "quoted-printable-encoded-string";
        codec.decode(encoded, "XXX");
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidQuotedPrintable() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] encoded = {0x01, 0x02, 0x03, 0x04, '=', '=', '=', 0x05};
        codec.decode(encoded);
    }

    @Test
    public void testEncodeDecodeBitSet() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        BitSet input = new BitSet(256);
        for (int i = 33; i <= 126; i++) {
            input.set(i);
        }

        byte[] output = codec.encode(input.toByteArray());
        BitSet decoded = new BitSet(256);
        decoded.setBytes(codec.decode(output));

        assertEquals(input, decoded);
    }
}