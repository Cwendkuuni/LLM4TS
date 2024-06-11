import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone(" "));
        assertEquals("1111111111", caverphone.caverphone("  "));
        assertEquals("1111111111", caverphone.caverphone("   "));
        assertEquals("1111111111", caverphone.caverphone("    "));
        
        assertEquals("1111111111", caverphone.caverphone("a"));
        assertEquals("1111111111", caverphone.caverphone("ab"));
        assertEquals("1111111111", caverphone.caverphone("abc"));
        
        assertEquals("1111111111", caverphone.caverphone("abcdef"));
        assertEquals("1111111111", caverphone.caverphone("abcdefg"));
        assertEquals("1111111111", caverphone.caverphone("abcdefgh"));
        assertEquals("1111111111", caverphone.caverphone("abcdefghi"));
        assertEquals("1111111111", caverphone.caverphone("abcdefghij"));
        assertEquals("1111111111", caverphone.caverphone("abcdefghijk"));
        
        assertEquals("K111111111", caverphone.caverphone("k"));
        assertEquals("KA11111111", caverphone.caverphone("ka"));
        assertEquals("K1111111A11", caverphone.caverphone("kA"));
        assertEquals("K1111111A11", caverphone.caverphone("Ka"));
        assertEquals("K1111111A11", caverphone.caverphone("KA"));
        
        assertEquals("K2", caverphone.caverphone("ck"));
        assertEquals("K2A", caverphone.caverphone("cka"));
        assertEquals("KT", caverphone.caverphone("ckt"));
        
        assertEquals("S2A", caverphone.caverphone("sha"));
        assertEquals("984Q4", caverphone.caverphone("984Q4"));
        
        assertEquals("Y2A", caverphone.caverphone("ya"));
        assertEquals("Y3", caverphone.caverphone("y"));
        
        assertEquals("1234567890", caverphone.caverphone("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("1111K", caverphone.caverphone("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals(true, caverphone.isCaverphoneEqual(null, null));
        assertEquals(true, caverphone.isCaverphoneEqual("", ""));
        assertEquals(true, caverphone.isCaverphoneEqual(" ", " "));
        assertEquals(true, caverphone.isCaverphoneEqual("  ", "  "));
        assertEquals(true, caverphone.isCaverphoneEqual("   ", "   "));
        assertEquals(true, caverphone.isCaverphoneEqual("    ", "    "));
        
        assertEquals(false, caverphone.isCaverphoneEqual(null, ""));
        assertEquals(false, caverphone.isCaverphoneEqual("", " "));
        assertEquals(false, caverphone.isCaverphoneEqual(" ", "  "));
        assertEquals(false, caverphone.isCaverphoneEqual("  ", "   "));
        assertEquals(false, caverphone.isCaverphoneEqual("   ", "    "));
        
        assertEquals(true, caverphone.isCaverphoneEqual("abc", "abc"));
        assertEquals(true, caverphone.isCaverphoneEqual("abc", "ABC"));
        
        assertEquals(false, caverphone.isCaverphoneEqual("abc", "def"));
        assertEquals(false, caverphone.isCaverphoneEqual("abc", "ABCDEF"));
    }
}