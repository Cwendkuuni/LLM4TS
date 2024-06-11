
import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testConstructor_ValidValue() {
        String value = "abc123";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_NullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testGetValue() {
        String value = "abc123";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}

