package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * QuotedPrintableCodecTest.
 */
public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        String encoded = codec.encodeToString(bytes, codec.getDefaultCharset());
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
        assertEquals(bytes, decoded);
    }

    @Test
    public void testEncodeDecodeCR() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = {'C', 'R', '\r'};
        String encoded = codec.encodeToString(bytes, codec.getDefaultCharset());
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
        assertEquals(bytes, decoded);
    }

    @Test
    public void testEncodeDecodeLF() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = {'C', 'R', '\n'};
        String encoded = codec.encodeToString(bytes, codec.getDefaultCharset());
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
        assertEquals(bytes, decoded);
    }

    @Test
    public void testEncodeDecodeCRLF() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = {'C', 'R', '\r', '\n'};
        String encoded = codec.encodeToString(bytes, codec.getDefaultCharset());
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
        assertEquals(bytes, decoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidHex() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encoded = "%ff%01";
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidFirstChar() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encoded = "=%01";
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeMissingHex() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encoded = "=%";
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeMissingSecondHex() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encoded = "%ff";
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidLastChar() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encoded = "%01=";
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeTrailingCR() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encoded = "foo%0D";
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeTrailingLF() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encoded = "foo%0A";
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeTrailingCRLF() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encoded = "foo%0D%0A";
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
    }

    @Test
    public void testEncodeSpace() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = {' '};
        String encoded = codec.encodeToString(bytes, codec.getDefaultCharset());
        byte[] decoded = Hex.decodeHex(encoded.substring(1, encoded.length()).toCharArray());
        assertEquals(new byte[]{0x20}, decoded);
    }

    @Test
    public void testEncodeTab() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = {'\t'};
        String encoded = codec.encodeToString(bytes, codec.getDefaultCharset());
        byte[] decoded = Hex.decodeHex(encoded.substring(1, encoded.length()).toCharArray());
        assertEquals(new byte[]{0x09}, decoded);
    }

    @Test
    public void testEncodePrintable() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '&', '*', '(', ')', '_', '+', '=', '{', '}', '[', ']', '|', '\\', ';', ':', '<', '>', '?', '/'};
        String encoded = codec.encodeToString(bytes, codec.getDefaultCharset());
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
        assertEquals(bytes, decoded);
    }

    @Test
    public void testEncodeControlChars() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = {'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007', '\u0008', '\u000B', '\u000C', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012', '\u0013', '\u0014', '\u0015', '\u0016', '\u0017'};
        String encoded = codec.encodeToString(bytes, codec.getDefaultCharset());
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
        assertEquals(bytes, decoded);
    }

    @Test
    public void testEncodeInvalidChar() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = {'~'};
        String encoded = codec.encodeToString(bytes, codec.getDefaultCharset());
        byte[] decoded = Hex.decodeHex(encoded.substring(1, encoded.length()).toCharArray());
        assertEquals(new byte[]{0x7E}, decoded);
    }

    @Test
    public void testEncodeMaxLength() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = new byte[Integer.MAX_VALUE];
        String encoded = codec.encodeToString(bytes, codec.getDefaultCharset());
        byte[] decoded = codec.decode(encoded.getBytes(codec.getDefaultCharset()));
        assertEquals(bytes, decoded);
    }

    @Test
    public void testEncodeCharset() throws Exception {
        QuotedPrintableCodec codec1 = new QuotedPrintableCodec();
        String encoded1 = codec1.encodeToString("ABC\u00E1DEF".getBytes("UTF-8"));
        String decoded1 = new String(codec1.decode(encoded1.getBytes()), "UTF-8");
        assertEquals("ABC\u00E1DEF", decoded1);

        QuotedPrintableCodec codec2 = new QuotedPrintableCodec("ISO-8859-1");
        String encoded2 = codec2.encodeToString("ABC\u00E1DEF".getBytes("UTF-8"));
        String decoded2 = new String(codec2.decode(encoded2.getBytes()), "ISO-8859-1");
        assertEquals("ABC??DE?F", decoded2);
    }
}