import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        
        // Typical use case
        assertEquals(2, indexedString.count('l'));
        
        // Edge case
        assertEquals(0, indexedString.count('z'));
        
        // Error scenario
        assertEquals(0, indexedString.count('\0'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'e', 'l', 'l'};
        
        // Typical use case
        assertEquals(1, indexedString.indexOf(target));
        
        // Edge case
        char[] emptyTarget = {};
        assertEquals(-1, indexedString.indexOf(emptyTarget));
        
        // Error scenario
        char[] notPresentTarget = {'z'};
        assertEquals(-1, indexedString.indexOf(notPresentTarget));
    }
}