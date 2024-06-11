import org.junit.*;
import static org.junit.Assert.*;

public class VerifierTest {
    
    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("testValue");
        assertNotNull(verifier);
    }
    
    @Test
    public void testConstructorNullValue() {
        try {
            Verifier verifier = new Verifier(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }
}