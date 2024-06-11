package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("test");
        assertEquals(2, indexedString.count('t'));
        assertEquals(1, indexedString.count('e'));
        assertEquals(1, indexedString.count('s'));
        assertEquals(0, indexedString.count('z'));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("test");
        assertEquals(0, indexedString.indexOf(new char[]{'t'}));
        assertEquals(1, indexedString.indexOf(new char[]{'e'}));
        assertEquals(2, indexedString.indexOf(new char[]{'s'}));
        assertEquals(3, indexedString.indexOf(new char[]{'t'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'z'}));
        assertEquals(1, indexedString.indexOf(new char[]{'e', 's', 't'}));
        assertEquals(0, indexedString.indexOf(new char[]{'t', 'e', 's'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'z', 'e', 's', 't'}));
    }
}