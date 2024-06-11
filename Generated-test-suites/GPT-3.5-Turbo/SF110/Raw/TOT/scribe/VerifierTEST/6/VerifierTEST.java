import org.junit.Test;
import org.scribe.utils.*;
import static org.junit.Assert.*;

public class VerifierTest {
    
    @Test
    public void testGetValue() {
        // Test with a valid string value
        Verifier verifier = new Verifier("valid");
        assertEquals("valid", verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetValueWithNullValue() {
        // Test with null value
        Verifier verifier = new Verifier(null);
        verifier.getValue();
    }
    
    @Test
    public void testGetValueWithEmptyString() {
        // Test with empty string value
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }
    
    @Test
    public void testGetValueWithWhitespace() {
        // Test with whitespace string value
        Verifier verifier = new Verifier("    ");
        assertEquals("    ", verifier.getValue());
    }
    
    @Test
    public void testGetValueWithSpecialChars() {
        // Test with special characters string value
        Verifier verifier = new Verifier("!@#$%^&*()");
        assertEquals("!@#$%^&*()", verifier.getValue());
    }
    
    @Test
    public void testGetValueWithLongString() {
        // Test with a long string value
        Verifier verifier = new Verifier("This is a very long string that exceeds the normal limit");
        assertEquals("This is a very long string that exceeds the normal limit", verifier.getValue());
    }

}