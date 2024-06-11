import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {
    
    Verifier verifier;
    
    @Before
    public void setUp() {
        verifier = new Verifier("testValue");
    }
    
    @Test
    public void testGetValue() {
        assertEquals("testValue", verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullValueVerifier() {
        Verifier nullVerifier = new Verifier(null);
    }
    
    @Test
    public void testEmptyValueVerifier() {
        Verifier emptyVerifier = new Verifier("");
        assertEquals("", emptyVerifier.getValue());
    }

}