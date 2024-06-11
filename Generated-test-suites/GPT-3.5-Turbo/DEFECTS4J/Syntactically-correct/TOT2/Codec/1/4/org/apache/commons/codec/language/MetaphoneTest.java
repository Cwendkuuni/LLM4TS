import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        assertEquals("MTFN", metaphone.metaphone("Metaphone"));
    }

    @Test
    public void testEncodeString() {
        Metaphone metaphone = new Metaphone();
        assertEquals("MTFN", metaphone.encode("Metaphone"));
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        assertEquals("MTFN", (String)metaphone.encode((Object)"Metaphone"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("Metaphone", "Metaphone"));
        assertFalse(metaphone.isMetaphoneEqual("Metaphone", "Metaphon"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidObject() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        metaphone.encode(123);
    }

}