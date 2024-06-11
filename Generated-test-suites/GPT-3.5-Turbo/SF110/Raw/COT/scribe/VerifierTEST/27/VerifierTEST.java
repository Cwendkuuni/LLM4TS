import org.junit.Test;
import org.scribe.model.Verifier;
import static org.junit.Assert.assertEquals;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }

    // Edge Cases
    @Test
    public void testEmptyValue() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }

    // Exception Handling
    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Verifier verifier = new Verifier(null);
    }
}