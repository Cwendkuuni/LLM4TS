import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MetaphoneTest {

    Metaphone metaphone = new Metaphone();

    @Test
    public void testMetaphone_basic() {
        assertEquals("AT", metaphone.metaphone("at"));
    }

    @Test
    public void testMetaphone_singleCharacter() {
        assertEquals("A", metaphone.metaphone("a"));
    }

    @Test
    public void testMetaphone_nullString() {
        assertEquals("", metaphone.metaphone(null));
    }

    @Test
    public void testMetaphone_emptyString() {
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testIsMetaphoneEqual_identicalStrings() {
        assertTrue(metaphone.isMetaphoneEqual("hello", "hello"));
    }

    @Test
    public void testIsMetaphoneEqual_differentStrings() {
        assertTrue(!metaphone.isMetaphoneEqual("hello", "world"));
    }

    @Test
    public void testIsMetaphoneEqual_emptyStrings() {
        assertTrue(metaphone.isMetaphoneEqual("", ""));
    }

    // Add more test cases for edge cases and error scenarios

}