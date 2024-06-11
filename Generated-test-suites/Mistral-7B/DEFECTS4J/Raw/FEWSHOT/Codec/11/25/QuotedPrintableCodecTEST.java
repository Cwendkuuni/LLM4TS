package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.BitSet;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeByteArray() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        byte[] expectedOutput = Hex.decodeHex("00010203040506=0708=090A0B0C0D=0E0F");

        byte[] output = codec.encode(input);
        assertArrayEquals(expectedOutput, output);

        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "foo\nbar\nbaz";
        String expectedOutput = "foo\n=\nbar\n=\n=\nbaz";

        String output = codec.encode(input);
        assertEquals(expectedOutput, output);

        String decodedOutput = codec.decode(output);
        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeWhitespaceAtEndOfLine() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "foo\nbar\n ";
        String expectedOutput = "foo\nbar\n=\n ";

        String output = codec.encode(input);
        assertEquals(expectedOutput, output);

        String decodedOutput = codec.decode(output);
        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeTrailingWhitespace() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "foo bar";
        String expectedOutput = "foo bar=";

        String output = codec.encode(input);
        assertEquals(expectedOutput, output);

        String decodedOutput = codec.decode(output);
        assertEquals(input, decodedOutput);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalid() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x0f};
        byte[] output = codec.decode(input);

        assertNull(output);
    }
}