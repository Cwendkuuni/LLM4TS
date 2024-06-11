
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MetaphoneTest {

    private Metaphone metaphone = new Metaphone();

    @Test
    public void testMetaphone() {
        assertEquals("RLF", metaphone.metaphone("Ralph"));
        assertEquals("AT", metaphone.metaphone("at"));
        assertEquals("FX", metaphone.metaphone("fax"));
    }

    @Test
    public void testEncode() {
        assertEquals("JN", metaphone.encode("John"));
        assertEquals("KLN", metaphone.encode("clone"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        assertTrue(metaphone.isMetaphoneEqual("John", "Jon"));
        assertFalse(metaphone.isMetaphoneEqual("hello", "world"));
    }

    @Test
    public void testEdgeCases() {
        assertEquals("", metaphone.metaphone(""));
        assertEquals("T", metaphone.metaphone("T"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() {
        metaphone.encode(123);
    }
}
