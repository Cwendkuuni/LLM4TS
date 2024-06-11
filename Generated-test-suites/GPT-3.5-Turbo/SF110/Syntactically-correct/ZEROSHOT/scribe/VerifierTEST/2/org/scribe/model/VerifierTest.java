import org.junit.*;
import static org.junit.Assert.*;

public class VerifierTest {
    private Verifier verifier;
    
    @Before
    public void setUp() {
        verifier = new Verifier("test");
    }
    
    @Test
    public void testGetValue() {
        assertEquals("test", verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_nullValue() {
        verifier = new Verifier(null);
    }
    
    @Test
    public void testConstructor_validValue() {
        verifier = new Verifier("valid");
        assertEquals("valid", verifier.getValue());
    }
}