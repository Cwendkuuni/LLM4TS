import org.junit.*;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }

    @Test
    public void testVerifierNotNull() {
        try {
            Verifier verifier = new Verifier(null);
        } catch (NullPointerException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }
}