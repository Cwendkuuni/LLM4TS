import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {
    private IndexedString indexedString;
    
    @Test
    public void testCount() {
        indexedString = new IndexedString("hello");
        assertEquals(2, indexedString.count('l'));
    }
    
    @Test
    public void testIndexOfFound() {
        indexedString = new IndexedString("hello");
        assertEquals(2, indexedString.indexOf(new char[] {'l', 'l', 'o'}));
    }
    
    @Test
    public void testIndexOfNotFound() {
        indexedString = new IndexedString("hello");
        assertEquals(-1, indexedString.indexOf(new char[] {'w', 'o', 'r', 'l', 'd'}));
    }
    
    @Test
    public void testIndexOfEmptyTarget() {
        indexedString = new IndexedString("hello");
        assertEquals(-1, indexedString.indexOf(new char[] {}));
    }
    
    @Test
    public void testIndexOfTargetLongerThanValues() {
        indexedString = new IndexedString("hello");
        assertEquals(-1, indexedString.indexOf(new char[] {'h', 'e', 'l', 'l', 'o', '!'}));
    }
}