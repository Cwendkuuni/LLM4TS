package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        byte[] expectedOutput = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};

        byte[] output = codec.encode(input);
        assertArrayEquals(expectedOutput, output);

        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeSpecialChars() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = new byte[] {'\r', '\n', '\t', '\f', '\b', ' ', '=', '-'};
        byte[] expectedOutput = new byte[] {'=0D', '=0A', '=09', '=0C', '=08', '=20', '=3D', '=2D'};

        byte[] output = codec.encode(input);
        assertArrayEquals(expectedOutput, output);

        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeWhitespaceAtEnd() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, '\t'};
        byte[] expectedOutput = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, "=09"};

        byte[] output = codec.encode(input);
        assertArrayEquals(expectedOutput, output);

        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeLongSequence() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        byte[] expectedOutput = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};

        byte[] output = codec.encode(input);
        assertArrayEquals(expectedOutput, output);

        byte[] decodedOutput = codec.decode(output);
        assertArrayEquals(input, decodedOutput);

        byte[] longSequence = new byte[1024 * 1024]; // 1MB
        byte[] longSequenceOutput = codec.encode(longSequence);
        byte[] decodedLongSequenceOutput = codec.decode(longSequenceOutput);
        assertArrayEquals(longSequence, decodedLongSequenceOutput);
    }

    @Test
    public void testDecodeInvalidSequence() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, "=3D"}; // invalid sequence

        try {
            codec.decode(input);
            assert false : "DecoderException should have been thrown";
        } catch (DecoderException e) {
            // expected
        }
    }

    @Test
    public void testDecodeInvalidHex() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, "=FF"}; // invalid hex sequence

        try {
            codec.decode(input);
            assert false : "DecoderException should have been thrown";
        } catch (DecoderException e) {
            // expected
        }
    }
}