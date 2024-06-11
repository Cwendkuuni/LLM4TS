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
        IndexedString indexedString = new IndexedString("hello world");
        assertEquals(6, indexedString.indexOf(new char[]{'w', 'o', 'r', 'l', 'd'}));
    }
    
    @Test
    public void testEdgeCaseIndexOf() {
        IndexedString indexedString = new IndexedString("aaaaa");
        assertEquals(2, indexedString.indexOf(new char[]{'a', 'a', 'a'}));
    }
    
    @Test
    public void testErrorIndexOf() {
        IndexedString indexedString = new IndexedString("hello world");
        assertEquals(-1, indexedString.indexOf(new char[]{'x', 'y', 'z'}));
    }

}