import org.junit.*;
import static org.junit.Assert.*;

public class VerifierTest {

    // Test case for constructor with valid value
    @Test
    public void testConstructor_ValidValue() {
        String validValue = "valid";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }
    
    // Test case for constructor with null value
    @Test(expected = NullPointerException.class)
    public void testConstructor_NullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    // Test case for constructor with empty value
    @Test
    public void testConstructor_EmptyValue() {
        String emptyValue = "";
        Verifier verifier = new Verifier(emptyValue);
        assertEquals(emptyValue, verifier.getValue());
    }
    
    // Test case for getValue method
    @Test
    public void testGetValue() {
        String value = "value";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
    
    // Test case for getValue method with null value
    @Test
    public void testGetValue_NullValue() {
        String nullValue = null;
        Verifier verifier = new Verifier(nullValue);
        assertNull(verifier.getValue());
    }
}