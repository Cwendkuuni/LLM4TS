import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        assertEquals("AN", metaphone.metaphone("AN"));
        assertEquals("KJT", metaphone.metaphone("Subject"));
        assertEquals("0T", metaphone.metaphone("Thought"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        assertEquals("FX", metaphone.encode("Fox"));
        assertEquals("KR", metaphone.encode("Car"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("Hello", "Halo"));
        assertFalse(metaphone.isMetaphoneEqual("Test", "Met"));
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }
}