package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeByteArray() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x00, 0x0A, 0x0D, 0x01, 0x0B, 0x0C, 0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18};
        byte[] expectedOutput = Hex.decodeHex("01000D0A25313233343536373839310C0E0F101112131415161718".toCharArray());

        byte[] output = codec.encode(input);
        assertArrayEquals(expectedOutput, output);

        byte[] decoded = codec.decode(output);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "foo\rbar\bquux\n";
        String expectedOutput = "=76=6F=6F=21=62=61=72=61=73=78=65=78=65=0D=62=61=75=67=65=0A=63=6E=66=65=63=63=65=6D=0A";

        String output = codec.encode(input);
        assertEquals(expectedOutput, output);

        String decoded = codec.decode(output.getBytes());
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithEmptyInput() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "";
        String expectedOutput = "";

        String output = codec.encode(input);
        assertEquals(expectedOutput, output);

        String decoded = codec.decode(output.getBytes());
        assertEquals(input, decoded);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testEncodeDecodeStringWithUnsupportedCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "foo";
        String charset = "invalid";

        codec.encode(input.getBytes(charset));
    }
}