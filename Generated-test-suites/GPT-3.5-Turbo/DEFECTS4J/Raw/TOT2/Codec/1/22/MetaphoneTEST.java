import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {
    
    private Metaphone metaphone = new Metaphone(); 
    
    @Test
    public void testMetaphone_basic() {
        String result = metaphone.metaphone("cat");
        assertEquals("K", result);
    }
    
    @Test
    public void testMetaphone_emptyInput() {
        String result = metaphone.metaphone("");
        assertEquals("", result);
    }
    
    @Test
    public void testEncode_validInput() throws Exception {
        String result = (String) metaphone.encode("apple");
        assertEquals("APL", result);
    }
    
    @Test(expected = EncoderException.class)
    public void testEncode_invalidInput() throws Exception {
        metaphone.encode(123); // Passing non-String input
    }
    
    @Test
    public void testIsMetaphoneEqual_equalStrings() {
        boolean result = metaphone.isMetaphoneEqual("hello", "hello");
        assertTrue(result);
    }
    
    @Test
    public void testIsMetaphoneEqual_notEqualStrings() {
        boolean result = metaphone.isMetaphoneEqual("hello", "world");
        assertFalse(result);
    }
    
    @Test
    public void testSetMaxCodeLen_validValue() {
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }
    
    @Test
    public void testSetMaxCodeLen_negativeValue() {
        metaphone.setMaxCodeLen(-1);
        assertEquals(4, metaphone.getMaxCodeLen()); // Default value should be set
    }
    
}