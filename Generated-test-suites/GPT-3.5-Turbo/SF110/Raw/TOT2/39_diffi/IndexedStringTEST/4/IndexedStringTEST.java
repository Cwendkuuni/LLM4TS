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
        IndexedString indexedString = new IndexedString("hello world");
        char[] target = {'w', 'o', 'r', 'l', 'd'};
        assertEquals(6, indexedString.indexOf(target));
        
        char[] missingTarget = {'z', 'x', 'y'};
        assertEquals(-1, indexedString.indexOf(missingTarget));
        
        char[] partialTarget = {'l', 'o', ' ', 'w', 'o'};
        assertEquals(-1, indexedString.indexOf(partialTarget));
    }
}