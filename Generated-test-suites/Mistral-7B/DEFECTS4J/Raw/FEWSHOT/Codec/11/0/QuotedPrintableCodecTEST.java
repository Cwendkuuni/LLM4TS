package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec QPC = new QuotedPrintableCodec();

    @Test
    public void testEncodeQuotedPrintable() {
        byte[] input = "Hello World!".getBytes();
        byte[] expected = Hex.decodeHex("48656c6c6f20576f726c6421");
        byte[] output = QPC.encode(INPUT, PRINTABLE_CHARS);
        assertArrayEquals(expected, output);
    }

    @Test
    public void testDecodeQuotedPrintable() throws DecoderException {
        byte[] input = Hex.decodeHex("48656c6c6f20576f726c6421");
        byte[] output = QPC.decode(input);
        assertEquals("Hello World!", new String(output, CharEncoding.UTF_8));
    }

    @Test
    public void testEncodeQuotedPrintableWithWhitespace() {
        byte[] input = "Hello World!\n".getBytes();
        byte[] expected = Hex.decodeHex("48656c6c6f20576f726c640d0a");
        byte[] output = QPC.encode(INPUT, PRINTABLE_CHARS);
        assertArrayEquals(expected, output);
    }

    @Test
    public void testDecodeQuotedPrintableWithWhitespace() throws DecoderException {
        byte[] input = Hex.decodeHex("48656c6c6f20576f726c640d0a");
        byte[] output = QPC.decode(input);
        assertEquals("Hello World!\n", new String(output, CharEncoding.UTF_8));
    }

    @Test
    public void testEncodeQuotedPrintableWithCR() {
        byte[] input = "Hello World\r".getBytes();
        byte[] expected = Hex.decodeHex("48656c6c6f20576f726c640d");
        byte[] output = QPC.encode(INPUT, PRINTABLE_CHARS);
        assertArrayEquals(expected, output);
    }

    @Test
    public void testDecodeQuotedPrintableWithCR() throws DecoderException {
        byte[] input = Hex.decodeHex("48656c6c6f20576f726c640d");
        byte[] output = QPC.decode(input);
        assertEquals("Hello World\r", new String(output, CharEncoding.UTF_8));
    }

    @Test
    public void testEncodeQuotedPrintableWithLF() {
        byte[] input = "Hello World\n".getBytes();
        byte[] expected = Hex.decodeHex("48656c6c6f20576f726c640a");
        byte[] output = QPC.encode(INPUT, PRINTABLE_CHARS);
        assertArrayEquals(expected, output);
    }

    @Test
    public void testDecodeQuotedPrintableWithLF() throws DecoderException {
        byte[] input = Hex.decodeHex("48656c6c6f20576f726c640a");
        byte[] output = QPC.decode(input);
        assertEquals("Hello World\n", new String(output, CharEncoding.UTF_8));
    }

    @Test
    public void testEncodeQuotedPrintableWithControlCharacters() {
        byte[] input = "Hello \u0001World!".getBytes();
        byte[] expected = Hex.decodeHex("48656c6c6f20215c5c4101215e");
        byte[] output = QPC.encode(INPUT, PRINTABLE_CHARS);
        assertArrayEquals(expected, output);
    }

    @Test
    public void testDecodeQuotedPrintableWithControlCharacters() throws DecoderException {
        byte[] input = Hex.decodeHex("48656c6c6f20215c5c4101215e");
        byte[] output = QPC.decode(input);
        assertEquals("Hello \u0001World!", new String(output, CharEncoding.UTF_8));
    }

    @Test
    public void testEncodeQuotedPrintableWithNulCharacter() {
        byte[] input = "Hello \u0000World!".getBytes();
        byte[] expected = Hex.decodeHex("48656c6c6f20215c5c4200215e");
        byte[] output = QPC.encode(INPUT, PRINTABLE_CHARS);
        assertArrayEquals(expected, output);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeQuotedPrintableWithNulCharacter() throws DecoderException {
        byte[] input = Hex.decodeHex("48656c6c6f20215c5c4200215e");
        byte[] output = QPC.decode(input);
        assertEquals("Hello \u0000World!", new String(output, CharEncoding.UTF_8));
    }

    @Test
    public void testEncodeQuotedPrintableWithWhitespaceAtStart() {
        byte[] input = "\nHello World!".getBytes();
        byte[] expected = Hex.decodeHex("0a48656c6c6f20576f726c6421");
        byte[] output = QPC.encode(INPUT, PRINTABLE_CHARS);
        assertArrayEquals(expected, output);
    }

    @Test
    public void testDecodeQuotedPrintableWithWhitespaceAtStart() throws DecoderException {
        byte[] input = Hex.decodeHex("0a48656c6c6f20576f726c6421");
        byte[] output = QPC.decode(input);
        assertEquals("\nHello World!", new String(output, CharEncoding.UTF_8));
    }

    @Test
    public void testEncodeQuotedPrintableWithWhitespaceAtEnd() {
        byte[] input = "Hello World!\n".getBytes();
        byte[] expected = Hex.decodeHex("48656c6c6f20576f726c640a");
        byte[] output = QPC.encode(INPUT, PRINTABLE_CHARS);
        assertArrayEquals(expected, output);
    }

    @Test
    public void testDecodeQuotedPrintableWithWhitespaceAtEnd() throws DecoderException {
        byte[] input = Hex.decodeHex("48656c6c6f20576f726c640a");
        byte[] output = QPC.decode(input);
        assertEquals("Hello World!\n", new String(output, CharEncoding.UTF_8));
    }

    @Test
    public void testEncodeQuotedPrintableWithCRAtEnd() {
        byte[] input = "Hello World!\r".getBytes();
        byte[] expected = Hex.decodeHex("48656c6c6f20576f726c640d");
        byte[] output = QPC.encode(INPUT, PRINTABLE_CHARS);
        assertArrayEquals(expected, output);
    }

    @Test
    public void testDecodeQuotedPrintableWithCRAtEnd() throws DecoderException {
        byte[] input = Hex.decodeHex("48656c6c6f20576f726c640d");
        byte[] output = QPC.decode(input);
        assertEquals("Hello World!\r", new String(output, CharEncoding.UTF_8));
    }

    @Test
    public void testEncodeQuotedPrintableWithLFAtEnd() {
        byte[] input = "Hello World!\n".getBytes();
        byte[] expected = Hex.decodeHex("48656c6c6f20576f726c640a");
        byte[] output = QPC.encode(INPUT, PRINTABLE_CHARS);
        assertArrayEquals(expected, output);
    }

    @Test
    public void testDecodeQuotedPrintableWithLFAtEnd() throws DecoderException {
        byte[] input = Hex.decodeHex("48656c6c6f20576f726c640a");
        byte[] output = QPC.decode(input);
        assertEquals("Hello World!\n", new String(output, CharEncoding.UTF_8));
    }

    @Test
    public void testEncodeQuotedPrintableWithLongSequence() {
        byte[] input = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes();
        byte[] expected = Hex.decodeHex("313233343536373839303132333435363738393031323334353637383930313233343536373839303132333435363738393031323334353637383930313233343536373839303132333435363738393031323334353637383930313233343536373839303132333435");
        byte[] output = QPC.encode(INPUT, PRINTABLE_CHARS);
        assertArrayEquals(expected, output);
    }

    @Test
    public void testDecodeQuotedPrintableWithLongSequence() throws DecoderException {
        byte[] input = Hex.decodeHex("31323334353637383930313233343536373839303132333435363738393031323334353637383930313233343536373839303132333435363738393031323334353637383930313233343536373839303132333435363738393031323334353637383930313233343536373839303132333435363738393031323334353637383930313233343536373839303132333435363738393031323334353637383930313233343536373839303132333435363738393031323334353637383930313233343536373839303132333435");
        byte[] output = QPC.decode(input);
        assertEquals("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", new String(output, CharEncoding.UTF_8));
    }

    @Test
    public void testEncodeQuotedPrintableWithNullInput() {
        byte[] output = QPC.encode(null, PRINTABLE_CHARS);
        assertNull(output);
    }

    @Test
    public void testDecodeQuotedPrintableWithNullInput() throws DecoderException {
        byte[] input = null;
        byte[] output = QPC.decode(input);
        assertNull(output);
    }
}