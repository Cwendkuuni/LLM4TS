
import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString string = new IndexedString("aabbbccc");
        
        assertEquals(2, string.count('a'));
        assertEquals(3, string.count('b'));
        assertEquals(3, string.count('c'));
        assertEquals(0, string.count('d'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString string = new IndexedString("aabbbccc");
        
        assertEquals(0, string.indexOf(new char[] {'a', 'a'}));
        assertEquals(1, string.indexOf(new char[] {'a', 'b'}));
        assertEquals(2, string.indexOf(new char[] {'b', 'b'}));
        assertEquals(5, string.indexOf(new char[] {'c', 'c'}));
        assertEquals(-1, string.indexOf(new char[] {'c', 'a'}));
    }
}

