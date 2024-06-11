import org.junit.Test;
import org.junit.Assert;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        String value = verifier.getValue();
        Assert.assertEquals("testValue", value);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Verifier verifier = new Verifier(null);
        String value = verifier.getValue();
    }
}