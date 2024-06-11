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
        assertEquals("1111111111", caverphone.caverphone("    "));
        assertEquals("kp", caverphone.caverphone("coup"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("1111111111", caverphone.encode("    "));
        assertEquals("kp", caverphone.encode("coup"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("    ", "    "));
        assertTrue(caverphone.isCaverphoneEqual("coup", "coup"));
        assertFalse(caverphone.isCaverphoneEqual(null, ""));
        assertFalse(caverphone.isCaverphoneEqual("", "    "));
        assertFalse(caverphone.isCaverphoneEqual("coup", "coOp"));
    }
}