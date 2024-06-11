package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

/**
 * Test cases for QuotedPrintableCodec class.
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec QUOTED_PRINTABLE_CODEC = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeBytes() throws Exception {
        byte[] bytes = StringUtils.getBytesUsAscii("Hello World");
        byte[] encoded = QUOTED_PRINTABLE_CODEC.encode(bytes);
        byte[] decoded = QUOTED_PRINTABLE_CODEC.decode(encoded);

        assertEquals(bytes.length, decoded.length);
        assertArrayEquals(bytes, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        String origString = "Hello World";
        String encodedString = QUOTED_PRINTABLE_CODEC.encode(origString).toString();
        String decodedString = QUOTED_PRINTABLE_CODEC.decode(encodedString).toString();

        assertEquals(origString, decodedString);
    }

    @Test
    public void testEncodeDecodeObject() throws Exception {
        String origString = "Hello World";
        Object encodedObject = QUOTED_PRINTABLE_CODEC.encode(origString);
        Object decodedObject = QUOTED_PRINTABLE_CODEC.decode(encodedObject);

        assertEquals(origString, decodedObject);
    }

    @Test
    public void testEncodeBinaryData() throws Exception {
        byte[] binaryData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        byte[] encoded = QUOTED_PRINTABLE_CODEC.encode(binaryData);

        String expectedEncoded = new String(encoded)
                .replaceAll("(=([0-9A-Fa-f]{2}))", m -> "\\$1");
        String hexEncoded = Hex.encodeHexString(binaryData);
        assertEquals(expectedEncoded, hexEncoded);
    }

    @Test
    public void testDecodeInvalidString() throws Exception {
        assertNull(QUOTED_PRINTABLE_CODEC.decode(""));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidByteArray() throws Exception {
        QUOTED_PRINTABLE_CODEC.decode(new byte[0]);
    }
}