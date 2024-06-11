import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {
    
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        // Test case 1
        String result1 = metaphone.metaphone("hello");
        assertEquals("HL", result1);
        // Test case 2
        String result2 = metaphone.metaphone("apple");
        assertEquals("APL", result2);
        // Test case 3
        String result3 = metaphone.metaphone("world");
        assertEquals("WRL", result3);
        // Test case 4
        String result4 = metaphone.metaphone("cat");
        assertEquals("KT", result4);
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        // Test case 1
        Object result1 = metaphone.encode("hello");
        assertEquals("HL", result1);
        // Test case 2
        Object result2 = metaphone.encode("apple");
        assertEquals("APL", result2);
        // Test case 3
        Object result3 = metaphone.encode("world");
        assertEquals("WRL", result3);
        // Test case 4
        Object result4 = metaphone.encode("cat");
        assertEquals("KT", result4);
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        // Test case 1
        boolean result1 = metaphone.isMetaphoneEqual("hello", "helo");
        assertFalse(result1);
        // Test case 2
        boolean result2 = metaphone.isMetaphoneEqual("apple", "apl");
        assertTrue(result2);
        // Test case 3
        boolean result3 = metaphone.isMetaphoneEqual("world", "wrl");
        assertTrue(result3);
        // Test case 4
        boolean result4 = metaphone.isMetaphoneEqual("cat", "ct");
        assertFalse(result4);
    }
}