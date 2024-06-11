package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("test");
        char c = 't';
        int result = indexedString.count(c);
        assertEquals(2, result);
    }

    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("test");
        char[] expectedValues = {'t', 'e', 's', 't'};
        assertArrayEquals(expectedValues, indexedString.values);
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("test");
        char[] target = {'e', 's'};
        int result = indexedString.indexOf(target);
        assertEquals(1, result);
    }
}