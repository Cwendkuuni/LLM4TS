import static org.junit.Assert.*;
import org.junit.*;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testGetValue() {
        String value = "verificationCode123";
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
    public void testAlphanumericValue() {
        String value = "ABC123xyz";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
    
    @Test
    public void testSpecialCharactersValue() {
        String value = "!@#$%^&*()";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}