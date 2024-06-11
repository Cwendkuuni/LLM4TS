import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MetaphoneTest {

    Metaphone metaphone = new Metaphone();

    @Test
    public void testMetaphone() {
        assertEquals("HL", metaphone.metaphone("hello"));
        assertEquals("JN", metaphone.metaphone("john"));
    }

    @Test
    public void testEncodeObject() {
        try {
            assertEquals("HL", metaphone.encode("hello"));
        } catch (EncoderException e) {
            fail("Exception thrown when it shouldn't have been");
        }
    }

    @Test
    public void testEncodeString() {
        assertEquals("JN", metaphone.encode("john"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        assertTrue(metaphone.isMetaphoneEqual("hello", "HALO"));
        assertFalse(metaphone.isMetaphoneEqual("hello", "world"));
    }

    @Test
    public void testEdgeCases() {
        assertEquals("", metaphone.metaphone(""));
        assertEquals("X", metaphone.metaphone("x"));
        assertEquals("EH", metaphone.metaphone("eh"));
        assertEquals("JN", metaphone.metaphone("johnny"));
    }

    @Test
    public void testExceptionHandling() {
        try {
            metaphone.encode(123);
            fail("EncoderException not thrown");
        } catch (EncoderException e) {
            assertTrue(true);
        }
    }
}