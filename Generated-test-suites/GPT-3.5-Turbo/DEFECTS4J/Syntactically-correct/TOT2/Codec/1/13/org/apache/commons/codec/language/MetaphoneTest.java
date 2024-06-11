import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        assertEquals("AN", metaphone.metaphone("Anne"));
        assertEquals("0", metaphone.metaphone("thompson"));
        assertEquals("JN", metaphone.metaphone("johnson"));
        assertEquals("FLKN", metaphone.metaphone("franklin"));
    }

    @Test
    public void testEncodeObject() {
        Metaphone metaphone = new Metaphone();
        try {
            metaphone.encode(new Object());
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEncodeString() {
        Metaphone metaphone = new Metaphone();
        assertEquals("AN", metaphone.encode("Anne"));
        assertEquals("0", metaphone.encode("thompson"));
        assertEquals("JN", metaphone.encode("johnson"));
        assertEquals("FLKN", metaphone.encode("franklin"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("Anne", "Anne"));
        assertFalse(metaphone.isMetaphoneEqual("Anne", "Ann"));
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(3);
        assertEquals(3, metaphone.getMaxCodeLen());
    }
}