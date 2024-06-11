import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {
    
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Normal input
        assertEquals("KST", caverphone.caverphone("test"));
        assertEquals("KR1234", caverphone.caverphone("example"));
        
        // Edge cases
        assertEquals("K", caverphone.caverphone("t"));
        assertEquals("1231231111", caverphone.caverphone("123$%^&*()"));
        assertEquals("RH3AF7", caverphone.caverphone("rhythm"));
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Normal input
        assertEquals("KST", caverphone.encode("test"));
        assertEquals("KR1234", caverphone.encode("example"));
        
        // Error scenario
        try {
            caverphone.encode(123);
            fail("Should have thrown an EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // True case
        assertTrue(caverphone.isCaverphoneEqual("apple", "appel"));
        
        // False case
        assertFalse(caverphone.isCaverphoneEqual("example", "sample"));
        
        // Edge case
        assertTrue(caverphone.isCaverphoneEqual("", ""));
    }
}