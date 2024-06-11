
package de.beiri22.stringincrementor.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("foo");

        assertEquals(2, indexedString.count('o'));
        assertEquals(0, indexedString.count('z'));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("foo");

        char[] target = {'o'};
        assertEquals(1, indexedString.indexOf(target));

        char[] target2 = {'b', 'a', 'r'};
        assertEquals(-1, indexedString.indexOf(target2));
    }
}
