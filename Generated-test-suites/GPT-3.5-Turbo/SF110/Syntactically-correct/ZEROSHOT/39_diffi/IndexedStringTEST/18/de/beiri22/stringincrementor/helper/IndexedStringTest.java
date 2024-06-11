import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abcaba");
        assertEquals(3, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(1, indexedString.count('c'));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abcaba");
        assertEquals(0, indexedString.indexOf("ab".toCharArray()));
        assertEquals(1, indexedString.indexOf("bc".toCharArray()));
        assertEquals(2, indexedString.indexOf("ca".toCharArray()));
        assertEquals(3, indexedString.indexOf("ab".toCharArray()));
        assertEquals(-1, indexedString.indexOf("xy".toCharArray()));
    }
}