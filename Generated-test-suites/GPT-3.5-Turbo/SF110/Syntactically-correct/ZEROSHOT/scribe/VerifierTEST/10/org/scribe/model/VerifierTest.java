import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testConstructor_withValidString() {
        final Verifier verifier = new Verifier("validString");
        assertEquals("validString", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_withNullString() {
        new Verifier(null);
    }

    @Test
    public void testGetValue() {
        final Verifier verifier = new Verifier("validString");
        assertEquals("validString", verifier.getValue());
    }
}