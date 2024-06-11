package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone(" "));
        assertEquals("1111111111", caverphone.caverphone("$$#"));
        assertEquals("1111111111", caverphone.caverphone("12345"));
        assertEquals("A111111111", caverphone.caverphone("ABCdef"));
        assertEquals("S111111111", caverphone.caverphone("s!@#*()SDF"));
        assertEquals("KKK1111111", caverphone.caverphone("kkkPPPqqq"));
        assertEquals("TTTTTTTTTT", caverphone.caverphone("tttttttttt"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(null);
        } catch (EncoderException e) {
            assertTrue(true);
        }
        
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("A111111111", caverphone.encode("ABCdef"));
        assertEquals("S111111111", caverphone.encode("s!@#*()SDF"));
        assertEquals("KKK1111111", caverphone.encode("kkkPPPqqq"));
        assertEquals("TTTTTTTTTT", caverphone.encode("tttttttttt"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertFalse(caverphone.isCaverphoneEqual(null, ""));
        assertFalse(caverphone.isCaverphoneEqual("", null));
        
        assertTrue(caverphone.isCaverphoneEqual("ABCdef", "abcDEF"));
        assertTrue(caverphone.isCaverphoneEqual("abcDEF", "abcDEF"));
        assertFalse(caverphone.isCaverphoneEqual("ABCdef", "s!@#*()SDF"));
        assertFalse(caverphone.isCaverphoneEqual("s!@#*()SDF", "000"));
    }
}