import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class VerifierTest {

    @Test
    public void testConstructorValidValue() {
        Verifier verifier = new Verifier("valid");
        assertEquals("valid", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }

}