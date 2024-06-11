
import org.junit.Test;
import org.scribe.model.Verifier;
import static org.junit.Assert.assertEquals;

public class VerifierTest {
    
    @Test
    public void testGetValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        String value = null;
        Verifier verifier = new Verifier(value);
    }
}
