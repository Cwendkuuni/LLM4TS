package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeAndDecodeByteArray() throws Exception {
        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeAndDecodeByteArrayWithNullInput() {
        byte[] input = null;
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);
        assertNull(decoded);
    }

    @Test
    public void testEncodeAndDecodeByteArrayWithEmptyInput() {
        byte[] input = new byte[0];
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeAndDecodeString() throws Exception {
        String input = "foo\rbar";
        String encoded = codec.encode(input.getBytes(codec.getDefaultCharset()));
        String decoded = new String(codec.decode(encoded.getBytes(codec.getDefaultCharset())), codec.getDefaultCharset());
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeAndDecodeStringWithNullInput() {
        String input = null;
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        assertNull(decoded);
    }

    @Test
    public void testEncodeAndDecodeStringWithEmptyInput() {
        String input = "";
        String encoded = codec.encode(input.getBytes(codec.getDefaultCharset()));
        String decoded = new String(codec.decode(encoded.getBytes(codec.getDefaultCharset())), codec.getDefaultCharset());
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeAndDecodeObject() throws Exception {
        String input = "foo\rbar";
        Object encodedObj = codec.encode(input.getBytes(codec.getDefaultCharset()));
        Object decodedObj = codec.decode(encodedObj);
        assertEquals(input, ((String) decodedObj));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidObjectType() {
        Object obj = new Object() {
        };
        codec.encode(obj);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidObjectType() {
        Object obj = new Object() {
        };
        codec.decode(obj.getClass().getName().getBytes(codec.getDefaultCharset()));
    }
}