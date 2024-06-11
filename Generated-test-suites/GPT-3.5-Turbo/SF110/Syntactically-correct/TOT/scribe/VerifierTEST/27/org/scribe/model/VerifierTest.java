import org.junit.Before;
import org.junit.Test;
import org.scribe.utils.Preconditions;
import org.scribe.model.Verifier;

import static org.junit.Assert.*;

public class VerifierTest {

    private Verifier verifier;
    private final String testValue = "testString";

    @Before
    public void setUp() {
        verifier = new Verifier(testValue);
    }

    @Test
    public void testConstructor_ValidValue() {
        assertEquals(testValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullValue() {
        new Verifier(null);
    }

    @Test
    public void testGetValue() {
        assertEquals(testValue, verifier.getValue());
    }

    @Test
    public void testSetValue_ValidValue() {
        String newValue = "newTestValue";
        verifier.setValue(newValue);
        assertEquals(newValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSetValue_NullValue() {
        verifier.setValue(null);
    }
}