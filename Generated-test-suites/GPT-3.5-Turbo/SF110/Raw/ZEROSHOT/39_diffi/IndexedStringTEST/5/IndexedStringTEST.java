package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'l', 'o'};
        int expected = 3;
        int actual = indexedString.indexOf(target);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        char c = 'l';
        int expected = 2;
        int actual = indexedString.count(c);
        assertEquals(expected, actual);
    }
}