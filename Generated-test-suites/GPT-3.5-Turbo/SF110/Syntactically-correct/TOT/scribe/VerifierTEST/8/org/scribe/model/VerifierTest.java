import org.junit.Assert;
import org.junit.Test;

public class VerifierTest {
    
    @Test
    public void testGetValue() {
        // Test case 1: Typical use case
        String value = "12345";
        Verifier verifier = new Verifier(value);
        Assert.assertEquals(value, verifier.getValue());
        
        // Test case 2: Edge case - Empty string
        value = "";
        verifier = new Verifier(value);
        Assert.assertEquals(value, verifier.getValue());
        
        // Test case 3: Error scenario - Null value
        value = null;
        try {
            verifier = new Verifier(value);
            Assert.fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            Assert.assertNull(verifier);
            Assert.assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }
}