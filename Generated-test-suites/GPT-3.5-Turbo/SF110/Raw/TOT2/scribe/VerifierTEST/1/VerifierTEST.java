import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {
    
    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("testValue");
        assertNotNull(verifier);
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }
    
    @Test
    public void testNullConstructor() {
        try {
            new Verifier(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }
}