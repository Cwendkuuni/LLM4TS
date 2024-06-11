package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("test");
        assertEquals(2, indexedString.count('t'));
        assertEquals(0, indexedString.count('z'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("test");
        assertEquals(0, indexedString.indexOf("te".toCharArray()));
        assertEquals(-1, indexedString.indexOf("st".toCharArray()));
        assertEquals(1, indexedString.indexOf("es".toCharArray()));
        assertEquals(-1, indexedString.indexOf("abc".toCharArray()));
    }
}