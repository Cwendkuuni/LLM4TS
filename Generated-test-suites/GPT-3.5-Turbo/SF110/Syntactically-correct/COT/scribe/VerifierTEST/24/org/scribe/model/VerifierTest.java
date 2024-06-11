import static org.junit.Assert.*;
import org.junit.Test;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        String value = verifier.getValue();
        assertEquals("testValue", value);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructorNullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testConstructorEmptyValue() {
        Verifier verifier = new Verifier("");
        String value = verifier.getValue();
        assertEquals("", value);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructorInvalidValue() {
        Verifier verifier = new Verifier(" ");
    }
}