package org.apache.commons.codec.net;

import static org.junit.Assert.*;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeByteArray() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);

        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String encoded = codec.encode(input, "UTF-8");
        String decoded = codec.decode(encoded, "UTF-8");

        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeHexString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "4142434445464748494A4B4C4D4E4F";
        String encoded = codec.encode(Hex.decodeHex(input.toCharArray()), "UTF-8").toString();
        String decoded = codec.decode(encoded, "UTF-8").toString();

        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeHexByteArray() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = Hex.decodeHex("4142434445464748494A4B4C4D4E4F".toCharArray());
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);

        assertArrayEquals(input, decoded);
    }
}