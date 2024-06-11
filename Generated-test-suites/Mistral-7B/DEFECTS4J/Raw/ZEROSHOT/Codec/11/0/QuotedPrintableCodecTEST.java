import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeByte() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);

        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abcdefghijklmnopqrstuvwxyz";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);

        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeByteArrayString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
        String encoded = codec.encode(input);
        String decoded = new String(codec.decode(encoded.getBytes()), "UTF-8");

        assertArrayEquals(input, StringUtils.getBytesUsAscii(decoded));
    }

    @Test
    public void testEncodeDecodeStringArrayByte() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String[] input = {"abc", "def", "ghi"};
        byte[] encoded = codec.encode(input);
        String[] decoded = new String[input.length];

        for (int i = 0; i < input.length; i++) {
            decoded[i] = new String(codec.decode(encoded, 0, encoded.length), "UTF-8");
        }

        assertArrayEquals(input, decoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidInput() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, 0x02, 0xFF};
        codec.encode(input);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, 0x02, 0x03, 0xFF};
        codec.decode(input);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeInvalidCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encoded = "quoted-printable-encoded-string";
        codec.decode(encoded.getBytes("iso-8859-1"));
    }
}