import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {
    
    private IndexedString indexedString;

    @Before
    public void setup() {
        indexedString = new IndexedString("test string");
    }

    @Test
    public void testCount() {
        assertEquals(2, indexedString.count('t'));
        assertEquals(1, indexedString.count('e'));
        assertEquals(1, indexedString.count('s'));
        assertEquals(1, indexedString.count('r'));
        assertEquals(1, indexedString.count('i'));
        assertEquals(1, indexedString.count('n'));
        assertEquals(2, indexedString.count('g'));
        assertEquals(0, indexedString.count('x'));
    }

    @Test
    public void testIndexOf() {
        assertEquals(0, indexedString.indexOf("test".toCharArray()));
        assertEquals(5, indexedString.indexOf("string".toCharArray()));
        assertEquals(-1, indexedString.indexOf("xyz".toCharArray()));
        assertEquals(-1, indexedString.indexOf("testx".toCharArray()));
    }

}