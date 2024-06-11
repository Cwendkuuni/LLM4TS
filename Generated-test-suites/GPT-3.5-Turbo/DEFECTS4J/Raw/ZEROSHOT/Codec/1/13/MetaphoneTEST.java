import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MetaphoneTest {

    @Test
    public void testEmptyString() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        assertEquals("A", metaphone.metaphone("a"));
    }

    @Test
    public void testTwoCharacters() {
        Metaphone metaphone = new Metaphone();
        assertEquals("AS", metaphone.metaphone("as"));
    }

    @Test
    public void testThreeCharacters() {
        Metaphone metaphone = new Metaphone();
        assertEquals("KDN", metaphone.metaphone("kdn"));
    }

    @Test
    public void testFourCharacters() {
        Metaphone metaphone = new Metaphone();
        assertEquals("FOAR", metaphone.metaphone("foar"));
    }

    @Test
    public void testLongString() {
        Metaphone metaphone = new Metaphone();
        assertEquals("WLFRX", metaphone.metaphone("woolifax"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        assertEquals("HLW", metaphone.encode("hello world"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertEquals(true, metaphone.isMetaphoneEqual("hello", "hallo"));
        assertEquals(false, metaphone.isMetaphoneEqual("hello", "world"));
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