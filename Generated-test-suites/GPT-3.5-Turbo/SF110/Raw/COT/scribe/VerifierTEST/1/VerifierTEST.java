import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {
    
    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
    
    // Edge Cases and Exception Handling Tests
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_NullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testGetValue_NullValue() {
        Verifier verifier = new Verifier(null);
        assertNull(verifier.getValue());
    }
}