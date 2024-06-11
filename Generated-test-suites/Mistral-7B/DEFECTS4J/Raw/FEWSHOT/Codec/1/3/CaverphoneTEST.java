package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test empty string
        assertEquals("1111111111", caverphone.caverphone(""));

        // Test single character
        assertEquals("A", caverphone.caverphone("a"));
        assertEquals("2", caverphone.caverphone("e"));

        // Test some simple strings
        assertEquals("3222F2S2", caverphone.caverphone("hello"));
        assertEquals("K2S2S2S2T3K3K", caverphone.caverphone("The quick Brown fox jumps over the lazy dog"));

        // Test strings with special characters
        assertEquals("F2S2S2S2T3K3K2S2F2", caverphone.caverphone("The quick Brown fox jumps over the lazy dog!"));
        assertEquals("3F23F2S2S2T3K3K2S2F2", caverphone.caverphone("The quick Brown fox jumps over the lazy dog! "));

        // Test strings with numbers
        assertEquals("1T2S2T3K3KM3N3", caverphone.caverphone("The quick Brown fox jumps over the lazy dog 123"));

        // Test strings with multiple spaces
        assertEquals("T2S2T3K3KM3N3A10", caverphone.caverphone("The quick Brown fox jumps over the lazy dog  The quick Brown fox jumps over the lazy dog"));

        // Test strings with capital letters
        assertEquals("2T2S2T3K3KM3N3A10", caverphone.caverphone("THE quick Brown fox jumps over the lazy dog"));

        // Test encoding and decoding
        String encoded = Hex.encodeHex(caverphone.encode("The quick Brown fox jumps over the lazy dog".getBytes()));
        assertEquals("The quick Brown fox jumps over the lazy dog", caverphone.decode(encoded));
    }

    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();

        assertEquals("The quick Brown fox jumps over the lazy dog", caverphone.decode(caverphone.encode("The quick Brown fox jumps over the lazy dog")));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(new Object() {});
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        assertTrue(caverphone.isCaverphoneEqual("The quick Brown fox jumps over the lazy dog", "The quick Brown fox jumps over the lazy dog"));
        assertFalse(caverphone.isCaverphoneEqual("The quick Brown fox jumps over the lazy dog", "The quick Brown fox jumps over the dog"));
    }

    private byte[] decode(String encoded) {
        return Hex.decodeHex(encoded.toCharArray());
    }

    private String caverphone(String input) {
        return new String(caverphone.encode(input.getBytes()));
    }
}