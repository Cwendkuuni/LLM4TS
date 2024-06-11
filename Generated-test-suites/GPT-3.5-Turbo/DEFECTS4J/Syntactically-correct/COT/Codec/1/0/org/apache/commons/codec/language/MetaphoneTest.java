import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Basic test cases
        Assert.assertEquals("0000", metaphone.metaphone("hello"));
        Assert.assertEquals("dam", metaphone.metaphone("dumb"));
        
        // Edge cases and exception handling scenarios
        Assert.assertEquals("", metaphone.metaphone(""));
        Assert.assertEquals("T", metaphone.metaphone("T"));
        Assert.assertEquals("S", metaphone.metaphone("SS"));
        Assert.assertEquals("AE", metaphone.metaphone("AE"));
        Assert.assertEquals("WT", metaphone.metaphone("Wt"));
        Assert.assertEquals("MST", metaphone.metaphone("maze"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Basic test cases
        Assert.assertEquals("0000", metaphone.encode("hello"));
        Assert.assertEquals("dam", metaphone.encode("dumb"));
        
        // Edge cases and exception handling scenarios
        Assert.assertEquals("", metaphone.encode(""));
        Assert.assertEquals("T", metaphone.encode("T"));
        Assert.assertEquals("S", metaphone.encode("SS"));
        Assert.assertEquals("AE", metaphone.encode("AE"));
        Assert.assertEquals("WT", metaphone.encode("Wt"));
        Assert.assertEquals("MST", metaphone.encode("maze"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Basic test cases
        Assert.assertTrue(metaphone.isMetaphoneEqual("hello", "hello"));
        Assert.assertFalse(metaphone.isMetaphoneEqual("hello", "world"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithInvalidType() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        metaphone.encode(123);
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        Assert.assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        Assert.assertEquals(5, metaphone.getMaxCodeLen());
    }

}