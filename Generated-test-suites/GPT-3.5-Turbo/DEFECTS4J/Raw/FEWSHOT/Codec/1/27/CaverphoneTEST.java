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
        assertEquals("k12121", caverphone.caverphone("k"));
        assertEquals("111121", caverphone.caverphone("k123"));
        assertEquals("phe", caverphone.caverphone("phone"));
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode((String) null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("k12121", caverphone.encode("k"));
        assertEquals("111121", caverphone.encode("k123"));
        assertEquals("phe", caverphone.encode("phone"));
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode((Object) null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("k12121", caverphone.encode("k"));
        assertEquals("111121", caverphone.encode("k123"));
        assertEquals("phe", caverphone.encode("phone"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertFalse(caverphone.isCaverphoneEqual("k", ""));
        assertFalse(caverphone.isCaverphoneEqual("k", "k123"));
        assertTrue(caverphone.isCaverphoneEqual("phone", "phone"));
    }
}