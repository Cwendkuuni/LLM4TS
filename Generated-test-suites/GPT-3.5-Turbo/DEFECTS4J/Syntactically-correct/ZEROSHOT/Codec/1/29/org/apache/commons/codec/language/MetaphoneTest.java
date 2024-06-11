import org.junit.Test;

import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        assertEquals("SMNT", metaphone.metaphone("Smith"));
        assertEquals("HRT", metaphone.metaphone("Heart"));
        assertEquals("JMS", metaphone.metaphone("James"));
        assertEquals("CBT", metaphone.metaphone("Cabbage"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        assertEquals("SMNT", metaphone.encode("Smith"));
        assertEquals("HRT", metaphone.encode("Heart"));
        assertEquals("JMS", metaphone.encode("James"));
        assertEquals("CBT", metaphone.encode("Cabbage"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("Smith", "Smoth"));
        assertTrue(metaphone.isMetaphoneEqual("Heart", "Hart"));
        assertFalse(metaphone.isMetaphoneEqual("James", "John"));
        assertTrue(metaphone.isMetaphoneEqual("Cabbage", "Cabage"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        metaphone.encode(123);
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }
}