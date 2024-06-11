import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/**
 * Test class for QuotedPrintableCodec
 */
public class QuotedPrintableCodecTest {

    /**
     * Test encoding and decoding of bytes with printable characters
     */
    @Test
    public void testEncodeDecodePrintableBytes() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] printableBytes = "Hello World!".getBytes();
        byte[] encoded = codec.encode(printableBytes);
        byte[] decoded = codec.decode(encoded);

        Assert.assertArrayEquals(printableBytes, decoded);
    }

    /**
     * Test encoding and decoding of bytes with non-printable characters
     */
    @Test
    public void testEncodeDecodeNonPrintableBytes() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] nonPrintableBytes = "\u000D\u000AHello World!".getBytes();
        byte[] encoded = codec.encode(nonPrintableBytes);
        byte[] decoded = codec.decode(encoded);

        Assert.assertArrayEquals(nonPrintableBytes, decoded);
    }

    /**
     * Test encoding of string with printable characters
     */
    @Test
    public void testEncodePrintableString() throws UnsupportedEncodingException, EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String printableString = "Hello World!";
        byte[] encoded = codec.encode(printableString.getBytes());

        String decoded = new String(codec.decode(encoded), codec.getDefaultCharset());

        Assert.assertEquals(printableString, decoded);
    }

    /**
     * Test encoding of string with non-printable characters
     */
    @Test
    public void testEncodeNonPrintableString() throws UnsupportedEncodingException, EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String nonPrintableString = "\u000D\u000AHello World!";
        byte[] encoded = codec.encode(nonPrintableString.getBytes());

        String decoded = new String(codec.decode(encoded), codec.getDefaultCharset());

        Assert.assertEquals(nonPrintableString, decoded);
    }

    /**
     * Test decoding of quoted-printable encoded string with printable characters
     */
    @Test
    public void testDecodePrintableQuotedString() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encodedQuotedString = "=\r=\nHello World!";
        byte[] decodedBytes = codec.decode(encodedQuotedString.getBytes());

        Assert.assertEquals("Hello World!", new String(decodedBytes));
    }

    /**
     * Test decoding of quoted-printable encoded string with non-printable characters
     */
    @Test
    public void testDecodeNonPrintableQuotedString() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encodedQuotedString = "=\r=\n\u000D\u000AHello World!";
        byte[] decodedBytes = codec.decode(encodedQuotedString.getBytes());

        Assert.assertEquals("\u000D\u000AHello World!", new String(decodedBytes));
    }

    /**
     * Test decoding of quoted-printable encoded string with mixed printable and non-printable characters
     */
    @Test
    public void testDecodeMixedQuotedString() throws UnsupportedEncodingException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encodedQuotedString = "=\r=\nHello\u000D World!";
        byte[] decodedBytes = codec.decode(encodedQuotedString.getBytes());

        Assert.assertEquals("Hello\r World!", new String(decodedBytes));
    }
}