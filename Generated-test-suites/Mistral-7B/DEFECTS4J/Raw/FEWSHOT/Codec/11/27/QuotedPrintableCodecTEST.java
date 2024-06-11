package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

public class QuotedPrintableCodecTest {
    @Test
    public void testEncodeDecodeByteArray() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, 0x02, 0x03, (byte) 0x0d, 0x0a, 0x04, 0x05, 0x06, 0x07, 0x08};
        byte[] output = codec.encode(input);
        byte[] decodedOutput = codec.decode(output);

        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeDecodeString() throws EncoderException, DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abc\r\ndef\r\nghij\r\nklm\r\nopqr\r\nstuv\r\nwxyz";
        String output = codec.encode(input, CharEncoding.UTF_8.getName());
        String decodedOutput = codec.decode(output);

        assertEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeWhitespace() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {' '};
        byte[] output = codec.encode(input);

        assertTrue(output.length > 0);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidCharacter() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {'~'};
        codec.encode(input);
    }

    @Test
    public void testDecodeInvalidByteSequence() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {'=', '=', '='};
        byte[] output = codec.decode(input);

        assertNull(output);
    }
}