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
        char[] target = {'o', 'w'};
        assertEquals(7, indexedString.indexOf(target));
    }
    
    @Test
    public void testIndexOfNotFound() {
        IndexedString indexedString = new IndexedString("hello world");
        char[] target = {'z', 'w'};
        assertEquals(-1, indexedString.indexOf(target));
    }
    
    @Test
    public void testIndexOfEdgeCase() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'l'};
        assertEquals(2, indexedString.indexOf(target));
    }
}