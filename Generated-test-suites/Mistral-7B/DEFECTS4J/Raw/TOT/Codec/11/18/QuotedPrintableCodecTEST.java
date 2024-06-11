package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringBuilderEncoder;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecode() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello, World! WITH TAB: \t12345\nSPACES:  12345\nLINE BREAK";
        byte[] encodedBytes = codec.encode(input.getBytes(StandardCharsets.UTF_8));
        String decodedString = new StringDecoder().decodeToString(encodedBytes, StandardCharsets.UTF_8);
        Assertions.assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeBinaryData() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05};
        byte[] encodedBytes = codec.encode(input);
        String decodedString = new StringDecoder().decodeToString(encodedBytes, StandardCharsets.UTF_8);
        Assertions.assertArrayEquals(input, StringUtils.getBytesUsAscii(decodedString));
    }

    @Test
    public void testEncodeInvalidInput() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Invalid Input: µ\n";
        Assertions.assertThrows(EncoderException.class, () -> codec.encode(input));
    }

    @Test
    public void testDecodeInvalidInput() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encodedInput = "=?UTF-8?B?VG8gYXJlIGZvciBkZWZhdWx0IG1lYW5zLCBwbGVhc3RlciwgZXhhbXBsZSBoYXZlIGhhdmUgZmFsc28h?=";
        Assertions.assertThrows(DecoderException.class, () -> codec.decode(encodedInput));
    }

    @Test
    public void testDecodeEscapedChars() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encodedInput = "Hello,\rWorld!\rWITH TAB: =09\t12345\rSPACES: =09=\t12345\rLINE BREAK=";
        byte[] decodedBytes = codec.decode(encodedInput.getBytes(StandardCharsets.UTF_8));
        String decodedString = new StringDecoder().decodeToString(decodedBytes, StandardCharsets.UTF_8);
        Assertions.assertEquals("Hello,\rWorld!\rWITH TAB: \t12345\rSPACES:  12345\rLINE BREAK", decodedString);
    }

    @Test
    public void testDecodeLineBreaks() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encodedInput = "Hello,\rWorld!\rWITH TAB: =09\t12345\rSPACES: =09=\t12345\rLINE\rBREAK=";
        byte[] decodedBytes = codec.decode(encodedInput.getBytes(StandardCharsets.UTF_8));
        String decodedString = new StringDecoder().decodeToString(decodedBytes, StandardCharsets.UTF_8);
        Assertions.assertEquals("Hello,\rWorld!\rWITH TAB: \t12345\rSPACES:  12345\rLINE BREAK", decodedString);
    }

    @Test
    public void testDecodeExtraLineBreaks() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encodedInput = "Hello,\rWorld!\rWITH TAB: =09\t12345\rSPACES: =09=\t12345\rLINE BREAK=\rLINE BREAK=";
        byte[] decodedBytes = codec.decode(encodedInput.getBytes(StandardCharsets.UTF_8));
        String decodedString = new StringDecoder().
                decodeToString(decodedBytes, StandardCharsets.UTF_8);
        Assertions.assertEquals("Hello,\rWorld!\rWITH TAB: \t12345\rSPACES:  12345\rLINE BREAK", decodedString);
    }

    @Test
    public void testEncodeDecodeBuffer() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        StringBuilderEncoder stringBuilderEncoder = new StringBuilderEncoder();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        String input = "Hello, World!  WITH TAB: \t12345\nSPACES:  12345\nLINE BREAK";
        codec.encode(input.getBytes(StandardCharsets.UTF_8), stringBuilderEncoder);
        byte[] encodedBytes = byteArrayOutputStream.toByteArray();
        String decodedString = new StringDecoder().decodeToString(encodedBytes, StandardCharsets.UTF_8);
        Assertions.assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeDecodeFullCycle() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        StringBuilderEncoder stringBuilderEncoder = new StringBuilderEncoder();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        String input = "Hello, World!  WITH TAB: \t12345\nSPACES:  12345\nLINE BREAK";
        codec.encode(input.getBytes(StandardCharsets.UTF_8), stringBuilderEncoder);
        byte[] encodedBytes = byteArrayOutputStream.toByteArray();
        String decodedString = new StringDecoder().decodeToString(encodedBytes, StandardCharsets.UTF_8);
        Assertions.assertEquals(input, decodedString);

        byte[] decodedBytes = StringUtils.getBytesUsAscii(decodedString);
        String decodedString2 = new StringDecoder().decodeToString(decodedBytes, StandardCharsets.UTF_8);
        Assertions.assertEquals(input, decodedString2);
    }
}