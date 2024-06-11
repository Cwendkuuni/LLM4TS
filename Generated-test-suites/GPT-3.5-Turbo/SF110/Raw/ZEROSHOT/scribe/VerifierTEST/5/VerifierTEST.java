import static org.junit.Assert.*;
import org.junit.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }

    @Test
    public void testConstructorNullValue() {
        try {
            new Verifier(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }

    @Test
    public void testConstructorValidValue() {
        Verifier verifier = new Verifier("test");
        assertNotNull(verifier);
        assertEquals("test", verifier.getValue());
    }
}