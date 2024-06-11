import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        assertEquals("String comparison for metaphone match", "AN", metaphone.metaphone("Anne"));
        assertEquals("Empty string should return empty metaphone", "", metaphone.metaphone(""));
        assertEquals("Single character should return uppercase", "A", metaphone.metaphone("a"));
    }

    @Test
    public void testEncodeObject() {
        Metaphone metaphone = new Metaphone();
        
        try {
            Object result = metaphone.encode("Test");
            assertEquals("Object encoding should match string metaphone", "TST", result);
        } catch (EncoderException e) {
            fail("Exception thrown when encoding");
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        assertTrue("Metaphones of same strings should be equal", metaphone.isMetaphoneEqual("John", "Johnn"));
        assertFalse("Metaphones of different strings should not be equal", metaphone.isMetaphoneEqual("Anne", "Alice"));
    }

}