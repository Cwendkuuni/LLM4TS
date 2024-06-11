package org.apache.commons.codec.net;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    @Test
    public void testDefaultConstructor() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertEquals(CharEncoding.UTF_8.getName(), codec.getDefaultCharset());
    }

    @Test
    public void testConstructorWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        assertEquals("UTF-8", codec.getDefaultCharset());
    }

    @Test
    public void testEncodeByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {0x61, 0x62, 0x63, 0x72, 0x65, 0x63, 0x6F, 0x6D};
        byte[] output = codec.encode(input);
        String expected = "6265636F6D656361=61=62=63=";
        String actual = new String(output, CharEncoding.US_ASCII);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeByteArrayWithPrintableChars() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        BitSet printableChars = new BitSet(256);
        byte[] input = {0x41, 0x42, 0x43, 0x72, 0x65, 0x63, 0x6F, 0x6D};
        byte[] output = codec.encode(printableChars, input);
        String expected = "4142437265636F6D";
        String actual = new String(output, CharEncoding.US_ASCII);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeNullByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode(null);
    }

    @Test
    public void testEncodeString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "abcdefghijklmnopqrstuvwxyz";
        String output = codec.encode(input);
        String expected = "6162636465666768696a6b6c6d6e6f";
        String actual = new String(StringUtils.getBytesUsAscii(output), CharEncoding.US_ASCII);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeStringWithCharset() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "abcdefghijklmnopqrstuvwxyz";
        String output = codec.encode(input, CharEncoding.UTF_8);
        String expected = "6162636465666768696a6b6c6d6e6f";
        String actual = new String(StringUtils.getBytesUsAscii(output), CharEncoding.US_ASCII);
        assertEquals(expected, actual);
    }

    @Test
    public void testDecodeByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {"62", "65", "63", "6F", "6D", "65", "63", "61", "61", "62", "63"};
        byte[] output = codec.decode(input);
        byte[] expected = {0x61, 0x62, 0x63, 0x72, 0x65, 0x63, 0x6F, 0x6D};
        assertArrayEquals(expected, output);
    }

    @Test
    public void testDecodeByteArrayWithHexString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {"62", "65", "63", "6F", "6D", "65", "63", "61", "61", "62", "63"};
        byte[] output = codec.decode(Hex.decodeHex(new String(StringUtils.getBytesUsAscii("6265636F6D656361616263"))));
        byte[] expected = {0x61, 0x62, 0x63, 0x72, 0x65, 0x63, 0x6F, 0x6D};
        assertArrayEquals(expected, output);
    }

    @Test
    public void testDecodeString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "6162636465666768696a6b6c6d6e6f";
        String output = codec.decode(input);
        String expected = "abcdefghijklmnopqrstuvwxyz";
        assertEquals(expected, output);
    }

    @Test
    public void testDecodeStringWithCharset() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "6162636465666768696a6b6c6d6e6f";
        String output = codec.decode(input, CharEncoding.UTF_8);
        String expected = "abcdefghijklmnopqrstuvwxyz";
        assertEquals(expected, output);
    }

    @Test
    public void testEncodeNullString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        try {
            codec.encode(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDecodeNullString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        try {
            codec.decode(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testEncodeByteWithPrintableChar() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        codec.encodeQuotedPrintable((byte) 0x20, output);
        byte[] expected = {0x20, '='};
        byte[] actual = output.toByteArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEncodeByteWithNonPrintableChar() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        codec.encodeQuotedPrintable((byte) 0x7F, output);
        byte[] expected = {0x7F, '='};
        byte[] actual = output.toByteArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEncodeStringWithPrintableChars() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "abc\tdef";
        String output = codec.encode(input);
        String expected = "61626309646566";
        String actual = new String(StringUtils.getBytesUsAscii(output), CharEncoding.US_ASCII);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeStringWithNonPrintableChars() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "ab\0cde";
        String output = codec.encode(input);
        String expected = "6162006364656465";
        String actual = new String(StringUtils.getBytesUsAscii(output), CharEncoding.US_ASCII);
        assertEquals(expected, actual);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidHexDigit() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {"62", "65", "63", "6F", "6D", "65", "63", "61", "61", "62", "64"}; // intentional typo
        try {
            codec.decode(input);
            fail("Expected DecoderException");
        } catch (DecoderException e) {
            // expected
        }
    }

    @Test(expected = DecoderException.class)
    public void testDecodeTrailingCRLF() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {"62", "65", "63", "6F", "6D", "65", "63", "61", "61", "62", "63", "\r", "\n"};
        try {
            codec.decode(input);
            fail("Expected DecoderException");
        } catch (DecoderException e) {
            // expected
        }
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidEscapedCharacter() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {"62", "65", "63", "6F", "6D", "65", "63", "61", "61", "62", "63", "65"}; // intentional typo
        try {
            codec.decode(input);
            fail("Expected DecoderException");
        } catch (DecoderException e) {
            // expected
        }
    }
}