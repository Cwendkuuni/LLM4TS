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
        assertEquals(-1, indexedString.indexOf("java".toCharArray()));
    }
    
    @Test
    public void testConstructorEmptyString() {
        IndexedString indexedString = new IndexedString("");
        assertEquals(-1, indexedString.indexOf("test".toCharArray()));
    }
}