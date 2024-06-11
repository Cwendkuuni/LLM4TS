import org.junit.*;
import static org.junit.Assert.*;

public class VerifierTest {

    private Verifier verifier;

    @Before
    public void setUp() {
        verifier = new Verifier("testValue");
    }

    @Test
    public void testGetValue() {
        String expected = "testValue";
        String actual = verifier.getValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testNullValue() {
        try {
            verifier = new Verifier(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }

}