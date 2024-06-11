
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        assertEquals("FLX", metaphone.metaphone("flex"));
        assertEquals("KM", metaphone.metaphone("km"));
        assertEquals("", metaphone.metaphone(null));
        assertEquals("TST", metaphone.metaphone("test"));
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Metaphone metaphone = new Metaphone();

        assertEquals("TST", metaphone.encode("test"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeObjectException() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        
        metaphone.encode(123);
    }

    @Test
    public void testEncodeString() {
        Metaphone metaphone = new Metaphone();

        assertEquals("TST", metaphone.encode("test"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();

        assertTrue(metaphone.isMetaphoneEqual("hello", "hall"));
        assertFalse(metaphone.isMetaphoneEqual("world", null));
        assertTrue(metaphone.isMetaphoneEqual("test", "tist"));
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();

        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(3);

        assertEquals(3, metaphone.getMaxCodeLen());
    }
}
