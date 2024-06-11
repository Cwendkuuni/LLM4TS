package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CaverphoneTest {
    private Caverphone caverphone = new Caverphone();
    
    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("c2f", caverphone.caverphone("cough"));
        assertEquals("22", caverphone.caverphone("gh"));
        assertEquals("1f", caverphone.caverphone("f"));
        assertEquals("1111", caverphone.caverphone("a"));
        assertEquals("S", caverphone.caverphone("s"));
        assertEquals("F", caverphone.caverphone("f"));
        assertEquals("P", caverphone.caverphone("p"));
        assertEquals("K", caverphone.caverphone("k"));
        assertEquals("T", caverphone.caverphone("t"));
        assertEquals("2ch", caverphone.caverphone("ch"));
        assertEquals("22g", caverphone.caverphone("dg"));
        assertEquals("sio", caverphone.caverphone("tio"));
        assertEquals("sia", caverphone.caverphone("tia"));
    }
    
    @Test
    public void testEncode() throws EncoderException {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("c2f", caverphone.encode("cough"));
        assertEquals("22", caverphone.encode("gh"));
        assertEquals("1f", caverphone.encode("f"));
        assertEquals("1111", caverphone.encode("a"));
        assertEquals("S", caverphone.encode("s"));
        assertEquals("F", caverphone.encode("f"));
        assertEquals("P", caverphone.encode("p"));
        assertEquals("K", caverphone.encode("k"));
        assertEquals("T", caverphone.encode("t"));
        assertEquals("2ch", caverphone.encode("ch"));
        assertEquals("22g", caverphone.encode("dg"));
        assertEquals("sio", caverphone.encode("tio"));
        assertEquals("sia", caverphone.encode("tia"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("abc", "abc"));
        assertFalse(caverphone.isCaverphoneEqual("abc", "def"));
    }
}