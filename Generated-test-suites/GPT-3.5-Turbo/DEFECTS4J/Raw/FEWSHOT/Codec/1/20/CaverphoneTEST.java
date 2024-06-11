package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.junit.Before;
import org.junit.Test;

public class CaverphoneTest {
    
    private Caverphone caverphone;

    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("kfhksn", caverphone.caverphone("Caverphone"));
        assertEquals("SMN", caverphone.caverphone("sMn"));
        assertEquals("AMFN", caverphone.caverphone("afmN2"));
        assertEquals("pM", caverphone.caverphone("pmmmmmmmmm"));
        assertEquals("K2NTLF", caverphone.caverphone("Konetelufe"));
        assertEquals("kh3223A", caverphone.caverphone("Quackenzie"));
        assertEquals("tpfxw3333A", caverphone.caverphone("tripfixwellington"));
        assertEquals("TRPA3A", caverphone.caverphone("tripa3A"));
        assertEquals("11A1111111", caverphone.caverphone("11A1111111"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() throws EncoderException {
        caverphone.encode(123);
    }

    @Test
    public void testEncode() {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("kfhksn", caverphone.encode("Caverphone"));
        assertEquals("SMN", caverphone.encode("sMn"));
        assertEquals("AMFN", caverphone.encode("afmN2"));
        assertEquals("pM", caverphone.encode("pmmmmmmmmm"));
        assertEquals("K2NTLF", caverphone.encode("Konetelufe"));
        assertEquals("kh3223A", caverphone.encode("Quackenzie"));
        assertEquals("tpfxw3333A", caverphone.encode("tripfixwellington"));
        assertEquals("TRPA3A", caverphone.encode("tripa3A"));
        assertEquals("11A1111111", caverphone.encode("11A1111111"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("Caverphone", "Caverphone"));
        assertTrue(caverphone.isCaverphoneEqual("sMn", "sMn"));
        assertTrue(caverphone.isCaverphoneEqual("afmN2", "afmN2"));
        assertTrue(caverphone.isCaverphoneEqual("pmmmmmmmmm", "pmmmmmmmmm"));
        assertTrue(caverphone.isCaverphoneEqual("Konetelufe", "Konetelufe"));
        assertTrue(caverphone.isCaverphoneEqual("Quackenzie", "Quackenzie"));
        assertTrue(caverphone.isCaverphoneEqual("tripfixwellington", "tripfixwellington"));
        assertTrue(caverphone.isCaverphoneEqual("tripa3A", "tripa3A"));
        assertTrue(caverphone.isCaverphoneEqual("11A1111111", "11A1111111"));
        assertFalse(caverphone.isCaverphoneEqual("Caverphone", "Caverphon"));
        assertFalse(caverphone.isCaverphoneEqual("sMn", "smN"));
        assertFalse(caverphone.isCaverphoneEqual("afmN2", "afmn2"));
        assertFalse(caverphone.isCaverphoneEqual("pmmmmmmmmm", "pppppppppp"));
        assertFalse(caverphone.isCaverphoneEqual("Konetelufe", "k0enetelufe"));
        assertFalse(caverphone.isCaverphoneEqual("Quackenzie", "quackenzie"));
        assertFalse(caverphone.isCaverphoneEqual("tripfixwellington", "tr1pfixwellington"));
        assertFalse(caverphone.isCaverphoneEqual("tripa3A", "tripa2A"));
        assertFalse(caverphone.isCaverphoneEqual("11A1111111", "11A111111"));
    }

}