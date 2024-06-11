package org.apache.commons.codec.net;

import static org.junit.Assert.*;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

/**
 * Test cases for QuotedPrintableCodec class.
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec QP_CODEC = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeByteArray() throws Exception {
        byte[] input = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63, (byte) 0x64};
        byte[] encoded = QP_CODEC.encode(input);
        byte[] decoded = QP_CODEC.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        String input = "abcd";
        String encoded = QP_CODEC.encode(input.getBytes("UTF-8"));
        String decoded = new String(QP_CODEC.decode(Hex.decodeHex(encoded.substring(1, encoded.length() - 1).toCharArray())), "UTF-8");
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeObject() throws Exception {
        String input = "abcd";
        Object encodedObj = QP_CODEC.encode(input);
        Object decodedObj = QP_CODEC.decode(encodedObj);
        assertEquals(input, decodedObj);
    }

    @Test
    public void testEncodeByteArrayWithCharset() throws Exception {
        byte[] input = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63, (byte) 0x64};
        byte[] encoded = QP_CODEC.encode(input, "UTF-8");
        byte[] decoded = QP_CODEC.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testDecodeStringWithCharset() throws Exception {
        String encoded = "%61%62%63%64";
        String decoded = QP_CODEC.decode(encoded, "UTF-8");
        assertEquals("abcd", decoded);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeWithNullString() {
        QP_CODEC.decode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeWithNullBytes() {
        QP_CODEC.decode(null);
    }
}