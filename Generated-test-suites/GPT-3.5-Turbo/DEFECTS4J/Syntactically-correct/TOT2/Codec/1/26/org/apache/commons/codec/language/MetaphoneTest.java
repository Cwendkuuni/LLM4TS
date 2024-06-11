import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    Metaphone metaphone = new Metaphone();

    @Test
    public void testMetaphone() {
        String result = metaphone.metaphone("hello");
        assertEquals("Expected metaphone code for 'hello'", "HL", result);
    }

    @Test
    public void testEncodeObject() {
        try {
            Object result = metaphone.encode("world");
            assertEquals("Expected encoded metaphone code for 'world'", "WR", result);
        } catch (EncoderException e) {
            fail("Exception should not be thrown for valid input");
        }
    }

    @Test
    public void testEncodeString() {
        String result = metaphone.encode("software");
        assertEquals("Expected encoded metaphone code for 'software'", "SFTWR", result);
    }

    @Test
    public void testIsMetaphoneEqual() {
        boolean result = metaphone.isMetaphoneEqual("house", "haus");
        assertTrue("Expected metaphone equality for 'house' and 'haus'", result);
    }

    // Add more test cases for edge cases and exception handling scenarios as needed

}