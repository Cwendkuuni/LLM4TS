import org.junit.Test;

import static org.junit.Assert.*;

public class MetaphoneTest {

    private Metaphone metaphone = new Metaphone();

    @Test
    public void testMetaphone() {
        assertEquals("MTFN", metaphone.metaphone("Metaphone"));
        assertEquals("X", metaphone.metaphone("X-ray"));
        assertEquals("PT", metaphone.metaphone("Potato"));
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testEncode() throws EncoderException {
        assertEquals("MTFN", (String) metaphone.encode("Metaphone"));
        assertEquals("X", (String) metaphone.encode("X-ray"));
        assertEquals("PT", (String) metaphone.encode("Potato"));
        assertNotNull(metaphone.encode(""));
        assertTrue(metaphone.encode("").isEmpty());

        try {
            metaphone.encode(123);
            fail("EncoderException not thrown for non-string parameter");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        assertTrue(metaphone.isMetaphoneEqual("Metaphone", "MeTaPhOnE"));
        assertFalse(metaphone.isMetaphoneEqual("Metaphone", "Soundex"));
    }

    @Test
    public void testGetMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());

        metaphone.setMaxCodeLen(0);
        assertEquals(0, metaphone.getMaxCodeLen());

        metaphone.setMaxCodeLen(-1);
        assertEquals(-1, metaphone.getMaxCodeLen());
    }
}