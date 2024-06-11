
import static org.junit.Assert.*;
import org.junit.Test;

public class VerifierTest {
    
    @Test
    public void testGetValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        
        assertEquals(value, verifier.getValue());
    }
}
