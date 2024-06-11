package org.apache.commons.codec.language;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("2222222222", caverphone.caverphone("hopeless"));
        assertEquals("1111111111", caverphone.caverphone("11111111"));
        assertEquals("ahltstnttlrs", caverphone.caverphone("AlphabetLetters"));
        assertEquals("ffnfnt", caverphone.caverphone("Venefant"));
        assertEquals("1111111111", caverphone.caverphone("Vowelsaeiou"));
        assertEquals("kwkt", caverphone.caverphone("Quick"));
        assertEquals("ASE", caverphone.caverphone("Apple"));
        assertEquals("WHAP", caverphone.caverphone("WHAP"));
        assertEquals("AMANANANANANANANAS", caverphone.caverphone("MmmmmmmmmM MmmmMmmmMmmMmm MmmmmmMMmmm"));
        assertEquals("2222222222", caverphone.caverphone("22222222"));
        assertEquals("AMANANANANANANANAS", caverphone.caverphone("MmmmmmmmmM MmmmMmmmMM mmmmmmMMmmm"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("2222222222", caverphone.encode("hopeless"));
        assertEquals("1111111111", caverphone.encode("11111111"));
        assertEquals("ahltstnttlrs", caverphone.encode("AlphabetLetters"));
        assertEquals("ffnfnt", caverphone.encode("Venefant"));
        assertEquals("1111111111", caverphone.encode("Vowelsaeiou"));
        assertEquals("kwkt", caverphone.encode("Quick"));
        assertEquals("ASE", caverphone.encode("Apple"));
        assertEquals("WHAP", caverphone.encode("WHAP"));
        assertEquals("AMANANANANANANANAS", caverphone.encode("MmmmmmmmmM MmmmMmmmMmmMmm MmmmmmMMmmm"));
        assertEquals("2222222222", caverphone.encode("22222222"));
        assertEquals("AMANANANANANANANAS", caverphone.encode("MmmmmmmmmM MmmmMmmmMM mmmmmmMMmmm"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals(true, caverphone.isCaverphoneEqual(null, null));
        assertEquals(false, caverphone.isCaverphoneEqual(null, ""));
        assertEquals(true, caverphone.isCaverphoneEqual("hopeless", "hopeless"));
        assertEquals(true, caverphone.isCaverphoneEqual("11111111", "11111111"));
        assertEquals(true, caverphone.isCaverphoneEqual("AlphabetLetters", "ahltstnttlrs"));
        assertEquals(false, caverphone.isCaverphoneEqual("Venefant", "Vowelsaeiou"));
        assertEquals(true, caverphone.isCaverphoneEqual("Quick", "kwkt"));
        assertEquals(true, caverphone.isCaverphoneEqual("Apple", "ASE"));
        assertEquals(true, caverphone.isCaverphoneEqual("WHAP", "WHAP"));
        assertEquals(true, caverphone.isCaverphoneEqual("MmmmmmmmmM MmmmMmmmMmmMmm MmmmmmMMmmm", "AMANANANANANANANAS"));
        assertEquals(true, caverphone.isCaverphoneEqual("22222222", "2222222222"));
        assertEquals(true, caverphone.isCaverphoneEqual("MmmmmmmmmM MmmmMmmmMM mmmmmmMMmmm", "AMANANANANANANANAS"));
    }
}