
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testGetValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertNotNull(verifier.getValue());
        assertEquals(value, verifier.getValue());
    }
    
    // Edge case: passing null value
    @Test(expected = NullPointerException.class)
    public void testConstructorNullValue() {
        new Verifier(null);
    }
}
