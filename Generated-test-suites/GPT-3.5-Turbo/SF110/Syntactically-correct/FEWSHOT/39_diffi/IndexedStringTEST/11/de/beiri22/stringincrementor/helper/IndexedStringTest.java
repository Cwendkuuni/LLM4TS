
package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("aabbc");
        
        assertEquals(2, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(1, indexedString.count('c'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abracadabra");
        char[] target1 = {'a', 'b', 'r', 'a'};
        char[] target2 = {'d', 'a', 'b', 'r', 'a'};
        char[] target3 = {'c', 'a', 'd', 'a'};
        
        assertEquals(0, indexedString.indexOf(target1));
        assertEquals(7, indexedString.indexOf(target2));
        assertEquals(-1, indexedString.indexOf(target3));
    }
}
