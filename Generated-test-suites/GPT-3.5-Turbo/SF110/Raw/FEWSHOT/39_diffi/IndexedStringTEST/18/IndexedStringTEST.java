package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IndexedStringTest {
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(2, indexedString.count('l'));
        assertEquals(1, indexedString.count('o'));
        assertEquals(0, indexedString.count('z'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(2, indexedString.indexOf("l".toCharArray()));
        assertEquals(-1, indexedString.indexOf("z".toCharArray()));
        assertEquals(0, indexedString.indexOf("h".toCharArray()));
        assertEquals(3, indexedString.indexOf("lo".toCharArray()));
    }
}