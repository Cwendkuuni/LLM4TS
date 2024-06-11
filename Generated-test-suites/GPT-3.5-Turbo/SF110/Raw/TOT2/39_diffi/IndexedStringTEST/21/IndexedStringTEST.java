package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("aabbc");
        assertEquals(2, indexedString.count('a'));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("aabbc");
        char[] target = {'b', 'c'};
        assertEquals(3, indexedString.indexOf(target));
    }
}