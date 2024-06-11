import org.junit.Test;

import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone_emptyString() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("");
        assertEquals("", result);
    }

    @Test
    public void testMetaphone_singleCharacter() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("A");
        assertEquals("A", result);
    }

    @Test
    public void testMetaphone_wordWithNoChanges() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("CAT");
        assertEquals("KT", result);
    }

    @Test
    public void testMetaphone_wordWithChanges() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("WHY");
        assertEquals("Y", result);
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.encode("CAT");
        assertEquals("KT", result);
    }

    @Test
    public void testEncode_ObjectNotString() {
        Metaphone metaphone = new Metaphone();
        try {
            metaphone.encode(new Object());
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsMetaphoneEqual_equalStrings() {
        Metaphone metaphone = new Metaphone();
        boolean result = metaphone.isMetaphoneEqual("CAT", "KT");
        assertTrue(result);
    }

    @Test
    public void testIsMetaphoneEqual_notEqualStrings() {
        Metaphone metaphone = new Metaphone();
        boolean result = metaphone.isMetaphoneEqual("CAT", "DOG");
        assertFalse(result);
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        int result = metaphone.getMaxCodeLen();
        assertEquals(4, result);
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(3);
        int result = metaphone.getMaxCodeLen();
        assertEquals(3, result);
    }

}