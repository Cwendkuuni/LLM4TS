package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone(" "));
        assertEquals("ca11111111", caverphone.caverphone("caverphone"));
        assertEquals("proh111111", caverphone.caverphone("proh1234"));
        assertEquals("wot1121111", caverphone.caverphone("wotcha123"));
        assertEquals("wotsA11111", caverphone.caverphone("wotsup1234"));
        assertEquals("l3r3r3r3r", caverphone.caverphone("abcdefghijklmnopqrstuvwxyz"));
    }

    @Test
    public void testEncode_Object() throws EncoderException {
        Caverphone caverphone = new Caverphone();

        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("1111111111", caverphone.encode(" "));
        assertEquals("ca11111111", caverphone.encode("caverphone"));
        assertEquals("proh111111", caverphone.encode("proh1234"));
        assertEquals("wot1121111", caverphone.encode("wotcha123"));
        assertEquals("wotsA11111", caverphone.encode("wotsup1234"));
        assertEquals("l3r3r3r3r", caverphone.encode("abcdefghijklmnopqrstuvwxyz"));
    }

    @Test
    public void testEncode_String() {
        Caverphone caverphone = new Caverphone();

        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("1111111111", caverphone.encode(" "));
        assertEquals("ca11111111", caverphone.encode("caverphone"));
        assertEquals("proh111111", caverphone.encode("proh1234"));
        assertEquals("wot1121111", caverphone.encode("wotcha123"));
        assertEquals("wotsA11111", caverphone.encode("wotsup1234"));
        assertEquals("l3r3r3r3r", caverphone.encode("abcdefghijklmnopqrstuvwxyz"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual(" ", " "));
        assertTrue(caverphone.isCaverphoneEqual("caverphone", "caverphone"));
        assertFalse(caverphone.isCaverphoneEqual("caverphone", "caverphon1"));
        assertFalse(caverphone.isCaverphoneEqual("caverphone", null));
        assertFalse(caverphone.isCaverphoneEqual(null, "caverphone"));
    }   
}