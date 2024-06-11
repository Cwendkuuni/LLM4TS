package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {

    private IndexedString indexedString;

    @Before
    public void setup() {
        indexedString = new IndexedString("hello");
    }

    @Test
    public void testCount() {
        char c = 'l';
        int result = indexedString.count(c);
        assertEquals(2, result);
    }

    @Test
    public void testIndexOf() {
        char[] target1 = {'h', 'e'};
        int result1 = indexedString.indexOf(target1);
        assertEquals(0, result1);

        char[] target2 = {'o', 'l'};
        int result2 = indexedString.indexOf(target2);
        assertEquals(2, result2);

        char[] target3 = {'z', 'y'};
        int result3 = indexedString.indexOf(target3);
        assertEquals(-1, result3);
    }
}