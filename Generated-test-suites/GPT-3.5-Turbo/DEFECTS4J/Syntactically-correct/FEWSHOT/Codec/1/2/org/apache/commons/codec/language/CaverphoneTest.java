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
        assertEquals("1111111111", caverphone.caverphone(" "));
        assertEquals("kfkt111111", caverphone.caverphone("kafka"));
        assertEquals("1111111111", caverphone.caverphone("kafka "));
        assertEquals("HL22511111", caverphone.caverphone("Halloween"));
    }
    
    @Test
    public void testEncode() throws EncoderException {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("1111111111", caverphone.encode(" "));
        assertEquals("kfkt111111", caverphone.encode("kafka"));
        assertEquals("1111111111", caverphone.encode("kafka "));
        assertEquals("HL22511111", caverphone.encode("Halloween"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertFalse(caverphone.isCaverphoneEqual("", null));
        assertFalse(caverphone.isCaverphoneEqual(null, ""));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("kafka", "kafka"));
        assertFalse(caverphone.isCaverphoneEqual("kafka", "kafku"));
        assertTrue(caverphone.isCaverphoneEqual("Halloween", "Hall0wen"));
    }

}