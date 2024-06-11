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
        IndexedString indexedString = new IndexedString("aabaa");
        assertEquals(0, indexedString.indexOf("a".toCharArray()));
        assertEquals(1, indexedString.indexOf("ab".toCharArray()));
        assertEquals(1, indexedString.indexOf("aab".toCharArray()));
        assertEquals(1, indexedString.indexOf("aabaa".toCharArray()));
        assertEquals(-1, indexedString.indexOf("abc".toCharArray()));
    }

    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("abcde");
        char[] values = {'a', 'b', 'c', 'd', 'e'};
        assertEquals(values.length, indexedString.getValues().length);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], indexedString.getValues()[i]);
        }
    }
}