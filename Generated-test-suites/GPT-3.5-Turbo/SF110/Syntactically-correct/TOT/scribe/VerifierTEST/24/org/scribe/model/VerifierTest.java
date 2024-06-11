import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {
    
    @Test
    public void testConstructorValidValue() {
        Verifier verifier = new Verifier("validValue");
        assertEquals("validValue", verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("value");
        assertEquals("value", verifier.getValue());
    }
}