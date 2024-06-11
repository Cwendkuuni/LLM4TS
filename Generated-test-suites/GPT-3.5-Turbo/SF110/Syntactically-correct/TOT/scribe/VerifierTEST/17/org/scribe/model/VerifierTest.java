import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testConstructor_withValidValue() {
        String value = "valid";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_withNullValue() {
        String value = null;
        Verifier verifier = new Verifier(value);
    }
    
    @Test
    public void testGetValue() {
        String value = "valid";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

}