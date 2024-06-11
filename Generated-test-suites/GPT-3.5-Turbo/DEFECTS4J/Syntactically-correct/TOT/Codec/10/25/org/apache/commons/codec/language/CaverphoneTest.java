import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case: test with a valid string
        assertEquals("2222", caverphone.caverphone("test"));
        
        // Edge case: test with an empty string
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Edge case: test with a null string
        assertEquals("1111111111", caverphone.caverphone(null));
        
        // Error scenario: test with a string containing non-alphabetic characters
        assertEquals("2222", caverphone.caverphone("test123"));
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case: test with a valid string
        assertEquals("2222", caverphone.encode("test"));
        
        // Edge case: test with an empty string
        assertEquals("1111111111", caverphone.encode(""));
        
        // Edge case: test with a null string
        assertNull(caverphone.encode(null));
        
        // Error scenario: test with a non-string object
        try {
            caverphone.encode(new Integer(123));
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected: do nothing
        }
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case: test with two identical strings
        assertTrue(caverphone.isCaverphoneEqual("test", "test"));
        
        // Typical use case: test with two different strings
        assertFalse(caverphone.isCaverphoneEqual("test", "hello"));
        
        // Edge case: test with two empty strings
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        
        // Edge case: test with two null strings
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        
        // Error scenario: test with one null string and one non-null string
        assertFalse(caverphone.isCaverphoneEqual(null, "test"));
        
        // Error scenario: test with one non-null string and one null string
        assertFalse(caverphone.isCaverphoneEqual("test", null));
    }
}