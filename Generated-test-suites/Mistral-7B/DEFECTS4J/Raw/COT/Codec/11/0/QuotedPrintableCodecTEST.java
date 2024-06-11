import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for QuotedPrintableCodec.
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeString() throws Exception {
        String original = "Hello, World!";
        String encoded = CODEC.encode(original).toString();
        String decoded = CODEC.decode(encoded).toString();
        assertEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeByteArray() throws Exception {
        String original = "Hello, World!";
        byte[] bytes = CODEC.encode(original.getBytes(CharEncoding.UTF_8));
        byte[] decodedBytes = CODEC.decode(bytes);
        String decoded = new String(decodedBytes, CharEncoding.UTF_8);
        assertEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeObject() throws Exception {
        String original = "Hello, World!";
        Object encoded = CODEC.encode(original);
        Object decoded = CODEC.decode(encoded);
        assertEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeNull() throws Exception {
        String encoded = CODEC.encode((String) null);
        String decoded = CODEC.decode(encoded.toString()).toString();
        assertEquals(null, decoded);
    }

    @Test
    public void testEncodeDecodeBinaryDecoder() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BinaryEncoder encoder = CODEC;
        byte[] data = "Hello, World!".getBytes();
        encoder.encode(data, output);
        byte[] encodedData = output.toByteArray();

        BinaryDecoder decoder = CODEC;
        byte[] decodedData = decoder.decode(encodedData);
        assertEquals(data.length, decodedData.length);
        assertArrayEquals(data, decodedData);
    }

    @Test
    public void testEncodeDecodeBinaryEncoder() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BinaryDecoder decoder = CODEC;
        byte[] data = "Hello, World!".getBytes();
        decoder.decode(data, output);
        byte[] encodedData = output.toByteArray();

        BinaryEncoder encoder = CODEC;
        byte[] decodedData = encoder.encode(decodedData);
        assertEquals(data.length, decodedData.length);
        assertArrayEquals(data, decodedData);
    }

    @Test
    public void testEncodeDecodeStringDecoder() throws Exception {
        String original = "Hello, World!";
        StringDecoder decoder = new StringDecoder(CharEncoding.UTF_8);
        String encoded = CODEC.encode(decoder.decode(original)).toString();
        String decoded = CODEC.decode(encoded).toString();
        assertEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeStringEncoder() throws Exception {
        String original = "Hello, World!";
        StringEncoder encoder = new StringEncoder(CharEncoding.UTF_8, 72);
        String encoded = CODEC.encode(encoder.encode(original)).toString();
        String decoded = CODEC.decode(encoded).toString();
        assertEquals(original, decoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() throws Exception {
        Object obj = new Object();
        CODEC.encode(obj);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeException() throws Exception {
        byte[] bytes = new byte[0];
        CODEC.decode(bytes);
    }

    @Test
    public void testEncodeMaxLength() throws Exception {
        String str = new String(new char[65536]).replace("\0", "X");
        byte[] encoded = CODEC.encode(str.getBytes());
        assertEquals(encoded.length, 65536 + 2 * 3 * (65536 / 72) + 2);
    }

    @Test
    public void testDecodeMaxLength() throws Exception {
        byte[] encoded = new byte[65536 + 2 * 3 * (65536 / 72) + 2];
        byte[] decoded = CODEC.decode(encoded);
        assertEquals(decoded.length, 65536);
    }
}