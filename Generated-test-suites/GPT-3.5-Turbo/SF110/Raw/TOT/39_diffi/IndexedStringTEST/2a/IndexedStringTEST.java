import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IndexedStringTest {
    
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("aaabbbccc");
        assertEquals(3, indexedString.count('a'));
        assertEquals(3, indexedString.count('b'));
        assertEquals(3, indexedString.count('c'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("aaabbbccc");
        
        // Typical Use Cases
        assertEquals(0, indexedString.indexOf(new char[]{'a'}));
        assertEquals(1, indexedString.indexOf(new char[]{'a', 'a'}));
        assertEquals(3, indexedString.indexOf(new char[]{'b', 'b'}));
        assertEquals(5, indexedString.indexOf(new char[]{'c', 'c', 'c'}));
        
        // Edge Cases
        assertEquals(6, indexedString.indexOf(new char[]{'c'}));
        assertEquals(6, indexedString.indexOf(new char[]{'c', 'c'}));
        assertEquals(8, indexedString.indexOf(new char[]{'c', 'c', 'c'}));
        assertEquals(9, indexedString.indexOf(new char[]{'c', 'c', 'c', 'c'}));
        
        // Error Scenarios
        assertEquals(-1, indexedString.indexOf(new char[]{'d'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'a', 'b'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'b', 'a'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'c', 'b', 'a'}));
    }
}