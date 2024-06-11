
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    Metaphone metaphone = new Metaphone();

    @Test
    public void testMetaphone() {
        assertEquals("AN", metaphone.metaphone("AN"));
        assertEquals("", metaphone.metaphone(""));
        assertEquals("FX", metaphone.metaphone("PHOENIX"));
        assertEquals("FX", metaphone.metaphone("PHONICS"));
        assertEquals("SS", metaphone.metaphone("CE"));
        assertEquals("AJT", metaphone.metaphone("ACHT"));
    }

    @Test
    public void testMetaphoneWithExceptions() {
        assertEquals("S", metaphone.metaphone("SAM"));
        assertEquals("J", metaphone.metaphone("GEORGE"));
        assertEquals("AJ", metaphone.metaphone("AUGUST"));
    }

    @Test
    public void testEncode() throws EncoderException {
        assertEquals("AN", (String) metaphone.encode("AN"));
        assertEquals("FX", (String) metaphone.encode("PHONICS"));
        assertThrows(EncoderException.class, () -> metaphone.encode(123));
    }

    @Test
    public void testIsMetaphoneEqual() {
        assertTrue(metaphone.isMetaphoneEqual("AN", "AN"));
        assertFalse(metaphone.isMetaphoneEqual("FOX", "FAUX"));
        assertTrue(metaphone.isMetaphoneEqual("CAN", "KEN"));
    }

    @Test
    public void testMaxCodeLen() {
        metaphone.setMaxCodeLen(3);
        assertEquals(3, metaphone.getMaxCodeLen());
    }

}
