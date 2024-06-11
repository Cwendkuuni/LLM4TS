package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testCount() {
        // Test case 1
        IndexedString indexedString1 = new IndexedString("aababc");
        assertEquals(3, indexedString1.count('a'));
        
        // Test case 2
        IndexedString indexedString2 = new IndexedString("aababc");
        assertEquals(2, indexedString2.count('b'));
    }
    
    @Test
    public void testIndexOf() {
        // Test case 1
        IndexedString indexedString1 = new IndexedString("aababc");
        int[] expected1 = {0, 3};
        assertArrayEquals(expected1, indexedString1.indexOf(new char[] {'a', 'b'}));
        
        // Test case 2
        IndexedString indexedString2 = new IndexedString("aababc");
        int[] expected2 = {1};
        assertArrayEquals(expected2, indexedString2.indexOf(new char[] {'a', 'a'}));
        
        // Test case 3
        IndexedString indexedString3 = new IndexedString("aababc");
        int[] expected3 = {-1};
        assertArrayEquals(expected3, indexedString3.indexOf(new char[] {'c', 'd'}));
    }
}