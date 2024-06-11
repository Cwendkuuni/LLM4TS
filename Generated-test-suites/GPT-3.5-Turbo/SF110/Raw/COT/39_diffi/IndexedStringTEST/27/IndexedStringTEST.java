import org.junit.*;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        int count = indexedString.count('l');
        assertEquals(2, count);
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        int index = indexedString.indexOf(new char[]{'l','o'});
        assertEquals(3, index);
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testCountNoMatch() {
        IndexedString indexedString = new IndexedString("hello");
        int count = indexedString.count('a');
        assertEquals(0, count);
    }
    
    @Test
    public void testCountMultipleMatches() {
        IndexedString indexedString = new IndexedString("hello");
        int count = indexedString.count('l');
        assertEquals(2, count);
    }
    
    @Test
    public void testIndexOfNotFound() {
        IndexedString indexedString = new IndexedString("hello");
        int index = indexedString.indexOf(new char[]{'a','b'});
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOfEmptyTarget() {
        IndexedString indexedString = new IndexedString("hello");
        int index = indexedString.indexOf(new char[]{});
        assertEquals(-1, index);
    }
    
}