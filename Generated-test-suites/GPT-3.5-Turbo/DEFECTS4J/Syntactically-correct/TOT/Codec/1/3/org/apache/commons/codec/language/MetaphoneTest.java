import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use cases
        Assert.assertEquals("MTFN", metaphone.metaphone("Metaphone"));
        Assert.assertEquals("BR", metaphone.metaphone("Brooklyn"));
        Assert.assertEquals("MRC", metaphone.metaphone("Marcel"));
        
        // Edge cases
        Assert.assertEquals("A", metaphone.metaphone("A")); // Single character input
        Assert.assertEquals("AX", metaphone.metaphone("Axe")); // Input ends with 'e'
        Assert.assertEquals("KQK", metaphone.metaphone("KQOK")); // Input has consecutive duplicates
        
        // Error scenarios
        Assert.assertEquals("", metaphone.metaphone(null)); // Null input
        Assert.assertEquals("", metaphone.metaphone("")); // Empty input
        Assert.assertEquals("AXT", metaphone.metaphone("Axe$T..")); // Input with special characters
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use cases
        Assert.assertEquals("MTFN", metaphone.encode("Metaphone"));
        Assert.assertEquals("BR", metaphone.encode("Brooklyn"));
        Assert.assertEquals("MRC", metaphone.encode("Marcel"));
        
        // Error scenarios
        try {
            metaphone.encode(12345); // Input of invalid type
            Assert.fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use cases
        Assert.assertTrue(metaphone.isMetaphoneEqual("Metaphone", "METAPHONE"));
        Assert.assertTrue(metaphone.isMetaphoneEqual("Brooklyn", "BRUUKLYN"));
        Assert.assertFalse(metaphone.isMetaphoneEqual("Marcel", "Marcela"));
        
        // Edge cases
        Assert.assertTrue(metaphone.isMetaphoneEqual("Axe", "AXE"));
        Assert.assertTrue(metaphone.isMetaphoneEqual("", "")); // Empty inputs
        Assert.assertFalse(metaphone.isMetaphoneEqual("abc", "def")); // Different inputs
        
        // Error scenarios
        Assert.assertFalse(metaphone.isMetaphoneEqual(null, null)); // Both inputs null
        Assert.assertFalse(metaphone.isMetaphoneEqual("abc", null)); // One input null
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Default max code length is 4
        Assert.assertEquals(4, metaphone.getMaxCodeLen());
        
        metaphone.setMaxCodeLen(5);
        Assert.assertEquals(5, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        metaphone.setMaxCodeLen(5);
        Assert.assertEquals(5, metaphone.getMaxCodeLen());
        
        metaphone.setMaxCodeLen(10);
        Assert.assertEquals(10, metaphone.getMaxCodeLen());
    }

}