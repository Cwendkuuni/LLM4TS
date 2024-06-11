package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeBytes() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};
        byte[] output = codec.encode(input);
        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeDecodeString() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "foo\u0001bar";
        String output = codec.encode(input);
        String decodedOutput = codec.decode(output);
        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeByteArrayWithEscapedCharacters() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {'"', '=', 'C', '=', 'o', '=', 'p', '=', 'y', '=',
                '=', ' ', '=', 'T', 'a', 'b'};
        byte[] output = codec.encode(input);
        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeStringWithEscapedCharacters() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "=C=o=p=y=\r=\n   =T\u0061b";
        String output = codec.encode(input);
        String decodedOutput = codec.decode(output);
        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeByteArrayWithWhitespaceAtEnd() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {'C', 'o', 'p', 'y', ' ', '\r'};
        byte[] output = codec.encode(input);
        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeStringWithWhitespaceAtEnd() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Cop\r";
        String output = codec.encode(input);
        String decodedOutput = codec.decode(output);
        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeByteArrayWithCRLF() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {'C', 'o', 'p', '\r', '\n'};
        byte[] output = codec.encode(input);
        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeStringWithCRLF() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Cop\r\n";
        String output = codec.encode(input);
        String decodedOutput = codec.decode(output);
        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeByteArrayWithTabCharacter() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {'C', 'o', 'p', '\t'};
        byte[] output = codec.encode(input);
        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeStringWithTabCharacter() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Cop\t";
        String output = codec.encode(input);
        String decodedOutput = codec.decode(output);
        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeByteArrayWithLeadingAndTrailingDoubleQuotes() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {'"', 'C', 'o', 'p', 'y', '"'};
        byte[] output = codec.encode(input);
        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeStringWithLeadingAndTrailingDoubleQuotes() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "\"Copy\"";
        String output = codec.encode(input);
        String decodedOutput = codec.decode(output);
        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeByteArrayWithHexEscapes() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = {'C', 'o', 'p', 'y', (byte) 0x2D, '=', '1', 'A', 'C', '=', '2'};
        byte[] output = codec.encode(input);
        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeAndDecodeStringWithHexEscapes() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Copy-\r-1AC-2";
        String output = codec.encode(input);
        String decodedOutput = codec.decode(output);
        assertEquals(input, decodedOutput);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(new byte[]{0x01});
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInputWithInvalidHexEscape() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(new byte[]{"=B", "="});
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInputMissingTerminator() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(new byte[]{"C", "o", "p"});
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeWithUnsupportedCharset() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-16LE");
        codec.decode(new byte[0]);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testEncodeWithUnsupportedCharset() throws UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-16LE");
        codec.encode(new byte[0]);
    }
}