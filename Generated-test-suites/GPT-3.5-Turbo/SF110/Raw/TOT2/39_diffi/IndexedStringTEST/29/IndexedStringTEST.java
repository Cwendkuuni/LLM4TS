import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {
    
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("example");
        assertEquals(2, indexedString.count('e'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("example");
        char[] target = {'a', 'm', 'p', 'l', 'e'};
        assertEquals(1, indexedString.indexOf(target));
    }
    
    @Test
    public void testIndexOfNonExist() {
        IndexedString indexedString = new IndexedString("example");
        char[] target = {'z', 'y'};
        assertEquals(-1, indexedString.indexOf(target));
    }
    
    @Test
    public void testIndexOfEdgeCase() {
        IndexedString indexedString = new IndexedString("example");
        char[] target = {'e', 'x', 'a', 'm', 'p', 'l', 'e'};
        assertEquals(-1, indexedString.indexOf(target));
    }
}