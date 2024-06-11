import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {
   
    @Test
    public void testGetValue() {
        // Test typical use case
        Verifier verifier = new Verifier("abc123");
        assertEquals("abc123", verifier.getValue());
        
        // Test empty string
        verifier = new Verifier("");
        assertEquals("", verifier.getValue());
        
        // Test null value
        verifier = new Verifier(null);
        assertNull(verifier.getValue());
    }
  
    @Test
    public void testConstructor() {
        // Test typical use case
        Verifier verifier = new Verifier("abc123");
        assertNotNull(verifier);
        
        // Test empty string
        verifier = new Verifier("");
        assertNotNull(verifier);
        
        // Test null value
        verifier = new Verifier(null);
        assertNotNull(verifier);
    }
}