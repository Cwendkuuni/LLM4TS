package org.apache.commons.codec.net;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeBytes() throws IOException, DecoderException {
        byte[] original = {0x61, 0x62, 0x63};
        byte[] encoded = codec.encode(original);
        byte[] decoded = codec.decode(encoded);

        Assert.assertArrayEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws UnsupportedEncodingException, DecoderException {
        String original = "abc";
        String encoded = codec.encode(original, StandardCharsets.UTF_8);
        String decoded = codec.decode(encoded, StandardCharsets.UTF_8);

        Assert.assertEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeObject() throws UnsupportedEncodingException, DecoderException {
        String original = "abc";
        Object encoded = codec.encode(original);
        Object decoded = codec.decode(encoded);

        Assert.assertEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeEmptyString() throws UnsupportedEncodingException, DecoderException {
        String original = "";
        String encoded = codec.encode(original, StandardCharsets.UTF_8);
        String decoded = codec.decode(encoded, StandardCharsets.UTF_8);

        Assert.assertEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeWhitespace() throws UnsupportedEncodingException, DecoderException {
        String original = "    ";
        String encoded = codec.encode(original, StandardCharsets.UTF_8);
        String decoded = codec.decode(encoded, StandardCharsets.UTF_8);

        Assert.assertEquals(original, decoded);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeNullBytes() {
        codec.encode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeNullString() {
        codec.encode(null, StandardCharsets.UTF_8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeNullObject() {
        codec.encode(null);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidHex() throws UnsupportedEncodingException {
        byte[] encoded = {0x61, 0x62, (byte) 0x78, 0x63}; // invalid hex character 'x'
        byte[] decoded = codec.decode(encoded);

        Assert.fail("DecoderException expected but not thrown");
    }

    @Test
    public void testEncodeDecodeBackslash() throws UnsupportedEncodingException, DecoderException {
        String original = "\\";
        String encoded = codec.encode(original, StandardCharsets.UTF_8);
        String decoded = codec.decode(encoded, StandardCharsets.UTF_8);

        Assert.assertEquals("\\", decoded);
    }

    @Test
    public void testEncodeDecodeCR() throws UnsupportedEncodingException, DecoderException {
        String original = "\r";
        String encoded = codec.encode(original, StandardCharsets.UTF_8);
        String decoded = codec.decode(encoded, StandardCharsets.UTF_8);

        Assert.assertEquals("\r", decoded);
    }

    @Test
    public void testEncodeDecodeLF() throws UnsupportedEncodingException, DecoderException {
        String original = "\n";
        String encoded = codec.encode(original, StandardCharsets.UTF_8);
        String decoded = codec.decode(encoded, StandardCharsets.UTF_8);

        Assert.assertEquals("\n", decoded);
    }

    @Test
    public void testEncodeDecodeCRLF() throws UnsupportedEncodingException, DecoderException {
        String original = "\r\n";
        String encoded = codec.encode(original, StandardCharsets.UTF_8);
        String decoded = codec.decode(encoded, StandardCharsets.UTF_8);

        Assert.assertEquals("\r\n", decoded);
    }
}