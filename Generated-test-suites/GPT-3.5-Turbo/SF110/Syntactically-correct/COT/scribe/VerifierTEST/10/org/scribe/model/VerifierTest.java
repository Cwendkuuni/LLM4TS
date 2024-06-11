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
    
    @Test
    public void testConstructorWithValidValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        
        assertNotNull(verifier);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        String value = null;
        Verifier verifier = new Verifier(value);
    }
    
    @Test
    public void testConstructorWithEmptyValue() {
        String value = "";
        Verifier verifier = new Verifier(value);
        
        assertNotNull(verifier);
    }
}