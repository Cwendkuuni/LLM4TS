package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeAsciiString() throws UnsupportedEncodingException, DecoderException {
        String testString = "This is a test string.";
        byte[] encodedBytes = codec.encode(testString.getBytes("UTF-8"));
        String decodedString = new String(codec.decode(encodedBytes), "UTF-8");
        Assert.assertEquals(testString, decodedString);
    }

    @Test
    public void testEncodeDecodeBinaryData() throws DecoderException, UnsupportedEncodingException {
        byte[] inputBytes = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] encodedBytes = codec.encode(inputBytes);
        byte[] decodedBytes = codec.decode(encodedBytes);
        Assert.assertArrayEquals(inputBytes, decodedBytes);
    }

    @Test
    public void testEncodeQuotedPrintable() throws UnsupportedEncodingException, DecoderException {
        String testString = "This\ris\ta\ttest\rstring.";
        byte[] encodedBytes = codec.encode(testString.getBytes("UTF-8"));
        String decodedString = new String(codec.decode(encodedBytes), "UTF-8");
        String expectedQuotedPrintable = "This%0D%0Ais%0A%2Atest%0D%0A%2Atstring%0D%0A.";
        Assert.assertEquals(expectedQuotedPrintable, StringUtils.newStringUsAscii(encodedBytes));
        Assert.assertEquals(testString, decodedString);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidQuotedPrintable() throws UnsupportedEncodingException, DecoderException {
        byte[] invalidBytes = new byte[]{'=', '=', '='};
        codec.decode(invalidBytes);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(null);
        codec.encode("test".getBytes());
    }
}