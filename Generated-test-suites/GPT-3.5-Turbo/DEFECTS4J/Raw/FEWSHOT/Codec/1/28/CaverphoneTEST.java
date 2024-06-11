package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone(" "));
        
        assertEquals("1r3ll", caverphone.caverphone("Raul"));
        assertEquals("1r3w2n", caverphone.caverphone("Rowan"));
        assertEquals("1ls3", caverphone.caverphone("Liz"));
        assertEquals("1m2l3", caverphone.caverphone("Emily"));
        assertEquals("m2rwn", caverphone.caverphone("Marwen"));
        assertEquals("ks2nlt", caverphone.caverphone("Consolata"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("1111111111", caverphone.encode(" "));
        
        assertEquals("1r3ll", caverphone.encode("Raul"));
        assertEquals("1r3w2n", caverphone.encode("Rowan"));
        assertEquals("1ls3", caverphone.encode("Liz"));
        assertEquals("1m2l3", caverphone.encode("Emily"));
        assertEquals("m2rwn", caverphone.encode("Marwen"));
        assertEquals("ks2nlt", caverphone.encode("Consolata"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals(true, caverphone.isCaverphoneEqual(null, null));
        assertEquals(true, caverphone.isCaverphoneEqual("", ""));
        assertEquals(true, caverphone.isCaverphoneEqual(" ", " "));
        assertEquals(false, caverphone.isCaverphoneEqual(null, ""));
        assertEquals(false, caverphone.isCaverphoneEqual("", null));
        assertEquals(false, caverphone.isCaverphoneEqual(" ", null));
        assertEquals(false, caverphone.isCaverphoneEqual(null, " "));
        assertEquals(false, caverphone.isCaverphoneEqual("", " "));
        assertEquals(false, caverphone.isCaverphoneEqual(" ", ""));
        
        assertEquals(true, caverphone.isCaverphoneEqual("Raul", "Raul"));
        assertEquals(true, caverphone.isCaverphoneEqual("Rowan", "Rowan"));
        assertEquals(true, caverphone.isCaverphoneEqual("Liz", "Liz"));
        assertEquals(true, caverphone.isCaverphoneEqual("Emily", "Emily"));
        assertEquals(true, caverphone.isCaverphoneEqual("Marwen", "Marwen"));
        assertEquals(true, caverphone.isCaverphoneEqual("Consolata", "Consolata"));
        
        assertEquals(false, caverphone.isCaverphoneEqual("Raul", "Rowan"));
        assertEquals(false, caverphone.isCaverphoneEqual("Liz", "Emily"));
        assertEquals(false, caverphone.isCaverphoneEqual("Marwen", "Consolata"));
    }
}