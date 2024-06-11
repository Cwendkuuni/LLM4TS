import org.junit.*;

public class VerifierTest {

    @Test
    public void testConstructor_ValidValue() {
        Verifier verifier = new Verifier("test");
        String expectedValue = "test";
        String actualValue = verifier.getValue();
        Assert.assertEquals(expectedValue, actualValue);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_NullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        String expectedValue = "test";
        String actualValue = verifier.getValue();
        Assert.assertEquals(expectedValue, actualValue);
    }
}