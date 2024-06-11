import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VerifierTest {
    private Verifier verifier;
    private String value;
    
    @Before
    public void setup() {
        value = "testValue";
        verifier = new Verifier(value);
    }
    
    @Test
    public void testGetValue() {
        String expectedResult = value;
        String actualResult = verifier.getValue();
        
        Assert.assertEquals(expectedResult, actualResult);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        String nullValue = null;
        Verifier nullVerifier = new Verifier(nullValue);
    }
}