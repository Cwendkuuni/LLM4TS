import org.junit.Assert;
import org.junit.Test;

public class VerifierTest {

    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("validValue");
        Assert.assertEquals("validValue", verifier.getValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        Assert.assertEquals("testValue", verifier.getValue());
    }
}