import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {
    private IndexedString indexedString;
    
    @Before
    public void setUp() {
        indexedString = new IndexedString("test string");
    }
    
    @Test
    public void testCount() {
        int result = indexedString.count('t');
        assertEquals(3, result);
    }
    
    @Test
    public void testIndexOf() {
        int result = indexedString.indexOf(new char[] {'s', 't', 'r', 'i'});
        assertEquals(5, result);
    }
    
    @Test
    public void testIndexOfNotFound() {
        int result = indexedString.indexOf(new char[] {'x', 'y'});
        assertEquals(-1, result);
    }
}