package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("example");
        assertEquals(2, indexedString.count('e'));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("example");
        assertEquals(0, indexedString.indexOf("ex".toCharArray()));
    }
}