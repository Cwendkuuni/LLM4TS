import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VerifierTest {

    @Test
    public void testVerifierConstructor_ValidString() {
        Verifier verifier = new Verifier("valid string");
        assertEquals("valid string", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructor_NullString() {
        Verifier verifier = new Verifier(null);
    }

    @Test
    public void testVerifierConstructor_EmptyString() {
        Verifier verifier = new Verifier("");
        assertNotNull(verifier.getValue());
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test value");
        assertEquals("test value", verifier.getValue());
    }

}