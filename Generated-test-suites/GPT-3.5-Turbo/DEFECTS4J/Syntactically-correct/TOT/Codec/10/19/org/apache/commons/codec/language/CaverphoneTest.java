import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {
    
    Caverphone caverphone = new Caverphone();
    
    @Test
    public void testCaverphone() {
        // Typical use case
        String result1 = caverphone.caverphone("example");
        Assert.assertEquals("AXMPL11111", result1);
        
        // Edge case: Empty string
        String result2 = caverphone.caverphone("");
        Assert.assertEquals("1111111111", result2);
        
        // Edge case: Null input
        String result3 = caverphone.caverphone(null);
        Assert.assertEquals("1111111111", result3);
        
        // Error scenario: String with special characters and numbers
        String result4 = caverphone.caverphone("!@#$123");
        Assert.assertEquals("1111111111", result4);
    }
    
    @Test
    public void testEncodeObject() throws EncoderException {
        // Typical use case
        Object result1 = caverphone.encode("example");
        Assert.assertEquals("AXMPL11111", result1);
        
        // Edge case: Empty string
        Object result2 = caverphone.encode("");
        Assert.assertEquals("1111111111", result2);
        
        // Edge case: Null input
        try {
            Object result3 = caverphone.encode(null);
        } catch (EncoderException e) {
            Assert.assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
        
        // Error scenario: Object of type other than java.lang.String
        try {
            Object result4 = caverphone.encode(123);
        } catch (EncoderException e) {
            Assert.assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }
    
    @Test
    public void testEncodeString() {
        // Typical use case
        String result1 = caverphone.encode("example");
        Assert.assertEquals("AXMPL11111", result1);
        
        // Edge case: Empty string
        String result2 = caverphone.encode("");
        Assert.assertEquals("1111111111", result2);
        
        // Edge case: Null input
        String result3 = caverphone.encode(null);
        Assert.assertEquals("1111111111", result3);
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        // Typical use case: Two identical strings
        boolean result1 = caverphone.isCaverphoneEqual("example", "example");
        Assert.assertTrue(result1);
        
        // Typical use case: Two different strings
        boolean result2 = caverphone.isCaverphoneEqual("example1", "example2");
        Assert.assertFalse(result2);
        
        // Edge case: Empty strings
        boolean result3 = caverphone.isCaverphoneEqual("", "");
        Assert.assertTrue(result3);
        
        // Edge case: Null strings
        boolean result4 = caverphone.isCaverphoneEqual(null, null);
        Assert.assertTrue(result4);
    }
}