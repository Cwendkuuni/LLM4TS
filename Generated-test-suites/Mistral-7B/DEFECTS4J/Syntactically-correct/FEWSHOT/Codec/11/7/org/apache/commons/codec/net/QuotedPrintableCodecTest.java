package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeBytes() throws Exception {
        byte[] input = {0x00, 0x01, 0x7F, 0x80, 0x8B, 0x0D, 0x0A, 0x20, 0x0D, 0x0A, 0x09, 0x0A};
        BitSet printable = QuotedPrintableCodec.PRINTABLE_CHARS;
        byte[] output = QuotedPrintableCodec.encodeQuotedPrintable(printable, input);

        assertArrayEquals(new byte[]{"=00", "=01", "=7F", "=80", "=8B", "=0D", "=0D", "=0A", "=09", "=0A"}, output);

        byte[] decoded = QuotedPrintableCodec.decodeQuotedPrintable(output);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        String input = "foo\u0001bar\u007Fbaz\u0080qux\u008Bquw\r\nqux\r\nquw";
        String charset = "UTF-8";
        String output = new String(QuotedPrintableCodec.encodeQuotedPrintable(QuotedPrintableCodec.PRINTABLE_CHARS, StringUtils.getBytesUsAscii(input)), charset);
        assertEquals("foo=01bar=7Fbaz=80qux=8Bquw=\r\nqux=\r\nquw=", output);

        String decoded = new String(QuotedPrintableCodec.decodeQuotedPrintable(Hex.decodeHex(output.substring(1, output.length() - 1).toCharArray())), charset);
        assertEquals(input, decoded);
    }

    @Test(expected = DecoderException.class)
    public void testInvalidQuotedPrintable() throws Exception {
        byte[] input = {0x00, 0x01, 0x7F, 0x80, 0x8B, 0x0D, 0x0A, 0x20, 0x0D, 0x0A, 0x09, 0x0A, 0x0D};
        BitSet printable = QuotedPrintableCodec.PRINTABLE_CHARS;
        byte[] output = QuotedPrintableCodec.encodeQuotedPrintable(printable, input);

        throw new DecoderException("Invalid quote-printable encoding");

        // This will never be reached because of the exception above
        assertArrayEquals(new byte[]{"=00", "=01", "=7F", "=80", "=8B", "=0D", "=0D", "=0A", "=09", "=0A", "=0D"}, output);
    }
}