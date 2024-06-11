import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {

    private IndexedString indexedString;
    
    @Before
    public void setup() {
        indexedString = new IndexedString("hello world");
    }
    
    @Test
    public void testCount() {
        assertEquals(0, indexedString.count('x')); // Test case when character is not present in the string
        assertEquals(3, indexedString.count('l')); // Test case when character is present multiple times in the string
        assertEquals(2, indexedString.count('o')); // Test case when character is present multiple times in the string
        assertEquals(1, indexedString.count('h')); // Test case when character is present only once in the string
    }
    
    @Test
    public void testIndexOf() {
        assertEquals(-1, indexedString.indexOf("test".toCharArray())); // Test case when target is not present
        assertEquals(2, indexedString.indexOf("llo".toCharArray())); // Test case when target is present in the middle
        assertEquals(9, indexedString.indexOf("ld".toCharArray())); // Test case when target is present at the end
        assertEquals(0, indexedString.indexOf("he".toCharArray())); // Test case when target is present at the start
    }
}