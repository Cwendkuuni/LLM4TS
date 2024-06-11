package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "Hello, World!".getBytes();
        byte[] expectedOutput = QuotedPrintableCodec.encodeQuotedPrintable(QuotedPrintableCodec.PRINTABLE_CHARS, input);

        byte[] decoded = QuotedPrintableCodec.decodeQuotedPrintable(expectedOutput);
        assertEquals(input.length, decoded.length);
        assertArrayEquals(input, decoded);

        String inputString = new String(input, "UTF-8");
        String encodedString = codec.encode(inputString, "UTF-8");
        String decodedString = codec.decode(encodedString, "UTF-8");
        assertEquals(inputString, decodedString);
    }

    @Test
    public void testEncodeWhitespace() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "Hello\nWorld\n";
        String encodedString = codec.encode(input, "UTF-8");

        String decodedString = codec.decode(encodedString, "UTF-8");
        assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeBinaryData() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] expectedOutput = QuotedPrintableCodec.encodeQuotedPrintable(QuotedPrintableCodec.PRINTABLE_CHARS, input);

        byte[] decoded = QuotedPrintableCodec.decodeQuotedPrintable(expectedOutput);
        assertEquals(input.length, decoded.length);
        assertArrayEquals(input, decoded);

        String inputString = StringUtils.newStringFromByteArray(input, "UTF-8");
        String encodedString = codec.encode(inputString, "UTF-8");
        String decodedString = codec.decode(encodedString, "UTF-8");
        assertEquals(inputString, decodedString);
    }

    @Test
    public void testEncodeWithDefaultCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "Hello, World!".getBytes();
        byte[] expectedOutput = QuotedPrintableCodec.encodeQuotedPrintable(QuotedPrintableCodec.PRINTABLE_CHARS, input);

        String inputString = new String(input);
        String encodedString = codec.encode(inputString);

        byte[] decoded = QuotedPrintableCodec.decodeQuotedPrintable(expectedOutput);
        assertEquals(input.length, decoded.length);
        assertArrayEquals(input, decoded);

        String decodedString = codec.decode(encodedString);
        assertEquals(inputString, decodedString);
    }

    @Test
    public void testDecodeNullInput() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        assertNull(codec.decode(null));
    }

    @Test
    public void testEncodeNullInput() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        assertNull(codec.encode(null));
    }
}