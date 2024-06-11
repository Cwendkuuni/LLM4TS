import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }

    @Test
    public void testMetaphone() {
        // Typical use case
        String code = metaphone.metaphone("hello");
        assertEquals("HL", code);

        // Edge case: empty string
        code = metaphone.metaphone("");
        assertEquals("", code);

        // Error scenario: null input
        code = metaphone.metaphone(null);
        assertEquals("", code);
    }

    @Test
    public void testEncode() {
        // Typical use case
        Object code = metaphone.encode("world");
        assertEquals("WL", code);

        // Error scenario: non-string input
        try {
            code = metaphone.encode(12345);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        // Typical use case
        boolean isEqual = metaphone.isMetaphoneEqual("hello", "hollow");
        assertTrue(isEqual);

        // Edge case: empty string
        isEqual = metaphone.isMetaphoneEqual("", "");
        assertTrue(isEqual);

        // Error scenario: null input
        isEqual = metaphone.isMetaphoneEqual(null, "world");
        assertFalse(isEqual);
    }

    @Test
    public void testMaxCodeLen() {
        // Typical use case
        metaphone.setMaxCodeLen(5);
        int maxCodeLen = metaphone.getMaxCodeLen();
        assertEquals(5, maxCodeLen);

        // Edge case: 0 code length
        metaphone.setMaxCodeLen(0);
        maxCodeLen = metaphone.getMaxCodeLen();
        assertEquals(0, maxCodeLen);

        // Error scenario: negative code length
        metaphone.setMaxCodeLen(-1);
        maxCodeLen = metaphone.getMaxCodeLen();
        assertEquals(0, maxCodeLen);
    }

}