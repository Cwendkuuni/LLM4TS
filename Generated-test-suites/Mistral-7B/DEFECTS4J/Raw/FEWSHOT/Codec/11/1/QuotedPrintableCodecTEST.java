package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeByteArray() throws Exception {
        byte[] input = {0x00, 0x61, 0x62, 0x63, (byte) 0x7F, 0x80, 0x81, 0x82, 0x83, 0x84};
        byte[] output = CODEC.encode(input);
        byte[] decodedOutput = CODEC.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        String input = "foo\u007F\u0080\u0081\u0082\u0083\u0084";
        String output = CODEC.encode(input, CharEncoding.US_ASCII.name());
        String decodedOutput = CODEC.decode(output, CharEncoding.US_ASCII.name());
        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeDecodeStringDefaultCharset() throws Exception {
        String input = "foo\u007F\u0080\u0081\u0082\u0083\u0084";
        String output = CODEC.encode(input);
        String decodedOutput = CODEC.decode(output);
        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeByteArrayWithNullInput() {
        assertNull(CODEC.encode(null));
    }

    @Test
    public void testEncodeStringWithNullInput() {
        assertThrows(NullPointerException.class, () -> CODEC.encode(null));
    }

    @Test
    public void testEncodeByteArrayWithEmptyInput() {
        byte[] input = {};
        byte[] output = CODEC.encode(input);
        assertEquals("", CODEC.decode(output, CharEncoding.US_ASCII.name()));
    }

    @Test
    public void testEncodeStringWithEmptyInput() {
        String input = "";
        String output = CODEC.encode(input);
        assertEquals("", CODEC.decode(output, CharEncoding.US_ASCII.name()));
    }

    @Test
    public void testEncodeDecodeByteArrayWithHoweverManyBytesAsNeeded() throws Exception {
        byte[] input = {'\u0001', 'a', 'b', 'c', '\u007F', '\u0080', '\u0081', '\u0082', '\u0083', '\u0084'};
        byte[] output = CODEC.encode(input);
        byte[] decodedOutput = CODEC.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeDecodeStringWithWhitespaceAtEnd() throws Exception {
        String input = "foo\n";
        String output = CODEC.encode(input);
        String decodedOutput = CODEC.decode(output, CharEncoding.US_ASCII.name());
        assertEquals("foo\n", decodedOutput);
    }

    @Test
    public void testEncodeDecodeStringWithWhitespaceAtEndEncoded() throws Exception {
        String input = "foo=\r\n";
        String output = CODEC.encode(input);
        String decodedOutput = CODEC.decode(output, CharEncoding.US_ASCII.name());
        assertEquals("foo\r\n", decodedOutput);
    }

    @Test
    public void testEncodeDecodeStringWithWhitespaceAtEndEncodedTwice() throws Exception {
        String input = "foo=\r\n";
        String output = CODEC.encode(input);
        String decodedOutput = CODEC.decode(output, CharEncoding.US_ASCII.name());
        assertEquals("foo\r\n", decodedOutput);
        output = CODEC.encode(decodedOutput);
        decodedOutput = CODEC.decode(output, CharEncoding.US_ASCII.name());
        assertEquals("foo\r\n", decodedOutput);
    }
}