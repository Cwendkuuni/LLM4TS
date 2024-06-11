package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    @Parameterized.Parameters(name = "{0} - input: {1}, expected: {2}")
    public static Collection<Object[]> testData() {
        return List.of(
                new Object[]{"", ""},
                new Object[]{"Hello", "=48=65=6C=6C=6F"},
                new Object[]{"Hello World", "=48=65=6C=6C=6F=20=57=6F=72=6C=64"},
                new Object[]{"Hello\nWorld", "=48=65=6C=6C=6F=0D=0A=57=6F=72=6C=64"},
                new Object[]{"Hello\rWorld", "=48=65=6C=6C=6F=0D=57=6F=72=6C=64"},
                new Object[]{"Hello\r\nWorld", "=48=65=6C=6C=6F=0D=0A=57=6F=72=6C=64"},
                new Object[]{"Hello\tWorld", "=48=65=6C=6C=6F=09=57=6F=72=6C=64"},
                new Object[]{"Hello  World", "=48=65=6C=6C=6F=20=57=6F=72=6C=64"}
        );
    }

    private final String input;
    private final String expected;

    public QuotedPrintableCodecTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void testEncodeString() throws EncoderException {
        String encoded = CODEC.encode(input);
        assertEquals(expected, encoded);
    }

    @Test
    public void testEncodeByteArray() throws EncoderException {
        byte[] bytes = input.getBytes();
        byte[] encoded = CODEC.encode(bytes);
        assertArrayEquals(expected.getBytes(), encoded);
    }

    @Test
    public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
        String decoded = CODEC.decode(expected, CharEncoding.US_ASCII);
        assertEquals(input, decoded);
    }

    @Test
    public void testDecodeByteArray() throws DecoderException, UnsupportedEncodingException {
        byte[] bytes = expected.getBytes();
        byte[] decoded = CODEC.decode(bytes);
        assertArrayEquals(input.getBytes(), decoded);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeByteArrayUnsupportedEncoding() {
        CODEC.decode(new byte[0], "invalidCharset");
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Object object = input;
        Object encodedObject = CODEC.encode(object);
        if (object instanceof String) {
            String encoded = ((String) encodedObject).intern();
            assertEquals(expected, encoded);
        } else if (object instanceof byte[]) {
            byte[] encodedBytes = (byte[]) encodedObject;
            assertArrayEquals(expected.getBytes(), encodedBytes);
        } else {
            throw new AssertionError("Unsupported object type: " + object.getClass());
        }
    }

    @Test
    public void testDecodeObjectString() throws DecoderException, UnsupportedEncodingException {
        Object object = expected;
        Object decodedObject = CODEC.decode(expected, CharEncoding.US_ASCII);
        if (object instanceof String) {
            String decoded = (String) decodedObject;
            assertEquals(input, decoded);
        } else if (object instanceof byte[]) {
            byte[] decodedBytes = (byte[]) decodedObject;
            assertArrayEquals(input.getBytes(), decodedBytes);
        } else {
            throw new AssertionError("Unsupported object type: " + object.getClass());
        }
    }

    @Test
    public void testDecodeObjectByteArray() throws DecoderException, UnsupportedEncodingException {
        Object object = expected;
        Object decodedObject = null;
        if (object instanceof String) {
            decodedObject = CODEC.decode(expected.getBytes(), CharEncoding.US_ASCII);
        } else if (object instanceof byte[]) {
            decodedObject = CODEC.decode((byte[]) object);
        } else {
            throw new AssertionError("Unsupported object type: " + object.getClass());
        }

        assertNotNull(decodedObject);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeObjectInvalidInput() throws DecoderException, UnsupportedEncodingException {
        Object object = "invalid input";
        CODEC.decode(object.getClass(), CharEncoding.US_ASCII);
    }

    @Test
    public void testEncodeByteArrayToString() throws EncoderException {
        byte[] inputBytes = input.getBytes();
        String encoded = CODEC.encodeToString(inputBytes);
        assertEquals(expected, encoded);
    }

    @Test
    public void testDecodeStringToByteArray() throws DecoderException, UnsupportedEncodingException {
        byte[] decodedBytes = CODEC.decode(expected);
        String decoded = new String(decodedBytes, CharEncoding.US_ASCII);
        assertEquals(input, decoded);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeStringToByteArrayUnsupportedEncoding() throws UnsupportedEncodingException {
        CODEC.decode(CharEncoding.ISO_8859_1, "invalidInput");
    }

    @Test
    public void testIsPrintableCharacter() {
        BitSet printable = QuotedPrintableCodec.PRINTABLE_CHARS;
        for (int i = 0; i < 256; i++) {
            byte b = (byte) i;
            boolean expected = printable.get(b);
            boolean actual = CODEC.isPrintableCharacter(b);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testIsPrintableCharacterSpace() {
        assertFalse(CODEC.isPrintableCharacter(' '));
    }

    @Test
    public void testIsPrintableCharacterTab() {
        assertFalse(CODEC.isPrintableCharacter('\t'));
    }

    @Test
    public void testIsPrintableCharacterCR() {
        assertFalse(CODEC.isPrintableCharacter('\r'));
    }

    @Test
    public void testIsPrintableCharacterLF() {
        assertFalse(CODEC.isPrintableCharacter('\n'));
    }

    @Test
    public void testEncodeQuotedPrintableByte() throws EncoderException {
        byte b = input.charAt(0);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        CODEC.encodeQuotedPrintable(b, buffer);
        assertArrayEquals(new byte[]{ESCAPE_CHAR, (byte) Character.digit(b >> 4, 16), (byte) Character.digit(b & 0xF, 16)}, buffer.toByteArray());
    }

    @Test
    public void testEncodeQuotedPrintableString() throws EncoderException {
        String inputString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        CODEC.encodeQuotedPrintable(inputString, buffer);
        byte[] encoded = buffer.toByteArray();

        byte[] expected = new byte[encoded.length + 2];
        System.arraycopy(encoded, 0, expected, 0, encoded.length);
        expected[encoded.length] = ESCAPE_CHAR;
        expected[encoded.length + 1] = (byte) 0;
        assertArrayEquals(expected, encoded);
    }

    @Test
    public void testDecodeQuotedPrintableByteArray() throws DecoderException {
        byte[] bytes = new byte[]{ESCAPE_CHAR, '5', '4', '1'};
        byte[] decoded = CODEC.decodeQuotedPrintable(bytes);
        assertEquals(new byte[]{0x41}, decoded);
    }

    @Test
    public void testDecodeQuotedPrintableInvalidByteArray() throws DecoderException {
        byte[] bytes = new byte[]{ESCAPE_CHAR, '5', '4', (byte) 0xFF};
        assertThrows(DecoderException.class, () -> CODEC.decodeQuotedPrintable(bytes));
    }

    @Test
    public void testDecodeQuotedPrintableByteArrayWithTrailingCR() throws DecoderException, UnsupportedEncodingException {
        byte[] bytes = new byte[]{ESCAPE_CHAR, '5', '4', '1', (byte) 0xD};
        byte[] decoded = CODEC.decodeQuotedPrintable(bytes);
        assertEquals(new byte[]{0x41}, decoded);
    }

    @Test
    public void testDecodeQuotedPrintableByteArrayWithTrailingLF() throws DecoderException, UnsupportedEncodingException {
        byte[] bytes = new byte[]{ESCAPE_CHAR, '5', '4', '1', (byte) 0xA};
        byte[] decoded = CODEC.decodeQuotedPrintable(bytes);
        assertEquals(new byte[]{0x41}, decoded);
    }

    @Test
    public void testDecodeQuotedPrintableByteArrayWithTrailingCR_LF() throws DecoderException, UnsupportedEncodingException {
        byte[] bytes = new byte[]{ESCAPE_CHAR, '5', '4', '1', (byte) 0xD, (byte) 0xA};
        byte[] decoded = CODEC.decodeQuotedPrintable(bytes);
        assertEquals(new byte[]{0x41}, decoded);
    }

    @Test
    public void testDecodeQuotedPrintableByteArrayWithTrailingSpace() throws DecoderException, UnsupportedEncodingException {
        byte[] bytes = new byte[]{ESCAPE_CHAR, '5', '4', '1', (byte) 0x20};
        byte[] decoded = CODEC.decodeQuotedPrintable(bytes);
        assertEquals(new byte[]{0x41}, decoded);
    }

    @Test
    public void testDecodeQuotedPrintableByteArrayWithTrailingTab() throws DecoderException, UnsupportedEncodingException {
        byte[] bytes = new byte[]{ESCAPE_CHAR, '5', '4', '1', (byte) 0x09};
        byte[] decoded = CODEC.decodeQuotedPrintable(bytes);
        assertEquals(new byte[]{0x41}, decoded);
    }

    @Test
    public void testDecodeQuotedPrintableByteArrayWithInvalidHex() throws DecoderException, UnsupportedEncodingException {
        byte[] bytes = new byte[]{ESCAPE_CHAR, '5', '4', '1', (byte) 0x63, (byte) 0x74, (byte) 0x61, 'L'};
        assertThrows(DecoderException.class, () -> CODEC.decodeQuotedPrintable(bytes));
    }

    @Test
    public void testDecodeQuotedPrintableByteArrayWithInvalidHexLength() throws DecoderException, UnsupportedEncodingException {
        byte[] bytes = new byte[]{ESCAPE_CHAR, '5', '4', '1', (byte) 0x63};
        assertThrows(DecoderException.class, () -> CODEC.decodeQuotedPrintable(bytes));
    }

    @Test
    public void testDecodeQuotedPrintableByteArrayWithMissingHex() throws DecoderException, UnsupportedEncodingException {
        byte[] bytes = new byte[]{ESCAPE_CHAR, '5', '4', '1'};
        assertThrows(DecoderException.class, () -> CODEC.decodeQuotedPrintable(bytes));
    }
}