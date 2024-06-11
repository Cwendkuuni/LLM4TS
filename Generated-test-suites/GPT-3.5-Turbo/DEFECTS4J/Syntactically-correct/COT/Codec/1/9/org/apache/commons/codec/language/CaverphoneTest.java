import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Basic functionality test
        String result = caverphone.caverphone("test");
        assertEquals("1111111111", result);
        
        // Test with empty string
        result = caverphone.caverphone("");
        assertEquals("1111111111", result);
        
        // Test with null input
        result = caverphone.caverphone(null);
        assertEquals("1111111111", result); 
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        
        // Basic functionality test
        Object result = caverphone.encode("test");
        assertEquals("1111111111", result);
        
        // Test with empty string
        result = caverphone.encode("");
        assertEquals("1111111111", result);
        
        // Test with null input
        try {
            result = caverphone.encode(null);
            fail("EncoderException should have been thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
        
        // Test with non-string input
        try {
            result = caverphone.encode(123);
            fail("EncoderException should have been thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test with equal strings
        boolean result = caverphone.isCaverphoneEqual("test", "test");
        assertTrue(result);
        
        // Test with different strings
        result = caverphone.isCaverphoneEqual("test", "Test");
        assertFalse(result);
    }
    
}