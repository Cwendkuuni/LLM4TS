package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {
    
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("aabbcc");
        assertEquals(2, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(2, indexedString.count('c'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("aabbcc");
        assertEquals(0, indexedString.indexOf("aa".toCharArray()));
        assertEquals(2, indexedString.indexOf("bb".toCharArray()));
        assertEquals(4, indexedString.indexOf("cc".toCharArray()));
        assertEquals(-1, indexedString.indexOf("dd".toCharArray()));
    }
}