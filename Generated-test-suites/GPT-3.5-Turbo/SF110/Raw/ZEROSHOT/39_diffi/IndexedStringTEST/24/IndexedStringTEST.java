import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {
    
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(2, indexedString.count('l'));
        assertEquals(1, indexedString.count('o'));
        assertEquals(0, indexedString.count('z'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(2, indexedString.indexOf("llo".toCharArray()));
        assertEquals(0, indexedString.indexOf("he".toCharArray()));
        assertEquals(-1, indexedString.indexOf("xyz".toCharArray()));
    }
}