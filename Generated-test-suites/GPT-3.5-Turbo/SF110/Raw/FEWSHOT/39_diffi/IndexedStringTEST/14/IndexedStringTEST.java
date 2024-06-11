package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IndexedStringTest {
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("aaaabbbccc");
        assertEquals(4, indexedString.count('a'));
        assertEquals(3, indexedString.count('b'));
        assertEquals(3, indexedString.count('c'));
    }

    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("aaaabbbccc");
        assertEquals("aaaabbbccc", new String(indexedString.values));
        assertEquals(10, indexedString.index.length);

        indexedString = new IndexedString("abcdefg");
        assertEquals("abcdefg", new String(indexedString.values));
        assertEquals(10, indexedString.index.length);
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("aaaabbbccc");
        assertEquals(0, indexedString.indexOf(new char[]{'a', 'a', 'a', 'a'}));
        assertEquals(1, indexedString.indexOf(new char[]{'a', 'b', 'b', 'b'}));
        assertEquals(7, indexedString.indexOf(new char[]{'c', 'c', 'c'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'d', 'e', 'f'}));
    }
}