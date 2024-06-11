package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abcde");
        assertEquals(0, indexedString.count('f'));
        assertEquals(1, indexedString.count('a'));
        assertEquals(1, indexedString.count('e'));
        assertEquals(0, indexedString.count(' '));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(0, indexedString.indexOf("hello".toCharArray()));
        assertEquals(0, indexedString.indexOf("h".toCharArray()));
        assertEquals(1, indexedString.indexOf("el".toCharArray()));
        assertEquals(-1, indexedString.indexOf("abc".toCharArray()));
    }
}