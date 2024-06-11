import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {
    
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        
        assertEquals("T11111", caverphone.caverphone("Tough"));
        assertEquals("kawn", caverphone.caverphone("Cough"));
        
        assertEquals("ses", caverphone.caverphone("cE"));
        assertEquals("xample", caverphone.caverphone("Example"));
        
        assertEquals("ASASASASAS", caverphone.caverphone("aeiou"));
        assertEquals("HAMT2AM", caverphone.caverphone("Hambam"));
        
        assertEquals("example", caverphone.caverphone("exa!!!mple????"));
    }
    
    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        
        try {
            caverphone.encode(new Object());
            fail("Should throw EncoderException for non-string input");
        } catch (EncoderException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("HAMT2AM", caverphone.encode("Hambam"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        assertTrue(caverphone.isCaverphoneEqual("example", "!!!exa????mple"));
        assertFalse(caverphone.isCaverphoneEqual("apple", "banana"));
    }

}