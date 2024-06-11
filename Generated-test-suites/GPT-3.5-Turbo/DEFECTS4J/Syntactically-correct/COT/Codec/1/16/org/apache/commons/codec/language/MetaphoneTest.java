
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {
    
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Test cases for basic functionality
        Assert.assertEquals("SM", metaphone.metaphone("sum"));
        Assert.assertEquals("PT", metaphone.metaphone("pot"));
        
        // Test cases for edge cases and exceptions
        Assert.assertEquals("", metaphone.metaphone(""));
        Assert.assertEquals("X", metaphone.metaphone("x"));
        
        // Test cases for additional edge cases and exceptions
        Assert.assertEquals("MTFN", metaphone.metaphone("metaphone"));
        Assert.assertEquals("XKM", metaphone.metaphone("xk"));
        Assert.assertEquals("JN", metaphone.metaphone("john"));
    }
    
    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Test cases for basic functionality
        Assert.assertEquals("SM", metaphone.encode("sum"));
        Assert.assertEquals("PT", metaphone.encode("pot"));
        
        // Test cases for edge cases and exceptions
        Assert.assertEquals("", metaphone.encode(""));
        Assert.assertEquals("X", metaphone.encode("x"));
        
        // Test cases for additional edge cases and exceptions
        Assert.assertEquals("MTFN", metaphone.encode("metaphone"));
        Assert.assertEquals("XKM", metaphone.encode("xk"));
        Assert.assertEquals("JN", metaphone.encode("john"));
    }
    
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Test cases for basic functionality
        Assert.assertTrue(metaphone.isMetaphoneEqual("sum", "some"));
        Assert.assertTrue(metaphone.isMetaphoneEqual("pot", "pat"));
        
        // Test cases for edge cases and exceptions
        Assert.assertTrue(metaphone.isMetaphoneEqual("", ""));
        Assert.assertTrue(metaphone.isMetaphoneEqual("x", "x"));
        
        // Test cases for additional edge cases and exceptions
        Assert.assertTrue(metaphone.isMetaphoneEqual("metaphone", "metaphone"));
        Assert.assertTrue(metaphone.isMetaphoneEqual("xk", "xk"));
        Assert.assertFalse(metaphone.isMetaphoneEqual("sum", "pot"));
    }
    
    @Test(expected = EncoderException.class)
    public void testEncodeException() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        
        metaphone.encode(1234);
    }
    
}
