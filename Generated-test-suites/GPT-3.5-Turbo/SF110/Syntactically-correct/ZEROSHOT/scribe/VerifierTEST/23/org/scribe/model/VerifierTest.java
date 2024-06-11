import static org.junit.Assert.*;
import org.junit.Test;

public class VerifierTest {

    @Test
    public void testGetValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testEmptyValue() {
        String value = "";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
    
    @Test
    public void testLongValue() {
        String value = "This is a long value";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}