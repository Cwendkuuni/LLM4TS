
package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("k111111111", caverphone.caverphone(null));
        assertEquals("k", caverphone.caverphone(""));
        assertEquals("k", caverphone.caverphone(" "));
        assertEquals("k1", caverphone.caverphone("1"));
        assertEquals("k2", caverphone.caverphone("2"));
        assertEquals("k3", caverphone.caverphone("3"));
        assertEquals("k4", caverphone.caverphone("4"));
        assertEquals("k5", caverphone.caverphone("5"));
        assertEquals("k6", caverphone.caverphone("6"));
        assertEquals("k7", caverphone.caverphone("7"));
        assertEquals("k8", caverphone.caverphone("8"));
        assertEquals("k9", caverphone.caverphone("9"));
        assertEquals("kaaaaakaaaa", caverphone.caverphone("AAAAAAAAAA"));
        assertEquals("kkkkkkkkkk", caverphone.caverphone("BBBBBBBBBB"));
        assertEquals("k2222222222", caverphone.caverphone("CCCCCCCCCC"));
        assertEquals("kkkkkkkk3a", caverphone.caverphone("BBBBBBBBBB3"));
        assertEquals("kkkkkkkkka", caverphone.caverphone("BBBBBBBBBB4"));
        assertEquals("kkkkkkkkky", caverphone.caverphone("BBBBBBBBBB6"));
        assertEquals("kkkk4u4ksy", caverphone.caverphone("BBBBBBBBBB6!"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        assertEquals("k", caverphone.encode(null));
        assertEquals("k", caverphone.encode(""));
        assertEquals("k", caverphone.encode(" "));
        assertEquals("k1", caverphone.encode("1"));
        assertEquals("k2", caverphone.encode("2"));
        assertEquals("k3", caverphone.encode("3"));
        assertEquals("k4", caverphone.encode("4"));
        assertEquals("k5", caverphone.encode("5"));
        assertEquals("k6", caverphone.encode("6"));
        assertEquals("k7", caverphone.encode("7"));
        assertEquals("k8", caverphone.encode("8"));
        assertEquals("k9", caverphone.encode("9"));
        assertEquals("kaaaaakaaaa", caverphone.encode("AAAAAAAAAA"));
        assertEquals("kkkkkkkkkk", caverphone.encode("BBBBBBBBBB"));
        assertEquals("k2222222222", caverphone.encode("CCCCCCCCCC"));
        assertEquals("kkkkkkkk3a", caverphone.encode("BBBBBBBBBB3"));
        assertEquals("kkkkkkkkka", caverphone.encode("BBBBBBBBBB4"));
        assertEquals("kkkkkkkkky", caverphone.encode("BBBBBBBBBB6"));
        assertEquals("kkkk4u4ksy", caverphone.encode("BBBBBBBBBB6!"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertEquals(true, caverphone.isCaverphoneEqual(null, null));
        assertEquals(true, caverphone.isCaverphoneEqual("", ""));
        assertEquals(true, caverphone.isCaverphoneEqual(" ", " "));
        assertEquals(false, caverphone.isCaverphoneEqual("", " "));
        assertEquals(true, caverphone.isCaverphoneEqual("AAAAAAAAAA", "AAAAAAAAAA"));
        assertEquals(false, caverphone.isCaverphoneEqual("AAAAAAAAAA", "BBBBBBBBBB"));
        assertEquals(false, caverphone.isCaverphoneEqual("BBBBBBBBBB", "CCCCCCCCCC"));
        assertEquals(true, caverphone.isCaverphoneEqual("BBBBBBBBBB3", "BBBBBBBBBB3"));
        assertEquals(false, caverphone.isCaverphoneEqual("BBBBBBBBBB3", "BBBBBBBBBB4"));
        assertEquals(false, caverphone.isCaverphoneEqual("BBBBBBBBBB4", "BBBBBBBBBB6"));
        assertEquals(false, caverphone.isCaverphoneEqual("BBBBBBBBBB6!", "BBBBBBBBBB6"));
    }
}
