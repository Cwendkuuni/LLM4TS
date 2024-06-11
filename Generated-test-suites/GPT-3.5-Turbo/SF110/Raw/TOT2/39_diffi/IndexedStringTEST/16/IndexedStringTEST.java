import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(2, indexedString.count('l'));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(2, indexedString.indexOf("ll".toCharArray()));
    }

    @Test
    public void testIndexOfNotFound() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(-1, indexedString.indexOf("abc".toCharArray()));
    }

    @Test
    public void testIndexOfEdgeCase() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(-1, indexedString.indexOf("lol".toCharArray()));
    }

    @Test
    public void testIndexOfEmptyString() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(-1, indexedString.indexOf("".toCharArray()));
    }
}