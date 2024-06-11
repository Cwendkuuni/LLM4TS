
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("aabbc");
        assertEquals(2, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(1, indexedString.count('c'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("aabbc");
        
        char[] target1 = {'a', 'a'};
        assertEquals(0, indexedString.indexOf(target1));
        
        char[] target2 = {'a', 'a', 'b'};
        assertEquals(0, indexedString.indexOf(target2));
        
        char[] target3 = {'b', 'c'};
        assertEquals(3, indexedString.indexOf(target3));
        
        char[] target4 = {'c', 'd'};
        assertEquals(-1, indexedString.indexOf(target4));
    }
}

