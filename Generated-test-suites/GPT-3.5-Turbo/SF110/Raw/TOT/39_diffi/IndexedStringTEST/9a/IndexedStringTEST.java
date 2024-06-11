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
        IndexedString indexedString = new IndexedString("This is a test string");

        // Typical use case
        char[] target1 = {'i', 's'};
        assertEquals(2, indexedString.indexOf(target1));
        
        // Typical use case - multiple occurrences
        char[] target2 = {'s', 't'};
        assertEquals(3, indexedString.indexOf(target2));
        
        // Typical use case - edge case (first occurrence)
        char[] target3 = {'T', 'h', 'i', 's'};
        assertEquals(0, indexedString.indexOf(target3));
        
        // Typical use case - edge case (last occurrence)
        char[] target4 = {'s', 't', 'r', 'i', 'n', 'g'};
        assertEquals(10, indexedString.indexOf(target4));
        
        // Error scenario - target array is empty
        char[] target5 = {};
        assertEquals(-1, indexedString.indexOf(target5));
        
        // Error scenario - target array is longer than IndexedString values array
        char[] target6 = {'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', 's', 't','r', 'i', 'n', 'g', ' ', 'w', 'i', 't', 'h', ' ', 'e', 'x', 't', 'r', 'a', ' ', 'w', 'o', 'r', 'd', 's'};
        assertEquals(-1, indexedString.indexOf(target6));
    }
}