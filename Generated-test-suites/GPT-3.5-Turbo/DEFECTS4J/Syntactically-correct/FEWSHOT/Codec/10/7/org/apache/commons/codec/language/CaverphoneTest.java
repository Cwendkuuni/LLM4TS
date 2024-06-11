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
        assertEquals("k11y792", caverphone.caverphone("coughy"));
        assertEquals("ruc7", caverphone.caverphone("rough"));
        assertEquals("111111", caverphone.caverphone("enough"));
        assertEquals("trou2f", caverphone.caverphone("trough"));
        assertEquals("l2323", caverphone.caverphone("length"));
        assertEquals("7312", caverphone.caverphone("troughy"));
    }

    @Test
    public void testEncode() {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("1111111111", caverphone.encode(" "));
        assertEquals("k11y792", caverphone.encode("coughy"));
        assertEquals("ruc7", caverphone.encode("rough"));
        assertEquals("111111", caverphone.encode("enough"));
        assertEquals("trou2f", caverphone.encode("trough"));
        assertEquals("k23412", caverphone.encode("koughy"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() throws EncoderException {
        caverphone.encode(123);
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("coughy", "coughy"));
        assertFalse(caverphone.isCaverphoneEqual("rough", "tough"));
        assertFalse(caverphone.isCaverphoneEqual("length", "width"));
    }

}