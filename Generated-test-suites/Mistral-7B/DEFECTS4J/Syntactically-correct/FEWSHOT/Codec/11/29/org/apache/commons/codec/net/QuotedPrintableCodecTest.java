package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeQuotedPrintable() {
        byte[] input = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0A};
        BitSet printable = QuotedPrintableCodec.PRINTABLE_CHARS;
        byte[] output = QuotedPrintableCodec.encodeQuotedPrintable(printable, input);

        byte[] expected = new byte[]{'=', '0', '1', '=', '30', '=', '2', '=', '31', '=', '32', '=', '33', '=', '34', '=', '35', '=', '36', '=', '37', '=', '38', '=', '39', '=', '3A'};
        assertArrayEquals(expected, output);
    }

    @Test
    public void testDecodeQuotedPrintable() {
        byte[] input = new byte[]{'=', '0', '1', '=', '30', '=', '2', '=', '31', '=', '32', '=', '33', '=', '34', '=', '35', '=', '36', '=', '37', '=', '38', '=', '39', '=', '3A'};
        byte[] output = QuotedPrintableCodec.decodeQuotedPrintable(input);

        byte[] expected = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0A};
        assertArrayEquals(expected, output);
    }

    @Test
    public void testEncodeQuotedPrintableString() throws UnsupportedEncodingException {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String charset = "UTF-8";
        String output = QuotedPrintableCodec.encode(input, charset).toString();

        String expected = Hex.encodeHexString(QuotedPrintableCodec.encodeQuotedPrintable(QuotedPrintableCodec.PRINTABLE_CHARS, StringUtils.getBytesUsAscii(input)));
        assertEquals(expected, output);
    }

    @Test
    public void testDecodeQuotedPrintableString() throws DecoderException, UnsupportedEncodingException {
        String input = "=61=62=63=64=65=66=67=68=69=6A=6B=6C=6D=6E=6F=70=71=72=73=74=75=76=77=78=79=7A=";
        String charset = "UTF-8";
        String output = QuotedPrintableCodec.decode(input, charset).toString();

        assertEquals("abcdefghijklmnopqrstuvwxyz", output);
    }

    @Test
    public void testEncodeQuotedPrintableObject() throws UnsupportedEncodingException, EncoderException {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Object output = QuotedPrintableCodec.encode(input);

        if (output instanceof String) {
            String stringOutput = (String) output;
            String charset = QuotedPrintableCodec.getDefaultCharset();
            String expected = Hex.encodeHexString(QuotedPrintableCodec.encodeQuotedPrintable(QuotedPrintableCodec.PRINTABLE_CHARS, StringUtils.getBytesUsAscii(input)));
            assertEquals(expected, stringOutput);
        } else if (output instanceof byte[]) {
            byte[] byteOutput = (byte[]) output;
            byte[] expected = QuotedPrintableCodec.encodeQuotedPrintable(QuotedPrintableCodec.PRINTABLE_CHARS, StringUtils.getBytesUsAscii(input));
            assertArrayEquals(expected, byteOutput);
        } else {
            fail("Unexpected output type: " + output.getClass());
        }
    }

    @Test
    public void testDecodeQuotedPrintableObject() throws DecoderException, UnsupportedEncodingException {
        String input = "=61=62=63=64=65=66=67=68=69=6A=6B=6C=6D=6E=6F=70=71=72=73=74=75=76=77=78=79=7A=";
        Object output = QuotedPrintableCodec.decode(input);

        if (output instanceof String) {
            String stringOutput = (String) output;
            assertEquals("abcdefghijklmnopqrstuvwxyz", stringOutput);
        } else if (output instanceof byte[]) {
            byte[] byteOutput = (byte[]) output;
            byte[] expected = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63, (byte) 0x64, (byte) 0x65, (byte) 0x66, (byte) 0x67, (byte) 0x68, (byte) 0x69, (byte) 0x6A, (byte) 0x6B, (byte) 0x6C, (byte) 0x6D, (byte) 0x6E, (byte) 0x6F, (byte) 0x70, (byte) 0x71, (byte) 0x72, (byte) 0x73, (byte) 0x74, (byte) 0x75, (byte) 0x76, (byte) 0x77, (byte) 0x78, (byte) 0x79, (byte) 0x7A};
            assertArrayEquals(expected, byteOutput);
        } else {
            fail("Unexpected output type: " + output.getClass());
        }
    }

    @Test
    public void testEncodeQuotedPrintableNull() {
        assertNull(QuotedPrintableCodec.encode(null));
    }

    @Test
    public void testDecodeQuotedPrintableNull() {
        assertNull(QuotedPrintableCodec.decode(null));
    }
}