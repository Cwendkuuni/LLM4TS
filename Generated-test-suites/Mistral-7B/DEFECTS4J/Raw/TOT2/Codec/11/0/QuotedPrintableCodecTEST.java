import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * QuotedPrintableCodecTest class
 */
public class QuotedPrintableCodecTest {

    /**
     * Test encoding of printable characters.
     */
    @Test
    public void testEncodePrintableCharacters() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] expected = new byte[]{'A', 'B', 'C', 'D', 'E'};
        byte[] result = codec.encode(expected);
        assertArrayEquals(expected, result);
    }

    /**
     * Test encoding of non-printable characters.
     */
    @Test
    public void testEncodeNonPrintableCharacters() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] expected = new byte[]{'=3D', '=31', '=31', '=31', '=3D'}; // hex '=' and '1'
        byte[] result = codec.encode((byte) 0x80);
        assertArrayEquals(expected, result);
    }

    /**
     * Test decoding of printable characters.
     */
    @Test
    public void testDecodePrintableCharacters() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encoded = "ABCDE";
        byte[] expected = new byte[]{'A', 'B', 'C', 'D', 'E'};
        byte[] result = codec.decode(encoded.getBytes());
        assertArrayEquals(expected, result);
    }

    /**
     * Test decoding of non-printable characters.
     */
    @Test
    public void testDecodeNonPrintableCharacters() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encoded = "=3D313131=3D";
        byte[] expected = new byte[]{(byte) 0x80};
        byte[] result = codec.decode(encoded.getBytes());
        assertArrayEquals(expected, result);
    }

    /**
     * Test encoding of a null byte array.
     */
    @Test(expected = NullPointerException.class)
    public void testEncodeNullByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode(null);
    }

    /**
     * Test decoding of a null byte array.
     */
    @Test(expected = NullPointerException.class)
    public void testDecodeNullByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(null);
    }

    /**
     * Test encoding of empty byte array.
     */
    @Test
    public void testEncodeEmptyByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] result = codec.encode(new byte[0]);
        assertEquals(0, result.length);
    }

    /**
     * Test decoding of empty byte array.
     */
    @Test
    public void testDecodeEmptyByteArray() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] result = codec.decode(new byte[0]);
        assertEquals(0, result.length);
    }

    /**
     * Test encoding of a byte array containing a null byte.
     */
    @Test
    public void testEncodeByteArrayWithNullByte() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] expected = new byte[]{'=3D', '=30', '=30', '=30', '=3D', (byte) 0xFF}; // hex '=' and '0'
        byte[] result = codec.encode((byte) 0x00);
        assertArrayEquals(expected, result);
    }

    /**
     * Test decoding of a byte array containing a null byte.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeByteArrayWithNullByte() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encoded = "=3D30303D=FF";
        byte[] result = codec.decode(encoded.getBytes());
        assertArrayEquals(new byte[0], result);
    }
}