package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaverphoneTest {
    
    private final Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("333333", caverphone.caverphone("abcde12345"));
        assertEquals("tst", caverphone.caverphone("test"));
        assertEquals("t3st", caverphone.caverphone("Test"));
        assertEquals("TST", caverphone.caverphone("TEST"));
        assertEquals("KT", caverphone.caverphone("cat"));
        assertEquals("KPATA", caverphone.caverphone("capita"));
        assertEquals("KS4", caverphone.caverphone("catheter"));
        assertEquals("S", caverphone.caverphone("sss"));
        assertEquals("T", caverphone.caverphone("ttt"));
        assertEquals("P", caverphone.caverphone("ppp"));
        assertEquals("K", caverphone.caverphone("kkk"));
        assertEquals("F", caverphone.caverphone("fff"));
        assertEquals("M", caverphone.caverphone("mmm"));
        assertEquals("N", caverphone.caverphone("nnn"));
        assertEquals("WA", caverphone.caverphone("wawawa"));
        assertEquals("WhA", caverphone.caverphone("whawhawa"));
        assertEquals("I", caverphone.caverphone("hi"));
        assertEquals("IW", caverphone.caverphone("hiw"));
        assertEquals("R3", caverphone.caverphone("r"));
        assertEquals("2", caverphone.caverphone("l"));
        assertEquals("A", caverphone.caverphone("yyy"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeObjectInvalidInput() throws EncoderException {
        caverphone.encode(12345);
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        assertEquals("1111111111", caverphone.encode((Object) null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("tst", caverphone.encode("test"));
    }

    @Test
    public void testEncode() {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("tst", caverphone.encode("test"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertEquals(true, caverphone.isCaverphoneEqual("", ""));
        assertEquals(false, caverphone.isCaverphoneEqual("test", ""));
        assertEquals(false, caverphone.isCaverphoneEqual("", "test"));
        assertEquals(true, caverphone.isCaverphoneEqual("test", "tst"));
    }

}