import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("test");
        assertNotNull(verifier);
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        String value = verifier.getValue();
        assertEquals("test", value);
    }
}