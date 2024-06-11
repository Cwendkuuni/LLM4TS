import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    private QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecode() throws UnsupportedEncodingException, DecoderException {
        String input = "This is a test for QuotedPrintableCodec test.";
        byte[] bytes = codec.encode(input.getBytes("UTF-8"));
        String result = codec.decode(bytes, "UTF-8");
        Assert.assertEquals(input, result);
    }

    @Test
    public void testEncodeBinaryData() throws UnsupportedEncodingException, DecoderException {
        byte[] input = {0x01, 0x02, 0x03, 0x04};
        byte[] bytes = codec.encode(input);
        byte[] decodedBytes = codec.decode(bytes);
        Assert.assertArrayEquals(input, decodedBytes);
    }

    @Test
    public void testEncodePrintableData() throws UnsupportedEncodingException, DecoderException {
        String printableInput = "This is a test for QuotedPrintableCodec test.";
        byte[] bytes = codec.encode(printableInput.getBytes("UTF-8"));
        String result = new String(codec.decode(bytes), "UTF-8");
        Assert.assertEquals(printableInput, result);
    }

    @Test
    public void testEncodeNonPrintableData() throws UnsupportedEncodingException, DecoderException {
        String nonPrintableInput = "\u0001\u0002\u0003\u0004";
        byte[] bytes = codec.encode(nonPrintableInput.getBytes("UTF-8"));
        String result = new String(codec.decode(bytes), "UTF-8");
        Assert.assertEquals(nonPrintableInput, result);
    }

    @Test
    public void testEncodeEscapeCharacter() throws UnsupportedEncodingException, DecoderException {
        String input = "This is a test for QuotedPrintableCodec test=";
        byte[] bytes = codec.encode(input.getBytes("UTF-8"));
        String result = new String(codec.decode(bytes), "UTF-8");
        Assert.assertEquals(input, result);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidData() throws UnsupportedEncodingException {
        byte[] bytes = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        codec.decode(bytes);
    }
}