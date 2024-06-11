import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    private Metaphone metaphone;

    @Test
    public void testMetaphone() {
        metaphone = new Metaphone();
        // Typical use case
        assertEquals("STR", metaphone.metaphone("string"));
        // Edge case: empty string
        assertEquals("", metaphone.metaphone(""));
        // Error scenario: null input
        assertEquals("", metaphone.metaphone(null));
    }

    @Test
    public void testEncode() {
        metaphone = new Metaphone();
        // Typical use case
        assertEquals("STR", metaphone.encode("string"));
        // Error scenario: non-string input
        Throwable exception = assertThrows(EncoderException.class, () -> {
            metaphone.encode(123);
        });
        assertEquals("Parameter supplied to Metaphone encode is not of type java.lang.String", exception.getMessage());
    }

    @Test
    public void testIsMetaphoneEqual() {
        metaphone = new Metaphone();
        // Typical use case: same metaphone code
        assertTrue(metaphone.isMetaphoneEqual("string", "strrng"));
        // Edge case: empty string
        assertFalse(metaphone.isMetaphoneEqual("", "strng"));
        // Error scenario: null input
        assertFalse(metaphone.isMetaphoneEqual(null, "strng"));
    }

    @Test
    public void testGetMaxCodeLen() {
        metaphone = new Metaphone();
        // Typical use case: default max code length is 4
        assertEquals(4, metaphone.getMaxCodeLen());
        // Edge case: negative max code length
        metaphone.setMaxCodeLen(-1);
        assertEquals(-1, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        metaphone = new Metaphone();
        // Typical use case
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());
        // Edge case: negative max code length
        metaphone.setMaxCodeLen(-1);
        assertEquals(-1, metaphone.getMaxCodeLen());
    }

}