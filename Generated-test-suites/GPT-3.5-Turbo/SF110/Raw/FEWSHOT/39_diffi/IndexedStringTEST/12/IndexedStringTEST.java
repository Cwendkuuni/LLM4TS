import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {
    
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("");
        assertEquals(0, indexedString.count('a'));
        
        indexedString = new IndexedString("abcdeabcdeabcde");
        assertEquals(3, indexedString.count('a'));
        assertEquals(3, indexedString.count('b'));
        assertEquals(3, indexedString.count('c'));
        assertEquals(3, indexedString.count('d'));
        assertEquals(3, indexedString.count('e'));
        
        indexedString = new IndexedString("hello world");
        assertEquals(0, indexedString.count('a'));
        assertEquals(0, indexedString.count('b'));
        assertEquals(0, indexedString.count('c'));
        assertEquals(0, indexedString.count('d'));
        assertEquals(0, indexedString.count('e'));
        assertEquals(0, indexedString.count('f'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("");
        assertEquals(-1, indexedString.indexOf(new char[]{'a'}));
        
        indexedString = new IndexedString("abcdeabcdeabcde");
        assertEquals(0, indexedString.indexOf(new char[]{'a'}));
        assertEquals(1, indexedString.indexOf(new char[]{'b'}));
        assertEquals(2, indexedString.indexOf(new char[]{'c'}));
        assertEquals(3, indexedString.indexOf(new char[]{'d'}));
        assertEquals(4, indexedString.indexOf(new char[]{'e'}));
        assertEquals(5, indexedString.indexOf(new char[]{'a','b'}));
        assertEquals(6, indexedString.indexOf(new char[]{'b','c'}));
        assertEquals(7, indexedString.indexOf(new char[]{'c','d'}));
        assertEquals(8, indexedString.indexOf(new char[]{'d','e'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'e','a'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'a','d'}));
        
        indexedString = new IndexedString("hello world");
        assertEquals(4, indexedString.indexOf(new char[]{'o'}));
        assertEquals(7, indexedString.indexOf(new char[]{'w'}));
        assertEquals(6, indexedString.indexOf(new char[]{' ', 'w'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'a'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'b'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'c'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'d'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'e'}));
    }
}