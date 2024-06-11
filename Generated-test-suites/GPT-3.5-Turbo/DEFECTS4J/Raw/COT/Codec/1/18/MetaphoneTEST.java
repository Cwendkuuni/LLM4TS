
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Test empty string
        String emptyResult = metaphone.metaphone("");
        Assert.assertEquals("", emptyResult);
        
        // Test single character
        String singleCharResult = metaphone.metaphone("a");
        Assert.assertEquals("A", singleCharResult);
        
        // Test input with only characters in the A - Z range
        String validResult = metaphone.metaphone("Test");
        Assert.assertEquals("TST", validResult);
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        
        // Test non-string object
        try {
            metaphone.encode(123);
            Assert.fail("Expected EncoderException not thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
        
        // Test string object
        String encodedResult = metaphone.encode("Test");
        Assert.assertEquals("TST", encodedResult);
    }
    
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Test equal metaphones
        boolean equalResult = metaphone.isMetaphoneEqual("Test", "Tst");
        Assert.assertTrue(equalResult);
        
        // Test different metaphones
        boolean notEqualResult = metaphone.isMetaphoneEqual("Test", "Best");
        Assert.assertFalse(notEqualResult);
    }

    // Additional test cases for edge cases and exception handling
    
    @Test
    public void testMetaphoneWithNull() {
        Metaphone metaphone = new Metaphone();
        
        // Test null string
        String nullResult = metaphone.metaphone(null);
        Assert.assertEquals("", nullResult);
    }
    
    @Test
    public void testEncodeWithNull() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        
        // Test null string
        String nullResult = (String)metaphone.encode(null);
        Assert.assertEquals("", nullResult);
    }
    
    @Test
    public void testIsMetaphoneEqualWithNull() {
        Metaphone metaphone = new Metaphone();
        
        // Test null strings
        boolean nullResult = metaphone.isMetaphoneEqual(null, null);
        Assert.assertTrue(nullResult);
        
        boolean oneNullResult = metaphone.isMetaphoneEqual(null, "Test");
        Assert.assertFalse(oneNullResult);
    }
    
    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Test default maxCodeLen value (4)
        int defaultMaxCodeLen = metaphone.getMaxCodeLen();
        Assert.assertEquals(4, defaultMaxCodeLen);
        
        // Test setting custom maxCodeLen value
        metaphone.setMaxCodeLen(6);
        int customMaxCodeLen = metaphone.getMaxCodeLen();
        Assert.assertEquals(6, customMaxCodeLen);
    }
}

