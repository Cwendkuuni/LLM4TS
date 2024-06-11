import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("hello");
        assertEquals("Error in metaphone calculation for hello", "HL", result);
    }

    @Test
    public void testMetaphoneEmptyString() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("");
        assertEquals("Empty string metaphone not handled correctly", "", result);
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("a");
        assertEquals("Single character metaphone not handled correctly", "A", result);
    }

    @Test
    public void testMetaphoneBoundaryCases() {
        Metaphone metaphone = new Metaphone();
        String result1 = metaphone.metaphone("knock");
        String result2 = metaphone.metaphone("wrap");
        String result3 = metaphone.metaphone("x-ray");
        assertEquals("Boundary case 1 not handled correctly", "NKK", result1);
        assertEquals("Boundary case 2 not handled correctly", "RP", result2);
        assertEquals("Boundary case 3 not handled correctly", "XRY", result3);
    }

    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        Object result = metaphone.encode("hello");
        assertEquals("Error in encode method for hello", "HL", result);
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        boolean result1 = metaphone.isMetaphoneEqual("hello", "hollow");
        boolean result2 = metaphone.isMetaphoneEqual("hello", "help");
        boolean result3 = metaphone.isMetaphoneEqual("hello", "hello");
        assertEquals("Metaphones should be equal for words with similar sound", true, result1);
        assertEquals("Metaphones should not be equal for words with different sound", false, result2);
        assertEquals("Metaphones should be equal for identical words", true, result3);
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals("Max code length should be 4", 4, metaphone.getMaxCodeLen());
        metaphone.setMaxCodeLen(5);
        assertEquals("Max code length should be set to 5", 5, metaphone.getMaxCodeLen());
    }
}