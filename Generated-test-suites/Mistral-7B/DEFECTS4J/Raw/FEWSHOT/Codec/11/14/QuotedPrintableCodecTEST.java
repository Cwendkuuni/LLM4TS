package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

/**
 * Test cases for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "Hello, World!".getBytes();
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);

        assertEquals(input.length, decoded.length);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "Hello, World!";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);

        assertEquals(input, decoded);
    }

    @Test
    public void testDecodeInvalidHexDigit() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = new byte[]{'A', 'B', '=', 'C'};
        try {
            codec.decode(input);
            fail();
        } catch (DecoderException e) {
            assertEquals("Invalid hex digit: =", e.getMessage());
        }
    }

    @Test
    public void testEncodeDecodeBinaryData() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = new byte[]{1, 2, 3, 4, 5};
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);

        assertEquals(input.length, decoded.length);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeBinaryDataWithLineBreaks() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = new byte[]{1, 2, 3, 4, 5, 13, 10, 1, 2, 3, 4, 5};
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);

        assertEquals(input.length, decoded.length);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithLineBreaks() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "line1\nline2";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);

        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithBinaryData() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "line1\nline2\n<binary-data>\n--\n";
        byte[] binaryData = Hex.decodeHex("4142434445");
        String encoded = codec.encode(input + new String(binaryData) + "\n--");
        String decoded = codec.decode(encoded);

        assertEquals(input, decoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNullInput() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode(null);
        fail();
    }

    @Test(expected = DecoderException.class)
    public void testDecodeNullInput() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(null);
        fail();
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(new byte[]{0x01});
        fail();
    }

    @Test
    public void testEncodeDecodeSpecialCharacters() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = new byte[]{'-', '--', '\t', '\r', '\n'};
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);

        assertEquals(input.length, decoded.length);
        assertArrayEquals(input, decoded);
    }
}