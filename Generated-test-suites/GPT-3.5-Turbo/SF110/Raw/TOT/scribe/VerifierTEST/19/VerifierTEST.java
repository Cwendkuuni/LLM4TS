import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {
    
    @Test
    public void testVerifierConstructor_ValidValue() {
        Verifier verifier = new Verifier("validValue");
        assertEquals("validValue", verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testVerifierConstructor_NullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testVerifierConstructor_EmptyValue() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }
    
    @Test
    public void testVerifierGetValue() {
        Verifier verifier = new Verifier("sampleValue");
        assertEquals("sampleValue", verifier.getValue());
    }
}