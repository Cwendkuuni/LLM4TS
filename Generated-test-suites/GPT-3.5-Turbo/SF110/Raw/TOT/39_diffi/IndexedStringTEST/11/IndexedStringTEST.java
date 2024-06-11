import org.junit.*;
import static org.junit.Assert.*;

public class IndexedStringTest {
    
    // Test case for count method
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("Hello World");
        assertEquals(3, indexedString.count('l'));
    }
    
    // Test case for constructor
    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("Hello World");
        assertNotNull(indexedString);
    }
    
    // Test case for indexOf method - typical use case
    @Test
    public void testIndexOf_typical() {
        IndexedString indexedString = new IndexedString("Hello World");
        char[] target = {'l', 'o'};
        assertEquals(4, indexedString.indexOf(target));
    }
    
    // Test case for indexOf method - edge case (target longer than values)
    @Test
    public void testIndexOf_targetLongerThanValues() {
        IndexedString indexedString = new IndexedString("Hello World");
        char[] target = {'l', 'o', 'n', 'g', 'e', 'r'};
        assertEquals(-1, indexedString.indexOf(target));
    }
    
    // Test case for indexOf method - edge case (target is empty)
    @Test
    public void testIndexOf_emptyTarget() {
        IndexedString indexedString = new IndexedString("Hello World");
        char[] target = {};
        assertEquals(-1, indexedString.indexOf(target));
    }
    
    // Test case for indexOf method - error scenario (values is null)
    @Test(expected = NullPointerException.class)
    public void testIndexOf_nullValues() {
        IndexedString indexedString = new IndexedString(null);
        char[] target = {'l', 'o'};
        indexedString.indexOf(target);
    }
}