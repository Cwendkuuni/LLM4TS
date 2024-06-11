import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {
    
    @Test
    public void testConstructor() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNullValue() {
        String value = null;
        Verifier verifier = new Verifier(value);
    }
    
    @Test
    public void testGetValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}