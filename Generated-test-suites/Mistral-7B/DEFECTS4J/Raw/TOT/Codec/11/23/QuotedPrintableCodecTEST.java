package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.binary.BytesUtils;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    private QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeBinary() throws Exception {
        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
        byte[] expectedOutput = {(byte) 0x01, '=', '3', '1', (byte) 0x02, '=', '3', '2', (byte) 0x03, '=', '3', '3', (byte) 0x04, '=', '3', '4', (byte) 0x05, '=', '3', '5', (byte) 0x06, '=', '3', '6', (byte) 0x07, '=', '3', '7', (byte) 0x08, '=', '3', '8', (byte) 0x09};
        byte[] output = codec.encode(input);
        Assert.assertArrayEquals(expectedOutput, output);

        byte[] decodedOutput = codec.decode(output);
        Assert.assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        String input = "Hello, World!";
        String expectedOutput = "=48=65=6C=6C=6F=2C+20=57=6F=72=6C=64=21=";
        String output = codec.encode(input);
        Assert.assertEquals(expectedOutput, output);

        String decodedOutput = codec.decode(output);
        Assert.assertEquals(input, decodedOutput);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() throws Exception {
        byte[] input = {0x01, 0x02, 0x03, '=', '5'};
        codec.decode(input);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidInput() throws Exception {
        String input = "Hello, World!=\n";
        codec.encode(input.getBytes());
    }
}