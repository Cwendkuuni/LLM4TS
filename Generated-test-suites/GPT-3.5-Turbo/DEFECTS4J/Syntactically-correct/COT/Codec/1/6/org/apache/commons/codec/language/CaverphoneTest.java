import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Basic test case
        String result = caverphone.caverphone("example");
        assertEquals("expml", result);
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Basic test case
        Object result = caverphone.encode("example");
        assertEquals("expml", result);
    }

    @Test
    public void testEncodeException() {
        Caverphone caverphone = new Caverphone();
        
        // Test EncoderException when parameter is not a string
        Object obj = new Object();
        try {
            Object result = caverphone.encode(obj);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Successfully caught EncoderException
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test equal caverphones
        boolean result1 = caverphone.isCaverphoneEqual("example1", "example2");
        assertTrue(result1);
        
        // Test different caverphones
        boolean result2 = caverphone.isCaverphoneEqual("example1", "different");
        assertFalse(result2);
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string input
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }
    
    @Test
    public void testCaverphoneNullString() {
        Caverphone caverphone = new Caverphone();
        
        // Test null input
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }
    
    @Test
    public void testCaverphoneEdgeCases() {
        Caverphone caverphone = new Caverphone();
        
        // Test edge cases
        String result1 = caverphone.caverphone("coughing");
        assertEquals("coufn", result1);
        
        String result2 = caverphone.caverphone("enough");
        assertEquals("en", result2);
        
        String result3 = caverphone.caverphone("gn");
        assertEquals("2n", result3);
        
        String result4 = caverphone.caverphone("mb");
        assertEquals("m2", result4);
    }
    
    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testEncodeEmptyString() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string input
        String result = caverphone.encode("");
        assertEquals("1111111111", result);
    }
    
    @Test
    public void testEncodeNullString() {
        Caverphone caverphone = new Caverphone();
        
        // Test null input
        String result = caverphone.encode(null);
        assertEquals("1111111111", result);
    }
    
    @Test
    public void testEncodeEdgeCases() {
        Caverphone caverphone = new Caverphone();
        
        // Test edge cases
        String result1 = caverphone.encode("coughing");
        assertEquals("coufn", result1);
        
        String result2 = caverphone.encode("enough");
        assertEquals("en", result2);
        
        String result3 = caverphone.encode("gn");
        assertEquals("2n", result3);
        
        String result4 = caverphone.encode("mb");
        assertEquals("m2", result4);
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testIsCaverphoneEqualSameString() {
        Caverphone caverphone = new Caverphone();
        
        // Test same strings
        boolean result = caverphone.isCaverphoneEqual("example", "example");
        assertTrue(result);
    }
    
    // Test all public methods
    
    @Test
    public void testCaverphonePublicMethods() {
        Caverphone caverphone = new Caverphone();
        
        // Test all public methods
        // caverphone(String txt)
        String result1 = caverphone.caverphone("test");
        assertEquals("test", result1);

        // encode(Object pObject)
        Object obj = "test";
        try {
            Object result2 = caverphone.encode(obj);
            assertEquals("test", result2);
        } catch (EncoderException e) {
            fail("Unexpected EncoderException");
        }

        // encode(String pString)
        String result3 = caverphone.encode("test");
        assertEquals("test", result3);

        // isCaverphoneEqual(String str1, String str2)
        boolean result4 = caverphone.isCaverphoneEqual("test1", "test2");
        assertFalse(result4);
    }
}