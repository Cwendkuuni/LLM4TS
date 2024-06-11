import static org.junit.Assert.*;
import org.junit.Test;

public class VerifierTest {
    
    @Test
    public void testConstructor_validValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
    
    // Edge case: passing null value to constructor
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullValue() {
        new Verifier(null);
    }
    
    // Error scenario: passing empty string to constructor
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_emptyValue() {
        new Verifier("");
    }
}