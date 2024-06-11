package org.apache.commons.codec.net; // make sure the package is correct

import org.apache.commons.codec.BitSet;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {
    // Test variables
    private static final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    // Test method for encode(byte[])
    @Test
    public void testEncodeByteArray() throws Exception {
        byte[] input = {0x61, 0x62, 0x63, 0x64, 0x65};
        byte[] expected = {0x61, 0x62, 0x63, 0x3D, 0x3D, 0x64, 0x3D, 0x3D, 0x65, 0x3D, 0x3D};
        byte[] output = codec.encode(input);
        assertArrayEquals(expected, output);
    }

    // Test method for encode(byte[], BitSet)
    @Test
    public void testEncodeByteArrayWithPrintableCharacters() throws Exception {
        byte[] input = {0x61, 0x62, 0x63, 0x64, 0x65};
        BitSet printable = new BitSet();
        byte[] expected = {0x61, 0x62, 0x63, 0x64, 0x65};
        byte[] output = codec.encode(input, printable);
        assertArrayEquals(expected, output);
    }

    // Test method for encode(byte[], BitSet) with non-printable characters
    @Test
    public void testEncodeByteArrayWithNonPrintableCharacters() throws Exception {
        byte[] input = {0x01, 0x02, 0x03, (byte) 0x7F, 0x04, 0x05};
        BitSet printable = new BitSet();
        byte[] expected = {0x01, 0x3D, 0x3D, 0x3B, 0x7F, 0x3D, 0x3D, 0x3B, 0x04, 0x3D, 0x3D, 0x05, 0x3D, 0x3D};
        byte[] output = codec.encode(input, printable);
        assertArrayEquals(expected, output);
    }

    // Test method for encode(byte[]) with default charset
    @Test
    public void testEncodeByteArrayWithDefaultCharset() throws Exception {
        byte[] input = {0x61, 0x62, 0x63, 0x64, 0x65};
        String expected = "61-62-3D-3D-63-3D-3D-64-3D-3D-65-3D-3D";
        String output = Hex.encodeHexString(codec.encode(input));
        assertEquals(expected, output);
    }

    // Test method for encode(String)
    @Test
    public void testEncodeString() throws Exception {
        String input = "abcdef";
        String expected = "61-62-3D-3D-63-3D-3D-64-3D-3D-65-3D-3D";
        String output = codec.encode(input);
        assertEquals(expected, output);
    }

    // Test method for encode(String, String)
    @Test
    public void testEncodeStringWithCharset() throws Exception {
        String input = "abcdef";
        String charset = "UTF-8";
        String expected = "61-62-3D-3D-63-3D-3D-64-3D-3D-65-3D-3D";
        String output = codec.encode(input, charset);
        assertEquals(expected, output);
    }

    // Test method for decode(byte[])
    @Test
    public void testDecodeByteArray() throws Exception {
        byte[] input = {0x61, 0x62, 0x3D, 0x3D, 0x63, 0x3D, 0x3D, 0x64, 0x3D, 0x3D, 0x65, 0x3D, 0x3D};
        byte[] expected = {0x61, 0x62, 0x63, 0x64, 0x65};
        byte[] output = codec.decode(input);
        assertArrayEquals(expected, output);
    }

    // Test method for decode(byte[]) with invalid input
    @Test(expected = DecoderException.class)
    public void testDecodeByteArrayWithInvalidInput() throws Exception {
        byte[] input = {0x61, 0x62, 0x3D, 0x3D, 0x63, 0x3D, 0x3D, 0x64, 0x3D, 0x3D, 0x65, 0x3D, 0x3D, 0x3D};
        codec.decode(input);
    }

    // Test method for decode(byte[]) with empty input
    @Test
    public void testDecodeByteArrayWithEmptyInput() throws Exception {
        byte[] input = {};
        byte[] output = codec.decode(input);
        assertArrayEquals(input, output);
    }

    // Test method for decode(byte[]) with null input
    @Test(expected = NullPointerException.class)
    public void testDecodeByteArrayWithNullInput() throws Exception {
        byte[] input = null;
        codec.decode(input);
    }

    // Test method for decode(String)
    @Test
    public void testDecodeString() throws Exception {
        String input = "61-62-3D-3D-63-3D-3D-64-3D-3D-65-3D-3D";
        byte[] expected = {0x61, 0x62, 0x63, 0x64, 0x65};
        byte[] output = codec.decode(input);
        assertArrayEquals(expected, output);
    }

    // Test method for decode(String, String)
    @Test
    public void testDecodeStringWithCharset() throws Exception {
        String input = "61-62-3D-3D-63-3D-3D-64-3D-3D-65-3D-3D";
        String charset = "UTF-8";
        byte[] expected = {0x61, 0x62, 0x63, 0x64, 0x65};
        byte[] output = codec.decode(input, charset);
        assertArrayEquals(expected, output);
    }

    // Test method for decode(String) with invalid input
    @Test(expected = DecoderException.class)
    public void testDecodeStringWithInvalidInput() throws Exception {
        String input = "61-62-3D-3D-63-3D-3D-64-3D-3D-65-3D-3D-3D";
        codec.decode(input);
    }

    // Test method for decode(String) with empty input
    @Test
    public void testDecodeStringWithEmptyInput() throws Exception {
        String input = "";
        byte[] output = codec.decode(input);
        assertArrayEquals(new byte[0], output);
    }

    // Test method for decode(String) with null input
    @Test(expected = NullPointerException.class)
    public void testDecodeStringWithNullInput() throws Exception {
        String input = null;
        codec.decode(input);
    }
}