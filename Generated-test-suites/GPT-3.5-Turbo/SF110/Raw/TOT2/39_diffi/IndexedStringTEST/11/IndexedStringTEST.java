package de.beiri22.stringincrementor.helper;

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
        assertEquals(2, indexedString.indexOf(new char[] {'l', 'o'}));
    }
    
    @Test
    public void testIndexOfNotFound() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(-1, indexedString.indexOf(new char[] {'z', 'e'}));
    }

    @Test
    public void testIndexOfEdgeCase() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(-1, indexedString.indexOf(new char[] {'h', 'e', 'l', 'l', 'o'}));
    }
    
}