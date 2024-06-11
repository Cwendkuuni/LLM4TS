import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {

    // Test case for count method
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("testing");
        assertEquals(2, indexedString.count('t')); // Typical use case
        assertEquals(1, indexedString.count('e')); // Typical use case
        assertEquals(0, indexedString.count('z')); // Edge case: character not in the string
    }
    
    // Test case for indexOf method
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("testing");
        assertEquals(0, indexedString.indexOf("tes".toCharArray())); // Typical use case
        assertEquals(1, indexedString.indexOf("est".toCharArray())); // Typical use case
        assertEquals(-1, indexedString.indexOf("xyz".toCharArray())); // Edge case: target not found
        assertEquals(-1, indexedString.indexOf("esting".toCharArray())); // Edge case: partial target match
    }
}