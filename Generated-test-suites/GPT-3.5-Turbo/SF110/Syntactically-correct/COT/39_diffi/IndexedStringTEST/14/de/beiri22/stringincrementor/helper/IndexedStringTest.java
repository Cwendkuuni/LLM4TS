import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IndexedStringTest {
    
    // Test the count() method
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("aabbc");
        assertEquals(2, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(1, indexedString.count('c'));
        assertEquals(0, indexedString.count('d'));
    }
    
    // Test the constructor
    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("aabbc");
        char[] expectedValues = {'a', 'a', 'b', 'b', 'c'};
        int[][] expectedIndex = new int[256][];
        expectedIndex['a'] = new int[]{0, 1};
        expectedIndex['b'] = new int[]{2, 3};
        expectedIndex['c'] = new int[]{4};
        assertArrayEquals(expectedValues, indexedString.values);
        assertArrayEquals(expectedIndex, indexedString.index);
    }
    
    // Test the indexOf() method
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("aabbc");
        char[] target = {'a', 'b'};
        assertEquals(0, indexedString.indexOf(target));
        
        target = new char[]{'b', 'c'};
        assertEquals(2, indexedString.indexOf(target));
        
        target = new char[]{'c', 'd'};
        assertEquals(-1, indexedString.indexOf(target));
    }
}