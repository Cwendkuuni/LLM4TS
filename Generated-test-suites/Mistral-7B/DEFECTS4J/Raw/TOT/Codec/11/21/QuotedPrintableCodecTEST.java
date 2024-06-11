package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * QuotedPrintableCodecTest.java
 */
public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeBytes() throws EncoderException {
        byte[] bytes = {0x01, 0x02, 0x03, 0x04, 0x05};
        byte[] encodedBytes = new QuotedPrintableCodec().encode(bytes);
        String expectedEncodedString = "01=01 02=02 03=03 04=04 05=";
        String encodedString = new String(encodedBytes);
        assertEquals(expectedEncodedString, encodedString);
    }

    @Test
    public void testEncodeString() throws EncoderException, UnsupportedEncodingException {
        String inputString = "Hello, World!";
        byte[] encodedBytes = new QuotedPrintableCodec().encode(inputString.getBytes("UTF-8"));
        String expectedEncodedString = "48=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21=";
        String encodedString = new String(encodedBytes);
        assertEquals(expectedEncodedString, encodedString);
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        String inputString = "Hello, World!";
        Object inputObject = inputString;
        byte[] encodedBytes = new QuotedPrintableCodec().encode(inputObject);
        String expectedEncodedString = "48=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21=";
        String encodedString = new String(encodedBytes);
        assertEquals(expectedEncodedString, encodedString);
    }

    @Test
    public void testDecodeBytes() throws DecoderException {
        byte[] encodedBytes = {"01", "=", "01", " ", "02", "=", "02", " ", "03", "=", "03", " ", "04", "=", "04", " ", "05", "="};
        byte[] decodedBytes = new QuotedPrintableCodec().decode(encodedBytes);
        byte[] expectedDecodedBytes = {0x01, 0x02, 0x03, 0x04, 0x05};
        assertArrayEquals(expectedDecodedBytes, decodedBytes);
    }

    @Test
    public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
        String encodedString = "48=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21=";
        byte[] encodedBytes = encodedString.getBytes("US-ASCII");
        String decodedString = new String(new QuotedPrintableCodec().decode(encodedBytes), "UTF-8");
        assertEquals("Hello, World!", decodedString);
    }

    @Test
    public void testDecodeObject() throws DecoderException {
        String encodedString = "48=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21";
        byte[] encodedBytes = Hex.decodeHex(encodedString.toCharArray());
        String decodedString = new String(new QuotedPrintableCodec().decode(encodedBytes));
        assertEquals("Hello, World!", decodedString);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalid() throws DecoderException, UnsupportedEncodingException {
        String encodedString = "48=65=6C=6C=6F=2C=20=57=6F=72=6C=64=21=";
        byte[] encodedBytes = Hex.decodeHex(encodedString.toCharArray());
        encodedBytes[0] = (byte) 0xFF; // Change the first byte to an invalid value
        String decodedString = new String(new QuotedPrintableCodec().decode(encodedBytes));
    }

}