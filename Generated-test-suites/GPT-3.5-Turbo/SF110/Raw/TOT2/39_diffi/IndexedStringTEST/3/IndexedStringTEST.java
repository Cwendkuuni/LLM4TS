import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(1, indexedString.count('h'));
        assertEquals(2, indexedString.count('l'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'h', 'e', 'l', 'l', 'o'};
        assertEquals(0, indexedString.indexOf(target));
        
        char[] notFound = {'z'};
        assertEquals(-1, indexedString.indexOf(notFound));
    }
}