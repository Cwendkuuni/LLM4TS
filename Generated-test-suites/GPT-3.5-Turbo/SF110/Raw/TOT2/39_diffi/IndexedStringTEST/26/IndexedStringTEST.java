import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    // Test case for the count method
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(2, indexedString.count('l'));
    }

    // Test case for the indexOf method with a valid target
    @Test
    public void testIndexOfValidTarget() {
        IndexedString indexedString = new IndexedString("hello world");
        int index = indexedString.indexOf(new char[]{'o', ' '});
        assertEquals(4, index);
    }

    // Test case for the indexOf method with an invalid target
    @Test
    public void testIndexOfInvalidTarget() {
        IndexedString indexedString = new IndexedString("hello world");
        int index = indexedString.indexOf(new char[]{'x', ' '});
        assertEquals(-1, index);
    }

    // Test case for the indexOf method with edge case where target is at the end of the string
    @Test
    public void testIndexOfEdgeCase() {
        IndexedString indexedString = new IndexedString("hello world");
        int index = indexedString.indexOf(new char[]{'r', 'l', 'd'});
        assertEquals(7, index);
    }
}