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
        assertEquals("ca", caverphone.caverphone("ca"));
        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("s", caverphone.caverphone("s"));
        assertEquals("p223233", caverphone.caverphone("python"));
        assertEquals("k2t3t3r", caverphone.caverphone("cater"));
        assertEquals("LANDS3ND", caverphone.caverphone("landsend"));
        assertEquals("O80T8G0", caverphone.caverphone("otago"));
    }

    @Test
    public void testEncode() {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("ca", caverphone.encode("ca"));
        assertEquals("rou2f", caverphone.encode("rough"));
        assertEquals("s", caverphone.encode("s"));
        assertEquals("p223233", caverphone.encode("python"));
        assertEquals("k2t3t3r", caverphone.encode("cater"));
        assertEquals("LANDS3ND", caverphone.encode("landsend"));
        assertEquals("O80T8G0", caverphone.encode("otago"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertFalse(caverphone.isCaverphoneEqual(null, ""));
        assertFalse(caverphone.isCaverphoneEqual("", null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertFalse(caverphone.isCaverphoneEqual("ca", "ka"));
        assertTrue(caverphone.isCaverphoneEqual("rough", "rouf"));
        assertFalse(caverphone.isCaverphoneEqual("s", "ss"));
        assertTrue(caverphone.isCaverphoneEqual("python", "pithon"));
        assertFalse(caverphone.isCaverphoneEqual("cater", "kater"));
        assertTrue(caverphone.isCaverphoneEqual("landsend", "landsenda"));
        assertTrue(caverphone.isCaverphoneEqual("otago", "otago"));
    }

}