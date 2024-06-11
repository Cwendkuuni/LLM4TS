import static org.junit.Assert.*;
import org.junit.Test;

public class VerifierTest {
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
    
    // Edge case: value is null
    // Exception handling: null value
    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    // Edge case: empty string value
    @Test
    public void testEmptyStringValue() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }
}