
package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {
    
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("This is a test");
        assertEquals(3, indexedString.count('i'));
        assertEquals(0, indexedString.count('z'));
        assertEquals(1, indexedString.count('T'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("This is a test");

        // Test cases where the target is found
        assertEquals(0, indexedString.indexOf("This".toCharArray()));
        assertEquals(5, indexedString.indexOf("is".toCharArray()));
        assertEquals(8, indexedString.indexOf("a".toCharArray()));
        assertEquals(10, indexedString.indexOf("test".toCharArray()));

        // Test cases where the target is not found
        assertEquals(-1, indexedString.indexOf("not".toCharArray()));
        assertEquals(-1, indexedString.indexOf("found".toCharArray()));
    }
}

