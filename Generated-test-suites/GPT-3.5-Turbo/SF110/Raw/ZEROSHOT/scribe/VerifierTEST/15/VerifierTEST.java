import org.junit.Assert;
import org.junit.Test;

public class VerifierTest {
    
    @Test
    public void testGetValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        String result = verifier.getValue();
        Assert.assertEquals(value, result);
    }
    
    @Test
    public void testConstructorNullValue() {
        try {
            Verifier verifier = new Verifier(null);
            Assert.fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }
    
    @Test
    public void testConstructorEmptyValue() {
        try {
            Verifier verifier = new Verifier("");
            Assert.fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }
}