import org.junit.*;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testConstructor_validValue() {
        Verifier verifier = new Verifier("validValue");
        assertEquals("validValue", verifier.getValue());
    }

    @Test
    public void testConstructor_nullValue() {
        try {
            Verifier verifier = new Verifier(null);
            fail("Constructor should throw NullPointerException for null value");
        } catch (NullPointerException e) {
            String expectedMessage = "Must provide a valid string as verifier";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }
  
    @Test
    public void testGetValue_emptyValue() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }

}