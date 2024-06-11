import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        
        String result = verifier.getValue();
        
        assertEquals(value, result);
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructor_NullValue() {
        new Verifier(null);
    }
    
    @Test
    public void testVerifierConstructor_ValidValue() {
        String value = "testValue";
        
        Verifier verifier = new Verifier(value);
        
        assertNotNull(verifier);
        assertEquals(value, verifier.getValue());
    }

}