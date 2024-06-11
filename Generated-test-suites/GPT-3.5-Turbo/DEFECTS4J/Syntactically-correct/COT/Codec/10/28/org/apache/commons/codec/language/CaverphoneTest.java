
import org.junit.Test;
import org.junit.Assert;

public class CaverphoneTest {
    
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test case 1: Null input
        String result1 = caverphone.caverphone(null);
        Assert.assertEquals("1111111111", result1);
        
        // Test case 2: Empty input
        String result2 = caverphone.caverphone("");
        Assert.assertEquals("1111111111", result2);
        
        // Test case 3: Valid input
        String result3 = caverphone.caverphone("hello");
        Assert.assertEquals("HLL1111111", result3);
        
        // Add more test cases for different inputs
        
    }
    
    @Test(expected = EncoderException.class)
    public void testEncode_Object_Exception() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123); // Test case for non-string input
    }
    
    @Test
    public void testEncode_String() {
        Caverphone caverphone = new Caverphone();
        
        // Test case 1: Null input
        String result1 = caverphone.encode(null);
        Assert.assertEquals("1111111111", result1);
        
        // Test case 2: Empty input
        String result2 = caverphone.encode("");
        Assert.assertEquals("1111111111", result2);
        
        // Test case 3: Valid input
        String result3 = caverphone.encode("hello");
        Assert.assertEquals("HLL1111111", result3);
        
        // Add more test cases for different inputs
        
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test case 1: Equal inputs
        boolean result1 = caverphone.isCaverphoneEqual("hello", "hello");
        Assert.assertTrue(result1);
        
        // Test case 2: Non-equal inputs
        boolean result2 = caverphone.isCaverphoneEqual("hello", "world");
        Assert.assertFalse(result2);
        
        // Add more test cases for different inputs
        
    }
    
}

