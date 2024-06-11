package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {
    
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abcabc");
        assertEquals(2, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(2, indexedString.count('c'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abcabc");
        assertEquals(0, indexedString.indexOf(new char[]{'a', 'b'}));
        assertEquals(1, indexedString.indexOf(new char[]{'b', 'c'}));
        assertEquals(2, indexedString.indexOf(new char[]{'c', 'a'}));
        assertEquals(3, indexedString.indexOf(new char[]{'a', 'b'}));
        assertEquals(4, indexedString.indexOf(new char[]{'b', 'c'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'c', 'a', 'b'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'d', 'e'}));
    }
}