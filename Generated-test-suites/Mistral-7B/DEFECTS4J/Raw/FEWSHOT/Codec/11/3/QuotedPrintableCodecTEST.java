package org.apache.commons.codec.net;

import static org.junit.Assert.*;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeByte() throws Exception {
        for (int i = 0; i < 256; i++) {
            byte input = (byte) i;
            byte[] encoded = codec.encode(input);
            byte decoded = codec.decode(encoded)[0];
            assertEquals(input, decoded);
        }
    }

    @Test
    public void testEncodeDecodeByteArray() throws Exception {
        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05};
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        String input = "abcdef";
        String encoded = codec.encode(input.getBytes());
        String decoded = new String(codec.decode(Hex.decodeHex(encoded.toCharArray()))[0]);
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithCharset() throws Exception {
        String input = "abcdef";
        String encoded = codec.encode(input.getBytes("UTF-8"));
        String decoded = new String(codec.decode(encoded.getBytes("UTF-8")), "UTF-8");
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithDefaultCharset() throws Exception {
        String input = "abcdef";
        String encoded = codec.encode(input);
        String decoded = new String(codec.decode(encoded), codec.getDefaultCharset());
        assertEquals(input, decoded);
    }
}