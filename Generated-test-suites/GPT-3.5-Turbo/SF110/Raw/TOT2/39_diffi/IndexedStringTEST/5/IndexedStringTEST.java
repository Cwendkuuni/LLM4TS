import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("hello");

        assertNotNull(indexedString);
        assertEquals(5, indexedString.indexOf("hello".toCharArray()));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");

        assertEquals(0, indexedString.indexOf("he".toCharArray()));
        assertEquals(1, indexedString.indexOf("el".toCharArray()));
        assertEquals(-1, indexedString.indexOf("world".toCharArray()));
        assertEquals(-1, indexedString.indexOf("hi".toCharArray()));
    }

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");

        assertEquals(2, indexedString.count('l'));
        assertEquals(0, indexedString.count('z'));
    }
}