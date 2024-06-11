package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeAndDecode() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec();

        String input = "Hello, 世界! 你好! こんにちは! こんにちは、世界!";
        byte[] bytes = input.getBytes("UTF-8");
        byte[] encodedBytes = quotedPrintableCodec.encode(bytes);
        String encodedString = new String(encodedBytes, "UTF-8");
        byte[] decodedBytes = quotedPrintableCodec.decode(encodedBytes);
        String decodedString = new String(decodedBytes, "UTF-8");

        assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeAndDecodeWithDefaultCharset() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec();

        String input = "Hello, 世界! 你好! こんにちは! こんにちは、世界!";
        byte[] bytes = input.getBytes();
        byte[] encodedBytes = quotedPrintableCodec.encode(bytes);
        String encodedString = new String(encodedBytes);
        byte[] decodedBytes = quotedPrintableCodec.decode(encodedBytes);
        String decodedString = new String(decodedBytes);

        assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeEmptyString() {
        QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec();
        String input = "";
        byte[] bytes = input.getBytes();
        byte[] encodedBytes = quotedPrintableCodec.encode(bytes);
        String encodedString = new String(encodedBytes);
        byte[] decodedBytes = quotedPrintableCodec.decode(encodedBytes);
        String decodedString = new String(decodedBytes);

        assertEquals(input, decodedString);
        assertArrayEquals(new byte[0], encodedBytes);
        assertArrayEquals(new byte[0], decodedBytes);
    }

    @Test
    public void testEncodeAndDecodeWithBinaryData() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec();

        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05};
        byte[] encodedBytes = quotedPrintableCodec.encode(input);
        String encodedString = new String(encodedBytes, "UTF-8");
        byte[] decodedBytes = quotedPrintableCodec.decode(encodedBytes);

        assertArrayEquals(input, decodedBytes);
        assertEquals(encodedString, new String(encodedBytes, "UTF-8"));
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeWithUnsupportedCharset() throws DecoderException {
        QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec();
        String encodedString = "=?UTF-16LE?B?VG8gV1BQdXN0IGlzIG1pbiBkYXRlZCBzaWduIGFsbG93IHRoZSBuZWVkIG5vdCBpbiB0aGUxLg==";
        byte[] encodedBytes = encodedString.getBytes();
        quotedPrintableCodec.decode(encodedBytes);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeWithInvalidEncoding() throws DecoderException {
        QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec();
        String encodedString = "=?UTF-8?B?VG8gV1BQdXN0IGlzIG1pbiBkYXRlZCBzaWduIGFsbG93IHRoZSBuZWVkIG5vdCBpbiB0aGUxLg==";
        byte[] encodedBytes = encodedString.getBytes();
        quotedPrintableCodec.decode(encodedBytes);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeWithInvalidData() throws DecoderException {
        QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec();
        String encodedString = "=?UTF-8?B?VG8gV1BQdXN0IGlzIG1pbiBkYXRlZCBzaWduIGFsbG93IHRoZSBuZWVkIG5vdCBpbiB0aGUx";
        byte[] encodedBytes = encodedString.getBytes();
        quotedPrintableCodec.decode(encodedBytes);
    }
}