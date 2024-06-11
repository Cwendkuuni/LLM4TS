import static org.junit.Assert.*;
import org.junit.Test;

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
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullValue() {
        Verifier verifier = new Verifier(null);
    }
}