import org.junit.Assert;
import org.junit.Test;
import org.scribe.utils.Preconditions;

public class VerifierTest {
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        Assert.assertEquals("test", verifier.getValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructor_NullValue() {
        new Verifier(null);
    }
    
    @Test
    public void testVerifierConstructor_ValidValue() {
        Verifier verifier = new Verifier("test");
        Assert.assertEquals("test", verifier.getValue());
    }
}