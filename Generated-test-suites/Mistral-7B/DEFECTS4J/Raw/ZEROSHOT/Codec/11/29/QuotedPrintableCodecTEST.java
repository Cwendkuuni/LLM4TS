import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeBasic() throws EncoderException, DecoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "Hello World!".getBytes();
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, codec.getDefaultCharset());
        Assert.assertEquals("=48=65=6C=6C=6F=21=0D", encodedString);

        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeSpecialChars() throws EncoderException, DecoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "\u0001\u0002\u000F\u000E\u000D\u000A".getBytes();
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, codec.getDefaultCharset());
        Assert.assertEquals("=01=02=0C=0E=0D=0A=0D", encodedString);

        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeWhitespace() throws EncoderException, DecoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "Hello\tWorld!".getBytes();
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, codec.getDefaultCharset());
        Assert.assertEquals("=48=65=6C=20=57=6F=72=6C=21=0D", encodedString);

        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeCRLF() throws EncoderException, DecoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "Hello\r\nWorld!".getBytes();
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, codec.getDefaultCharset());
        Assert.assertEquals("=48=65=6C=0D=0A=57=6F=72=6C=21=0D", encodedString);

        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals(input, decoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalid() throws DecoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "=01=02=03=04=05=06=07=08=09=0A".getBytes();
        codec.decode(input);
    }

    @Test
    public void testEncodeDecodeTabs() throws EncoderException, DecoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "\tHello World!".getBytes();
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, codec.getDefaultCharset());
        Assert.assertEquals("=09=48=65=6C=6C=6F=21=0D", encodedString);

        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeSpaces() throws EncoderException, DecoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = " Hello World!".getBytes();
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, codec.getDefaultCharset());
        Assert.assertEquals("=20=48=65=6C=6C=6F=20=21=0D", encodedString);

        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeEscaped() throws EncoderException, DecoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "Hello=\rWorld!".getBytes();
        byte[] encoded = codec.encode(input);
        String encodedString = new String(encoded, codec.getDefaultCharset());
        Assert.assertEquals("=48=65=6C=3D=0D=57=6F=72=6C=21=0D", encodedString);

        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeBitset() throws EncoderException, DecoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        BitSet printable = new BitSet();
        printable.set(0, 127);
        printable.set(11, 12);
        printable.set(13, 10);

        byte[] input = "Hello\nWorld!".getBytes();
        byte[] encoded = codec.encode(input, printable);
        String encodedString = new String(encoded, codec.getDefaultCharset());
        Assert.assertEquals("=48=65=6C=0D=11=12=57=6F=72=6C=21=0D", encodedString);

        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals(input, decoded);
    }
}