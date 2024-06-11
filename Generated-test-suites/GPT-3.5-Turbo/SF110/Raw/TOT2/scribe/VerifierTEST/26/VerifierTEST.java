import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testConstructorWithValidValue() {
        Verifier verifier = new Verifier("testValue");
        assertNotNull(verifier);
    }

    @Test
    public void testConstructorWithNullValue() {
        try {
            Verifier verifier = new Verifier(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }

}