package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abcccc");
        assertEquals(2, indexedString.count('a'));
        assertEquals(4, indexedString.count('c'));
        assertEquals(0, indexedString.count('d'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abcabc");
        assertEquals(0, indexedString.indexOf(new char[] {'a'}));
        assertEquals(3, indexedString.indexOf(new char[] {'a', 'b', 'c'}));
        assertEquals(1, indexedString.indexOf(new char[] {'b', 'c'}));
        assertEquals(-1, indexedString.indexOf(new char[] {'d', 'e'}));
    }
}