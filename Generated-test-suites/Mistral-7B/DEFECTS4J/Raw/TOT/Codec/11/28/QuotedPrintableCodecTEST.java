package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    private static final byte[] ESCAPED_HEX = new byte[]{'=', '5', 'C', '3'};
    private static final byte[] ESCAPED_CR = new byte[]{'=', 'D', 'C', '2'};
    private static final byte[] ESCAPED_LF = new byte[]{'=', 'D', 'A', 'C'};

    @Test
    public void testEncodeDecodeString() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "test@example.com\r\nHello, world!";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded).getString(CharEncoding.US_ASCII);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeByteArray() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "test@example.com\r\nHello, world!";
        byte[] inputBytes = StringUtils.getBytesUsAscii(input);
        byte[] encodedBytes = codec.encode(inputBytes);
        byte[] decodedBytes = codec.decode(encodedBytes);
        String decoded = new String(decodedBytes, CharEncoding.US_ASCII);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeUnicode() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "\u00E9test@example.com\r\nHello, world!";
        byte[] inputBytes = StringUtils.getBytesUsAscii(input);
        byte[] encodedBytes = codec.encode(inputBytes);
        String decoded = codec.decode(encodedBytes).getString(CharEncoding.US_ASCII);
        Assert.assertEquals(input, decoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalid() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = new byte[]{'T', 'e', 's', 't'};
        byte[] decoded = codec.decode(input);
    }

    @Test
    public void testEncodeCR() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "test@example.com\rHello, world!";
        byte[] encodedBytes = codec.encode(input.getBytes(CharEncoding.US_ASCII));
        byte[] decodedBytes = codec.decode(encodedBytes);
        String decoded = new String(decodedBytes, CharEncoding.US_ASCII);
        Assert.assertEquals(input, decoded);
        Assert.assertArrayEquals(ESCAPED_CR, new byte[]{encodedBytes[input.length()]});
    }

    @Test
    public void testEncodeLF() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "test@example.com\nHello, world!";
        byte[] encodedBytes = codec.encode(input.getBytes(CharEncoding.US_ASCII));
        byte[] decodedBytes = codec.decode(encodedBytes);
        String decoded = new String(decodedBytes, CharEncoding.US_ASCII);
        Assert.assertEquals(input, decoded);
        Assert.assertArrayEquals(ESCAPED_LF, new byte[]{encodedBytes[input.length()]});
    }

    @Test
    public void testEncodeWhitespace() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "test@example.com    \r\nHello, world!";
        byte[] encodedBytes = codec.encode(input.getBytes(CharEncoding.US_ASCII));
        byte[] decodedBytes = codec.decode(encodedBytes);
        String decoded = new String(decodedBytes, CharEncoding.US_ASCII);
        Assert.assertEquals(input, decoded);
        Assert.assertArrayEquals(ESCAPED_CR, new byte[]{encodedBytes[input.length() + 2]});
        Assert.assertArrayEquals(ESCAPED_LF, new byte[]{encodedBytes[input.length() + 3]});
        Assert.assertArrayEquals(ESCAPED_SPACE, new byte[]{encodedBytes[input.length() + 4], encodedBytes[input.length() + 5], encodedBytes[input.length() + 6]});
    }

    @Test
    public void testEncodePrintable() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "test@example.comPrintableText!";
        byte[] encodedBytes = codec.encode(input.getBytes(CharEncoding.US_ASCII));
        byte[] decodedBytes = codec.decode(encodedBytes);
        String decoded = new String(decodedBytes, CharEncoding.US_ASCII);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeHex() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "test@example.com\r\n0xDEADBEEFHello, world!";
        byte[] encodedBytes = codec.encode(input.getBytes(CharEncoding.US_ASCII));
        byte[] decodedBytes = codec.decode(encodedBytes);
        String decoded = new String(decodedBytes, CharEncoding.US_ASCII);
        Assert.assertEquals(input, decoded);
        Assert.assertArrayEquals(new byte[]{'=', '3', 'D', 'E', 'A', 'D', 'B', 'E', 'E', 'F'}, new byte[]{encodedBytes[input.length()], encodedBytes[input.length() + 1], encodedBytes[input.length() + 2], encodedBytes[input.length() + 3], encodedBytes[input.length() + 4], encodedBytes[input.length() + 5], encodedBytes[input.length() + 6], encodedBytes[input.length() + 7], encodedBytes[input.length() + 8], encodedBytes[input.length() + 9]});
    }

    @Test
    public void testEncodeError() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "test@example.com\r\nHello, world!\u0000!";
        byte[] encodedBytes = codec.encode(input.getBytes(CharEncoding.US_ASCII));
        Assert.assertArrayIsNotNull(encodedBytes);
        Assert.assertArrayIsNotEmpty(encodedBytes);
        BitSet printable = new BitSet(256);
        printable.clear(0);
        printable.set(ESCAPE_CHAR);
        printable.set(ESCAPED_HEX[0]);
        printable.set(ESCAPED_CR[0]);
        printable.set(ESCAPED_LF[0]);
        printable.set(0x00);
        for (byte b : encodedBytes) {
            Assert.assertTrue(printable.get(b));
        }
    }
}