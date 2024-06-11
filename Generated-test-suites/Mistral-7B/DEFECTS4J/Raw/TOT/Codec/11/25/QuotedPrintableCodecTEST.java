package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecode() throws UnsupportedEncodingException, DecoderException {
        String source = "This is a test. It contains spaces (spaces), tabs (tabs), and line feeds (LF)";
        byte[] bytes = new QuotedPrintableCodec().encode(source.getBytes("UTF-8"));
        String decoded = new String(new QuotedPrintableCodec().decode(bytes), "UTF-8");
        Assert.assertEquals(source, decoded);
    }

    @Test
    public void testEncodeBinaryData() throws UnsupportedEncodingException, DecoderException {
        byte[] source = {(byte) 0x01, (byte) 0x02, (byte) 0x03};
        byte[] encoded = new QuotedPrintableCodec().encode(source);
        byte[] decoded = new QuotedPrintableCodec().decode(encoded);
        Assert.assertArrayEquals(source, decoded);
    }

    @Test
    public void testEncodeAndDecodeEdgeCases() throws UnsupportedEncodingException, DecoderException {
        String source = "\u0000\u0001\u007F\t\r\n\u0080\u00FF";
        byte[] bytes = new QuotedPrintableCodec().encode(source.getBytes("UTF-8"));
        String decoded = new String(new QuotedPrintableCodec().decode(bytes), "UTF-8");
        Assert.assertEquals(source, decoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalid() throws UnsupportedEncodingException, DecoderException {
        new QuotedPrintableCodec().decode(new byte[]{(byte) 0x01});
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalid() throws UnsupportedEncodingException, EncoderException {
        new QuotedPrintableCodec().encode(new byte[]{(byte) 0xFF});
    }

    @Test
    public void testEncodeDecodeCustomCharset() throws UnsupportedEncodingException, DecoderException {
        String source = "äöü";
        byte[] bytes = new QuotedPrintableCodec(CharEncoding.ISO_8859_1).encode(source.getBytes("ISO-8859-1"));
        String decoded = new String(new QuotedPrintableCodec(CharEncoding.ISO_8859_1).decode(bytes), "ISO-8859-1");
        Assert.assertEquals(source, decoded);
    }
}