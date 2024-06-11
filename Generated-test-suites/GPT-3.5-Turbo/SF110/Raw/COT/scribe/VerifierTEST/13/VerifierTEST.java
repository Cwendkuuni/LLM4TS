import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }

    @Test
    public void testConstructorWithNullValue() {
        try {
            Verifier verifier = new Verifier(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }
}