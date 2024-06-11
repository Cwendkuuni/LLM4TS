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
        assertEquals("test", verifier.getValue());
    }

    @Test
    public void testConstructor_NullValueError() {
        try {
            verifier = new Verifier(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }

    @Test
    public void testConstructor_EmptyValueError() {
        try {
            verifier = new Verifier("");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }

    @Test
    public void testConstructor_WhiteSpaceValueError() {
        try {
            verifier = new Verifier("    ");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }
}