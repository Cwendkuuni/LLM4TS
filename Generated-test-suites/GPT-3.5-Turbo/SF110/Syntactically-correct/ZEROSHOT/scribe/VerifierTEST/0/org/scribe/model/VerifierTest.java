import org.junit.*;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
    
    @Test(expected=NullPointerException.class)
    public void testNullValue() {
        Verifier verifier = new Verifier(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testEmptyValue() {
        Verifier verifier = new Verifier("");
    }

}