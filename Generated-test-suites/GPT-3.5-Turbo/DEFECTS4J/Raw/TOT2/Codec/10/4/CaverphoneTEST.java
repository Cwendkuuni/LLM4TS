import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {
    
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        
        assertEquals("1111111111", caverphone.caverphone("123"));
        assertEquals("KKKK", caverphone.caverphone("K!@#k"));
        
        assertEquals("T3S2", caverphone.caverphone("test"));
        assertEquals("R0U2F", caverphone.caverphone("rough"));
        
        assertEquals("SWS", caverphone.caverphone("sws"));
        assertEquals("T3S2", caverphone.caverphone("t!es2t"));
        
        assertEquals("xxxxxx3111", caverphone.caverphone("xxxxxx"));
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("T3S2", caverphone.encode("test"));
        assertEquals("R0U2F", caverphone.encode("rough"));
        
        assertEquals("xxxxxx3111", caverphone.encode("xxxxxx"));
        
        try {
            caverphone.encode(new Integer(123));
            fail("Should have thrown an EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        assertTrue(caverphone.isCaverphoneEqual("test", "test"));
        assertTrue(caverphone.isCaverphoneEqual("ea", "ea"));
        
        assertFalse(caverphone.isCaverphoneEqual("test", "toast"));
        assertFalse(caverphone.isCaverphoneEqual("apple", "orange"));
    }

}