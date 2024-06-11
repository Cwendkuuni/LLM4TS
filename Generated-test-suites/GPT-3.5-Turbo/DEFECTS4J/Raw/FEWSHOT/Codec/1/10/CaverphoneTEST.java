import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("r111111111", caverphone.caverphone("r"));
        assertEquals("11r1111111", caverphone.caverphone("rr"));
        assertEquals("A111111111", caverphone.caverphone("a"));
        assertEquals("A1111A1A12", caverphone.caverphone("ay"));
        assertEquals("3111111111", caverphone.caverphone("e"));
        assertEquals("AWF1111111", caverphone.caverphone("awful"));
        assertEquals("2K31111111", caverphone.caverphone("cook"));
        assertEquals("2KA1111111", caverphone.caverphone("chook"));
        assertEquals("3131111111", caverphone.caverphone("i"));
        assertEquals("FY13513111", caverphone.caverphone("foreword"));
        assertEquals("PIG1111111", caverphone.caverphone("pity"));      
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("r111111111", caverphone.encode("r"));
        assertEquals("11r1111111", caverphone.encode("rr"));
        assertEquals("A111111111", caverphone.encode("a"));
        assertEquals("A1111A1A12", caverphone.encode("ay"));
        assertEquals("3111111111", caverphone.encode("e"));
        assertEquals("AWF1111111", caverphone.encode("awful"));
        assertEquals("2K31111111", caverphone.encode("cook"));
        assertEquals("2KA1111111", caverphone.encode("chook"));
        assertEquals("3131111111", caverphone.encode("i"));
        assertEquals("FY13513111", caverphone.encode("foreword"));
        assertEquals("PIG1111111", caverphone.encode("pity"));   
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("r111111111", caverphone.encode("r"));
        assertEquals("11r1111111", caverphone.encode("rr"));
        assertEquals("A111111111", caverphone.encode("a"));
        assertEquals("A1111A1A12", caverphone.encode("ay"));
        assertEquals("3111111111", caverphone.encode("e"));
        assertEquals("AWF1111111", caverphone.encode("awful"));
        assertEquals("2K31111111", caverphone.encode("cook"));
        assertEquals("2KA1111111", caverphone.encode("chook"));
        assertEquals("3131111111", caverphone.encode("i"));
        assertEquals("FY13513111", caverphone.encode("foreword"));
        assertEquals("PIG1111111", caverphone.encode("pity"));   
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertFalse(caverphone.isCaverphoneEqual("r", ""));
        assertTrue(caverphone.isCaverphoneEqual("r", "r"));
        assertFalse(caverphone.isCaverphoneEqual("r", "rr"));
        assertTrue(caverphone.isCaverphoneEqual("a", "a"));
        assertFalse(caverphone.isCaverphoneEqual("a", "ay"));
        assertTrue(caverphone.isCaverphoneEqual("e", "e"));
        assertTrue(caverphone.isCaverphoneEqual("awful", "awful"));
        assertFalse(caverphone.isCaverphoneEqual("awful", "cook"));
        assertFalse(caverphone.isCaverphoneEqual("chook", "cook"));
        assertTrue(caverphone.isCaverphoneEqual("i", "i"));
        assertTrue(caverphone.isCaverphoneEqual("foreword", "foreword"));
        assertFalse(caverphone.isCaverphoneEqual("foreword", "pity"));    
    }
}