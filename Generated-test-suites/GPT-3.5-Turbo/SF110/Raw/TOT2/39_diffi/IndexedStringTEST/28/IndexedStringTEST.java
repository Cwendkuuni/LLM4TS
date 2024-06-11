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
        IndexedString indexedString = new IndexedString("hello world");
        assertEquals(6, indexedString.indexOf("world".toCharArray()));
    }

    @Test
    public void testIndexOfNotFound() {
        IndexedString indexedString = new IndexedString("hello world");
        assertEquals(-1, indexedString.indexOf("foo".toCharArray()));
    }

    @Test
    public void testIndexOfEdgeCase() {
        IndexedString indexedString = new IndexedString("hellohello");
        assertEquals(5, indexedString.indexOf("hello".toCharArray()));
    }

    @Test
    public void testIndexOfEmptyString() {
        IndexedString indexedString = new IndexedString("hello world");
        assertEquals(-1, indexedString.indexOf("".toCharArray()));
    }

    @Test
    public void testIndexOfInvalidIndex() {
        IndexedString indexedString = new IndexedString("hello world");
        assertEquals(-1, indexedString.indexOf(new char[]{'o', 'r', 'l', 'd', '!'}));
    }
}