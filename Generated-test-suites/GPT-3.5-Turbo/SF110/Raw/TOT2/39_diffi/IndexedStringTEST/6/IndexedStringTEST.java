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
        assertEquals(2, indexedString.indexOf("ll".toCharArray()));
    }

    @Test
    public void testIndexOfNotFound() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(-1, indexedString.indexOf("xyz".toCharArray()));
    }

    @Test
    public void testIndexOfWithEmptyArray() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(-1, indexedString.indexOf("".toCharArray()));
    }

    @Test
    public void testIndexOfWithArrayThatExceedsStringLength() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(-1, indexedString.indexOf("longer".toCharArray()));
    }

}