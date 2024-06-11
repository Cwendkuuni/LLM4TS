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
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'e', 'l', 'l'};
        assertEquals(1, indexedString.indexOf(target));
    }
    
    @Test
    public void testIndexOfEdgeCase() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'o'};
        assertEquals(4, indexedString.indexOf(target));
    }
    
    @Test
    public void testIndexOfErrorScenario() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'z'};
        assertEquals(-1, indexedString.indexOf(target));
    }
}