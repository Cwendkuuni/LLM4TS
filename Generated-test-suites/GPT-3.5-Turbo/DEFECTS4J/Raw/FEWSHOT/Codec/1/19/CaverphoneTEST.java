package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("4111111111", caverphone.caverphone("a"));
        assertEquals("22r4111111", caverphone.caverphone("ar"));
        assertEquals("KA41111111", caverphone.caverphone("kar"));
        assertEquals("KA41111111", caverphone.caverphone("kar1"));
        assertEquals("KA41111111", caverphone.caverphone("kar11"));
        assertEquals("JA22", caverphone.caverphone("jar"));
        assertEquals("SMN223KM1111", caverphone.caverphone("sAmnqTest!"));
    }

    @Test
    public void testEncode() {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("4111111111", caverphone.encode("a"));
        assertEquals("22r4111111", caverphone.encode("ar"));
        assertEquals("KA41111111", caverphone.encode("kar"));
        assertEquals("KA41111111", caverphone.encode("kar1"));
        assertEquals("KA41111111", caverphone.encode("kar11"));
        assertEquals("JA22", caverphone.encode("jar"));
        assertEquals("SMN223KM1111", caverphone.encode("sAmnqTest!"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertEquals(true, caverphone.isCaverphoneEqual(null, null));
        assertEquals(true, caverphone.isCaverphoneEqual("", ""));
        assertEquals(false, caverphone.isCaverphoneEqual("a", ""));
        assertEquals(true, caverphone.isCaverphoneEqual("kar", "kar"));
        assertEquals(true, caverphone.isCaverphoneEqual("jar", "jar"));
        assertEquals(false, caverphone.isCaverphoneEqual("kar", "jar"));
    }

}