
import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {
    
    @Test
    public void testConstructor() {
        String s = "test";
        IndexedString indexedString = new IndexedString(s);
        
        assertNotNull(indexedString);
        assertArrayEquals(s.toCharArray(), indexedString.values);
    }
    
    @Test
    public void testIndexOf() {
        String s = "hello";
        IndexedString indexedString = new IndexedString(s);
        
        char[] target1 = {'h', 'e', 'l', 'l', 'o'};
        assertEquals(0, indexedString.indexOf(target1));
        
        char[] target2 = {'l', 'l', 'o'};
        assertEquals(2, indexedString.indexOf(target2));
        
        char[] target3 = {'w', 'o', 'r', 'l', 'd'};
        assertEquals(-1, indexedString.indexOf(target3));
    }
    
}
