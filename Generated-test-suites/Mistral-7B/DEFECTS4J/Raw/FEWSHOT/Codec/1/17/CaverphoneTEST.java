package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEncode() {
        Caverphone caverphone = new Caverphone();

        assertEquals("324567810111", caverphone.encode("Alice"));
        assertEquals("2357610111111", caverphone.encode("Bob"));
        assertEquals("111111111111", caverphone.encode(""));
        assertEquals("232323232323", caverphone.encode("Aaaa"));
        assertEquals("111111111111", caverphone.encode(null));

        try {
            caverphone.encode(new Integer(1));
            assert false;
        } catch (EncoderException e) {
            //Expected
        }
    }

    @Test
    public void testCaverphoneEncodeDecode() {
        Caverphone caverphone = new Caverphone();

        String original = "Alice";
        String encoded = caverphone.encode(original);
        String decoded = caverphone.encode(encoded);

        assertEquals(original, decoded);
    }

    @Test
    public void testCaverphoneIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        assertEquals(true, caverphone.isCaverphoneEqual("Alice", "Alice"));
        assertEquals(true, caverphone.isCaverphoneEqual("Bob", "Bob"));
        assertEquals(false, caverphone.isCaverphoneEqual("Alice", "Bob"));
    }
}