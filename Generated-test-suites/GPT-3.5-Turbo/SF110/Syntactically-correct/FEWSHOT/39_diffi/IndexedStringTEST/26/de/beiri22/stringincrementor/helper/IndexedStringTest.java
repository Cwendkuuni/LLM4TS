package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("testing");
        assertEquals(2, indexedString.count('t'));
        assertEquals(1, indexedString.count('e'));
        assertEquals(0, indexedString.count('z'));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("test");
        assertEquals(0, indexedString.indexOf("tes".toCharArray()));
        assertEquals(1, indexedString.indexOf("est".toCharArray()));
        assertEquals(-1, indexedString.indexOf("xyz".toCharArray()));
    }
}