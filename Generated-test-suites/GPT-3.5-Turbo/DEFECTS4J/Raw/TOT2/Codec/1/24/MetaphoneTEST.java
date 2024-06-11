import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        assertEquals("MTFN", metaphone.metaphone("Metaphone"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        assertEquals("MTFN", metaphone.encode("Metaphone"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("hello", "helo"));
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }

    @Test
    public void testInvalidInput() {
        Metaphone metaphone = new Metaphone();
        try {
            metaphone.encode(123); // Invalid input type
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected behavior
        }
    }
}