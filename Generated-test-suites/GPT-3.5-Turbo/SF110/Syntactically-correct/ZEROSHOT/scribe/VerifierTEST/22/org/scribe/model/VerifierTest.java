import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {
    private Verifier verifier;
    
    @Before
    public void setUp() {
        verifier = new Verifier("test");
    }
    
    @Test
    public void testGetValue() {
        String result = verifier.getValue();
        assertEquals("test", result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNullValue() {
        Verifier nullVerifier = new Verifier(null);
    }
    
    @Test
    public void testConstructorNonNullValue() {
        Verifier nonNullVerifier = new Verifier("test");
        assertNotNull(nonNullVerifier);
    }
}