package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

/**
 * Test cases for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Test encoding and decoding of bytes.
     */
    @Test
    public void testBytes() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // test empty array
        byte[] input = new byte[0];
        byte[] output = codec.encode(input);
        assertArrayEquals(input, output);
        assertNull(codec.decode(output));

        // test single byte
        byte b = 0x61; // 'a'
        input = new byte[] { b };
        output = codec.encode(input);
        assertArrayEquals(input, output);
        assertEquals(String.valueOf(b), codec.decode(output));

        // test multiple bytes
        input = new byte[] { 0x61, 0x62, 0x63 }; // 'abc'
        output = codec.encode(input);
        assertEquals("616263", new String(output, StandardCharsets.US_ASCII));
        assertArrayEquals(input, codec.decode(output));

        // test special characters
        input = new byte[] { 0x0D, 0x0A }; // CR LF
        output = codec.encode(input);
        assertEquals("0D0DA", new String(output, StandardCharsets.US_ASCII));
        assertArrayEquals(input, codec.decode(output));

        input = new byte[] { 0x7F }; // DEL
        output = codec.encode(input);
        assertEquals("7F=", new String(output, StandardCharsets.US_ASCII));
        assertArrayEquals(input, codec.decode(output));

        input = new byte[] { 0x00 }; // NUL
        output = codec.encode(input);
        assertEquals("00=00", new String(output, StandardCharsets.US_ASCII));
        assertArrayEquals(input, codec.decode(output));

        // test invalid byte sequence
        input = new byte[] { 0x00, 0x01 }; // NUL followed by a non-NUL byte
        output = codec.encode(input);
        assertEquals("00=01", new String(output, StandardCharsets.US_ASCII));
        assertArrayEquals(new byte[] { 0x00 }, codec.decode(output));

        // test high bit set
        input = new byte[] { 0x80 }; // 0x80 is 1000 0000
        output = codec.encode(input);
        assertEquals("C2=80", new String(output, StandardCharsets.US_ASCII));
        assertArrayEquals(new byte[] { 0xC2, 0x80 }, codec.decode(output));

        // test multibyte sequence
        input = new byte[] { 0x81, 0x82, 0x83 }; // 0x81 0x82 0x83 is 1001 0100 0101
        output = codec.encode(input);
        assertEquals("C3=81 C2=82 C2=83", new String(output, StandardCharsets.US_ASCII));
        assertArrayEquals(input, codec.decode(output));
    }

    /**
     * Test encoding and decoding of strings with default charset.
     */
    @Test
    public void testStrings() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // test empty string
        String input = "";
        String output = codec.encode(input);
        assertEquals("", output);
        assertEquals(input, codec.decode(output));

        // test single character
        char c = 'a';
        input = Character.toString(c);
        output = codec.encode(input);
        assertEquals("61", new String(output, StandardCharsets.US_ASCII));
        assertEquals(Character.toString(c), codec.decode(output));

        // test multiple characters
        String s = "abc";
        input = s;
        output = codec.encode(input);
        assertEquals("616263", new String(output, StandardCharsets.US_ASCII));
        assertEquals(s, codec.decode(output));

        // test special characters
        String t = "\r\n";
        input = t;
        output = codec.encode(input);
        assertEquals("0D0DA", new String(output, StandardCharsets.US_ASCII));
        assertEquals(t, codec.decode(output));

        // test high bit set
        String u = "\u0080";
        input = u;
        output = codec.encode(input);
        assertEquals("C2=80", new String(output, StandardCharsets.US_ASCII));
        assertEquals(u, codec.decode(output));

        // test multibyte sequence
        String v = "\u0081\u0082\u0083";
        input = v;
        output = codec.encode(input);
        assertEquals("C3=81 C2=82 C2=83", new String(output, StandardCharsets.US_ASCII));
        assertEquals(v, codec.decode(output));

        // test decoding from invalid byte sequence
        String w = "\u0000\u0001"; // NUL followed by a non-NUL byte
        input = w;
        try {
            output = codec.encode(input);
            fail("DecoderException expected");
        } catch (DecoderException e) {
            // expected
        }
        assertEquals("00=01", new String(codec.decode(new byte[] { 0x00, 0x01 }), StandardCharsets.US_ASCII));
        assertEquals(w, codec.decode(codec.encode(w.getBytes(StandardCharsets.UTF_8))));

        // test decoding from high bit set byte sequence
        String x = "\u0080";
        input = x;
        try {
            output = codec.encode(input);
            fail("DecoderException expected");
        } catch (DecoderException e) {
            // expected
        }
        assertEquals("C2=80", new String(codec.decode(new byte[] { 0xC2, 0x80 }), StandardCharsets.US_ASCII));
        assertEquals(x, codec.decode(codec.encode(x.getBytes(StandardCharsets.UTF_8))));
    }

    /**
     * Test encoding and decoding of strings with a specified charset.
     */
    @Test
    public void testStringsWithCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(StandardCharsets.UTF_8.name());

        // test empty string
        String input = "";
        String output = codec.encode(input);
        assertEquals("", output);
        assertEquals(input, codec.decode(output));

        // test single character
        char c = 'a';
        input = Character.toString(c);
        output = codec.encode(input);
        assertEquals("61", new String(output, StandardCharsets.US_ASCII));
        assertEquals(Character.toString(c), codec.decode(output));

        // test multiple characters
        String s = "abc";
        input = s;
        output = codec.encode(input);
        assertEquals("616263", new String(output, StandardCharsets.US_ASCII));
        assertEquals(s, codec.decode(output));

        // test special characters
        String t = "\r\n";
        input = t;
        output = codec.encode(input);
        assertEquals("0D0DA", new String(output, StandardCharsets.US_ASCII));
        assertEquals(t, codec.decode(output));

        // test high bit set
        String u = "\u0080";
        input = u;
        output = codec.encode(input);
        assertEquals("C2=80", new String(output, StandardCharsets.US_ASCII));
        assertEquals(u, codec.decode(output));

        // test multibyte sequence
        String v = "\u0081\u0082\u0083";
        input = v;
        output = codec.encode(input);
        assertEquals("C3=81 C2=82 C2=83", new String(output, StandardCharsets.US_ASCII));
        assertEquals(v, codec.decode(output));

        // test decoding from invalid byte sequence
        String w = "\u0000\u0001"; // NUL followed by a non-NUL byte
        input = w;
        try {
            output = codec.encode(input);
            fail("DecoderException expected");
        } catch (DecoderException e) {
            // expected
        }
        String decoded = new String(codec.decode(new byte[] { 0x00, 0x01 }), StandardCharsets.UTF_8);
        assertEquals(w, decoded);
        assertEquals(w, codec.decode(codec.encode(w)));

        // test decoding from high bit set byte sequence
        String x = "\u0080";
        input = x;
        try {
            output = codec.encode(input);
            fail("DecoderException expected");
        } catch (DecoderException e) {
            // expected
        }
        byte[] bytes = codec.encode(x.getBytes(StandardCharsets.UTF_8));
        assertEquals(Hex.encodeHexString(bytes), "C2=80");
        assertArrayEquals(new byte[] { 0xC2, 0x80 }, codec.decode(bytes));
        assertEquals(x, codec.decode(codec.encode(x)));
    }
}