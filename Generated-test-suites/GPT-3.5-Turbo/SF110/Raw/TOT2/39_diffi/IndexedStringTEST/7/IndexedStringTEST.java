
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("test string");
        assertEquals(2, indexedString.count('t'));
        assertEquals(2, indexedString.count('e'));
        assertEquals(1, indexedString.count('s'));
        assertEquals(1, indexedString.count('r'));
        assertEquals(2, indexedString.count(' '));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("test string");
        assertEquals(0, indexedString.indexOf("test".toCharArray()));
        assertEquals(5, indexedString.indexOf("string".toCharArray()));
        assertEquals(-1, indexedString.indexOf("notfound".toCharArray()));
    }
}
