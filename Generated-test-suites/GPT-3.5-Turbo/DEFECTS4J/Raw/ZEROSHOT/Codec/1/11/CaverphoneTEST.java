import org.junit.Test;
import org.junit.Assert;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string
        String result = caverphone.caverphone("");
        Assert.assertEquals("1111111111", result);

        // Test string with all non-alphabetic characters
        result = caverphone.caverphone("123!@#$%^&*()");
        Assert.assertEquals("1111111111", result);
        
        // Test string with lowercase and uppercase letters
        result = caverphone.caverphone("HelloWorld");
        Assert.assertEquals("H3LWR1111", result);
        
        // Test string with non-alphabetic characters
        result = caverphone.caverphone("Good Morning!");
        Assert.assertEquals("GDMNRN1111", result);
        
        // Test string with start options and replacements
        result = caverphone.caverphone("tough guy");
        Assert.assertEquals("TF46", result);
        
        // Test string with removals
        result = caverphone.caverphone("remove the 2s and 3s");
        Assert.assertEquals("RMVTH1111", result);
        
        // Test string with final e removal
        result = caverphone.caverphone("removee");
        Assert.assertEquals("RMV1111111", result);
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string
        try {
            Object result = caverphone.encode("");
            Assert.assertEquals("1111111111", result);
        } catch (EncoderException e) {
            Assert.fail("EncoderException thrown");
        }

        // Test non-string object
        try {
            Object obj = new Object();
            Object result = caverphone.encode(obj);
            Assert.fail("EncoderException not thrown");
        } catch (EncoderException e) {
            // Exception should be thrown
        }
        
        // Test string with lowercase and uppercase letters
        try {
            Object result = caverphone.encode("HelloWorld");
            Assert.assertEquals("H3LWR1111", result);
        } catch (EncoderException e) {
            Assert.fail("EncoderException thrown");
        }
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test identical strings
        boolean result = caverphone.isCaverphoneEqual("Hello", "Hello");
        Assert.assertTrue(result);
        
        // Test different strings
        result = caverphone.isCaverphoneEqual("Hello", "World");
        Assert.assertFalse(result);
        
        // Test string with lowercase and uppercase letters
        result = caverphone.isCaverphoneEqual("hello", "Hello");
        Assert.assertTrue(result);
    }

}