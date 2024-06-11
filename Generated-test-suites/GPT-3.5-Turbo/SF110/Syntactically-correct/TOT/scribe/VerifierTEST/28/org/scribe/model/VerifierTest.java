import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Verifier;
import static org.junit.Assert.*;

public class VerifierTest {

    private Verifier verifier;

    @Before
    public void setUp() {
        verifier = new Verifier("test123");
    }

    @Test
    public void testGetValue() {
        assertEquals("test123", verifier.getValue());
    }
}