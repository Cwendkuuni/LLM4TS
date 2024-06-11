import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("value");
        String result = verifier.getValue();
        assertEquals("value", result);
    }
}