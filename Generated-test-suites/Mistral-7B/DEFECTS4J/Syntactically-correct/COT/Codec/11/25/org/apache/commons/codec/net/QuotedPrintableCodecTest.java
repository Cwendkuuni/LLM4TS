import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;

import org.junit.Test;

/**
 * Test cases for QuotedPrintableCodec class
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    /**
     * Test constructor with null arg
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullArg() {
        new QuotedPrintableCodec(null);
    }

    /**
     * Test constructor with empty charset
     */
    @Test
    public void testConstructorWithEmptyCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("");
        assertEquals("", codec.getDefaultCharset());
    }

    /**
     * Test constructor with valid charset
     */
    @Test
    public void testConstructorWithValidCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        assertEquals("UTF-8", codec.getDefaultCharset());
    }

    /**
     * Test encode method with null input
     */
    @Test
    public void testEncodeNullInput() {
        assertNull(CODEC.encode(null));
    }

    /**
     * Test encode method with empty byte array input
     */
    @Test
    public void testEncodeEmptyByteArrayInput() {
        byte[] result = CODEC.encode(new byte[0]);
        assertArrayEquals(new byte[0], result);
    }

    /**
     * Test encode method with single printable character byte input
     */
    @Test
    public void testEncodeSinglePrintableCharacterByteInput() {
        byte[] byteArray = {'a'};
        byte[] result = CODEC.encode(byteArray);
        assertArrayEquals(byteArray, result);
    }

    /**
     * Test encode method with single non-printable character byte input
     */
    @Test
    public void testEncodeSingleNonPrintableCharacterByteInput() {
        byte[] byteArray = {'!'};
        byte[] result = CODEC.encode(byteArray);
        byte[] expectedResult = new byte[]{ESCAPE_CHAR, '!'};
        assertArrayEquals(expectedResult, result);
    }

    /**
     * Test encode method with multiple non-printable characters byte input
     */
    @Test
    public void testEncodeMultipleNonPrintableCharactersByteInput() {
        byte[] byteArray = {'!', '!', '!'};
        byte[] result = CODEC.encode(byteArray);
        byte[] expectedResult = new byte[]{ESCAPE_CHAR, '!', ESCAPE_CHAR, '!'};
        assertArrayEquals(expectedResult, result);
    }

    /**
     * Test encode method with byte array containing spaces and tabs
     */
    @Test
    public void testEncodeByteArrayContainingSpacesAndTabs() {
        byte[] byteArray = {' ', TAB};
        byte[] result = CODEC.encode(byteArray);
        byte[] expectedResult = new byte[]{ESCAPE_CHAR, ' '};
        assertArrayEquals(expectedResult, result);
    }

    /**
     * Test decode method with null input
     */
    @Test
    public void testDecodeNullInput() {
        assertNull(CODEC.decode(null));
    }

    /**
     * Test decode method with empty byte array input
     */
    @Test
    public void testDecodeEmptyByteArrayInput() {
        assertArrayEquals(new byte[0], CODEC.decode(new byte[0]));
    }

    /**
     * Test decode method with single printable character byte input
     */
    @Test
    public void testDecodeSinglePrintableCharacterByteInput() {
        byte[] byteArray = {'a'};
        byte[] result = CODEC.decode(byteArray);
        assertArrayEquals(byteArray, result);
    }

    /**
     * Test decode method with single escaped character byte input
     */
    @Test
    public void testDecodeSingleEscapedCharacterByteInput() {
        byte[] byteArray = {ESCAPE_CHAR, '!'};
        byte[] result = CODEC.decode(byteArray);
        assertArrayEquals(new byte[]{'!'}, result);
    }

    /**
     * Test decode method with multiple escaped characters byte input
     */
    @Test
    public void testDecodeMultipleEscapedCharactersByteInput() {
        byte[] byteArray = {ESCAPE_CHAR, '!', ESCAPE_CHAR, '!'};
        byte[] result = CODEC.decode(byteArray);
        assertArrayEquals(new byte[]{'!', '!'}, result);
    }

    /**
     * Test decode method with byte array containing spaces and tabs
     */
    @Test
    public void testDecodeByteArrayContainingSpacesAndTabs() {
        byte[] byteArray = {ESCAPE_CHAR, ' ', TAB, ESCAPE_CHAR};
        byte[] result = CODEC.decode(byteArray);
        byte[] expectedResult = new byte[]{' ', TAB};
        assertArrayEquals(expectedResult, result);
    }

    /**
     * Test encodeString method with null input
     */
    @Test
    public void testEncodeStringNullInput() {
        assertNull(CODEC.encode((String) null));
    }

    /**
     * Test encodeString method with empty string input
     */
    @Test
    public void testEncodeStringEmptyStringInput() {
        String result = CODEC.encode("");
        assertTrue(result.isEmpty());
    }

    /**
     * Test encodeString method with single printable character input
     */
    @Test
    public void testEncodeStringSinglePrintableCharacterInput() {
        String input = "a";
        String result = CODEC.encode(input);
        assertEquals(input, result);
    }

    /**
     * Test encodeString method with single non-printable character input
     */
    @Test
    public void testEncodeStringSingleNonPrintableCharacterInput() {
        String input = "!";
        String result = CODEC.encode(input);
        String expectedResult = StringUtils.newStringUsAscii(new byte[]{ESCAPE_CHAR, '!'});
        assertEquals(expectedResult, result);
    }

    /**
     * Test encodeString method with multiple non-printable characters input
     */
    @Test
    public void testEncodeStringMultipleNonPrintableCharactersInput() {
        String input = "!!";
        String result = CODEC.encode(input);
        String expectedResult = StringUtils.newStringUsAscii(new byte[]{ESCAPE_CHAR, '!', ESCAPE_CHAR, '!'});
        assertEquals(expectedResult, result);
    }

    /**
     * Test encodeString method with string containing spaces and tabs
     */
    @Test
    public void testEncodeStringByteArrayContainingSpacesAndTabs() {
        String input = "  \t";
        String result = CODEC.encode(input);
        String expectedResult = StringUtils.newStringUsAscii(new byte[]{ESCAPE_CHAR, ' ', ESCAPE_CHAR, TAB});
        assertEquals(expectedResult, result);
    }

    /**
     * Test decodeString method with null input
     */
    @Test
    public void testDecodeStringNullInput() {
        assertNull(CODEC.decode((String) null, CharEncoding.UTF_8));
    }

    /**
     * Test decodeString method with empty string input
     */
    @Test
    public void testDecodeStringEmptyStringInput() {
        String result = CODEC.decode("", CharEncoding.UTF_8);
        assertTrue(result.isEmpty());
    }

    /**
     * Test decodeString method with single printable character input
     */
    @Test
    public void testDecodeStringSinglePrintableCharacterInput() {
        String input = "a";
        String result = CODEC.decode(input, CharEncoding.UTF_8);
        assertEquals(input, result);
    }

    /**
     * Test decodeString method with single escaped character input
     */
    @Test
    public void testDecodeStringSingleEscapedCharacterInput() {
        String input = "=%21";
        String result = CODEC.decode(input, CharEncoding.UTF_8);
        assertEquals("!", result);
    }

    /**
     * Test decodeString method with multiple escaped characters input
     */
    @Test
    public void testDecodeStringMultipleEscapedCharactersInput() {
        String input = "%=31%=31";
        String result = CODEC.decode(input, CharEncoding.UTF_8);
        assertEquals("!", result);
    }

    /**
     * Test decodeString method with string containing spaces and tabs
     */
    @Test
    public void testDecodeStringByteArrayContainingSpacesAndTabs() {
        String input = "%20%09";
        String result = CODEC.decode(input, CharEncoding.UTF_8);
        assertEquals(" ", result);
    }

    /**
     * Test decodeString method with invalid input
     */
    @Test(expected = DecoderException.class)
    public void testDecodeStringInvalidInput() {
        String input = "%=10";
        CODEC.decode(input, CharEncoding.UTF_8);
    }

    /**
     * Test decodeString method with invalid charset input
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeStringInvalidCharsetInput() {
        String input = "%=21";
        CODEC.decode(input, " invalid_charset");
    }

    /**
     * Test isPrintable method with null BitSet input
     */
    @Test
    public void testIsPrintableNullBitSetInput() {
        assertFalse(CODEC.isPrintable(null));
    }

    /**
     * Test isPrintable method with empty BitSet input
     */
    @Test
    public void testIsPrintableEmptyBitSetInput() {
        BitSet bitSet = new BitSet();
        assertFalse(CODEC.isPrintable(bitSet));
    }

    /**
     * Test isPrintable method with BitSet containing printable characters
     */
    @Test
    public void testIsPrintablePrintableCharactersBitSetInput() {
        BitSet bitSet = PRINTABLE_CHARS;
        assertTrue(CODEC.isPrintable(bitSet));
    }

    /**
     * Test isPrintable method with BitSet containing non-printable characters
     */
    @Test
    public void testIsPrintableNonPrintableCharactersBitSetInput() {
        BitSet bitSet = new BitSet();
        bitSet.set(128);
        assertFalse(CODEC.isPrintable(bitSet));
    }

    /**
     * Test encode method with BinaryDecoder
     */
    @Test
    public void testEncodeBinaryDecoder() {
        byte[] input = {0x61, 0x72, 0x63};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BinaryDecoder decoder = new StringDecoder(CharEncoding.US_ASCII.name(), false);
        decoder.decode(input, 0, input.length, output, 0, output.size());
        byte[] decoded = output.toByteArray();
        byte[] encoded = CODEC.encode(decoded);
        byte[] expectedEncoded = new byte[]{ESCAPE_CHAR, 'a', ESCAPE_CHAR, 'b', ESCAPE_CHAR, 'c'};
        assertArrayEquals(expectedEncoded, encoded);
    }

    /**
     * Test encode method with BinaryEncoder
     */
    @Test
    public void testEncodeBinaryEncoder() {
        ByteArrayOutputStream input = new ByteArrayOutputStream();
        CODEC.encode("abc", input, CharEncoding.UTF_8);
        byte[] decoded = CODEC.decode(input.toByteArray(), CharEncoding.UTF_8);
        byte[] encoded = CODEC.encode(decoded);
        byte[] expectedEncoded = CODEC.encode("abc".getBytes(CharEncoding.UTF_8));
        assertArrayEquals(expectedEncoded, encoded);
    }
}