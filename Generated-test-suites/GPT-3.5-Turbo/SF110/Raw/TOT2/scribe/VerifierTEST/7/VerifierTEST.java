import static org.junit.Assert.*;
import org.junit.Test;

public class VerifierTest {
    
    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
    
    // Edge case: null value provided in constructor
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullValue() {
        new Verifier(null);
    }
    
    // Exception handling: verify that proper exception is thrown when null value is provided
    @Test
    public void testConstructorExceptionHandling() {
        try {
            new Verifier(null);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }
}