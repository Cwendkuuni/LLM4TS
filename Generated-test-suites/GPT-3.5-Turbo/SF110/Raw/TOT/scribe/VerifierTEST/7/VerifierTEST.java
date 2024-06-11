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
        String expected = "test";
        String result = verifier.getValue();
        assertEquals(expected, result);
    }
    
    @Test
    public void testNullValue() {
        Verifier verifier = new Verifier(null);
        assertNull(verifier.getValue());
    }
    
    @Test
    public void testEmptyValue() {
        Verifier verifier = new Verifier("");
        String expected = "";
        String result = verifier.getValue();
        assertEquals(expected, result);
    }
}