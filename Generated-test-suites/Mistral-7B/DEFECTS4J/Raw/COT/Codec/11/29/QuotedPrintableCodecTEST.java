package org.apache.commons.codec.net;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/**
 * Test for QuotedPrintableCodec class
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec(CharEncoding.UTF_8);

    /**
     * Test encode method with printable characters
     */
    @Test
    public void testEncodePrintable() {
        byte[] input = "Hello World!".getBytes(CharEncoding.UTF_8);
        byte[] expectedOutput = {"Hello World!\n".getBytes(CharEncoding.UTF_8)};

        byte[] output = CODEC.encode(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test encode method with non-printable characters
     */
    @Test
    public void testEncodeNonPrintable() {
        byte[] input = {"Hello\u0001World\u0002!".getBytes(CharEncoding.UTF_8)};
        byte[] expectedOutput = {"Hello=\01\u0001World=\02\u0002!\n".getBytes(CharEncoding.UTF_8)};

        byte[] output = CODEC.encode(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test encode method with CR and LF
     */
    @Test
    public void testEncodeCRLF() {
        byte[] input = {"Hello\r\nWorld!\r\n".getBytes(CharEncoding.UTF_8)};
        byte[] expectedOutput = {"Hello=\r\nWorld!\r\n".getBytes(CharEncoding.UTF_8)};

        byte[] output = CODEC.encode(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test encode method with whitespace at the end of a line
     */
    @Test
    public void testEncodeWhitespace() {
        byte[] input = {"Hello World!\n   ".getBytes(CharEncoding.UTF_8)};
        byte[] expectedOutput = {"Hello World!\n   =\n".getBytes(CharEncoding.UTF_8)};

        byte[] output = CODEC.encode(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test decode method with printable characters
     */
    @Test
    public void testDecodePrintable() {
        byte[] input = {"Hello World!\n".getBytes(CharEncoding.UTF_8)};
        String expectedOutput = "Hello World!";

        String output = CODEC.decodeToString(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test decode method with escaped characters
     */
    @Test
    public void testDecodeEscaped() {
        byte[] input = {"Hello=\01World=\02!\n".getBytes(CharEncoding.UTF_8)};
        String expectedOutput = "Hello\u0001World\u0002!";

        String output = CODEC.decodeToString(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test decode method with CR and LF
     */
    @Test
    public void testDecodeCRLF() {
        byte[] input = {"Hello=\r\nWorld!\r\n".getBytes(CharEncoding.UTF_8)};
        String expectedOutput = "Hello\r\nWorld!";

        String output = CODEC.decodeToString(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test decode method with whitespace at the end of a line
     */
    @Test
    public void testDecodeWhitespace() {
        byte[] input = {"Hello World!\n   =\n".getBytes(CharEncoding.UTF_8)};
        String expectedOutput = "Hello World!";

        String output = CODEC.decodeToString(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test encode method with null input
     */
    @Test(expected = NullPointerException.class)
    public void testEncodeNullInput() {
        CODEC.encode(null);
    }

    /**
     * Test decode method with null input
     */
    @Test(expected = NullPointerException.class)
    public void testDecodeNullInput() {
        CODEC.decode(null);
    }

    /**
     * Test decode method with invalid encoded string
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() {
        byte[] input = {"Hello=\rWorld!\n".getBytes(CharEncoding.UTF_8)};
        CODEC.decode(input);
    }
}