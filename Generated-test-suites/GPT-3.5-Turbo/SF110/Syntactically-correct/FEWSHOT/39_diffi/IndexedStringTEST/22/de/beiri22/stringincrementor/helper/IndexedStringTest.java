package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(1, indexedString.count('e'));
        assertEquals(2, indexedString.count('l'));
        assertEquals(0, indexedString.count('z'));
    }

    @Test
    public void testIndexedString() {
        IndexedString indexedString = new IndexedString("hello");
        char[] values = {'h', 'e', 'l', 'l', 'o'};
        assertEquals(values, indexedString.values);
        
        int[][] index = new int[256][];
        index['h'] = new int[] {0};
        index['e'] = new int[] {1};
        index['l'] = new int[] {2, 3};
        index['o'] = new int[] {4};
        
        assertEquals(index, indexedString.index);
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target1 = {'h', 'e', 'l', 'l', 'o'};
        char[] target2 = {'l', 'l'};
        char[] target3 = {'z'};
        
        assertEquals(0, indexedString.indexOf(target1));
        assertEquals(2, indexedString.indexOf(target2));
        assertEquals(-1, indexedString.indexOf(target3));
    }
}