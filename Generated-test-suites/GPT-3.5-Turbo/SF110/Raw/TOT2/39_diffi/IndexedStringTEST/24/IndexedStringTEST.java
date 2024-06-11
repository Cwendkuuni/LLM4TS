import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(2, indexedString.count('l'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(2, indexedString.indexOf(new char[]{'l', 'o'}));
    }
    
    @Test
    public void testIndexOf_NotFound() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(-1, indexedString.indexOf(new char[]{'h', 'i'}));
    }
    
    @Test
    public void testIndexOf_EdgeCase() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(-1, indexedString.indexOf(new char[]{'o', 'h'}));
    }
    
    @Test
    public void testIndexOf_EmptyString() {
        IndexedString indexedString = new IndexedString("");
        assertEquals(-1, indexedString.indexOf(new char[]{'h', 'i'}));
    }
}