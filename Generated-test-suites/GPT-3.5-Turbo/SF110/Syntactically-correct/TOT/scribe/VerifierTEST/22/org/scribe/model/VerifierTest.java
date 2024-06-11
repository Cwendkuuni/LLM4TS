import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Verifier verifier = new Verifier(null);
    }

    @Test
    public void testEmptyValue() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testMaxLengthValue() {
        String maxValue = "A".repeat(255);
        Verifier verifier = new Verifier(maxValue);
        assertEquals(maxValue, verifier.getValue());
    }
}