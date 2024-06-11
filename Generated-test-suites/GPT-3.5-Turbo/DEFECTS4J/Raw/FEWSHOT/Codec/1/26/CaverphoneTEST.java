package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("mas", caverphone.caverphone("mas"));
        assertEquals("kahb3ts", caverphone.caverphone("coughbets"));
        assertEquals("2j3n2n", caverphone.caverphone("janean"));
        assertEquals("dakt3r", caverphone.caverphone("doctor"));
        assertEquals("dakt3", caverphone.caverphone("doct"));
        assertEquals("miss3t2r", caverphone.caverphone("misster"));
        assertEquals("accs", caverphone.caverphone("access"));
        assertEquals("accss", caverphone.caverphone("accesss"));
        assertEquals("3ss", caverphone.caverphone("ess"));
        assertEquals("ss", caverphone.caverphone("ss"));
        assertEquals("33k", caverphone.caverphone("kek"));
        assertEquals("33k,3n1v3r2s225s3", caverphone.caverphone("kekkeneverseas"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("mas", caverphone.encode("mas"));
        assertEquals("kahb3ts", caverphone.encode("coughbets"));
        assertEquals("2j3n2n", caverphone.encode("janean"));
        assertEquals("dakt3r", caverphone.encode("doctor"));
        assertEquals("dakt3", caverphone.encode("doct"));
        assertEquals("miss3t2r", caverphone.encode("misster"));
        assertEquals("accs", caverphone.encode("access"));
        assertEquals("accss", caverphone.encode("accesss"));
        assertEquals("3ss", caverphone.encode("ess"));
        assertEquals("ss", caverphone.encode("ss"));
        assertEquals("33k", caverphone.encode("kek"));
        assertEquals("33k,3n1v3r2s225s3", caverphone.encode("kekkeneverseas"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertEquals(true, caverphone.isCaverphoneEqual(null, null));
        assertEquals(false, caverphone.isCaverphoneEqual(null, ""));
        assertEquals(false, caverphone.isCaverphoneEqual("", null));
        assertEquals(true, caverphone.isCaverphoneEqual("", ""));
        assertEquals(true, caverphone.isCaverphoneEqual("mas", "mas"));
        assertEquals(false, caverphone.isCaverphoneEqual("mas", "mas1"));
    }

}