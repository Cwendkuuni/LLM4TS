package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        int result = indexedString.count('l');
        assertEquals(2, result);
    }

    @Test
    public void testIndexedString() {
        IndexedString indexedString = new IndexedString("hello");
        assertNotNull(indexedString);
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'l', 'o'};
        int result = indexedString.indexOf(target);
        assertEquals(3, result);
    }
}