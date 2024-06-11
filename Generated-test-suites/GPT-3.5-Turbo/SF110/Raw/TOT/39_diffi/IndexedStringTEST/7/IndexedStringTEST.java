package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("Hello");
        int count = indexedString.count('l');
        // Typical use case
        assertEquals(2, count);
        
        indexedString = new IndexedString("Hello World");
        count = indexedString.count(' ');
        // Typical use case
        assertEquals(1, count);
        
        indexedString = new IndexedString("");
        count = indexedString.count('a');
        // Edge case - empty string
        assertEquals(0, count);
        
        indexedString = new IndexedString("Hello");
        count = indexedString.count('z');
        // Edge case - character not found
        assertEquals(0, count);
        
        indexedString = new IndexedString("12345678909876543210");
        count = indexedString.count('0');
        // Edge case - character appears multiple times
        assertEquals(4, count);
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("Hello");
        int index = indexedString.indexOf(new char[]{'H', 'e', 'l', 'l', 'o'});
        // Typical use case
        assertEquals(0, index);

        index = indexedString.indexOf(new char[]{'l', 'o'});
        // Typical use case - character array found in middle
        assertEquals(2, index);
        
        index = indexedString.indexOf(new char[]{'H', 'o'});
        // Typical use case - character array found at start
        assertEquals(0, index);
        
        index = indexedString.indexOf(new char[]{'h', 'e', 'l', 'l', 'o'});
        // Error scenario - character array with wrong case
        assertEquals(-1, index);
        
        index = indexedString.indexOf(new char[]{});
        // Edge case - empty character array
        assertEquals(-1, index);
        
        indexedString = new IndexedString("");
        index = indexedString.indexOf(new char[]{'a'});
        // Edge case - empty string
        assertEquals(-1, index);
        
        indexedString = new IndexedString("Hello");
        index = indexedString.indexOf(new char[]{'X'});
        // Edge case - character not found
        assertEquals(-1, index);
        
        indexedString = new IndexedString("12345678909876543210");
        index = indexedString.indexOf(new char[]{'1', '0'});
        // Edge case - character array found multiple times
        assertEquals(0, index);
    }
}