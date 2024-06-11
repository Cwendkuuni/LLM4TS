import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testConstructor_NullValue_ThrowsException() {
        try {
            Verifier verifier = new Verifier(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }
    
    @Test
    public void testConstructor_ValidValue_CreatesInstance() {
        Verifier verifier = new Verifier("validValue");
        assertEquals("validValue", verifier.getValue());
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("value");
        assertEquals("value", verifier.getValue());
    }

}