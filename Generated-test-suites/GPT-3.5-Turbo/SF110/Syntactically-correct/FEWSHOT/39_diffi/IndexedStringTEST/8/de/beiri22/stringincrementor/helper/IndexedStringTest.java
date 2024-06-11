package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("banana");
        assertEquals(3, indexedString.count('a'));
        assertEquals(1, indexedString.count('b'));
        assertEquals(0, indexedString.count('c'));
    }

    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("banana");
        assertEquals(6, indexedString.values.length);
        assertEquals(6, indexedString.index.length);
        assertEquals(3, indexedString.index['a'].length);
        assertEquals(1, indexedString.index['b'].length);
        assertEquals(0, indexedString.index['c'].length);
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("banana");
        assertEquals(1, indexedString.indexOf(new char[] {'a', 'n', 'a'}));
        assertEquals(3, indexedString.indexOf(new char[] {'a', 'n', 'a', 'n'}));
        assertEquals(-1, indexedString.indexOf(new char[] {'x', 'y', 'z'}));
    }
}