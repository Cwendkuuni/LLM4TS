import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.BytesUtil;
import org.apache.commons.codec.CharEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * QuotedPrintableCodecTest.java
 */
public class QuotedPrintableCodecTest {

    private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeAndDecodeByteArray() throws Exception {
        byte[] input = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64};
        byte[] expectedOutput = {0x68, 0x31, 0x30, 0x30, 0x33, 0x32, 0x65, 0x61, 0x64, 0x65, 0x64};
        byte[] output = codec.encode(input);
        Assert.assertArrayEquals("encode failed", expectedOutput, output);
        byte[] decoded = codec.decode(output);
        Assert.assertArrayEquals("decode failed", input, decoded);
    }

    @Test
    public void testEncodeAndDecodeASCIIString() throws Exception {
        String input = "hello world";
        String expectedQuotedPrintable = "=68=65=6c=6c=6f=20=77=6f=72=6c=64";
        String encodedOutput = codec.encode(input.getBytes(CharEncoding.US_ASCII)).toString();
        Assert.assertEquals("encode failed", expectedQuotedPrintable, encodedOutput);
        String decodedOutput = codec.decode(BytesUtil.toByteArray(encodedOutput)).toString(CharEncoding.US_ASCII);
        Assert.assertEquals("decode failed", input, decodedOutput);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeIllegalString() throws Exception {
        codec.decode("=68=65=6c=6c=6f=20=77=6f=72=6c=64=65".getBytes());
    }

    @Test(expected = EncoderException.class)
    public void testEncodeIllegalByteArray() throws Exception {
        codec.encode((byte[]) null);
    }
}