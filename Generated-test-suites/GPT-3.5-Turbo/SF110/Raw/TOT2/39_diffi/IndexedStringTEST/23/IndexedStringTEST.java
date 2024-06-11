import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(2, indexedString.count('l'));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'l', 'o'};
        assertEquals(3, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOfNotFound() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'w', 'o', 'r', 'l', 'd'};
        assertEquals(-1, indexedString.indexOf(target));
    }

    // Additional test cases for edge cases and exceptions can be added here

}