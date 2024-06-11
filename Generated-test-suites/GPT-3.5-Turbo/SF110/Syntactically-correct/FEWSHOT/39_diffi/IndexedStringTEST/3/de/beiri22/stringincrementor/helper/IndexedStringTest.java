package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abcde");
        assertEquals(1, indexedString.count('a'));
        assertEquals(1, indexedString.count('b'));
        assertEquals(1, indexedString.count('c'));
        assertEquals(1, indexedString.count('d'));
        assertEquals(1, indexedString.count('e'));
        assertEquals(0, indexedString.count('f'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abcdabcd");
        assertEquals(0, indexedString.indexOf("abcd".toCharArray()));
        assertEquals(4, indexedString.indexOf("abcd".toCharArray()));
        assertEquals(-1, indexedString.indexOf("efgh".toCharArray()));
    }
}