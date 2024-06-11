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

    // Additional test cases for edge cases and exception handling scenarios:

    @Test
    public void testCountNoOccurrence() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(0, indexedString.count('z'));
    }

    @Test
    public void testCountEmptyString() {
        IndexedString indexedString = new IndexedString("");
        assertEquals(0, indexedString.count('l'));
    }

    @Test
    public void testIndexOfNotFound() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'x', 'y'};
        assertEquals(-1, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOfEmptyTarget() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {};
        assertEquals(-1, indexedString.indexOf(target));
    }

}