import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }

    @Test
    public void testNullValueConstructor() {
        Verifier verifier = new Verifier(null);
        assertNull(verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValueConstructor() {
        new Verifier("");
    }

}