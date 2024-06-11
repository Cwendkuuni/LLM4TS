package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

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

    

    @Test
    public void testQuotedPrintableCodecDefaultConstructor() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNotNull(codec);
    }

    @Test
    public void testQuotedPrintableCodecWithCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(CharEncoding.UTF_8);
        assertNotNull(codec);
    }

    @Test
    public void testEncodeByteArray() {
        byte[] input = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64};
        byte[] expectedOutput = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64, 0x3D, 0x31, 0x30, 0x31, 0x3D, 0x31, 0x35, 0x3D, 0x31, 0x36, 0x3D, 0x31, 0x36, 0x34};
        byte[] output = new QuotedPrintableCodec().encode(input);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testDecodeByteArray() throws DecoderException {
        byte[] input = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64, 0x3D, 0x31, 0x30, 0x31, 0x3D, 0x31, 0x35, 0x3D, 0x31, 0x36, 0x3D, 0x31, 0x36, 0x34};
        byte[] expectedOutput = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64};
        byte[] output = new QuotedPrintableCodec().decode(input);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testEncodeString() throws EncoderException {
        String input = "HelloWorld";
        String expectedOutput = "HelloWorld=";
        String output = new QuotedPrintableCodec().encode(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
        String input = "HelloWorld=";
        String expectedOutput = "HelloWorld";
        String output = new QuotedPrintableCodec().decode(input, CharEncoding.UTF_8);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeStringWithUnsupportedCharset() throws DecoderException {
        String input = "HelloWorld=";
        String charset = "ISO-8859-1"; // Unsupported charset
        new QuotedPrintableCodec().decode(input, charset);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeObjectWithUnsupportedType() throws EncoderException {
        Integer number = 123;
        new QuotedPrintableCodec().encode(number);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeObjectWithUnsupportedType() throws DecoderException {
        String input = "HelloWorld=";
        new QuotedPrintableCodec().decode(new Integer(123));
    }

    
}