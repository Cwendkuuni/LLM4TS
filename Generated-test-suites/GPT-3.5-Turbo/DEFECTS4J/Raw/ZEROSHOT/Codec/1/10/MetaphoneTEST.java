import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    Metaphone metaphone = new Metaphone();

    @Test
    public void testMetaphone() {
        assertEquals("S", metaphone.metaphone("Smith"));
        assertEquals("MTFN", metaphone.metaphone("Metaphone"));
        assertEquals("HNK", metaphone.metaphone("Honk"));
        assertEquals("", metaphone.metaphone(""));
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("B", metaphone.metaphone("B"));
    }

    @Test
    public void testEncode() throws EncoderException {
        assertEquals("S", metaphone.encode("Smith"));
        assertEquals("MTFN", metaphone.encode("Metaphone"));
        assertEquals("HNK", metaphone.encode("Honk"));
        assertEquals("", metaphone.encode(""));
        assertEquals("A", metaphone.encode("A"));
        assertEquals("B", metaphone.encode("B"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        assertTrue(metaphone.isMetaphoneEqual("Smith", "Smoth"));
        assertFalse(metaphone.isMetaphoneEqual("Metaphone", "Metafone"));
        assertTrue(metaphone.isMetaphoneEqual("", ""));
        assertTrue(metaphone.isMetaphoneEqual("A", "A"));
        assertFalse(metaphone.isMetaphoneEqual("B", "C"));
    }

    @Test
    public void testGetMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }
}