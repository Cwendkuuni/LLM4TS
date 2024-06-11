
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {
    
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("aabbcc");
        assertEquals(2, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(2, indexedString.count('c'));
        assertEquals(0, indexedString.count('d'));
    }
    
    @Test
    public void testIndexof() {
        IndexedString indexedString = new IndexedString("aabbcc");
        assertEquals(0, indexedString.indexOf("aa".toCharArray()));
        assertEquals(2, indexedString.indexOf("bb".toCharArray()));
        assertEquals(4, indexedString.indexOf("cc".toCharArray()));
        assertEquals(-1, indexedString.indexOf("dd".toCharArray()));
    }
    
    // Additional test cases for edge cases and exception handling
    
    @Test
    public void testCountWithEmptyString() {
        IndexedString indexedString = new IndexedString("");
        assertEquals(0, indexedString.count('a'));
    }
    
    @Test
    public void testCountWithLongString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("a");
        }
        IndexedString indexedString = new IndexedString(sb.toString());
        assertEquals(1000, indexedString.count('a'));
    }
    
    @Test
    public void testIndexofWithEmptyTarget() {
        IndexedString indexedString = new IndexedString("abcdef");
        assertEquals(-1, indexedString.indexOf("".toCharArray()));
    }
    
    @Test
    public void testIndexofWithLongTarget() {
        IndexedString indexedString = new IndexedString("abcdef");
        assertEquals(-1, indexedString.indexOf("abcdefghi".toCharArray()));
    }
}
